package ehc.net;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import ehc.net.R;

import serverConnection.Post;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
		private Post _post;
		private TextView _createUser;
		private TextView _forgotPassword;
		private boolean _isLogIn;
		
		private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	//***********************************
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_in_view);
		
		/**
         * ------------------------------------
         * Linked:  variable <- component XML
         *-------------------------------------
         **/
        
        _buttonLog = ( Button ) findViewById( R.id.buttonLogin );
        _user = ( EditText ) findViewById( R.id.idText );
        _forgotPassword = ( TextView ) findViewById( R.id.forgotPassword );
        _password = ( EditText ) findViewById( R.id.passwordText );
        _logo = (ImageView) findViewById(R.id.HouseIconManagementMenu);     
        
        Animation _anim = AnimationUtils.loadAnimation(this.getBaseContext(), R.anim.rotate_indefinitely);
        //Start animating the image
         _logo.startAnimation(_anim);
        
        _buttonLog.setOnClickListener( new View.OnClickListener() 
        {	
			@Override
			public void onClick( View _v ) 
			{
				//It checks if exists connection
				ConnectivityManager _connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo _networkInfo = _connMgr.getActiveNetworkInfo();
			      
			    if (_networkInfo != null && _networkInfo.isConnected()) 			        
			    {			            			        			            
			    	_isLogIn = true;
			    	_post = new Post();
					ArrayList<String> _parametros = new ArrayList<String>();
					
					_parametros.add("command");
					_parametros.add("login2");
					_parametros.add("username");
					_parametros.add(_user.getText().toString());
//					_parametros.add("demo");
//					_parametros.add("bertoldo");
					_parametros.add("password");
					_parametros.add(_post.md5(_password.getText().toString()));
//					_parametros.add(_post.md5("demo"));
//					_parametros.add(_post.md5("bertoldo"))
			    	logInConnection _connection = new logInConnection(_parametros);
			    	_connection.execute();			    	
			    } 
			    else 			        
			    {
			        log("No network connection available.");			        
			    }				

			}													
		});
        
        _createUser = (TextView) findViewById(R.id.createUser);
    	_createUser.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				createUser();
			}
		});
    	
    	_forgotPassword = (TextView) findViewById(R.id.forgotPassword);
    	_forgotPassword.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				forgotPassword();
			}
		});
        
	}
	
	/**
     * -----------------------------------------
     * Executes the MainMenu's Activity
     * -----------------------------------------
     */
    
    private void createdIntent()
    {
    	try 
    	{
			Class<?> _clazz = Class.forName( "ehc.net.HousesMenu" );
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
     * Executes the CreateUser's Activity
     * -----------------------------------------
     */
    private void createUser()
    {
    	try 
    	{
			Class<?> _clazz = Class.forName( "ehc.net.CreateUser" );
			Intent _intent = new Intent( this,_clazz );
			startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
    }
    /**
     * 
     */
	private void forgotPassword() 
	{
		// TODO Auto-generated method stub
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.forgot_password_dialog);
		dialog.setTitle("Forgot password?");
 
		final EditText _userName = (EditText) dialog.findViewById(R.id.UserEditText);
		final EditText _userEmail = (EditText) dialog.findViewById(R.id.EmailEditText);
		
		Button _sendButton = (Button) dialog.findViewById(R.id.SendButton);
		// if button is clicked, close the custom dialog
		_sendButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(!_userName.getText().toString().isEmpty() && !_userEmail.getText().toString().isEmpty())
				{
					_isLogIn = false;
					_post = new Post();
					ArrayList<String> _parametros = new ArrayList<String>();
	
					_parametros.add("command");
					_parametros.add("lostpass");
					_parametros.add("username");
					_parametros.add(_userName.getText().toString());
					
					logInConnection _connection = new logInConnection(_parametros);
			    	_connection.execute();	
				}
				else 
				{
					if(_userName.getText().toString().isEmpty())
						Toast.makeText(getBaseContext(), "User box is empty.", Toast.LENGTH_SHORT).show();
					else if(_userEmail.getText().toString().isEmpty())
						Toast.makeText(getBaseContext(), "Email box is empty.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		Button _exitButton = (Button) dialog.findViewById(R.id.ExitButton);
		// if button is clicked, close the custom dialog
		_exitButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}
	
	
	// Background process
    private class logInConnection extends AsyncTask<String, String, String>
    {    	
    	private ProgressDialog _pDialog;
    	private String _message = "";
    	private int _internalError = 0;
    	private ArrayList<String> _parametros = new ArrayList<String>();
    	
    	public logInConnection(ArrayList<String> parametros) 
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
            _pDialog = new ProgressDialog(LogIn.this);
            _pDialog.setMessage("Loading. Please wait...");
            _pDialog.setIndeterminate(false);
            _pDialog.setCancelable(false);
            _pDialog.show();          
        }
    	
    	@Override
		protected String doInBackground(String... arg0) 
		{
			try 
			{		             
				//Query
				//Variable 'Data' saves the query response
				JSONObject _data = _post.getServerData(_parametros,"http://5.231.69.226/EHControlConnect/index.php");//"http://192.168.2.147/EHControlConnect/index.php");
				log(_data.toString());
				
				try 
				{
					JSONObject _json_data = _data.getJSONObject("error");
					switch(_json_data.getInt("ERROR"))
					{
						case 0:
						{
							_message = _json_data.getString("ENGLISH");					
							break;
						}
						default:
						{
							_internalError = _json_data.getInt("ERROR");
							_message = _json_data.getString("ENGLISH");
							break;
						}
					}
				
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				if(_isLogIn)
					if (_data != null && _data.length() > 0) 
					{				
						JSONObject _json_data = _data.getJSONObject("result");
						//log(json_data.toString());
						
						if (_json_data.getInt("IDUSER")==0) 
						{ 
							log("Incorrect user. ");
						}
						else
						{ 	
							//Save the profile's information.
							JSON.saveProfileInfo(_json_data,LogIn.this);
							//Save the house's configuration
							JSON.saveConfig(_json_data.get("JSON"),LogIn.this);
							//Activate the next Activity("MainMenu")
							createdIntent();
						}				
					}
					else 
					{
						log("JSON, ERROR ");
						log(_data.toString());
					}			 
			 }
			catch (Exception _e) 
			 {
			 	_e.printStackTrace();
			 }
			 // End call to PHP server
			return null;
		}
    	
		protected void onPostExecute(String file_url) 
		{
            // dismiss the dialog
            _pDialog.dismiss();
            if(_internalError!=0)Toast.makeText(getBaseContext(), _message, Toast.LENGTH_SHORT).show();
		}
    }
    
    @Override
    public void onBackPressed() 
    {
    	// TODO Auto-generated method stub
    	super.onStop();
    	Intent _intent = new Intent(Intent.ACTION_MAIN);
    	_intent.addCategory(Intent.CATEGORY_HOME);
    	_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(_intent); 
    }
    
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() 
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) 
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) 
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } 
            else 
            {
                Log.i("GCM", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    
    /**
     * Method for debug
     * @param _text
     */
    private void log( String _text )
    {
    	Log.d( "Action :", _text );
    }
    
    
    protected void onResume()
    {
    	super.onResume();
    	log( "Resumed" );
    	checkPlayServices();
    	//Reset the boxes
        _user.setHint("User");
        _password.setHint("Password");
    }
    
    protected void onPause()
    {
    	super.onPause();
    	log( "Paused" );
    }
    protected void onStop()
    {
    	super.onStop();
    	log( "Stoped" );
    }
}
	
