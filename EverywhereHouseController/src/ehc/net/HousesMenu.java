package ehc.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import framework.JSON;
import framework.HouseListAdapter;
import framework.Post;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
	private int _currentOption;
	private boolean _selectMode;
	private static Activity _activity;
	private Post _post;
	JSONObject _data = new JSONObject();
	//------------------------------------------
	private String _file;
	public static String _currentHouse;
       
	@Override
    protected void onCreate( Bundle savedInstanceState ) 
    {
		super.onCreate( savedInstanceState );
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/*
		Button button = new Button(this);
        button.setText(R.string.choose_file);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the file chooser dialog
                showChooser();
            }
        });

        setContentView(button);*/
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
        
        _ListAdapter = new HouseListAdapter(getApplicationContext(),_housesList,_urls,_accessHousesList,R.layout.house_item);
		_ListView.setAdapter(_ListAdapter);
		_ListAdapter.notifyDataSetChanged ();
		
		ImageButton _imageButton = (ImageButton) findViewById(R.id.imageOverflow);
		_imageButton.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				openOptionsMenu();
			}
		});
				
			
		_selectMode = false;
		_currentOption = 0;       
		_activity = this;
		_currentImage = new ImageView(_activity);
		_textViewFile = new TextView(_activity);
		parser();
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

	@Override
	public void onOptionsMenuClosed(Menu menu) 
	{
		// TODO Auto-generated method stub
		//super.onOptionsMenuClosed(menu);
		if(!_selectMode)
			setCheckBoxOFF();
		_selectMode = false;
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) 
	{
		// TODO Auto-generated method stub
		setCheckBoxON();
		return super.onMenuOpened(featureId, menu);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
	{
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        super.onOptionsItemSelected(item);
        
        switch(item.getItemId())
        {
	        case R.id.TakePicture:
	        	_selectMode = true;
	        	_currentOption = 1;
	        	_ListAdapter.setChosenOption(_currentOption);
	        	setCheckBoxON();
	        	break;    
	        case R.id.Picture:
	        	_selectMode = true;
	        	_currentOption = 2;
	        	_ListAdapter.setChosenOption(_currentOption);
	        	setCheckBoxON();
                break;
            case R.id.NewPicture:
                break;
            
        }
        return true;
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
     */
    private void setCheckBoxON()
    {
    	_ListAdapter.setCheckON();    	
    }
    
    /**
     * 
     */
    private void setCheckBoxOFF()
    {
    	_ListAdapter.setCheckOFF();
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
					_ListAdapter.setStateChosenImage(true);
					_ListAdapter.setPathImage(_textViewFile.getText().toString());
					_ListAdapter.setCheckOFF();
					
					_post = new Post();						
					uploadImageConnection _connection = new uploadImageConnection(_textViewFile.getText().toString());
			    	_connection.execute();
			    	
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
            _pDialog = new ProgressDialog(_activity);
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
			_data = _post.connectionPostUpload(_parametros, "http://5.231.69.226/EHControlConnect/index.php", _imagePath);			
			
			runOnUiThread(new Runnable() 
			{
				@Override
				public void run() 
				{
					// TODO Auto-generated method stub
//					try 
//					{
//						JSONObject _json_data = _data.getJSONObject("error");
//						_message = _json_data.getString("ENGLISH");
//					} 
//					catch (Exception e) 
//					{
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						_message = "internal error.";
//					}
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
					Toast.makeText(_activity, _message, Toast.LENGTH_SHORT).show();
					
					if(_correct)
					{
						try 
						{
							JSONObject obj = new JSONObject(_file);
						
							ArrayList<String> _parametros = new ArrayList<String>();
							_parametros.add("command");
							_parametros.add("modifyhouse");
							_parametros.add("username");
							_parametros.add(obj.getString("USERNAME"));
							_parametros.add("housename");
							_parametros.add(_currentHouse);
							_parametros.add("n_housename");
							_parametros.add(_currentHouse);
							_parametros.add("idimage");
							_parametros.add("http://ehcontrol.net/EHControlConnect/images/"+_imagePath);
							_data = _post.getServerData(_parametros, "http://5.231.69.226/EHControlConnect/index.php");
						
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
						catch (JSONException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Toast.makeText(_activity, _message, Toast.LENGTH_SHORT).show();
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
    	//super.onBackPressed();
    	
    	new AlertDialog.Builder(this)
        .setTitle("Log out")
        .setMessage("Are you sure you want to log out?")
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) 
            { 
            	try 
        		{
        			Class<?> _clazz = Class.forName("ehc.net.LogIn");
        			Intent _intent = new Intent(HousesMenu.this, _clazz);
        			startActivity(_intent);
        		} 
            	catch (ClassNotFoundException e) 
        		{
        			e.printStackTrace();
        		}
            }
         })
        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) 
            { 
                // do nothing
            }
         })
        .setIcon(android.R.drawable.ic_dialog_alert)
         .show();
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
		_ListAdapter.setCheckOFF();
		log("Paused");
	}

	protected void onStop() 
	{
		super.onStop();
		_ListAdapter.setCheckOFF();
		log("Stoped");
	}
}
