package ehc.net;

import java.io.FileOutputStream;
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
	private String _file;
	private ProgressDialog _pDialog;
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
			InputStream _is = openFileInput("profileInformation.json");
			int _size = _is.available();
	        byte[] buffer = new byte[_size];
	        _is.read(buffer);
	        _is.close();
	        this._file =null;
	        this._file = new String(buffer, "UTF-8");
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
		JSONObject _obj = new JSONObject(_file);
		_user.setText(_obj.getString("USERNAME"));
		_email.setText(_obj.getString("EMAIL"));
		_password.setText("*/*^^*/*^^*/*^^*/*");
	}
	
	
	private class modifyConnection extends AsyncTask<String, String, String>
	{
		private ArrayList<String> _parametros = new ArrayList<String>();
		
		/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{
            super.onPreExecute();
            _pDialog = new ProgressDialog(Profile.this);
            _pDialog.setMessage("Loading. Please wait...");
            _pDialog.setIndeterminate(false);
            _pDialog.setCancelable(false);
            _pDialog.show();
        }    	
    	    	
		@Override
		protected String doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			int _internalError = 0;
			
			log("query");

			try 
			{
				JSONObject obj = new JSONObject(_file);
				log("FILE MODIFYUSER: " + obj.toString());
			
				_parametros.add("command");
//				_parametros.add("modifyuser");
				_parametros.add("modifyuser2");
				_parametros.add("username");
				_parametros.add(obj.getString("USERNAME"));
				_parametros.add("password");
				_parametros.add(obj.getString("PASSWORD"));
				_parametros.add("n_username");
				_parametros.add(_user.getText().toString());
				_parametros.add("n_password");
				if(_password.getText().toString().equals("*/*^^*/*^^*/*^^*/*") || _post.md5(_password.getText().toString()).equals(obj.getString("PASSWORD")))
				{
					_parametros.add(obj.getString("PASSWORD"));
					_internalError = -7;
				}
				else _parametros.add(_post.md5(_password.getText().toString()));
				
				_parametros.add("n_email");
				_parametros.add(_email.getText().toString());
				_parametros.add("n_hint");
				_parametros.add(" ");
				
				if(_user.getText().toString().isEmpty())_internalError=-1;
				else if(_password.getText().toString().isEmpty())_internalError=-2;
				else if(_password.getText().toString().length()<2)_internalError=-3;
				else if(_email.getText().toString().isEmpty())_internalError=-4;
				else if(!_email.getText().toString().contains("@"))_internalError=-5;
				else if(_user.getText().toString().equals(obj.getString("USERNAME"))&&
						_email.getText().toString().equals(obj.getString("EMAIL")) &&
						_internalError==-7)_internalError=-6;
				
				if(errorControl(_parametros,_internalError))
				{
					_parametros.add("command");
					_parametros.add("login2");
					_parametros.add("username");
					_parametros.add(_user.getText().toString());
					_parametros.add("password");
					
					JSONObject _obj = new JSONObject(_file);
					
					_parametros.add(_obj.getString("PASSWORD"));
					
					//Variable 'Data' saves the query response
					JSONObject _data = _post.getServerData(_parametros,"http://5.231.69.226/EHControlConnect/index.php");//"http://192.168.2.147/EHControlConnect/index.php");
					
					saveProfileInfo(_data.getJSONObject("result"));
					parser();
				}
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
            _pDialog.dismiss();
            Toast.makeText(getBaseContext(), _message, Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * Error control for modify user.
	 */
	private boolean errorControl(ArrayList<String> parametros, int _internalError)
	{	
		switch(_internalError)
		{
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
				JSONObject _data = _post.getServerData(parametros,"http://5.231.69.226/EHControlConnect/index.php");//"http://192.168.2.147/EHControlConnect/index.php");
				//log(_data.toString());
				try 
				{
					JSONObject _json_data = _data.getJSONObject("error");
					switch(_json_data.getInt("ERROR"))
					{
						case 0:
						{
							_message = _json_data.getString("ENGLISH");							
					    	return true;
//							break;
						}
						default:
						{
							_message = _json_data.getString("ENGLISH");
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
		}
		return false;
	}
	
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
     * Method for debug
     * @param _text
     */
    private void log( String _text )
    {
    	Log.d( "Action :", _text );
    }
}
