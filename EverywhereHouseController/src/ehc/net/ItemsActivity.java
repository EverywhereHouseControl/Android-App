	package ehc.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import environment.DVD;
import environment.Light;
import framework.ExpandableListAdapter;
import framework.JSON;

	@SuppressLint("ValidFragment")
	public class ItemsActivity extends Fragment//Activity
	{

		private JSON JSONFile;
		private String button;
		com.actionbarsherlock.view.Menu menu;
		TextView _textRoom;
		ExpandableListView expListView;
		List<String> groupList;
		HashMap<String, List<String>> listDataChild;

		
//		@Override
//	    protected void onCreate( Bundle savedInstanceState ) 
//	    {
//			super.onCreate( savedInstanceState );
//			setContentView( R.layout.items_view );
//			
//			LinearLayout ll = (LinearLayout)findViewById(R.id.llid);
//			JSONFile = JSON.getInstance(getApplicationContext());
//
//			try {
//				String value = getIntent().getExtras().getString("Room");
//				ArrayList<String> items= JSONFile.getItems(value);
//				setItemViews(ll, items);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
		
		/**
		 * Mapping objects from a list of items.		
		 * @param ll
		 * @param itemList
		 */
//		public void setItemViews(LinearLayout ll, ArrayList<String> itemList){
//			for (int i=0; i <= itemList.size()-1; i++){			
//				if (itemList.get(i).equals("DVD")){
//					DVD dvd = new DVD();
//					dvd.setView(getApplicationContext(), ll);
//				}
//				if (itemList.get(i).equals("Lights")){
//					Light light = new Light();
//					light.setView(getApplicationContext(), ll);
//				}
//					
//			}
//		}
		

//		/**
//		 * Method which executes the next activity
//		 */
		
		public ItemsActivity(String button)
		{
			this.button = button;
		}
			    
	    /**
		 * Mapping objects from a list of items.		
		 * @param ll
		 * @param itemList
		 */
		public void setItemViews(LinearLayout ll, ArrayList<String> itemList)
		{
			for (int i=0; i < itemList.size(); i++)
			{			
				if (itemList.get(i).equals("DVD"))
				{
					DVD dvd = new DVD();
					Log.e("Activity: ",getActivity().getApplicationContext().toString());
					
					dvd.setView(getActivity().getApplicationContext(), ll);
				}
				if (itemList.get(i).equals("Lights"))
				{
					Light light = new Light(button);
					light.setView(getActivity().getApplicationContext(), ll);
				}
					
			}
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
	 
	    private HashMap<String, List<String>> createHashMapItems(List<String> items){
	    	for (int i=0; i<=items.size()-1; i++){
	    		if (items.get(i).equals("DOOR")){
	    			List<String> door = new ArrayList<String>();
	    			door.add("boolean");
	    			listDataChild.put(items.get(i),door);
	    		} else if (items.get(i).equals("LIGHTS")){
	    			List<String> lights = new ArrayList<String>();
	    			lights.add("float");
	    			lights.add("boolean");
	    			listDataChild.put(items.get(i),lights);
    			} else if (items.get(i).equals("AIRCONDITIONING")){
	    			List<String> air = new ArrayList<String>();
	    			air.add("float");
	    			air.add("boolean");
	    			listDataChild.put(items.get(i),air);
    			} else if (items.get(i).equals("STEREO")){
	    			List<String> stereo = new ArrayList<String>();
	    			stereo.add("float");
	    			stereo.add("boolean");
	    			listDataChild.put(items.get(i),stereo);
    			} 	    			
	    	}
	    	
	    	return listDataChild;
	    }
	    
	    /**Crea la vista ligando el archivo .XML al rootView
	     *@return la vista de la habitación
		**/
	    @Override
	    public View onCreateView( LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState )
	    {
	    	ViewGroup rootView = ( ViewGroup ) inflater.inflate( R.layout.items_view, container, false );
	    	expListView = (ExpandableListView) rootView.findViewById(R.id.itemlist);
	    	    	
	    	_textRoom = (TextView)rootView.findViewById(R.id.textRoom);
	    	_textRoom.setText(button);
	    	
	    	JSONFile = JSON.getInstance(getActivity().getApplicationContext());
	    		
	    	groupList = new ArrayList<String>();
			listDataChild = new HashMap<String, List<String>>();

	    	try 
	    	{
	    		groupList = JSONFile.getItems(button);
		        final ExpandableListAdapter expListAdapter = new 
		        		ExpandableListAdapter(button,
		        				rootView.getContext().getApplicationContext(), 
		        				groupList, createHashMapItems(groupList));
		        expListView.setAdapter(expListAdapter);       
	    	} 
	    	catch (JSONException e) 
	    	{
	    		e.printStackTrace();
	    	}

	     	return rootView;
	    }
	}