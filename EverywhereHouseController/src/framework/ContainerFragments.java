package framework;

import java.util.HashMap;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import ehc.net.LogIn;
import ehc.net.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ContainerFragments extends SherlockFragmentActivity//FragmentActivity
{
	//----------Variables------------
	private AdapterView _mAdapter;
	private ViewPager _mPager;
	private ActionBar _ab;
	private HashMap<String,String> _tableButtons;
	//--------------------------------	
	
	 @Override
	 protected void onCreate( Bundle savedInstanceState )
	 {
	    	super.onCreate( savedInstanceState );
	        setContentView( R.layout.pager_view);
	        
	        //----------------ActionBar-----
	         _ab = getSupportActionBar();
	         _ab.setDisplayShowHomeEnabled(false);
	         _ab.setDisplayUseLogoEnabled(false);
	         _ab.setDisplayShowTitleEnabled(false);
	         //-------------------------------
 
	        // Create a HashMap with < Key: position, Value button name >.
	        _tableButtons = new HashMap<String,String>();
	        
	        
	        for(int i=0; i<getIntent().getExtras().getInt("NumRooms"); i++)
	        {
	        	_tableButtons.put(Integer.toString(i), getIntent().getExtras().getString(Integer.toString(i)));
	        }
	       
	    	// Link the XML which contains the pager.
	        _mPager = (ViewPager) findViewById(R.id.pager);
	        // Create an adapter with the fragments that will show on the ViewPager.
	        _mAdapter = new AdapterView(getSupportFragmentManager(),_tableButtons); 
	    	// Set the adapter.
	    	_mPager.setAdapter(_mAdapter);
	        
	        // The position of the button that was clicked is obtained.
	    	String buttonClicked = getIntent().getExtras().getString("Room");
	    	String buttonPosition = getIntent().getExtras().getString(buttonClicked);
	    		    	
	        //Move the ViewPager to the desired view.
	        _mPager.setCurrentItem(Integer.parseInt(buttonPosition));
	        ////////////////////////////////////////////////////////////////////////////////
	        LinearLayout _l = (LinearLayout) findViewById(R.id.HorizontalListView);
	        for(int i=0; i<_tableButtons.size(); i++)
	        {
	        	final Button _newButton = new Button(this.getApplicationContext());
	        	_newButton.setBackgroundResource(R.drawable.button_config);
	        	_newButton.setTextColor(R.drawable.button_text_color);
	        	_newButton.setText(_tableButtons.get(Integer.toString(i)));
	        	_newButton.setOnClickListener(new View.OnClickListener() 
	        	{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						 _mPager.setCurrentItem(Integer.parseInt(getIntent().getExtras().getString(_newButton.getText().toString())));
					}
				});
	        	_l.addView(_newButton, i);
	        }
	        ////////////////////////////////////////////////////////////////////////////////
	 }
	 
	 @Override
	 public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) 
	 {
		 getSupportMenuInflater().inflate(R.menu.action_bar, menu);
		 return super.onCreateOptionsMenu(menu);
	 }
	 
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) 
	    {
	        super.onOptionsItemSelected(item);
	 
	        switch(item.getItemId())
	        {
	            case R.id.Profile:
				try 
				{
					Class<?> _clazz;
					_clazz = Class.forName( "ehc.net.Profile");
					Intent _intent = new Intent( this,_clazz );
	    			startActivity( _intent );
				} 
				catch (ClassNotFoundException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	                break;
	            case R.id.ChangeProfile:
	            	Intent exitIntent = new Intent(this,LogIn.class);
	            	exitIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            	startActivity(exitIntent);  	
	                break;
	            case R.id.Exit:
	            	Intent intent = new Intent(Intent.ACTION_MAIN);
	            	intent.addCategory(Intent.CATEGORY_HOME);
	            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            	startActivity(intent);  	
	                break;
	        }
	        return true;
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
