package ehc.net;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import framework.SlidingMenuAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class MainMenu extends SherlockActivity 
{
	// ---------Variables----------------
	private Button _buttonProfile;
	private Button _buttonManagement;
	private Button _buttonEvent;
	// private Button _buttonConfig;
	private ImageView _logo;
	private ActionBar _ab;

	// -------------------------------

	@Override
    protected void onCreate( Bundle savedInstanceState ) 
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main_menu_view );
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        /////////////////////////////////////////////////////////////////////////////////////////
        ListView _drawer = (ListView) findViewById(R.id.ListViewSlidingMenu);		
		final SlidingMenuAdapter _adapter = new 
				SlidingMenuAdapter(this.getBaseContext(),getIntent().getExtras().getString("House"));
		_drawer.setAdapter(_adapter);
		_adapter.notifyDataSetChanged();
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
			startActivity(_intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that executes Profile's activity
	 */

	private void createdEventIntent() {
		try {
			Class<?> _clazz = Class.forName("ehc.net.CaldroidSampleActivity");
			Intent _intent = new Intent(this, _clazz);
			startActivity(_intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
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

	protected void onResume() {
		super.onResume();
		log("Resumed");
	}

	protected void onPause() {
		super.onPause();
		log("Paused");
	}

	protected void onStop() {
		super.onStop();
		log("Stoped");
	}
}
