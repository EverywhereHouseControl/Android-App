package ehc.net;

import java.io.File;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;
import serverConnection.Post;
import serverConnection.Post.logOutConnection;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import ehc.net.R;

import adapters.HouseListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HousesMenu extends Activity implements ImageChooserListener
{
	//------------Variables-----------------------
	private JSON _JSONFile;
	private static ImageChooserManager imageChooserManager;
	private static int chooserType;
	private static String filePath;
	private ImageView _currentImage;
	private TextView _textViewFile;
	private HouseListAdapter _ListAdapter;
	private ArrayList<String> _housesList;
	public static boolean _selectMode;
	private static Activity _activity;
	JSONObject _data = new JSONObject();
	private String _file;
	public static String _currentHouse;
	private static ChosenImage _chosenImage = new ChosenImage();
	public static CheckBox _check;
	private Context _context;   
	//-------------------------------------------------------------
	
	@Override
    protected void onCreate( Bundle savedInstanceState ) 
    {
		super.onCreate( savedInstanceState );
		
		setContentView( R.layout.houses_menu_view);
		
		/**
         * ------------------------------------
         * Liked:  variable <- XML component 
         *-------------------------------------
         */
		 ListView _ListView = (ListView) findViewById(R.id.HousesListView);
        //-----------------It Reads config.json-----------------
        _JSONFile = JSON.getInstance(getApplicationContext());
        _housesList = new ArrayList<String>();
        
        if(_housesList.isEmpty())
        {
        	_context = this.getBaseContext();
        	ImageButton _button = (ImageButton) findViewById(R.id.HouseImageButton);
        	_button.setOnClickListener(new View.OnClickListener() 
        	{				
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					new AlertDialog.Builder(HousesMenu.this)
					.setTitle("Log out")
					.setMessage("Do you want to log out?")
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
					        logOutConnection _connection = new logOutConnection(HousesMenu.this);
					    	_connection.execute();
					    	
					    	Intent _intent = new Intent(_context,LogIn.class);
							_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			            	startActivity(_intent);
						}
					})
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();						
				}
			});
        }
        
        ArrayList<String> _accessHousesList = new ArrayList<String>();
        
        ArrayList<String> _urls = _JSONFile.getUrlsImage();
        				
        try 
        {
			_housesList = _JSONFile.getHousesName();
			_accessHousesList = _JSONFile.getHousesAccess();
		} 
        catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        _ListAdapter = new HouseListAdapter(HousesMenu.this,_housesList,_urls,_accessHousesList,R.layout.house_item);
		_ListView.setAdapter(_ListAdapter);
		_ListAdapter.notifyDataSetChanged ();
				
			
		_selectMode = false;     
		_activity = HousesMenu.this;
		_currentImage = new ImageView(_activity);
		_textViewFile = new TextView(_activity);
		
		//It's load the profile's information.
		_file = JSON.loadUserInformation(HousesMenu.this);
		
		_check = (CheckBox)findViewById(R.id.HouseCheckBox);
		_check.setVisibility(View.GONE);
		_check.setChecked(false);
		_check.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				// TODO Auto-generated method stub
				if(isChecked)
				{					
					uploadImageConnection _connection = new uploadImageConnection(_chosenImage.getFilePathOriginal());
			    	_connection.execute();
					
				}
			}
		});
		//-------------------------------------------
		ThreadPolicy tp = ThreadPolicy.LAX; 
		StrictMode.setThreadPolicy(tp);
		//-------------------------------------------
    }
	
	/**
	 /**
     * -----------------------------------------
     * Executes the MainMenu's Activity for a explicit house
     * -----------------------------------------
	 * @param house
	 */
	public static void createdMainMenuIntent(String house) 
	{
		try 
    	{
			Class<?> _clazz = Class.forName( "ehc.net.MainMenu" );
			Intent _intent = new Intent( _activity,_clazz );
			_intent.putExtra("House",house);
			_activity.startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
	}
    
    /**
     * 
     */
    public static void takePicture() 
    {
		chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
		imageChooserManager = new ImageChooserManager(_activity,
				ChooserType.REQUEST_CAPTURE_PICTURE, "myfolder", true);
		imageChooserManager.setImageChooserListener((ImageChooserListener) _activity);
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
    public static void chooseImage() 
    {
		chooserType = ChooserType.REQUEST_PICK_PICTURE;
		imageChooserManager = new ImageChooserManager(_activity,
				ChooserType.REQUEST_PICK_PICTURE, "myfolder", true);
		imageChooserManager.setImageChooserListener((ImageChooserListener) _activity);
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
					_textViewFile.setText(image.getFilePathOriginal());
					_currentImage.setImageURI(Uri.parse(new File(image.getFileThumbnail()).toString()));
					_ListAdapter.setChosenImage(image);
					_ListAdapter.setPathImage(_textViewFile.getText().toString());
					_selectMode = true;
					_chosenImage = image;
					
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
	
	// Background process
    private class uploadImageConnection extends AsyncTask<String, String, String>
    {

    	private ProgressDialog _pDialog;
    	private String _message = "";
    	private String _imagePath;
    	private boolean _correct = false;
    	
    	
    	public uploadImageConnection(String path)
    	{
    		_imagePath = path;
    	}
    	
    	/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{  
    		super.onPreExecute();
            _pDialog = new ProgressDialog(HousesMenu.this);
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
			ArrayList<String> _parametros = new ArrayList<String>();
			_parametros.add("command");
			_parametros.add("subir");
			
			File _imageFile = new File(_chosenImage.getFileThumbnail());
			_imageFile.getAbsolutePath();
			_imagePath = _imageFile.getAbsolutePath();
			
			_data = Post.connectionPostUpload(_parametros, "http://5.231.69.226/EHControlConnect/index.php", _imagePath);			
			
			runOnUiThread(new Runnable()
			{
				@Override
				public void run() 
				{
					// TODO Auto-generated method stub
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
					Toast.makeText(HousesMenu.this, _message, Toast.LENGTH_SHORT).show();
					JSONObject obj = null;
					if(_correct)
					{
						try 
						{							
							obj = new JSONObject(_file);
							JSON _JSONFile = JSON.getInstance(HousesMenu.this);
							Pair<String,String> _place = _JSONFile.getPlace(_currentHouse);
							
							File file = new File(_imagePath);
							
							ArrayList<String> _parametros = new ArrayList<String>();
							_parametros.add("command");
							_parametros.add("modifyhouse");
							_parametros.add("username");
							_parametros.add(obj.getString("USERNAME"));
							_parametros.add("housename");
							_parametros.add(_currentHouse);
							_parametros.add("n_housename");
							_parametros.add(_currentHouse);
							_parametros.add("city");
							_parametros.add(_place.first);
							_parametros.add("country");
							_parametros.add(_place.second);
							_parametros.add("image");
							_parametros.add("images/"+file.getName());
							
							Log.d("PARAMETROS",_parametros.toString());
							
							_data = Post.getServerData(_parametros, "http://5.231.69.226/EHControlConnect/index.php");
							
							Log.d("DATA",_data.toString());
							
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Toast.makeText(HousesMenu.this, _message, Toast.LENGTH_SHORT).show();
						if(_correct)
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
								JSONObject _data = Post.getServerData(_parametros,"http://5.231.69.226/EHControlConnect/index.php");//"http://192.168.2.147/EHControlConnect/index.php");
								JSONObject _json_data = _data.getJSONObject("result");
								
								//Save the profile's information.
								JSON.saveProfileInfo(_json_data,HousesMenu.this);
								//Save the house's configuration
								JSON.saveConfig(_json_data.get("JSON"),HousesMenu.this);
								//Reload the view.
								onCreate(null);
							} 
							catch (Exception e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
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
    @Override
    public void onBackPressed() 
    {
    	// TODO Auto-generated method stub
    	Intent _intent = new Intent(Intent.ACTION_MAIN);
    	_intent.addCategory(Intent.CATEGORY_HOME);
    	_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	this.startActivity(_intent);
    }
    
	/**
	 * Method for debug
	 * 
	 * @param _text
	 */
	private void log(String _text) 
	{
		Log.d("Action :", _text);
	}

	protected void onResume() 
	{
		super.onResume();
		log("Resumed");
	}

	protected void onPause() 
	{
		super.onPause();
		log("Paused");
	}

	protected void onStop() 
	{
		super.onStop();
		log("Stoped");
	}
}
