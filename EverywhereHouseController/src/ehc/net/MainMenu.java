package ehc.net;

import java.util.ArrayList;

import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;

import framework.JSON;
import framework.Post;
import framework.SlidingMenuAdapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainMenu extends SherlockActivity 
{
	// ---------Variables----------------
	private Button _buttonProfile;
	private Button _buttonManagement;
	private Button _buttonEvent;
	// private Button _buttonConfig;
	private ImageView _logo;
	private Post _post;

	// -------------------------------

	@Override
    protected void onCreate( Bundle savedInstanceState ) 
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main_menu_view );
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        /////////////////////////////////////////////////////////////////////////////////////////
        final ListView _drawer = (ListView) findViewById(R.id.ListViewSlidingMenu);		
		final SlidingMenuAdapter _adapter = new 
				SlidingMenuAdapter(this.getBaseContext(),getIntent().getExtras().getString("House"));
		_drawer.setAdapter(_adapter);
		_adapter.notifyDataSetChanged();
		
		ImageButton _iv = (ImageButton) findViewById(R.id.lateralMenu);
		_iv.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				DrawerLayout _dl = (DrawerLayout) findViewById(R.id.drawer_layout);
				_dl.openDrawer(_drawer);
			}
		});		
		/////////////////////////////////////////////////////////////////////////////////////////
        /**
         * ------------------------------------
         * Liked:  variable <- XML component 
         *-------------------------------------
         */
        _buttonProfile = ( Button ) findViewById( R.id.buttonProfile );
        _buttonManagement = ( Button ) findViewById( R.id.buttonManagement);
        _buttonEvent = ( Button ) findViewById( R.id.buttonEvents );
       // _buttonConfig = ( Button ) findViewById( R.id.buttonConfig );
        _logo = (ImageView) findViewById(R.id.world_loading_view);
        
        Animation anim = AnimationUtils.loadAnimation(this.getBaseContext(), R.anim.rotate_indefinitely);
        //Start animating the image
         _logo.startAnimation(anim);        
         
        _buttonManagement.setOnClickListener( new View.OnClickListener() 
        {
			@Override
			public void onClick( View v ) 
			{
				log( "Button 'Management'pressed" );
				createdManagementIntent();
			}
		});
        
        _buttonProfile.setOnClickListener( new View.OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				log( "Button 'Profile'pressed" );
				createdProfileIntent();
			}
		});
        
        _buttonEvent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				log(" Button events pressed");
				createdEventIntent();				
			}
		});
        
        _post = new Post();						
    	getWeather _connection = new getWeather();
    	_connection.execute();
                 
    }	
	/**
	 * Method that executes Management's activity
	 */

	private void createdManagementIntent() {
		try {
			Class<?> _clazz = Class.forName("ehc.net.ManagementMenu");
			Intent _intent = new Intent(this, _clazz);
			_intent.putExtra("House",getIntent().getExtras().getString("House"));
			startActivity(_intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that executes Profile's activity
	 */

	private void createdProfileIntent() {
		try {
			Class<?> _clazz = Class.forName("ehc.net.Profile");
			Intent _intent = new Intent(this, _clazz);
			_intent.putExtra("House",getIntent().getExtras().getString("House"));
			startActivity(_intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that executes Profile's activity
	 */

	private void createdEventIntent() 
	{
		try 
		{
			Class<?> _clazz = Class.forName("ehc.net.CaldroidSampleActivity");
			Intent _intent = new Intent(this, _clazz);
			startActivity(_intent);
		} catch (ClassNotFoundException e) {
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
			Class<?> _clazz = Class.forName("ehc.net.HousesMenu");
			Intent _intent = new Intent(this, _clazz);
			startActivity(_intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	// Background process
    private class getWeather extends AsyncTask<String, String, String>
    {    	
    	private ProgressDialog _pDialog;
    	private String _message = "";
    	private int _internalError = 0;
    	/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{  
    		super.onPreExecute();
            _pDialog = new ProgressDialog(MainMenu.this);
            //pDialog.setView(getLayoutInflater().inflate(R.layout.loading_icon_view,null));
            _pDialog.setMessage("Loading. Please wait...");
            _pDialog.setIndeterminate(false);
            _pDialog.setCancelable(false);
            _pDialog.show();          
        }
    	
    	@Override
		protected String doInBackground(String... arg0) 
		{
			try 
			{	
				JSON _JSONFile = JSON.getInstance(getApplicationContext());
				Pair<String,String> _place = _JSONFile.getPlace(getIntent().getExtras().getString("House"));
				
				//Query
				ArrayList<String> _parametros = new ArrayList<String>();
				
				_parametros.add("command");
				_parametros.add("getweather");
				_parametros.add("city");
				_parametros.add(_place.first);
				_parametros.add("country");
				_parametros.add(_place.second);
			 			
				//Variable 'Data' saves the query response
				JSONObject _data = _post.getServerData(_parametros,"http://5.231.69.226/EHControlConnect/index.php");//"http://192.168.2.147/EHControlConnect/index.php");
				log(_data.toString());
				
//				try 
//				{
//					JSONObject _json_data = _data.getJSONObject("error");
//					switch(_json_data.getInt("ERROR"))
//					{
//						case 0:
//						{
//							_message = _json_data.getString("ENGLISH");					
//							break;
//						}
//						default:
//						{
//							_internalError = _json_data.getInt("ERROR");
//							_message = _json_data.getString("ENGLISH");
//							break;
//						}
//					}
//				
//				} 
//				catch (JSONException e) 
//				{
//					e.printStackTrace();
//				}
					 
			 }
			catch (Exception _e) 
			 {
			 	_e.printStackTrace();
			 }
			 // End call to PHP server
			return null;
		}
    	
		protected void onPostExecute(String file_url) 
		{
            // dismiss the dialog
            _pDialog.dismiss();
            if(_internalError!=0)Toast.makeText(getBaseContext(), _message, Toast.LENGTH_SHORT).show();
		}
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
