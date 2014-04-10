package ehc.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import framework.ExpandableListAdapter;
import framework.JSON;
import framework.ListAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ExpandableListView.OnGroupCollapseListener;

@SuppressLint("ValidFragment")
public class ManagementMenuFragment extends Fragment//Activity
{
	private JSON _JSONFile;
	private String _houseName;
	private ArrayList<String> _rooms = new ArrayList<String>();
	private ArrayList<Button> _buttonList = new ArrayList<Button>();
	private Context _context;
	
	/**
	 * 
	 * @param houseName
	 */
	public ManagementMenuFragment(Context context, String houseName, ArrayList<String> rooms)
	{
		this._houseName = houseName;
		this._rooms = rooms;
		this._context = context;
	}
	
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );        
    }
 
    @Override
    public void onActivityCreated( Bundle savedInstanceState )
    {
        super.onActivityCreated( savedInstanceState );
    }
    
    /**
     * 
     * @return
     */
    public ArrayList<Button> getButtonList()
    {
    	return _buttonList;
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState )
    {
    	final ViewGroup rootView = ( ViewGroup ) inflater.inflate( R.layout.management_menu_view, container, false );
    	ListView _ListView = (ListView) rootView.findViewById(R.id.roomsListView);
//    	TableLayout _table1 = (TableLayout) rootView.findViewById(R.id.table1);
//        
//    	TableLayout _table2 = (TableLayout) rootView.findViewById(R.id.table2);
       
    	_buttonList.clear();
    	
		for (int i=0; i < _rooms.size(); i++)
		{
			final String selectedRoom = _rooms.get(i);
			final Button _button = new Button(_context);
			_button.setClickable(true);
			
			_button.setTextColor(R.drawable.button_text_color);
			//_button.setTextSize(25);
			_button.setText(selectedRoom);
			_button.setBackgroundResource(R.drawable.button_config);
			
			_button.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					//String buttonName = (String) _button.getText();
					createdManagementIntent(selectedRoom);
				}
		});
		_buttonList.add(_button);
		}
		
    	final ListAdapter _ListAdapter = new 
				ListAdapter(_houseName,
						rootView.getContext().getApplicationContext(), 
						_rooms);
		_ListAdapter.notifyDataSetChanged();
		_ListView.setAdapter(_ListAdapter);
		
		_ListView.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				// TODO Auto-generated method stub
				createdManagementIntent(_rooms.get(position));
			}
		});
		
		
//		//The buttons are added to tables
//		for(int i=0; i<_buttonList.size(); i++)
//        {
//			TableRow tr = new TableRow(_context);
//			if((i % 2) == 0)
//			{
//				_table1.addView(tr);
//				_table1.addView(_buttonList.get(i));
//			}
//			else
//			{
//				_table2.addView(tr);
//	        	_table2.addView(_buttonList.get(i));
//			}        	
//        }
		
//		rootView.post(new Runnable() 
//		{
//			@Override
//			public void run() 
//			{
//				// TODO Auto-generated method stub
//				//It Links the 'genericButton' button for obtain his dimensions.
//		    	 Button _genericButton = (Button) rootView.findViewById(R.id.genericButton);
//		    	 Log.d("Tamaño botón: " , Integer.toString(_genericButton.getHeight()) +" "+ Integer.toString(_genericButton.getWidth()));
//		    	 
//		    	 TableLayout _table1 = (TableLayout) rootView.findViewById(R.id.table1);
//		    			    	 
//		    	 //It applies the previous dimension for the other buttons
//		    	 for(int i=0; i<_buttonList.size(); i++)
//		         {
//		    		 _buttonList.get(i).setHeight(_genericButton.getHeight());
//		    		 _buttonList.get(i).setWidth(_genericButton.getWidth());
//		    		 _buttonList.get(i).setTextSize(_genericButton.getTextSize());
//		         }
//		    	 		    	 
//		    	 //It is deleted the button 'genericButton'
//		    	 _table1.getChildAt(0).setVisibility(8);
//			}
//		});
		
    	return rootView;
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
			Intent _intent = new Intent( _context,_class );
			
			//Current house
			_intent.putExtra("House", _houseName);
			
			//Room name being clicked
			_intent.putExtra("Room",room);
			//Room's number
			_intent.putExtra("NumRooms", _buttonList.size());
				
			for(int i=0; i<_buttonList.size(); i++)
			{		
				// Key: position, Value button Name.
				_intent.putExtra(Integer.toString(i) , _buttonList.get(i).getText().toString());	
				// Key: button name, Value: position.
				//Necessary to move the 'viewPager' to the desired view.
				_intent.putExtra(_buttonList.get(i).getText().toString(),Integer.toString(i));	
			}	
			startActivity( _intent );
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
