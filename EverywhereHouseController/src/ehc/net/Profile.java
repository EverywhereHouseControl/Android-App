package ehc.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import loadUrlImage.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import serverConnection.Post;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import ehc.net.R;


import adapters.HouseListAdapter;
import adapters.SlidingMenuAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Configuration;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Profile extends SherlockActivity implements ImageChooserListener
{
	//---------Variables-------------------------------
	private Button _buttonSave;
	private Button _buttonExit;
	private EditText _user;
	private ImageView _imageDialog;
	private EditText _email;
	private EditText _password;
	private String _file;
	private Post _post;
	private String _message = "";
	private String _currentHouse;
	private ImageLoader _imgLoader = HouseListAdapter._imgLoader;
	private static ImageChooserManager imageChooserManager;
	private static int chooserType;
	private static String filePath;
	private String _url;
	private boolean _selectImage,isModifyMode;
	private ChosenImage _selectedImage = new ChosenImage();
	//-------------------------------------------------
	private ActionBarDrawerToggle _actbardrawertoggle;
	private DrawerLayout _dl;
	private ListView _drawer;
	// -------------------------------
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_view);
		_currentHouse = getIntent().getExtras().getString("House");
		/////////////////////////////////////////////////////////////////////////////////////////
		_drawer = (ListView) findViewById(R.id.ListViewSlidingMenu);		
		final SlidingMenuAdapter _adapter = new 
		SlidingMenuAdapter(this.getBaseContext(),_currentHouse);
		_drawer.setAdapter(_adapter);
		_adapter.notifyDataSetChanged();
		
		getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        
		
		_dl = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		_actbardrawertoggle= new ActionBarDrawerToggle(this, _dl, R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close)
        {
			public void onDrawerClosed(View view) 
			{
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) 
			{
				super.onDrawerOpened(drawerView);
			
			}    	 
        };
        
        _dl.setDrawerListener(_actbardrawertoggle);	
		/////////////////////////////////////////////////////////////////////////////////////////
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
		
		ImageButton _image = (ImageButton) findViewById(R.id.UserImage);		
		_image.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				// custom dialog
				_selectImage = false;
				final Dialog dialog = new Dialog(Profile.this);
				dialog.setTitle("Image");
				dialog.setContentView(R.layout.image_dialog);
				
				_imageDialog = (ImageView) dialog.findViewById(R.id.userImageDialog);
				_imgLoader.DisplayImage(_url, R.drawable.base_picture, _imageDialog, 1);
				
				ImageButton _takePicture = (ImageButton)dialog.findViewById(R.id.takePicture);
				_takePicture.setOnClickListener(new View.OnClickListener() 
				{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						takePicture();
					}
				});
				
				ImageButton _choosePicture = (ImageButton)dialog.findViewById(R.id.choosePicture);
				_choosePicture.setOnClickListener(new View.OnClickListener() 
				{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						chooseImage();
					}
				});
				
				dialog.onBackPressed();
				dialog.setOnCancelListener(new OnCancelListener() 
				{
					
					@Override
					public void onCancel(DialogInterface dialog) 
					{
						// TODO Auto-generated method stub
						
						if(_selectImage)
						{
						
							new AlertDialog.Builder(Profile.this)
					        .setTitle("Image")
					        .setMessage("Save changes?")
					        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() 
					        {
					            public void onClick(DialogInterface dialog, int which) 
					            { 
					                // do nothing
					            }
					         })
					         .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() 
					        {
					            public void onClick(DialogInterface dialog, int which) 
					            { 
									_post = new Post();	
									isModifyMode = false;
									profileConnection _connection = new profileConnection(_selectedImage.getFilePathOriginal());
							    	_connection.execute();
					            }
					         })
					        .setIcon(android.R.drawable.ic_dialog_alert)
					         .show();
						}
					}
				});
				
				dialog.show();
			}
		});
						
		_buttonSave.setOnClickListener( new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				_post = new Post();		
				isModifyMode = true;
				profileConnection _connection = new profileConnection(_url);
		    	_connection.execute();	  
			}
		});
		
		_buttonExit.setOnClickListener( new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		parser();

		try 
		{
			JSONObject _obj = new JSONObject(_file);
			_url = _obj.getString("URL");
			if(_url!=null)
			{
				_imgLoader.DisplayImage(_url, R.drawable.base_picture, _image, 0);
			}
			else _image.setImageResource(R.drawable.base_picture);
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//-----------------------------------------
		ThreadPolicy tp = ThreadPolicy.LAX; 
		StrictMode.setThreadPolicy(tp);
		//--------------------------------------------
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) 
	{
		// TODO Auto-generated method stub
		if(item.getItemId()==android.R.id.home)
		 {
			 if(_dl.isDrawerOpen(_drawer))
			 {
				 _dl.closeDrawer(_drawer);
			 }
			 else {
				_dl.openDrawer(_drawer);
			}
		 }
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) 
	{
		super.onPostCreate(savedInstanceState);
		_actbardrawertoggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		_actbardrawertoggle.onConfigurationChanged(newConfig);
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
	
	// Background process
    private class profileConnection extends AsyncTask<String, String, String>
    {
    	private ProgressDialog _pDialog;
    	private String _imagePath;
    	private boolean _correct = false;
    	JSONObject _data = null;
    	ArrayList<String> _parametros;
    	int _internalError = 0;
    	
    	public profileConnection(String path)
    	{
    		_imagePath = path;
    	}
    	
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
			//Query			
			if(!isModifyMode)
			{
				_parametros = new ArrayList<String>();
				_parametros.add("command");
				_parametros.add("subir");
				_data = _post.connectionPostUpload(_parametros, "http://5.231.69.226/EHControlConnect/index.php", _imagePath);			
			}
			
			runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
					// TODO Auto-generated method stub
					if(!isModifyMode)
					{
						try 
						{
							JSONObject _json_data = _data.getJSONObject("error");
							switch(_json_data.getInt("ERROR"))
							{
								case 0:
								{
									_message = _json_data.getString("ENGLISH");
									_correct = true;
									break;
								}
								default:
								{
									_message = _json_data.getString("ENGLISH");
									_correct = false;
									break;
								}
							}
						
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
							_message = "internal error.";
						}
						Toast.makeText(Profile.this, _message, Toast.LENGTH_SHORT).show();	
					}
					
					JSONObject obj = null;
					
					if(_correct || isModifyMode)
					{
						try 
						{
							obj = new JSONObject(_file);
							int _internalError = 0;
							log(_imagePath);
							File file = new File(_imagePath);
							log(file.getName());
							
							_parametros = new ArrayList<String>();
							_parametros.add("command");
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
								Log.d("PASSWORD",obj.getString("PASSWORD"));
							}
							else _parametros.add(_post.md5(_password.getText().toString()));
							
							_parametros.add("n_email");
							_parametros.add(_email.getText().toString());
							_parametros.add("n_hint");
							_parametros.add(" ");
							_parametros.add("image");
							_parametros.add("images/"+file.getName());
							
							if(_user.getText().toString().isEmpty())_internalError=-1;
							else if(_password.getText().toString().isEmpty())_internalError=-2;
							else if(_password.getText().toString().length()<2)_internalError=-3;
							else if(_email.getText().toString().isEmpty())_internalError=-4;
							else if(!_email.getText().toString().contains("@"))_internalError=-5;
							else if(_user.getText().toString().equals(obj.getString("USERNAME"))&&
									_email.getText().toString().equals(obj.getString("EMAIL")) &&
									_internalError==-7)_internalError=-6;
						} 
						catch (Exception e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(errorControl(_parametros,_internalError))
						{
							try 
							{
								ArrayList<String> _parametros = new ArrayList<String>();
								_parametros.add("command");
								_parametros.add("login2");
								_parametros.add("username");
								_parametros.add(obj.getString("USERNAME"));
								_parametros.add("password");
								_parametros.add(obj.getString("PASSWORD"));
								
								//Variable 'Data' saves the query response
								JSONObject _data = _post.getServerData(_parametros,"http://5.231.69.226/EHControlConnect/index.php");//"http://192.168.2.147/EHControlConnect/index.php");
								JSONObject _json_data = _data.getJSONObject("result");
								
								//Save the profile's information.
								saveProfileInfo(_json_data);
								//Reload the view
								onCreate(null);
							} 
							catch (Exception e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						Toast.makeText(Profile.this, _message, Toast.LENGTH_SHORT).show();	
					}
				}
			});			
			return null;
		}
		
		protected void onPostExecute(String file_url) 
		{
            // dismiss the dialog after getting all products
			super.onPostExecute(file_url);
            _pDialog.dismiss();
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
				try 
				{
					JSONObject _json_data = _data.getJSONObject("error");
					switch(_json_data.getInt("ERROR"))
					{
						case 0:
						{
							_message = _json_data.getString("ENGLISH");							
					    	return true;
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
	 * Method that executes the previous activity
	 */
	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		try 
		{
			Class<?> _clazz = Class.forName("ehc.net.MainMenu");
			Intent _intent = new Intent(this, _clazz);
			_intent.putExtra("House",_currentHouse);
			startActivity(_intent);
		} catch (ClassNotFoundException e) {
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


    /**
     * 
     */
    private void takePicture() 
    {
		chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
		imageChooserManager = new ImageChooserManager(this,
				ChooserType.REQUEST_CAPTURE_PICTURE, "myfolder", true);
		imageChooserManager.setImageChooserListener((ImageChooserListener) this);
		try 
		{
			filePath = imageChooserManager.choose();
		} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 
     */
    private void chooseImage() 
    {
		chooserType = ChooserType.REQUEST_PICK_PICTURE;
		imageChooserManager = new ImageChooserManager(this,
				ChooserType.REQUEST_PICK_PICTURE, "myfolder", true);
		imageChooserManager.setImageChooserListener((ImageChooserListener) this);
		try 
		{
			filePath = imageChooserManager.choose();
		} catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
    
	@Override
	public void onImageChosen(final ChosenImage image) 
	{
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() 
		{
			@Override
			public void run() 
			{
				if (image != null) 
				{	

					_imageDialog.setImageURI(Uri.parse(new File(image.getFileThumbnail()).toString()));
					_selectImage = true;
					_selectedImage = image;			    	
					try 
					{
						finalize();
					} 
					catch (Throwable e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	@Override
	public void onError(String reason) 
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (resultCode == RESULT_OK
				&& (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
			if (imageChooserManager == null) 
			{
				reinitializeImageChooser();
			}
			imageChooserManager.submit(requestCode, data);
		} 
	}
	
	// Should be called if for some reason the ImageChooserManager is null (Due
	// to destroying of activity for low memory situations)
	private void reinitializeImageChooser() 
	{
		imageChooserManager = new ImageChooserManager(this, chooserType,
				"myfolder", true);
		imageChooserManager.setImageChooserListener(this);
		imageChooserManager.reinitialize(filePath);
	}

}
