	package ehc.net;

import java.util.ArrayList;
import org.json.JSONException;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import environment.DVD;
import environment.Light;
import framework.JSON;

	@SuppressLint("ValidFragment")
	public class ItemsActivity extends Fragment//Activity
	{
		//---------Variables----------------

		//private ImageView _logo;
		//private ViewGroup layout;
		//private ScrollView scrollView;
		private JSON JSONFile;
		private String button;
		
		
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
//		 
//		private void createdManagementIntent()
//		{
//
//		}
		
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
			for (int i=0; i <= itemList.size()-1; i++)
			{			
				if (itemList.get(i).equals("DVD"))
				{
					DVD dvd = new DVD();
					Log.d("Activity: ",getActivity().getApplicationContext().toString());
					
					dvd.setView(getActivity().getApplicationContext(), ll);
				}
				if (itemList.get(i).equals("Lights"))
				{
					Light light = new Light();
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
	 
	    /**Crea la vista ligando el archivo .XML al rootView
	     *@return la vista de la habitación
		**/
	    @Override
	    public View onCreateView( LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState )
	    {
	    	ViewGroup rootView = ( ViewGroup ) inflater.inflate( R.layout.items_view, container, false );
	    	LinearLayout ll = (LinearLayout)rootView.findViewById(R.id.llid);

	    	JSONFile = JSON.getInstance(getActivity().getApplicationContext());

	    	try 
	    	{
	    		ArrayList<String> items= JSONFile.getItems( button);
	    		Log.d("Item del botón: " + button,items.toString());
	    		setItemViews(ll, items);
	    	} 
	    	catch (JSONException e) 
	    	{
	    		e.printStackTrace();
	    	}

	     	return rootView;
	    }
	}