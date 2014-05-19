package ehc.net;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import ehc.net.R;
import gcmService.GCM.TaskRegisterGCM;

import serverConnection.Post;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUser extends Activity
{
	//------------Variables-----------------------
	private Button _buttonCancel;
	private Button _buttonConfirm;
	private EditText _user;
	private final String _ip =Post._ip;
	//--------------------------------------------
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) 
	{
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_user);
				
		/**
         * ------------------------------------
         * Linked:  variable <- component XML
         *-------------------------------------
         **/
		_buttonCancel = (Button) findViewById(R.id.newUserCancel);
		_buttonConfirm = (Button) findViewById(R.id.newUserConfirm);     
   
		// if button is clicked, close the custom dialog
		_buttonCancel.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});		
	 
		// if button is clicked, send the query and close the custom dialog
		_buttonConfirm.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				
				int _internalError = 0;
				
				//It checks if exists connection
				ConnectivityManager _connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo _networkInfo = _connMgr.getActiveNetworkInfo();
			      
				if (_networkInfo == null || !_networkInfo.isConnected()) 			        
				{			            			        			            
					_internalError=-1;  		
				}
			
				/**
				 * ------------------------------------
				 * Linked:  variable <- component XML
				 *-------------------------------------
				 **/
				_user = (EditText) findViewById(R.id.newUser);
				EditText _email = (EditText) findViewById(R.id.newEmail);
				EditText _password = (EditText) findViewById(R.id.newPassword);
				EditText _repeatPassword = (EditText) findViewById(R.id.newRepeatPassword);
				
				//Creation the query.
				ArrayList<String> _parametros = new ArrayList<String>();
				
				_parametros.add("command");
				_parametros.add("createuser2");
				_parametros.add("username");
				_parametros.add(_user.getText().toString());
				_parametros.add("password");
				_parametros.add(Post.md5(_password.getText().toString()));
				_parametros.add("email");
				_parametros.add(_email.getText().toString());
				_parametros.add("hint");
				_parametros.add("");
			
				//Identify errors.
				if( _user.getText().toString().isEmpty())_internalError=-2;
				else if(_email.getText().toString().isEmpty() )_internalError=-3;
				else if(!_email.getText().toString().contains("@"))_internalError=-4;				
				else if (_password.getText().toString().isEmpty())_internalError=-5;
				else if (_password.getText().toString().length()<2)_internalError=-6;
				else if (_repeatPassword.getText().toString().isEmpty())_internalError=-7;
				else if (!_password.getText().toString().equals(_repeatPassword.getText().toString()))_internalError=-8;
				
				errorControl(_parametros,_internalError);				
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

	/**
	 * Error control for creating user.
	 * @param dialog
	 * @param parametros
	 * @param _internalError
	 */
	private void errorControl(ArrayList<String> parametros,int internalError)
	{
		String _message = "";
		switch(internalError)
		{
			case 0:
			{					
		    	createUserConnection _createUser = new createUserConnection(parametros);
		    	_createUser.execute();	    	
				break;
			}
			case -1:
			{
				_message = "Not network connection available.";
				break;
			}
			case -2:
			{
				_message = "User box  is empty.";				
				break;
			}
			case -3:
			{
				_message = "E-mail box is empty.";
				break;
			}
			case -4:
			{
				_message = "Erroneous format in e-mail box.";
				break;
			}
			case -5:
			{
				_message = "Password box  is empty.";				
				break;
			}
			case -6:
			{
				_message = "Password is too sort.";
				break;
			}
			case -7:
			{
				_message = "Box repeat password is empty.";
				break;
			}
			case -8:
			{
				_message = "Passwords do not match.";
				break;
			}				
		}
		if(internalError!=0)Toast.makeText(getBaseContext(), _message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 
	 * @author Miguel
	 *
	 */
	private class createUserConnection extends AsyncTask<String, String, String>
	{
		private ArrayList<String> _parametros;
		private String _message = "";
		private ProgressDialog _pDialog;
		
		public createUserConnection(ArrayList<String> parametros) 
		{
			// TODO Auto-generated constructor stub
			this._parametros = parametros;
		}
		
		/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{
            super.onPreExecute();
            _pDialog = new ProgressDialog(CreateUser.this);
            _pDialog.setMessage("Loading. Please wait...");
            _pDialog.setIndeterminate(false);
            _pDialog.setCancelable(false);
            _pDialog.show();
        }    	
    	    	
		@Override
		protected String doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			//Variable 'Data' saves the query response
			JSONObject _data = Post.getServerData(_parametros,_ip);
			try 
			{
				JSONObject _json_data = _data.getJSONObject("error");
				switch(_json_data.getInt("ERROR"))
				{
					case 0:
					{
						_message = _json_data.getString("ENGLISH");
						//GCM registration.
						TaskRegisterGCM _task = new TaskRegisterGCM(CreateUser.this);
						_task.execute(_user.getText().toString());
						
						break;
					}
					default:
					{
						_message = _json_data.getString("ENGLISH");
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
            // dismiss the dialog
            _pDialog.dismiss();
            Toast.makeText(getBaseContext(), _message, Toast.LENGTH_SHORT).show();
		}
	}
	
	
}
