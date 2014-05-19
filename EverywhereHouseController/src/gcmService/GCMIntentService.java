package gcmService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;
import serverConnection.Post;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import ehc.net.Main;
import ehc.net.R;

public class GCMIntentService extends IntentService
{
	//------------Variables-----------------------
	public static NotificationManager _mNotificationManager = null;
	private final String _ip = Post._ip;
	//-----------------------------------
	
	public GCMIntentService() 
	{
	        super("GCMIntentService");
	}

	@Override
	protected void onHandleIntent( Intent intent )
	{
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance( this );
        
        String messageType = gcm.getMessageType( intent );
        Bundle extras = intent.getExtras();

        if ( !extras.isEmpty() )
        {
            if ( GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals( messageType ) )
            {
            	long _id = Calendar.getInstance().getTimeInMillis();
            	showNotification( _id, extras.getString("mensaje") );
            }
        }   
        
        GCMBroadcastReceiver.completeWakefulIntent( intent );
        
        SharedPreferences _pref = getSharedPreferences( "LOG",Context.MODE_PRIVATE );
                        	
    	ArrayList<String> _parametros = new ArrayList<String>();
		_parametros.add( "command" );
		_parametros.add( "login2" );
		_parametros.add( "username" );
		_parametros.add( _pref.getString( "USER", "" ) );
		_parametros.add( "password");
		_parametros.add( _pref.getString( "PASSWORD", "" ) );
		
		logInConnection _connection = new logInConnection( _parametros );
		_connection.execute();	
    }
	
	
	private void showNotification( long id, String msg )
	{
		Intent _notIntent = new Intent( this, Main.class );
		PendingIntent _contIntent = PendingIntent.getActivity( this, 0, _notIntent, 0 );
		
		String _msg = msg.substring( msg.indexOf( "*" ) + 1 );
		
		SharedPreferences _pref = getSharedPreferences( "LOG",Context.MODE_PRIVATE );
		
		
		NotificationCompat.Builder _mBuilder = new NotificationCompat.Builder( this )
		.setDefaults( NotificationCompat.PRIORITY_DEFAULT )
		.setSmallIcon( R.drawable.ic_launcher )
		.setContentTitle( "EHC notification." )
		.setLargeIcon( ( ( ( BitmapDrawable )getResources().getDrawable( R.drawable.ic_launcher ) ).getBitmap() ) )
		.setContentText( msg.substring( 0, msg.indexOf( "*" ) ) )
		.setTicker (msg.substring( 0, msg.indexOf( "*" ) ) )
		.setAutoCancel( true )
		.setContentIntent( _contIntent )
		.setDefaults( Notification.DEFAULT_SOUND );	
		
		
		
//		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		
//		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB)
//		{
		    // Do something for HONEYCOMB and above versions
			
					
				
			_mNotificationManager = ( NotificationManager ) this.getSystemService( Context.NOTIFICATION_SERVICE );
			int _id = ( int )id;
			_mNotificationManager.notify( _id, _mBuilder.build() );	
//		} 
//		else
//		{
//		    // do something for phones running an SDK before HONEYCOMB
//		    // Do something for HONEYCOMB and above versions
//			Notification mBuilder = new Notification(R.drawable.ic_launcher,msg,id);
//			
//			mBuilder.setLatestEventInfo(this, "EHC notification.", msg, contIntent);
//			mBuilder.flags |= Notification.DEFAULT_SOUND | Notification.FLAG_AUTO_CANCEL;	
//				
//			mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//			mNotificationManager.notify((int)id, mBuilder);	
//			
//		}
			if( !isApplicationBroughtToBackground() )
			if( _pref.getString( "ID", "" ).equals( _msg ) )
			{
				try 
				{
					Thread.sleep( 2000 );
				} 
				catch ( InterruptedException e ) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			    NotificationManager notificationManager = ( NotificationManager ) this
			            .getSystemService( Context.NOTIFICATION_SERVICE );
			    notificationManager.cancel( _id );
			}
	}
	
	public static void closeNotifications()
	{
		if( _mNotificationManager != null )_mNotificationManager.cancelAll();
	}

