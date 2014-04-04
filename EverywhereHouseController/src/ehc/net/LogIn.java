package ehc.net;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import framework.Post;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
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
	//***********************************
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_in_view);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		/**
         * ------------------------------------
         * Linked:  variable <- component XML
         *-------------------------------------
         **/
        
        _buttonLog = ( Button ) findViewById( R.id.buttonLogin );
        _user = ( EditText ) findViewById( R.id.idText );
        _password = ( EditText ) findViewById( R.id.passwordText );
        _logo = (ImageView) findViewById(R.id.HouseIconManagementMenu);     
        
        Animation _anim = AnimationUtils.loadAnimation(this.getBaseContext(), R.anim.rotate_indefinitely);
        //Start animating the image
         _logo.startAnimation(_anim);
      
        //final logInConnection connection = new logInConnection();
        
        _buttonLog.setOnClickListener( new View.OnClickListener() 
        {	
			@Override
			public void onClick( View _v ) 
			{
				log("Button pressed");
				
				//It checks if exists connection
				ConnectivityManager _connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo _networkInfo = _connMgr.getActiveNetworkInfo();
			      
			    if (_networkInfo != null && _networkInfo.isConnected()) 			        
			    {			            			        	
			    	log("Connection");			            
			    	_post = new Post();						
			    	logInConnection _connection = new logInConnection();
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
				log("Crear usuario");
				createUser();
			}
		});        
        
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
    	/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{  
    		super.onPreExecute();
            _pDialog = new ProgressDialog(LogIn.this);
            //pDialog.setView(getLayoutInflater().inflate(R.layout.loading_icon_view,null));
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
				ArrayList<String> _parametros = new ArrayList<String>();
				
				_parametros.add("command");
				_parametros.add("login");
				_parametros.add("username");
//				_parametros.add(_user.getText().toString());
				_parametros.add("bertoldo");
				_parametros.add("password");
//				_parametros.add(_post.md5(_password.getText().toString()));
				_parametros.add(_post.md5("bertoldo"));
			 			
				//Variable 'Data' saves the query response
				JSONArray _data = _post.getServerData(_parametros,"http://5.231.69.226/EHControlConnect/index.php");//"http://192.168.2.147/EHControlConnect/index.php");
				log(_data.toString());
				
				try 
				{
					JSONObject _json_data = _data.getJSONObject(0);
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
				
				if (_data != null && _data.length() > 0) 
				{				
					JSONObject _json_data = _data.getJSONObject(0);
					//log(json_data.toString());
					
					if (_json_data.getInt("IDUSER")==0) 
					{ 
						log("Incorrect user. ");
					}
					else
					{ 	
						//Save the profile's information.
						saveProfileInfo(_json_data);
						//Save the house's configuration
						saveConfig(_json_data.get("JSON"));
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
            // dismiss the dialog after getting all products
            _pDialog.dismiss();
            if(_internalError!=0)Toast.makeText(getBaseContext(), _message, Toast.LENGTH_SHORT).show();
		}
    }
    	
	/**
	 * Saves from the server query the profile information in the file 'profile.json'.
	 */
	private void saveProfileInfo(JSONObject JSON)
	{
		try 
		{
			FileOutputStream _outputStream = openFileOutput("profileInformation.json", MODE_PRIVATE);
			_outputStream.write(JSON.toString().getBytes());
			_outputStream.close();	
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Saves from the server query the house configuration in the file 'configuration.json'.
	 */
	private void saveConfig(Object JSON)
	{	
		try 
		{
			FileOutputStream _outputStream = openFileOutput("configuration.json", MODE_PRIVATE);
			_outputStream.write(JSON.toString().getBytes());
			_outputStream.close();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
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
			Class<?> _clazz = Class.forName( "ehc.net.MainMenu" );
			Intent _intent = new Intent( this,_clazz );
			startActivity( _intent );
			onDestroy();
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
			onDestroy();
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
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
    	//Reset the boxes
        _user.setText("");
        _password.setText("");
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
	
