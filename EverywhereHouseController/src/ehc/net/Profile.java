package ehc.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	private EditText _user;
	private EditText _email;
	private EditText _password;
	private String file;
	private int _modify;
	private ProgressDialog pDialog;
	private Post _post;
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
		
		parser();
		
		
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
	}
	
	/**
	 * Method that encrypts the password
	 * @param s
	 * @return
	 */
	private String md5(String s) 
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
	
	private class modifyConnection extends AsyncTask<String, String, String>
	{
		private ArrayList<String> parametros = new ArrayList<String>();
		private String _message = "";
		
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
			//Variable 'Data' saves the query response
			log("query");

			try 
			{
			
			JSONObject obj = new JSONObject(file);
			
			parametros.add("command");
			parametros.add("modifyuser");
			parametros.add("username");
			parametros.add(obj.getString("USERNAME"));
			parametros.add("password");
			parametros.add(obj.getString("PASSWORD"));
			parametros.add("n_username");
			parametros.add(_user.getText().toString());
			parametros.add("n_password");
			parametros.add(md5(_password.getText().toString()));
			parametros.add("n_email");
			parametros.add(_email.getText().toString());
			parametros.add("n_hint");
			parametros.add("hint");
			
			} 
			catch (Exception e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			log(parametros.toString());
			JSONArray data = _post.getServerData(parametros,"http://5.231.69.226/EHControlConnect/index.php");
			log(data.toString());
			
			try 
			{
				JSONObject json_data = data.getJSONObject(0);
				switch(json_data.getInt("error"))
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
}
