package ehc.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import framework.Post;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Profile extends Activity
{
	//---------Variables----------------
	private Button _buttonSave;
	private Button _buttonExit;
	private EditText _user;
	private EditText _email;
	private EditText _password;
	private String file;
	private ProgressDialog pDialog;
	private Post _post;
	private String _message = "";
	//-------------------------------
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_view);
		/**
         * ------------------------------------
         * Liked:  variable <- XML component 
         *-------------------------------------
         */
		_user = ( EditText ) findViewById( R.id.profileUser );
		_email = ( EditText ) findViewById( R.id.profileEmail );
		_password = ( EditText ) findViewById( R.id.profilePassword );
		
		_buttonSave = ( Button ) findViewById( R.id.profileSave );
		_buttonExit = (Button) findViewById(R.id.profileExit);
						
		_buttonSave.setOnClickListener( new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				_post = new Post();						
				modifyConnection connection = new modifyConnection();
		    	connection.execute();	 			
			}
		});
		
		_buttonExit.setOnClickListener( new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		parser();
	}
	
	
	/**
	 * 
	 */
	private void parser()
	{
		try 
		{
			InputStream is = openFileInput("profileInformation.json");
			int size = is.available();
	        byte[] buffer = new byte[size];
	        is.read(buffer);
	        is.close();
	        this.file = new String(buffer, "UTF-8");
	        loadProfileInfo();
		} 
    	catch (IOException ex) 
    	{
    		ex.printStackTrace();
    	}
		catch (Exception ex) 
    	{
    		ex.printStackTrace();
    	}
	}

	/**
	 * 
	 * @throws JSONException
	 */
	private void loadProfileInfo() throws JSONException 
	{
		JSONObject obj = new JSONObject(file);
		_user.setText(obj.getString("USERNAME"));
		_email.setText(obj.getString("EMAIL"));
		_password.setText("*/*^^*/*^^*/*^^*/*");
	}
	
	
	private class modifyConnection extends AsyncTask<String, String, String>
	{
		private ArrayList<String> parametros = new ArrayList<String>();
		
		/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{
            super.onPreExecute();
            pDialog = new ProgressDialog(Profile.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }    	
    	    	
		@Override
		protected String doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			int _internalError = 0;
			
			log("query");

			try 
			{
			
				JSONObject obj = new JSONObject(file);
				log("Antes: " + obj.toString());
			
				parametros.add("command");
				parametros.add("modifyuser");
				parametros.add("username");
				parametros.add(obj.getString("USERNAME"));
				parametros.add("password");
				parametros.add(obj.getString("PASSWORD"));
				parametros.add("n_username");
				parametros.add(_user.getText().toString());
				parametros.add("n_password");
				if(_password.getText().toString().equals("*/*^^*/*^^*/*^^*/*") || _post.md5(_password.getText().toString()).equals(obj.getString("PASSWORD")))
				{
					parametros.add(obj.getString("PASSWORD"));
					_internalError = -5;
				}
				else parametros.add(_post.md5(_password.getText().toString()));
				
				parametros.add("n_email");
				parametros.add(_email.getText().toString());
				parametros.add("n_hint");
				parametros.add("caca");
				
				if(_user.getText().toString().isEmpty())_internalError=-1;
				else if(_password.getText().toString().isEmpty())_internalError=-2;
				else if(_password.getText().toString().length()<2)_internalError=-3;
				else if(_email.getText().toString().isEmpty())_internalError=-4;
				else if(!_email.getText().toString().contains("@"))_internalError=-5;
				else if(_user.getText().toString().equals(obj.getString("USERNAME"))&&
						_email.getText().toString().equals(obj.getString("EMAIL")) &&
						_internalError==-5)_internalError=-6;
				
				
				log("Después: " + obj.toString());
				log("Parámetros: "+parametros.toString());
				errorControl(parametros,_internalError);	
			
			} 
			catch (Exception e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
	 * Error control for modify user.
	 */
	private void errorControl(ArrayList<String> parametros, int _internalError)
	{
		
		switch(_internalError)
		{
			case 0:
			{
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
				
				} 
				catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				break;
			}
			case -1:
			{
				_message = "Box username is empty.";
				break;
			}
			case -2:
			{
				_message = "Box password is empty.";
				break;
			}
			case -3:
			{
				_message = "Password is too short.";
				break;
			}
			case -4:
			{
				_message = "Box e-mail is empty.";
				break;
			}
			case -5:
			{
				_message = "Erroneous format in e-mail box";
				break;
			}
			case -6:
			{
				_message = "No change";
				break;
			}
			default:
			{
				
			}
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
}
