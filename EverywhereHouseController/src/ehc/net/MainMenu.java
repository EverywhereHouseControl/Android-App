package ehc.net;

import java.util.ArrayList;

import org.json.JSONException;
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
import android.widget.TextView;
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
	private Double _temp = 0.0;

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
    	private JSONObject _data;
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
				_parametros.add(_place.first.toUpperCase());
				_parametros.add("country");
				_parametros.add(_place.second.toUpperCase());
			 			
				//Variable 'Data' saves the query response
				_data = _post.getServerData(_parametros,"http://5.231.69.226/EHControlConnect/index.php");//"http://192.168.2.147/EHControlConnect/index.php");
				log(_data.toString());
				
				
//				ImageView  _weather = (ImageView) findViewById(R.id.WeatherImage); 
//				Animation anim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.rotate_indefinitely);
//		        //Start animating the image
//		         _weather.startAnimation(anim); 
				
//				String _clima = _data.getString("main");
//				if(_clima.equals("Clouds"))
//				{
//					_weather.setImageResource(R.drawable.clouds);
//				}
//				else if (_clima.equals("Rain"))
//				{
//					_weather.setImageResource(R.drawable.clouds);
//				}
//				else if (_clima.equals("Clear"))
//				{
//					_weather.setImageResource(R.drawable.clouds);
//				}
				
				
				
				
				
				
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
            
            try 
            {
				_temp = (Double) _data.get("temperature");
			} catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_temp = _temp - 273.15;
            TextView _temperature = (TextView) MainMenu.this.findViewById(R.id.temperature);
    		_temperature.setText(Integer.toString(_temp.intValue()) + "ºC");
    		Log.d("TEMPERATURE", Integer.toString(_temp.intValue()));
	         
    		ImageView  _weather1 = (ImageView)  MainMenu.this.findViewById(R.id.WeatherImage1); 
    		ImageView  _weather2 = (ImageView)  MainMenu.this.findViewById(R.id.WeatherImage2);
    		ImageView  _weather3 = (ImageView)  MainMenu.this.findViewById(R.id.WeatherImage3);
    		ImageView  _weather4 = (ImageView)  MainMenu.this.findViewById(R.id.WeatherImage4);
    		ImageView  _weather5 = (ImageView)  MainMenu.this.findViewById(R.id.WeatherImage5);
	         String _clima = null;
			try 
			{
				_clima = _data.getString("main");
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				if(_clima.equals("Clouds"))
				{
					Animation anim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.translate_enter);
					//Start animating the image
			         _weather1.startAnimation(anim);
					_weather1.setImageResource(R.drawable.clouds);
					Animation anim2 = AnimationUtils.loadAnimation(MainMenu.this, R.anim.translate_exit);
					//Start animating the image
			        _weather2.startAnimation(anim2);
					_weather2.setImageResource(R.drawable.clouds);
				}
				else if (_clima.equals("Rain"))
				{
					Animation anim1 = AnimationUtils.loadAnimation(MainMenu.this, R.anim.translate_down);
					//Start animating the image
			         _weather4.startAnimation(anim1);
					_weather4.setImageResource(R.drawable.rain);
					Animation anim2 = AnimationUtils.loadAnimation(MainMenu.this, R.anim.translate_down);
					//Start animating the image
			        _weather5.startAnimation(anim2);
					_weather5.setImageResource(R.drawable.rain);

					_weather1.setImageResource(R.drawable.clouds);
					_weather2.setImageResource(R.drawable.clouds);
				}
				else if (_clima.equals("Clear"))
				{
					_weather3.setImageResource(R.drawable.sun);
					Animation anim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.rotate_indefinitely);
					//Start animating the image
			         _weather3.startAnimation(anim);
			         
				}
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
