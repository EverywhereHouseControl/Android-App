package ehc.net;

import gcmService.GCM;
import gcmService.GCM.TaskRegisterGCM;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;
import serverConnection.Post;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends Activity 
{
	//------------Variables-----------------------
		private Button _buttonLog;
		private EditText _user;
		private EditText _password;
		private ImageView _logo;
		private TextView _createUser;
		private TextView _forgotPassword;
		private boolean _isLogIn;
		private final String _ip = Post._ip;
	//-----------------------------------
		
	@Override
	protected void onCreate( Bundle savedInstanceState ) 
	{
		super.onCreate( savedInstanceState );	
		setContentView (R.layout.log_in_view );
		
		/**
         * ------------------------------------
         * Linked:  variable <- component XML
         *-------------------------------------
         **/
        
        _buttonLog = ( Button ) findViewById( R.id.buttonLogin );
        _user = ( EditText ) findViewById( R.id.idText );
        _forgotPassword = ( TextView ) findViewById( R.id.forgotPassword );
        _password = ( EditText ) findViewById( R.id.passwordText );
        _logo = ( ImageView ) findViewById( R.id.HouseIconManagementMenu );     
        
        Animation _anim = AnimationUtils.loadAnimation( this.getBaseContext(), R.anim.rotate_indefinitely );
        //Start animating the image
         _logo.startAnimation( _anim );
        
        _buttonLog.setOnClickListener( new View.OnClickListener() 
        {	
			@Override
			public void onClick( View _v ) 
			{
				//It checks if exists connection
				ConnectivityManager _connMgr = ( ConnectivityManager ) getSystemService( Context.CONNECTIVITY_SERVICE );
			    NetworkInfo _networkInfo = _connMgr.getActiveNetworkInfo();
			      
			    if( _user.getText().toString().isEmpty() )
			    {
			    	Toast.makeText( getBaseContext(), "User box is empty.", Toast.LENGTH_SHORT ).show();
			    }
			    else if(_password.getText().toString().isEmpty())
			    {
			    	Toast.makeText( getBaseContext(), "Password box is empty.", Toast.LENGTH_SHORT ).show();
			    }
			    else if ( _networkInfo != null && _networkInfo.isConnected() ) 			        
			    {			            			        			            
			    	_isLogIn = true;
					ArrayList<String> _parametros = new ArrayList<String>();
					
					//////////////////////////////////////////////
					SharedPreferences _pref = getSharedPreferences( "LOG", Context.MODE_PRIVATE );
			        Editor _editor=_pref.edit();
			        _editor.putString( "USER", _user.getText().toString() );
			        _editor.putString( "PASSWORD", Post.md5( _password.getText().toString() ) );
			        _editor.commit();
			        //////////////////////////////////////////////				
					
					_parametros.add( "command" );
					_parametros.add( "login2" );
					_parametros.add( "username" );
					_parametros.add( _user.getText().toString() );
					_parametros.add( "password" );
					_parametros.add( Post.md5( _password.getText().toString() ) );
			    	logInConnection _connection = new logInConnection( _parametros );
			    	_connection.execute();			    	
			    } 
			    else 			        
			    {
			    	Toast.makeText(getBaseContext(), "No network connection available.", Toast.LENGTH_SHORT).show();		        
			    }				

			}													
		});
        
        _createUser = ( TextView ) findViewById( R.id.createUser );
    	_createUser.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick( View v ) 
			{
				// TODO Auto-generated method stub
				createUser();
			}
		});
    	
    	_forgotPassword = ( TextView ) findViewById( R.id.forgotPassword );
    	_forgotPassword.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick( View v ) 
			{
				// TODO Auto-generated method stub
				forgotPassword();
			}
		});
        
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
	
    /**
     * -----------------------------------------
     * Executes the CreateUser's Activity
     * -----------------------------------------
     */
    private void createUser()
    {
    	try 
    	{
			Class<?> _clazz = Class.forName( "ehc.net.CreateUser" );
			Intent _intent = new Intent( this, _clazz );
			startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
    }


	@Override
	public boolean onCreateOptionsMenu( Menu menu ) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}
	
	
	// Background process
    private class logInConnection extends AsyncTask<String, String, String>
    {    	
    	//------------Variables-----------------------
    	private ProgressDialog _pDialog;
    	private String _message = "";
    	private int _internalError = 0;
    	private ArrayList<String> _parametros = new ArrayList<String>();
    	//-----------------------------------
    	
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
            _pDialog = new ProgressDialog( LogIn.this );
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
					JSONObject _json_data = _data.getJSONObject ("error" );
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
				if( _isLogIn )
					if ( _data != null && _data.length() > 0 ) 
					{				
						JSONObject _json_data = _data.getJSONObject("result");
						
						if ( _json_data.getInt( "IDUSER" ) != 0 ) 
						{ 
							//Save the profile's information.
							JSON.saveProfileInfo( _json_data, LogIn.this );
							//Save the house's configuration
							JSON.saveConfig( _json_data.get( "JSON" ), LogIn.this );
							
							//////////////////////////////////////////////
							SharedPreferences _pref = getSharedPreferences( "LOG", Context.MODE_PRIVATE );
					        Editor _editor=_pref.edit();
					        _editor.putString( "LOGIN", "TRUE" );
					        _editor.commit();					        
					        
					        String _id = GCM.getRegistrationId( LogIn.this );
					        if( _id.equals("") )
					        {
					        	//GCM registration.
								TaskRegisterGCM _task = new TaskRegisterGCM( LogIn.this );
								_task.execute( _user.getText().toString() );
					        }				        							
					        //////////////////////////////////////////////						
							//Activate the next Activity("MainMenu")
							createdIntent();
						}				
					}
					
			 }
			catch ( Exception _e ) 
			 {
			 	_e.printStackTrace();
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
     * 
     */
	private void forgotPassword() 
	{
		// TODO Auto-generated method stub
		// custom dialog
		final Dialog _dialog = new Dialog( this );
		_dialog.setContentView( R.layout.forgot_password_dialog );
		_dialog.setTitle( "Forgot password?" );
 
		final EditText _userName = (EditText) _dialog.findViewById( R.id.UserEditText );
		
		Button _sendButton = ( Button ) _dialog.findViewById( R.id.SendButton );
		// if button is clicked, close the custom dialog
		_sendButton.setOnClickListener( new View.OnClickListener() 
		{
			@Override
			public void onClick( View v ) 
			{
				if( !_userName.getText().toString().isEmpty() )
				{
					_isLogIn = false;
					ArrayList<String> _parametros = new ArrayList<String>();
	
					_parametros.add( "command" );
					_parametros.add( "lostpass" );
					_parametros.add( "username" );
					_parametros.add( _userName.getText().toString() );
					
					logInConnection _connection = new logInConnection( _parametros );
			    	_connection.execute();	
				}
				else 
				{
					if( _userName.getText().toString().isEmpty() )
						Toast.makeText( getBaseContext(), "User box is empty.", Toast.LENGTH_SHORT ).show();
				}
			}
		});
		
		Button _exitButton = ( Button ) _dialog.findViewById( R.id.ExitButton );
		// if button is clicked, close the custom dialog
		_exitButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick( View v ) 
			{
				_dialog.dismiss();
			}
		});
		_dialog.show();
	}
    
    @Override
    public void onBackPressed() 
    {
    	// TODO Auto-generated method stub
    	super.onStop();
    	Intent _intent = new Intent( Intent.ACTION_MAIN );
    	_intent.addCategory( Intent.CATEGORY_HOME );
    	_intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
    	startActivity( _intent ); 
    }
      
    @Override
    protected void onResume()
    {
    	super.onResume();
    	GCM.checkPlayServices( LogIn.this );
    	//Reset the boxes
        _user.setHint( "User" );
        _password.setHint( "Password" );
    }
}
	
