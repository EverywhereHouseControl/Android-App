package ehc.net;

import java.util.ArrayList;

import org.json.JSONException;

import parserJSON.JSON;

import adapters.ListAdapter;
import adapters.SlidingMenuAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;

public class ManagementMenu extends SherlockActivity
{
	//-----------Variables-----------
	private ArrayList<String> _roomsList = new ArrayList<String>();
	private JSON _JSONFile;
	//-------------------------------

	@Override
    protected void onCreate( Bundle savedInstanceState ) 
    {   
		super.onCreate( savedInstanceState );
		setContentView( R.layout.management_menu_view );
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
        //-----------------It Reads config.json-----------------
		String _house = null;
        _JSONFile = JSON.getInstance(getApplicationContext());	
		try 
		{
			_house = getIntent().getExtras().getString("House");
			log("_house");
			_roomsList = _JSONFile.getRooms(_house);
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
	
	/**
	 * Method which executes the next activity
	 * @param room 
	 */
	private void createdManagementIntent(String room) 
	{
		try 
		{			
			Class<?> _class = Class.forName("framework.ContainerFragments");
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
}