	/**
	 * Check if the application is in background.
	 * @return
	 */
	private boolean isApplicationBroughtToBackground() 
	{
	    ActivityManager _am = ( ActivityManager ) this.getSystemService( Context.ACTIVITY_SERVICE );
	    List<RunningTaskInfo> _tasks = _am.getRunningTasks( 1 );
	    if ( !_tasks.isEmpty() ) 
	    {
	        ComponentName _topActivity = _tasks.get( 0 ).topActivity;
	        if ( !_topActivity.getPackageName().equals( this.getPackageName() ) ) 
	        {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 *
	 */
	private class logInConnection extends AsyncTask<String, String, String>
    {    	
    	private ArrayList<String> _parametros = new ArrayList<String>();
    	
    	public logInConnection( ArrayList<String> parametros ) 
    	{
			// TODO Auto-generated constructor stub
    		_parametros = parametros;
		}
    	  	
    	@Override
    	protected void onPreExecute() 
    	{  
    		super.onPreExecute();        
        }
    	
    	@Override
		protected String doInBackground( String... arg0 ) 
		{
			try 
			{		             
				//Query
				JSONObject _data = Post.getServerData( _parametros, _ip );
				
				try 
				{
					_data.getJSONObject( "error" );				
				} 
				catch ( JSONException e ) 
				{
					e.printStackTrace();
				}
				if ( _data != null && _data.length() > 0 ) 
				{				
					JSONObject _json_data = _data.getJSONObject( "result" );
					//log(json_data.toString());
					
					if ( _json_data.getInt( "IDUSER" )!=0 ) 
					{  	
						SharedPreferences _pref = getSharedPreferences( "LOG",Context.MODE_PRIVATE );
				        Editor _editor=_pref.edit();
				        _editor.putString( "LOGIN", "TRUE" );
				        _editor.commit();
				        
						//Save the profile's information.
						JSON.saveProfileInfo( _json_data, GCMIntentService.this );
						//Save the house's configuration
						JSON.saveConfig( _json_data.get( "JSON" ), GCMIntentService.this );
						JSON.getInstance( GCMIntentService.this );
					}
				}		 
			 }
			catch ( Exception e ) 
			 {
			 	e.printStackTrace();
			 }
			 // End call to PHP server
			return null;
		}
    	
    	@Override
		protected void onPostExecute( String file_url ) 
		{
            // dismiss the dialog
			super.onPostExecute(file_url);
			SharedPreferences _pref = getSharedPreferences( "LOG", Context.MODE_PRIVATE );		
			if( !_pref.getString( "NEWACTIVITY", "" ).equals( "OTHER" ) )
			{
				try 
				{
					Class<?> _clazz;
					
					if( _pref.getString( "NEWACTIVITY", "" ).equals( "FALSE" ) )
						_clazz = Class.forName( "ehc.net.ItemsFragmentsContainer" );
					else _clazz = Class.forName( "enviroment.RemoteController" );
					
					Intent _intent = new Intent( GCMIntentService.this, _clazz );
									
					_intent.putExtra( "Service", _pref.getString( "SERVICE", "" ) );				
					_intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
					
					ArrayList<String> _roomsList = new ArrayList<String>();
					try 
					{
						JSON.getInstance( GCMIntentService.this );
						_roomsList = JSON.getRooms( _pref.getString( "HOUSE", "" ) );
					} 
					catch ( JSONException e ) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//Current house
					_intent.putExtra( "House", _pref.getString( "HOUSE", "" ) );
					
					//Room name being clicked
					_intent.putExtra( "Room", _pref.getString( "ROOM", "" ) );
					//Room's number
					_intent.putExtra( "NumRooms", _roomsList.size() );
						
					for( int i=0; i<_roomsList.size(); i++ )
					{		
						// Key: position, Value button Name.
						_intent.putExtra( Integer.toString( i ) , _roomsList.get (i ) );	
						// Key: button name, Value: position.
						//Necessary to move the 'viewPager' to the desired view.
						_intent.putExtra( _roomsList.get( i ), Integer.toString( i ) );	
					}	
					
					GCMIntentService.this.getApplication().startActivity( _intent );
				}
				catch ( ClassNotFoundException e ) 
				{
					e.printStackTrace();
				}
			} 		
		}
    }
}
