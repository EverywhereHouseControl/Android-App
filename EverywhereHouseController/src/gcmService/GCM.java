package gcmService;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;
import serverConnection.Post;

import com.google.android.gms.common.*;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;


public class GCM
{
	//------------Variables-----------------------
	private static Activity _context;	
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	//-----------------------------------
    	
	/**
	 * 
	 * @param context
	 */
	public void setContext(Activity context)
	{
		_context = context;
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkPlayServices(Activity context) 
	{
		_context = context;
		
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable( _context );
	    if ( resultCode != ConnectionResult.SUCCESS )
	    {
	        if ( GooglePlayServicesUtil.isUserRecoverableError( resultCode ) )
	        {
	            GooglePlayServicesUtil.getErrorDialog( resultCode, _context,
	            		PLAY_SERVICES_RESOLUTION_REQUEST ).show();
	        }
	        else
	        {
	            Log.i("GCM", "Dispositivo no soportado.");
	        }
	        return false;
	    }
	    return true;
	}
	
	
	/**
	 * 
	 * @param context
	 * @return
	 * @throws JSONException
	 */
	public static String getRegistrationId( Activity context ) throws JSONException
	{
		_context = context;
		
		SharedPreferences _pref = _context.getSharedPreferences("LOG",Context.MODE_PRIVATE);
		String registrationId = _pref.getString("ID", "");
	 
	    if (registrationId.length() == 0)
	    {
	        Log.d("GCM", "Registro GCM no encontrado.");
	        return "";
	    }	 
	    return registrationId;
	}
	 
	
	/**
	 * GCM registration for new user.
	 *
	 */
	public static class TaskRegisterGCM extends AsyncTask<String,Integer,String>
	{
		//------------Variables-----------------------
		//Project Number: 701857172243 
		private String SENDER_ID = "701857172243";
		private GoogleCloudMessaging _gcm;
		private Context _context;
		//-----------------------------------
		
		public TaskRegisterGCM( Context context )
		{
			_context = context;
		}
		
	    @Override
	    protected String doInBackground( String... params )
	    {
	            String msg = "";
	            try
	            {
	                if ( _gcm == null )
	                {
	                    _gcm = GoogleCloudMessaging.getInstance( _context );
	                }
	 
	                //Registration into servers GCM
	                String _regid = _gcm.register( SENDER_ID );
	                
	                SharedPreferences _pref = _context.getSharedPreferences( "LOG", Context.MODE_PRIVATE );
	                Editor _editor=_pref.edit();
			        _editor.putString( "ID", _regid );
			        _editor.commit();
	                	                
	                ArrayList<String> _parametros = new ArrayList<String>();
	                _parametros.add( "command" );
					_parametros.add( "login3" );
					_parametros.add( "username" );
					_parametros.add( _pref.getString( "USER", "" ) );
					_parametros.add( "password" );
					_parametros.add( _pref.getString( "PASSWORD", "" ) );
					_parametros.add( "regid" );
					_parametros.add( _regid );
					_parametros.add( "os" );
					_parametros.add( "Android" );
					
					 _editor.putString( "ID", _regid );
					 
					Post.getServerData( _parametros, "http://5.231.69.226/EHControlConnect/index.php" );//"http://192.168.2.147/EHControlConnect/index.php");
	                
	            }
	            catch (IOException ex)
	            {
	                ex.printStackTrace();
	            }
	 
	            return msg;
	        }
	}

}
