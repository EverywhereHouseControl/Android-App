	package ehc.net;

	import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import environment.DVD;
import environment.Light;
import environment.Room;
import environment.RoomDecorator;
import framework.JSON;

	public class ItemsActivity extends Activity
	{
		//---------Variables----------------

		private ImageView _logo;
		private ViewGroup layout;
		private ScrollView scrollView;
		private JSON JSONFile;
		
		@Override
	    protected void onCreate( Bundle savedInstanceState ) 
	    {
			super.onCreate( savedInstanceState );
			setContentView( R.layout.items_view );
			
			LinearLayout ll = (LinearLayout)findViewById(R.id.llid);
			JSONFile = JSON.getInstance(getApplicationContext());

			try {
				String value = getIntent().getExtras().getString("Room");
				ArrayList<String> items= JSONFile.getItems(value);
				setItemViews(ll, items);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Mapping objects from a list of items.		
		 * @param ll
		 * @param itemList
		 */
		public void setItemViews(LinearLayout ll, ArrayList<String> itemList){
			for (int i=0; i <= itemList.size()-1; i++){			
				if (itemList.get(i).equals("DVD")){
					DVD dvd = new DVD();
					dvd.setView(getApplicationContext(), ll);
				}
				if (itemList.get(i).equals("Lights")){
					Light light = new Light();
					light.setView(getApplicationContext(), ll);
				}
					
			}
		}
		

		/**
		 * Method which executes the next activity
		 */
		 
		private void createdManagementIntent()
		{

		}
		
		
		 /**
	     * Debugger method
	     * @param _text
	     */
	    private void log( String _text )
	    {
	    	Log.d( "Acción :", _text );
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