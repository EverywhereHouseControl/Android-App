package ehc.net;

import gcmService.GCMIntentService;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;
import serverConnection.Post;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class Main extends Activity
{
	//---------Variables-------------------------------
	private final String _ip = Post._ip;
	//----------------------------------------
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) 
	{
		// TODO Auto-generated method stub
		super.onCreate( savedInstanceState );
		
		GCMIntentService.closeNotifications();
			
		SharedPreferences _pref = getSharedPreferences( "LOG", Context.MODE_PRIVATE );
		
		if( _pref.getString( "LOGIN" , "" ).equals( "TRUE" ) )
		{
			ArrayList<String> _parametros = new ArrayList<String>();
			_parametros.add( "command" );
			_parametros.add( "login2" );
			_parametros.add( "username" );
			_parametros.add( _pref.getString( "USER", "" ) );
			_parametros.add( "password" );
			_parametros.add( _pref.getString( "PASSWORD", "" ) );
			logInConnection _connection = new logInConnection( _parametros );
			_connection.execute();	
		}
		else
		{
			createdLogInIntent();
		}
	}
	
	
	
	// Background process
    private class logInConnection extends AsyncTask<String, String, String>
    {    	
    	//---------Variables-------------------------------
    	private ProgressDialog _pDialog;
    	private String _message = "";
    	private int _internalError = 0;
    	private ArrayList<String> _parametros = new ArrayList<String>();
    	//----------------------------------------
    	
    	public logInConnection( ArrayList<String> parametros ) 
    	{
			// TODO Auto-generated constructor stub
    		_parametros = parametros;
		}
    	  	
    	/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{  
    		super.onPreExecute();
            _pDialog = new ProgressDialog( Main.this );
            _pDialog.setMessage( "Loading. Please wait..." );
            _pDialog.setIndeterminate( false );
            _pDialog.setCancelable( false );
            _pDialog.show();          
        }
    	
    	@Override
		protected String doInBackground( String... arg0 ) 
		{
			try 
			{		             
				//Query
				//Variable 'Data' saves the query response
				JSONObject _data = Post.getServerData( _parametros, _ip );				
				try 
				{
					JSONObject _json_data = _data.getJSONObject( "error" );
					switch( _json_data.getInt( "ERROR" ) )
					{
						case 0:
						{
							_message = _json_data.getString( "ENGLISH" );					
							break;
						}
						default:
						{
							_internalError = _json_data.getInt( "ERROR" );
							_message = _json_data.getString( "ENGLISH" );
							break;
						}
					}
				
				} 
				catch ( JSONException e ) 
				{
					e.printStackTrace();
				}
				if ( _data != null && _data.length() > 0 ) 
				{				
					JSONObject _json_data = _data.getJSONObject( "result" );
					//log(json_data.toString());
					
					if ( _json_data.getInt( "IDUSER" ) != 0 ) 
					{ 
						//////////////////////////////////////////////
						SharedPreferences _pref = getSharedPreferences( "LOG", Context.MODE_PRIVATE );
				        Editor _editor=_pref.edit();
				        _editor.putString( "LOGIN", "TRUE" );
				        _editor.commit();
				        //////////////////////////////////////////////				        
						//Save the profile's information.
						JSON.saveProfileInfo( _json_data, Main.this );
						//Save the house's configuration
						JSON.saveConfig( _json_data.get( "JSON" ), Main.this );
						//Activate the next Activity("MainMenu")
						createdIntent();
					}
				} 
			 }
			catch (Exception e) 
			 {
			 	e.printStackTrace();
			 }
			 // End call to PHP server
			return null;
		}
    	
		protected void onPostExecute( String file_url ) 
		{
            // dismiss the dialog
            _pDialog.dismiss();
            if( _internalError != 0 )Toast.makeText( getBaseContext(), _message, Toast.LENGTH_SHORT ).show();
		}
    }
    
    /**
     * -----------------------------------------
     * Executes the logIn's Activity
     * -----------------------------------------
     */  
    private void createdLogInIntent()
    {
    	try 
    	{
			Class<?> _clazz = Class.forName( "ehc.net.LogIn" );
			Intent _intent = new Intent( this,_clazz );
			startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
    }
    
    
	/**
     * -----------------------------------------
     * Executes the HousesMenu's Activity
     * -----------------------------------------
     */    
    private void createdIntent()
    {
    	try 
    	{
			Class<?> _clazz = Class.forName( "ehc.net.HousesMenu" );
			Intent _intent = new Intent( this, _clazz );
			startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
    }
}
