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
         * Ligadura:  variable <- componente XML
         *-------------------------------------
         **/
        
        _buttonLog = ( Button ) findViewById( R.id.buttonLogin );
        _user = ( EditText ) findViewById( R.id.idText );
        _password = ( EditText ) findViewById( R.id.passwordText );
        _logo = (ImageView) findViewById(R.id.imageWorldManagementMenu);
        
        Animation anim = AnimationUtils.loadAnimation(this.getBaseContext(), R.anim.rotate_indefinitely);
        //Start animating the image
         _logo.startAnimation(anim);
      
        final logInConnection connection = new logInConnection();
        
        _buttonLog.setOnClickListener( new View.OnClickListener() 
        {	
			@Override
			public void onClick( View _v ) 
			{
				log("Botón pulsado");
				
				//Comprueba que hay conexión a internet
				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			      
			    if (networkInfo != null && networkInfo.isConnected()) 			        
			    {			            			        	
			    	log("Hay conecxión");			            
			    	_post = new Post();						
			    	//logInConnection connection = new logInConnection();
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
     * Ejecuta la activity Menu Principal
     * -----------------------------------------
     */
    
    private void createdIntent()
    {
    	try {
			Class<?> _clazz = Class.forName( "ehc.net.MainMenu" );
			Intent _intent = new Intent( this,_clazz );
			startActivity( _intent );
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
	
	// Proceso que se ejecuta en segundo plano
    private class logInConnection extends AsyncTask<String, String, String>
    {
    	
    	/**
    	 * Mensaje "cargando"
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
    	 * Método que encripta la contraseña 
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
				//Creación de la consulta
				ArrayList<String> parametros = new ArrayList<String>();
				parametros.add("command");
				parametros.add("login");
				parametros.add("username");
				//parametros.add(_user.getText().toString());
				parametros.add("luis");
				parametros.add("password");
				//parametros.add(md5(_password.getText().toString()));
				parametros.add(md5("luis"));
			 			
				//En la variable datos se guarda la respuesta a la consulta enviada
				JSONArray datos = _post.getServerData(parametros,"http://5.231.69.226/EHControlConnect/index.php"/*"http://ehcontrol.net/EHControlConnect/index.php"*/);
				log(datos.toString());
				
				if (datos != null && datos.length() > 0) 
				{				
					JSONObject json_data = datos.getJSONObject(0);
					log(json_data.toString());
					
					if (json_data.getInt("IdUser")==0) 
					{ 
						log("Usuario incorrecto. ");
					}
					else
					{ 	
						log("Usuario correcto. ");
						log(json_data.getString( "json" ));
						_houseEstructure = json_data.getString( "json" );
						log( _houseEstructure );
						//String currentDirectory = System.getProperty("user.dir");
						//log(currentDirectory);
						//Activa la siguiente Activity("MainMenu")
						createdIntent();
					}				
				}else 
				{
					log("JSON, ERROR ");
					log(datos.toString());
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
     * Método para debugear
     * @param _text
     */
    private void log( String _text )
    {
    	Log.d( "Acción :", _text );
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
