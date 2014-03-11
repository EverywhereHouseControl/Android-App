package framework;

import java.util.HashMap;
import ehc.net.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class ContainerFragments extends FragmentActivity
{
	//----------Variables------------
	private AdapterView _mAdapter;
	private ViewPager _mPager;
	//--------------------------------
	
	 @Override
	    protected void onCreate( Bundle savedInstanceState )
	    {
	    	super.onCreate( savedInstanceState );
	        setContentView( R.layout.pager_view);
	        
	        // Create a HashMap with < Key: position, Value button name >.
	        HashMap<String,String> tableButtons = new HashMap<String,String>();
	        
	        for(int i=0; i<getIntent().getExtras().getInt("NumRooms"); i++)
	        {
	        	tableButtons.put(Integer.toString(i), getIntent().getExtras().getString(Integer.toString(i)));
	        }
	       
	    	// Link the XML which contains the pager.
	        _mPager = (ViewPager) findViewById(R.id.pager);
	        // Create an adapter with the fragments that will show on the ViewPager.
	        _mAdapter = new AdapterView(getSupportFragmentManager(),tableButtons); 
	    	// Set the adapter.
	    	_mPager.setAdapter(_mAdapter);
	        
	        // The position of the button that was clicked is obtained.
	    	String buttonClicked = getIntent().getExtras().getString("Room");
	    	String buttonPosition = getIntent().getExtras().getString(buttonClicked);
	    		    	
	        //Move the ViewPager to the desired view.
	        _mPager.setCurrentItem(Integer.parseInt(buttonPosition));
	    }
	 
	 	/**
	     * Method for debug.
	     * @param _text
	     */
	    private void log( String _text )
	    {
	    	Log.d("Action :", _text);
	    }
	    
	    
	    protected void onResume()
	    {
	    	super.onResume();
	    	log( "Resumed" );
	    }
	    
	    protected void onPause()
	    {
	    	super.onPause();
	    	log( "Paused" );
	    }
	    protected void onStop()
	    {
	    	super.onStop();
	    	log( "Stoped" );
	    }
}
