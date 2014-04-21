package ehc.net;

import java.io.File;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HousesMenu extends Activity implements ImageChooserListener
{

	private TableLayout _table;
	private JSON _JSONFile;
	private static ImageChooserManager imageChooserManager;
	private static int chooserType;
	private ProgressBar pbar;
	private static String filePath;
	private ImageView _currentImage;
	private TextView _textViewFile;
	private HouseListAdapter _ListAdapter;
	private ArrayList<String> _housesList;
	private int _currentOption;
	private boolean _selectMode;
	private static Activity _activity;
	private Post _post;
	private ChosenImage _choosenImage = null;

    private static final int REQUEST_CODE = 6384; // onActivityResult request
                                                  // code
       
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
        
        ArrayList<String> _urls = _JSONFile.getUrlsImage();
                    
        try 
        {
			_housesList = _JSONFile.getHousesName();			
		} 
        catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        _ListAdapter = new HouseListAdapter("",getApplicationContext(),_housesList,_urls,R.layout.house_item);
		_ListView.setAdapter(_ListAdapter);
		_ListAdapter.notifyDataSetChanged ();
			
		_selectMode = false;
		_currentOption = 0;       
		_activity = this;
		_currentImage = new ImageView(_activity);
		_textViewFile = new TextView(_activity);
        
		/*
		new Thread( new Runnable() 
		{
			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				while(true)
				{
					if(_choosenImage!=null)
					{
						_post = new Post();						
						uploadImageConnection _connection = new uploadImageConnection(_textViewFile.getText().toString());
				    	_connection.execute();
				    	_choosenImage=null;
					}
				}
			}
		}).start();*/
    }
	
	/**
	 * 
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
//					_ListAdapter.notifyDataSetChanged();
					_ListAdapter.setCheckOFF();
					_choosenImage = image;
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
		Log.d("IMAGEN","LISTA");
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
    	
    	public uploadImageConnection(String path)
    	{
    		_imagePath = path;
    		Log.d("IMAGE PATH",_imagePath);
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
			JSONObject _data = _post.connectionPostUpload(_parametros, "http://5.231.69.226/EHControlConnect/index.php", _imagePath);
			try 
			{
				JSONObject _json_data = _data.getJSONObject("error");
				_message = _json_data.getString("ENGLISH");			
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();				
			}
			return null;
		}
		
		protected void onPostExecute(String file_url) 
		{
            // dismiss the dialog after getting all products
            _pDialog.dismiss();
            Toast.makeText(_activity, _message, Toast.LENGTH_SHORT).show();
		}
    	
    }
	
	/**
	 * Method for debug
	 * 
	 * @param _text
	 */
	private void log(String _text) {
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
