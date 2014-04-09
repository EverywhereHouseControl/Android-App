package ehc.net;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.MenuItem;

import framework.JSON;

public class ManagementMenuExample extends SherlockFragmentActivity
{

	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private TextView tabCenter;
	private TextView tabText;
	private JSON _JSONFile;
	private ArrayList<String> _houseNames = new ArrayList<String>();
	private ManagementMenuFragment _mMF;

	
	/**
	 * Method called when the view has been loaded.
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) 
	{
	    // TODO Auto-generated method stub
	    if (hasFocus) 
	    {
	    	//It Links the 'genericButton' button for obtain his dimensions.
	    	 Button _genericButton = (Button) findViewById(R.id.genericButton);
	    	 Log.d("Tamaño botón: " , Integer.toString(_genericButton.getHeight()) +" "+ Integer.toString(_genericButton.getWidth()));
	    	 
	    	 TableLayout _table1 = (TableLayout) findViewById(R.id.table1);
	    	 
	    	 for(int j=0; j<mTabsAdapter.getCount(); j++)
	    	 {
	    	 
		    	 ArrayList<Button> _buttonList = new ArrayList<Button>();
		    	 mTabsAdapter.getItem(j);
		    	 _buttonList = _mMF.getButtonList();
		    	 Log.d("BUTTONS: ", _buttonList.toString());
		    	 
		    	 //It applies the previous dimension for the other buttons
		    	 for(int i=0; i<_buttonList.size(); i++)
		         {
		    		 _buttonList.get(i).setHeight(_genericButton.getHeight());
		    		 _buttonList.get(i).setWidth(_genericButton.getWidth());		 
		         }
	    	 
	    	 }
	    	 //It is deleted the button 'genericButton'
	    	 _table1.getChildAt(0).setVisibility(8);
	    	 
	    }
	    super.onWindowFocusChanged(hasFocus);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView( R.layout.management_pager_view);

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.mainPager);

		setContentView(mViewPager);
		
		ActionBar _ab = getSupportActionBar();
		_ab.setDisplayShowHomeEnabled(false);
        _ab.setDisplayUseLogoEnabled(false);
        _ab.setDisplayShowTitleEnabled(false);
		_ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mTabsAdapter = new TabsAdapter(this, mViewPager);

		//-----------------It Reads config.json-----------------

        _JSONFile = JSON.getInstance(getApplicationContext());
		
		try 
		{
			_houseNames = _JSONFile.getHouses();
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0; i<_houseNames.size(); i++)
		{
			mTabsAdapter.addTab(_ab.newTab().setText(_houseNames.get(i)),null, null);
		}

	}

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
			
			ArrayList<String> _rooms = new ArrayList<String>();
			
			try 
			{
				_rooms = _JSONFile.getRooms(_houseNames.get(position));
			} 
			catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_mMF = new ManagementMenuFragment(mContext,_houseNames.get(position),_rooms);
			return _mMF;
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
}
