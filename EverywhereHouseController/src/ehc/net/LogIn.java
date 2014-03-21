package ehc.net;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import ehc.net.R.color;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
		private ProgressDialog pDialog;
		private String _houseEstructure = "";
		//***********************************
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_in_view);
		
		/**
         * ------------------------------------
         * Link:  variable <- component XML
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}
	
	// Background process
    private class logInConnection extends AsyncTask<String, String, String>
    {
    	
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
    	
    	/**
    	 * Method that encrypts the password
    	 * @param s
    	 * @return
    	 */
    	public String md5(String s) 
    	{
    	    try 
    	    {
    	        // Create MD5 Hash
    	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
    	        digest.update(s.getBytes());
    	        byte messageDigest[] = digest.digest();

    	        // Create Hex String
    	        StringBuffer hexString = new StringBuffer();
    	        for (int i=0; i<messageDigest.length; i++)
    	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
    	        return hexString.toString();

    	    } catch (NoSuchAlgorithmException e) 
    	    {
    	        e.printStackTrace();
    	    }
    	    return "";
    	}
    	
		@Override
		protected String doInBackground(String... arg0) 
		{
			// TODO Auto-generated method stub
			//ProgressDialog pDialog = new ProgressDialog(LogIn.this);
			
			try 
			{	
				//Query
				ArrayList<String> parametros = new ArrayList<String>();
				
				parametros.add("command");
				parametros.add("login");
				parametros.add("username");
				//parametros.add(_user.getText().toString());
				parametros.add("luis");
				parametros.add("password");
				//parametros.add(md5(_password.getText().toString()));
				parametros.add(md5("luis"));
			 			
				//Variable 'Data' saves the query response
				JSONArray data = _post.getServerData(parametros,"http://5.231.69.226/EHControlConnect/index.php"/*"http://ehcontrol.net/EHControlConnect/index.php"*/);
				log(data.toString());
				
				///////////////////////////////////////////////////////
				ArrayList<String> parametros2 = new ArrayList<String>();
				parametros2.add("command");
				parametros2.add("doaction");
				parametros2.add("username");
				//parametros.add(_user.getText().toString());
				parametros2.add("luis");
				
				parametros2.add("servicename");
				//parametros.add(_user.getText().toString());
				parametros2.add("saloon");
				
				parametros2.add("actionname");
				//parametros.add(_user.getText().toString());
				parametros2.add("encender");
				
				parametros2.add("data");
				//parametros.add(_user.getText().toString());
				parametros2.add("cara mierda");
			 			
				//Variable 'Data' saves the query response
				JSONArray data2 = _post.getServerData(parametros2,"http://5.231.69.226/EHControlConnect/index.php"/*"http://ehcontrol.net/EHControlConnect/index.php"*/);
				log(data2.toString());
				
				/////////////////////////////////////////////////////

				
				if (data != null && data.length() > 0) 
				{				
					JSONObject json_data = data.getJSONObject(0);
					log(json_data.toString());
					
					if (json_data.getInt("IDUSER")==0) 
					{ 
						log("Incorrect user. ");
					}
					else
					{ 	
						log("Correct user. ");
						log(json_data.getString( "JSON" ));
						_houseEstructure = json_data.getString( "JSON" );
						log( _houseEstructure );
						//String currentDirectory = System.getProperty("user.dir");
						//log(currentDirectory);
						//Activa la siguiente Activity("MainMenu")
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
			 // FIN Llamada a Servidor Web PHP
			return null;
		}
    	
		protected void onPostExecute(String file_url) 
		{
            // dismiss the dialog after getting all products
            pDialog.dismiss();
		}
    }
          
    
    /**
     * M�todo para debugear
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
