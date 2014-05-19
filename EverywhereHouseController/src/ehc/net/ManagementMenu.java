package ehc.net;

import java.util.ArrayList;

import org.json.JSONException;

import parserJSON.JSON;

import adapters.ListAdapter;
import adapters.SlidingMenuAdapter;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

import ehc.net.R;

public class ManagementMenu extends SherlockActivity
{
	//-----------Variables-----------
	private ArrayList<String> _roomsList = new ArrayList<String>();
	private JSON _JSONFile;
	//-------------------------------
	private ActionBarDrawerToggle _actbardrawertoggle;
	private DrawerLayout _dl;
	private ListView _drawer;
	private String _house;
	// -------------------------------

	@Override
    protected void onCreate( Bundle savedInstanceState ) 
    {   
		super.onCreate( savedInstanceState );
		setContentView( R.layout.management_menu_view );
				
        /////////////////////////////////////////////////////////////////////////////////////////
       _drawer = (ListView) findViewById(R.id.ListViewSlidingMenu);		
		final SlidingMenuAdapter _adapter = new 
				SlidingMenuAdapter(this.getBaseContext(),getIntent().getExtras().getString("House"));
		_drawer.setAdapter(_adapter);
		_adapter.notifyDataSetChanged();
		
		getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        
		
		_dl = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		_actbardrawertoggle= new ActionBarDrawerToggle(this, _dl, R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close)
        {
			public void onDrawerClosed(View view) 
			{
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) 
			{
				super.onDrawerOpened(drawerView);
			
			}    	 
        };
        
        _dl.setDrawerListener(_actbardrawertoggle);	
		/////////////////////////////////////////////////////////////////////////////////////////
        //-----------------It Reads config.json-----------------
        JSON.getInstance(getApplicationContext());	
		try 
		{
			_house = getIntent().getExtras().getString("House");
			log("_house");
			_roomsList = JSON.getRooms(_house);
			log("Después _roomList");
			 
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(_roomsList.size()!=0)
		{
			ListView _ListView = (ListView) findViewById(R.id.roomsListView);
			
			final ListAdapter _ListAdapter = new 
					ListAdapter(this.getBaseContext(),_roomsList,R.layout.group_item,_house);
			_ListAdapter.notifyDataSetChanged();
			_ListView.setAdapter(_ListAdapter);
			
			_ListView.setOnItemClickListener(new OnItemClickListener() 
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) 
				{
					// TODO Auto-generated method stub
					createdManagementIntent(_roomsList.get(position));
				}
			});
		}
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) 
	{
		// TODO Auto-generated method stub
		if(item.getItemId()==android.R.id.home)
		 {
			 if(_dl.isDrawerOpen(_drawer))
			 {
				 _dl.closeDrawer(_drawer);
			 }
			 else {
				_dl.openDrawer(_drawer);
			}
		 }
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) 
	{
		super.onPostCreate(savedInstanceState);
		_actbardrawertoggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		_actbardrawertoggle.onConfigurationChanged(newConfig);
	}	
	/**
	 * Method which executes the next activity
	 * @param room 
	 */
	private void createdManagementIntent(String room) 
	{
		try 
		{			
			Class<?> _class = Class.forName("ehc.net.ItemsFragmentsContainer");
			Intent _intent = new Intent( getApplicationContext(),_class );
			
			//Current house
			_intent.putExtra("House", getIntent().getExtras().getString("House"));
			
			//Room name being clicked
			_intent.putExtra("Room",room);
			//Room's number
			_intent.putExtra("NumRooms", _roomsList.size());
				
			for(int i=0; i<_roomsList.size(); i++)
			{		
				// Key: position, Value button Name.
				_intent.putExtra(Integer.toString(i) , _roomsList.get(i));	
				// Key: button name, Value: position.
				//Necessary to move the 'viewPager' to the desired view.
				_intent.putExtra(_roomsList.get(i),Integer.toString(i));	
			}	
			startActivity( _intent );
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
    
    protected void onDestroy()
    {
    	super.onDestroy();
    	log( "Destroy" );
    }
    
    @Override
    public void onBackPressed() 
    {
    	// TODO Auto-generated method stub
//    	super.onBackPressed();
    	onDestroy();
    	try 
    	{
			Class<?> _clazz = Class.forName( "ehc.net.MainMenu" );
			Intent _intent = new Intent( this,_clazz );
			_intent.putExtra("House",_house);
			startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
    }
    
    
}
