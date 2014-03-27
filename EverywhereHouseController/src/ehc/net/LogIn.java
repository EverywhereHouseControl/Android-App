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
import android.app.Dialog;
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
		//******************************
		private Activity _activity;
		private Post _post;
		//private ProgressDialog pDialog;
		private TextView _createUser;
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
        _password = ( EditText ) findViewById( R.id.passwordText );
        _logo = (ImageView) findViewById(R.id.HouseIconManagementMenu);     
        
        Animation anim = AnimationUtils.loadAnimation(this.getBaseContext(), R.anim.rotate_indefinitely);
        //Start animating the image
         _logo.startAnimation(anim);
      
        //final logInConnection connection = new logInConnection();
        
        _buttonLog.setOnClickListener( new View.OnClickListener() 
        {	
			@Override
			public void onClick( View _v ) 
			{
				log("Button pressed");
				
				//It checks if exists connection
				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			      
			    if (networkInfo != null && networkInfo.isConnected()) 			        
			    {			            			        	
			    	log("Connection");			            
			    	_post = new Post();						
			    	logInConnection connection = new logInConnection();
			    	connection.execute();			    	
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
		getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}
	
	
	// Background process
    private class logInConnection extends AsyncTask<String, String, String>
    {    	
    	private ProgressDialog pDialog;
    	private String _message = "";
    	private int _internalError = 0;
    	/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{  
    		super.onPreExecute();
            pDialog = new ProgressDialog(LogIn.this);
            //pDialog.setView(getLayoutInflater().inflate(R.layout.loading_icon_view,null));
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            
//            AlertDialog.Builder keyBuilder = new AlertDialog.Builder(LogIn.this);
//            keyBuilder.setView(getLayoutInflater().inflate(R.layout.loading_icon_view, null));
//            pDialog = keyBuilder.create();
//            ImageView _logo = (ImageView) findViewById(R.id.world_loading_view);
//            Animation anim = AnimationUtils.loadAnimation(LogIn.this, R.anim.rotate_indefinitely);
//            //Start animating the image
//             _logo.startAnimation(anim);     
//            pDialog.show();            
        }
    	
    	@Override
		protected String doInBackground(String... arg0) 
		{
			try 
			{		             
				//Query
				ArrayList<String> parametros = new ArrayList<String>();
				
				parametros.add("command");
				parametros.add("login");
				parametros.add("username");
				parametros.add(_user.getText().toString());
				//parametros.add("luis");
				parametros.add("password");
				parametros.add(_post.md5(_password.getText().toString()));
				//parametros.add(_post.md5("luis"));
			 			
				//Variable 'Data' saves the query response
				JSONArray data = _post.getServerData(parametros,"http://5.231.69.226/EHControlConnect/index.php"/*"http://ehcontrol.net/EHControlConnect/index.php"*/);
				log(data.toString());
				
				
				//////////////////////////////////////////////////
				
//				parametros.add("command");
//				parametros.add("doaction");
//				parametros.add("username");
//				parametros.add(_user.getText().toString());
//				parametros.add("housename");
//				parametros.add("basicHouse1");
//				parametros.add("roomname");
//				parametros.add("LAB");
//				parametros.add("servicename");
//				parametros.add("TV");
//				parametros.add("actionname");
//				parametros.add("ENVIAR");
//				parametros.add("data");
//				parametros.add("POWER");
//				JSONArray data2 = _post.getServerData(parametros,"http://5.231.69.226/EHControlConnect/index.php"/*"http://ehcontrol.net/EHControlConnect/index.php"*/);
//				log(data2.toString());
				//////////////////////////////////////////////////
				
				
				try 
				{
					JSONObject json_data = data.getJSONObject(0);
					switch(json_data.getInt("ERROR"))
					{
						case 0:
						{
							_message = json_data.getString("ENGLISH");					
							break;
						}
						default:
						{
							_internalError = json_data.getInt("ERROR");
							_message = json_data.getString("ENGLISH");
							break;
						}
					}
				
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}	
				
//				parametros.add("command");
//				parametros.add("deleteuser");
//				parametros.add("username");
//				//parametros.add(_user.getText().toString());
//				parametros.add("alex");
//				parametros.add("password");
//				//parametros.add(md5(_password.getText().toString()));
//				parametros.add(md5("luis"));
//				
//				JSONArray data2 = _post.getServerData(parametros,"http://5.231.69.226/EHControlConnect/index.php"/*"http://ehcontrol.net/EHControlConnect/index.php"*/);
//				log(data2.toString());
				
				
//				parametros.add("command");
//				parametros.add("modifyuser");
//				parametros.add("username");
//				//parametros.add(_user.getText().toString());
//				parametros.add("alex");
//				parametros.add("password");
//				//parametros.add(md5(_password.getText().toString()));
//				parametros.add(md5("luis"));
//				parametros.add("n_username");
//				//parametros.add(_user.getText().toString());
//				parametros.add("luis_caca_caca");
//				parametros.add("password");
//				//parametros.add(md5(_password.getText().toString()));
//				parametros.add("email");
//				//parametros.add(md5(_password.getText().toString()));
//				parametros.add(md5("luis"));
//				parametros.add("hint");
//				//parametros.add(md5(_password.getText().toString()));
//				parametros.add(md5("luis"));
//				
//				
//				JSONArray data2 = _post.getServerData(parametros,"http://5.231.69.226/EHControlConnect/index.php"/*"http://ehcontrol.net/EHControlConnect/index.php"*/);
//				log(data2.toString());
				
				
				///////////////////////////////////////////////////////
//				ArrayList<String> parametros2 = new ArrayList<String>();
//				parametros2.add("command");
//				parametros2.add("doaction");
//				parametros2.add("username");
//				parametros2.add("bertoldo");
//				parametros2.add("housename");
//				parametros2.add("casaBertoldo");
//				parametros2.add("roomname");
//				parametros2.add("cocina");
//				parametros2.add("servicename");
//				parametros2.add("TV");
//				parametros2.add("actionname");
//				parametros2.add("ENCENDER");
//				parametros2.add("data");
//				parametros2.add("CACA");
//			 			
//				//Variable 'Data' saves the query response
//				JSONArray data2 = _post.getServerData(parametros2,"http://5.231.69.226/EHControlConnect/index.php"/*"http://ehcontrol.net/EHControlConnect/index.php"*/);
//				log(data2.toString());
				
				/////////////////////////////////////////////////////

				
				if (data != null && data.length() > 0) 
				{				
					JSONObject json_data = data.getJSONObject(0);
					//log(json_data.toString());
					
					if (json_data.getInt("IDUSER")==0) 
					{ 
						log("Incorrect user. ");
					}
					else
					{ 	
						//Save the profile's information.
						saveProfileInfo(json_data);
						//Save the house's configuration
						saveConfig(json_data.get("JSON"));
						//Activate the next Activity("MainMenu")
						createdIntent();
					}				
				}else 
				{
					log("JSON, ERROR ");
					log(data.toString());
				}			 
			 }catch (Exception e) 
			 {
			 	e.printStackTrace();
			 }
			 // End call to PHP server
			return null;
		}
    	
		protected void onPostExecute(String file_url) 
		{
            // dismiss the dialog after getting all products
            pDialog.dismiss();
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
			FileOutputStream outputStream = openFileOutput("profileInformation.json", MODE_PRIVATE);
			outputStream.write(JSON.toString().getBytes());
			outputStream.close();	
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
			FileOutputStream outputStream = openFileOutput("configuration.json", MODE_PRIVATE);
			outputStream.write(JSON.toString().getBytes());
			outputStream.close();
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
    	try {
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
	 * Method that created a new user.
	 */
	private void createUser()
	{
		// custom dialog
		final Dialog dialog = new Dialog(LogIn.this);
		dialog.setContentView(R.layout.create_user);
		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.newUserCancel);
		Button dialogButtonConfirm = (Button) dialog.findViewById(R.id.newUserConfirm);
	 
		dialog.setTitle("NEW USER");    	
	
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
    
		dialog.show();
		dialog.getWindow().setAttributes(lp);        
   
		// if button is clicked, close the custom dialog
		dialogButtonCancel.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});		
	 
		// if button is clicked, send the query and close the custom dialog
		dialogButtonConfirm.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				
				int _internalError = 0;
				
				//It checks if exists connection
				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			      
				if (networkInfo != null && networkInfo.isConnected()) 			        
				{			            			        			            
					_post = new Post();    		
				} 
				else 			        
				{
					_internalError=-1;
				}		
			
				/**
				 * ------------------------------------
				 * Linked:  variable <- component XML
				 *-------------------------------------
				 **/
				EditText user = (EditText) dialog.findViewById(R.id.newUser);
				EditText email = (EditText) dialog.findViewById(R.id.newEmail);
				EditText password = (EditText) dialog.findViewById(R.id.newPassword);
				EditText repeatPassword = (EditText) dialog.findViewById(R.id.newRepeatPassword);
				
				//Creation the query.
				ArrayList<String> parametros = new ArrayList<String>();
				
				parametros.add("command");
				parametros.add("createuser");
				parametros.add("username");
				parametros.add(user.getText().toString());
				parametros.add("password");
				parametros.add(_post.md5(password.getText().toString()));
				parametros.add("email");
				parametros.add(email.getText().toString());
				parametros.add("hint");
				parametros.add("");
			
				//Identify errors.
				if( user.getText().toString().isEmpty())_internalError=-2;
				else if(email.getText().toString().isEmpty() )_internalError=-3;
				else if(!email.getText().toString().contains("@"))_internalError=-4;				
				else if (password.getText().toString().isEmpty())_internalError=-5;
				else if (password.getText().toString().length()<2)_internalError=-6;
				else if (repeatPassword.getText().toString().isEmpty())_internalError=-7;
				else if (!password.getText().toString().equals(repeatPassword.getText().toString()))_internalError=-8;
				
				errorControl(dialog,parametros,_internalError);				
			}
		});
	}

	/**
	 * Error control for creating user.
	 * @param dialog
	 * @param parametros
	 * @param _internalError
	 */
	private void errorControl(Dialog dialog,ArrayList<String> parametros,int _internalError)
	{
		String _message = "";
		switch(_internalError)
		{
			case 0:
			{
				_post = new Post();						
		    	createUserConnection createUser = new createUserConnection(parametros);
		    	createUser.execute();	    	
				break;
			}
			case -1:
			{
				_message = "Not network connection available";
				break;
			}
			case -2:
			{
				_message = "Box user is empty";				
				break;
			}
			case -3:
			{
				_message = "Box e-mail is empty";
				break;
			}
			case -4:
			{
				_message = "Erroneous format in e-mail box";
				break;
			}
			case -5:
			{
				_message = "Box password is empty";				
				break;
			}
			case -6:
			{
				_message = "Password is too sort";
				break;
			}
			case -7:
			{
				_message = "Box repeat password is empty";
				break;
			}
			case -8:
			{
				_message = "Passwords do not match";
				break;
			}				
		}
		if(_internalError!=0)Toast.makeText(getBaseContext(), _message, Toast.LENGTH_SHORT).show();
			else dialog.dismiss();
	}
	
	private class createUserConnection extends AsyncTask<String, String, String>
	{
		private ArrayList<String> parametros;
		private String _message = "";
		private ProgressDialog pDialog;
		
		public createUserConnection(ArrayList<String> parametros) 
		{
			// TODO Auto-generated constructor stub
			this.parametros = parametros;
		}
		
		/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{
            super.onPreExecute();
            pDialog = new ProgressDialog(LogIn.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }    	
    	    	
		@Override
		protected String doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			//Variable 'Data' saves the query response
			log("query");
			log(parametros.toString());
			JSONArray data = _post.getServerData(parametros,"http://5.231.69.226/EHControlConnect/index.php");
			log(data.toString());
			try 
			{
				JSONObject json_data = data.getJSONObject(0);
				switch(json_data.getInt("ERROR"))
				{
					case 0:
					{
						_message = json_data.getString("ENGLISH");					
						break;
					}
					default:
					{
						_message = json_data.getString("ENGLISH");
						break;
					}
				}
			
			} catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return null;
		}
		
		/**
		 * 
		 */
		protected void onPostExecute(String file_url) 
		{
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            Toast.makeText(getBaseContext(), _message, Toast.LENGTH_SHORT).show();
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
	
