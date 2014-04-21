package framework;

import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import ehc.net.ItemsActivity;
import ehc.net.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;

public class ContainerFragments extends SherlockFragmentActivity//FragmentActivity
{
	//----------Variables------------
	private AdapterView _mAdapter;
	private ViewPager _mPager;
	private ActionBar _ab;
	private HashMap<String,String> _tableButtons;
	private TabsAdapter _mTabsAdapter;
	private String _houseName;
	//--------------------------------	
	
	 @Override
	 protected void onCreate( Bundle savedInstanceState )
	 {
	    	super.onCreate( savedInstanceState );
	        setContentView( R.layout.pager_view);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        
	        //----------------ActionBar-----
	        ActionBar _ab = getSupportActionBar();
			_ab.setDisplayShowHomeEnabled(false);
	        _ab.setDisplayUseLogoEnabled(false);
	        _ab.setDisplayShowTitleEnabled(false);
			_ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	         //-------------------------------
			
	        /////////////////////////////////////////////////////////////////////////////////////////
	        ListView _drawer = (ListView) findViewById(R.id.ListViewSlidingMenu);		
			final SlidingMenuAdapter _adapter = new 
					SlidingMenuAdapter(this.getBaseContext(),getIntent().getExtras().getString("House"));
			_drawer.setAdapter(_adapter);
			_adapter.notifyDataSetChanged();
			/////////////////////////////////////////////////////////////////////////////////////////
 
	        // Create a HashMap with < Key: position, Value button name >.
	        _tableButtons = new HashMap<String,String>();
	        
	        
	        for(int i=0; i<getIntent().getExtras().getInt("NumRooms"); i++)
	        {
	        	_tableButtons.put(Integer.toString(i), getIntent().getExtras().getString(Integer.toString(i)));
	        }
	       
	    	// Link the XML which contains the pager.
	        _mPager = (ViewPager) findViewById(R.id.pager);
	        // Create an adapter with the fragments that will show on the ViewPager.
	       _houseName = getIntent().getExtras().getString("House");
	       
//	        _mAdapter = new AdapterView(getSupportFragmentManager(),_tableButtons,_houseName); 
	        
	        _mTabsAdapter = new TabsAdapter(this, _mPager);
	        
	    	for(int i=0; i<_tableButtons.size(); i++)
	    	{
	    		_mTabsAdapter.addTab(_ab.newTab().setText(_tableButtons.get(Integer.toString(i))),null, null);
	    	}
	        
	        
	        // Set the adapter.
//	    	_mPager.setAdapter(_mAdapter);
	        
	        // The position of the button that was clicked is obtained.
	    	String buttonClicked = getIntent().getExtras().getString("Room");
	    	String buttonPosition = getIntent().getExtras().getString(buttonClicked);
	    		    	
	        //Move the ViewPager to the desired view.
	        _mPager.setCurrentItem(Integer.parseInt(buttonPosition));
	 }
	 
//	 @Override
//	 public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) 
//	 {
//		 getSupportMenuInflater().inflate(R.menu.action_bar, menu);
//		 return super.onCreateOptionsMenu(menu);
//	 }
//	 
//	 @Override
//    public boolean onOptionsItemSelected(MenuItem item) 
//    {
//        super.onOptionsItemSelected(item);
// 
//        switch(item.getItemId())
//        {
//            case R.id.Profile:
//			try 
//			{
//				Class<?> _clazz;
//				_clazz = Class.forName( "ehc.net.Profile");
//				Intent _intent = new Intent( this,_clazz );
//    			startActivity( _intent );
//			} 
//			catch (ClassNotFoundException e) 
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//                break;
//            case R.id.ChangeProfile:
//            	Intent exitIntent = new Intent(this,LogIn.class);
//            	exitIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            	startActivity(exitIntent);  	
//                break;
//            case R.id.Exit:
//            	Intent intent = new Intent(Intent.ACTION_MAIN);
//            	intent.addCategory(Intent.CATEGORY_HOME);
//            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            	startActivity(intent);  	
//                break;
//        }
//        return true;
//    }
	 
	 
	 private class TabsAdapter extends FragmentPagerAdapter implements
		ActionBar.TabListener, ViewPager.OnPageChangeListener
	{
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
	
		final class TabInfo
		{
			private final Class<?> clss;
			private final Bundle args;
	
			TabInfo(Class<?> _class, Bundle _args)
			{
				clss = _class;
				args = _args;
			}
		}
	
		public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager)
		{
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}
	
		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args)
		{
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}
	
		@Override
		public int getCount()
		{
			return mTabs.size();
		}
	
		@Override
		public Fragment getItem(int position)
		{
			 return new ItemsActivity(_tableButtons.get(String.valueOf(position)),_houseName);			
		}
	
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels)
		{
		}
	
		public void onPageSelected(int position)
		{
			mActionBar.setSelectedNavigationItem(position);
		}
	
		public void onPageScrollStateChanged(int state)
		{
		}
	
		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++)
			{
				if (mTabs.get(i) == tag)
				{
					mViewPager.setCurrentItem(i);
				}
			}
		}
	
		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
		}
	
		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
		}
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
