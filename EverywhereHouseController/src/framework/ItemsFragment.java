	package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import ehc.net.R;
import parserJSON.JSON;

import adapters.ExpandableListAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.TextView;

	@SuppressLint("ValidFragment")
	public class ItemsFragment extends Fragment
	{
		//------------Variables-----------------------
		private JSON _JSONFile;
		private String _button;
		private String _house;
		private TextView _textRoom;
		private ExpandableListView _expListView;
		private List<String> _groupList;
		private HashMap<String, List<String>> _listDataChild;
		private Context _context;
		//--------------------------------------------
		
		public ItemsFragment(Context context,String button, String houseName)
		{
			_button = button;
			_house = houseName;
			_context = context;
		}
	    
	    @Override
	    public void onCreate( Bundle savedInstanceState )
	    {
	        super.onCreate( savedInstanceState );
	        Log.e( "Test", "hello" );	        
	    }
	 
	    @Override
	    public void onActivityCreated( Bundle savedInstanceState )
	    {
	        super.onActivityCreated( savedInstanceState );
	    }
	 
	    private HashMap<String, List<String>> createHashMapItems(String room, List<String> items)
	    {
	    	
	    	for (int i=0; i<=items.size()-1; i++)
	    	{
	    		String _laptopName = items.get(i);	    		
	    		JSONObject _list = new JSONObject();
				_list = JSON.getServices(_house,room,_laptopName);   		
	    		
				try
				{
					switch(_list.getInt("interface"))
					{
						case 1:
						{							
							List<String> controller = new ArrayList<String>();
			    			controller.add("controller");
			    			_listDataChild.put(items.get(i),controller);			    			
							break;
						}
						case 2:
						{
							List<String> lights = new ArrayList<String>();
			    			lights.add("boolean");
			    			_listDataChild.put(items.get(i),lights);
							break;
						}
						case 3:
						{
							List<String> intercom = new ArrayList<String>();
							intercom.add("boolean");
			    			_listDataChild.put(items.get(i),intercom);
							break;
						}
						case 4:
						{
							List<String> plug = new ArrayList<String>();
							plug.add("boolean");
			    			_listDataChild.put(items.get(i),plug);
							break;
						}
						case 5:
						{
							List<String> controller = new ArrayList<String>();
			    			controller.add("controller");
			    			_listDataChild.put(items.get(i),controller);			    			
							break;
						}
						case 6:
						{
							List<String> temp = new ArrayList<String>();
							temp.add("real");
			    			_listDataChild.put(items.get(i),temp);
							break;
						}
						case 7:
						{
							List<String> blinds = new ArrayList<String>();
			    			blinds.add("integer");
			    			blinds.add("integer");
			    			_listDataChild.put(items.get(i),blinds);
							break;
						}
						case 8:
						{
							List<String> door = new ArrayList<String>();
			    			door.add("boolean");
			    			_listDataChild.put(items.get(i),door);
							break;
						}
						case 9:
						{
							List<String> motion = new ArrayList<String>();
			    			_listDataChild.put(items.get(i),motion);
							break;
						}
						case 10:
						{
							List<String> rain = new ArrayList<String>();
			    			_listDataChild.put(items.get(i),rain);
							break;
						}
						case 11:
						{
							List<String> temp = new ArrayList<String>();
							temp.add("real");
			    			_listDataChild.put(items.get(i),temp);
							break;
						}
						default:
						{
							List<String> in = new ArrayList<String>();
			    			_listDataChild.put(items.get(i),in);
							break;
						}
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
	    	}	
	    	
	    	return _listDataChild;
	    }
	    
	    /**Crea la vista ligando el archivo .XML al rootView
	     *@return la vista de la habitación
		**/
	    @Override
	    public View onCreateView( LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState )
	    {
	    	_JSONFile = JSON.getInstance(getActivity().getApplicationContext());
	    	    	
	    	ViewGroup rootView = ( ViewGroup ) inflater.inflate( R.layout.items_view, container, false );
	    	_expListView = (ExpandableListView) rootView.findViewById(R.id.itemlist);
	    	    	
	    	_textRoom = (TextView)rootView.findViewById(R.id.textRoom);
	    	_textRoom.setText(_button.toUpperCase());
	    	_textRoom.setVisibility(View.GONE);	    	
	    	
	    	
	    	_groupList = new ArrayList<String>();
			_listDataChild = new HashMap<String, List<String>>();

	    	try 
	    	{
	    		_groupList = _JSONFile.getItems(_button,_house);
		        if(_groupList.size()!=0)
		        {
		    		final ExpandableListAdapter _expListAdapter = new 
			        		ExpandableListAdapter(_house,_button,
			        				rootView.getContext().getApplicationContext(), 
			        				_groupList, createHashMapItems(_button,_groupList));
			        _expListAdapter.notifyDataSetChanged();
			        _expListView.setAdapter(_expListAdapter);
			        
			        _expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() 
			        {
			            int previousItem = -1;
						@Override
						public void onGroupCollapse(int groupPosition) 
						{
							// TODO Auto-generated method stub
							if(groupPosition != previousItem )
								 _expListView.collapseGroup(previousItem );
			                previousItem = groupPosition;
						}
			        });
		        }
		        
	    	} 
	    	catch (JSONException e) 
	    	{
	    		e.printStackTrace();
	    	}

	     	return rootView;
	    }
	}