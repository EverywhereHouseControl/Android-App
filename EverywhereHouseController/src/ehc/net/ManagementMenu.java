package ehc.net;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import framework.JSON;
import framework.ListAdapter;
import framework.SlidingMenuAdapter;

public class ManagementMenu extends SherlockActivity{//Activity{
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
        ListView _drawer = (ListView) findViewById(R.id.ListViewSlidingMenu);		
		final SlidingMenuAdapter _adapter = new 
				SlidingMenuAdapter(this.getBaseContext(),getIntent().getExtras().getString("House"));
		_drawer.setAdapter(_adapter);
		_adapter.notifyDataSetChanged();
		/////////////////////////////////////////////////////////////////////////////////////////
        //-----------------It Reads config.json-----------------

        _JSONFile = JSON.getInstance(getApplicationContext());
		try 
		{
			String _house = getIntent().getExtras().getString("House");
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
					ListAdapter(this.getBaseContext(),_roomsList,R.layout.group_item);
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
	
	//////////////////////////////////////////////////////////////////////////////
//	@Override
//    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) 
//	{
//        getSupportMenuInflater().inflate(R.menu.action_bar, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
// 
//    @Override
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
    //////////////////////////////////////////////////////////////////////////////
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
