package ehc.net;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import ehc.net.R;
import framework.JSON;

public class ManagementMenu extends Activity
{
	//-----------Variables-----------
	private TableLayout _table1;
	private TableLayout _table2;
	private ArrayList<Button> _buttonList = new ArrayList<Button>();
	private JSON JSONFile;
	//-------------------------------
	
	/**
	 * Method called when the view has been loaded.
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) 
	{
	    // TODO Auto-generated method stub
	    super.onWindowFocusChanged(hasFocus);
	    if (hasFocus) 
	    {
	    	//It Links the 'genericButton' button for obtain his dimensions.
	    	 final Button _genericButton = (Button) findViewById(R.id.genericButton);
	    	 log("Tamaño botón :" + Integer.toString(_genericButton.getHeight()) +" "+ Integer.toString(_genericButton.getWidth()));
	     
	    	 //It applies the previous dimension for the other buttons
	    	 for(int i=0; i<_buttonList.size(); i++)
	         {
	    		 _buttonList.get(i).setHeight(_genericButton.getHeight());
	    		 _buttonList.get(i).setWidth(_genericButton.getWidth());
	         }
	    	 //The 'genericButton' button it is deleted
	    	 _table1.removeViewAt(0);
	    }
	}
	
	@Override
    protected void onCreate( Bundle savedInstanceState ) 
    {   
		super.onCreate( savedInstanceState );
        setContentView( R.layout.management_menu_view );
        
        _table1 = (TableLayout) findViewById(R.id.table1);
                
        _table2 = (TableLayout) findViewById(R.id.table2);
        
        ImageView _logo = (ImageView) findViewById(R.id.imageWorldManagementMenu);
        
        Animation anim = AnimationUtils.loadAnimation(this.getBaseContext(), R.anim.rotate_indefinitely);
        //Start animating the image
         _logo.startAnimation(anim); 

         //-----------------It Reads config.json-----------------

        JSONFile = JSON.getInstance(getApplicationContext());
		ArrayList<String> rooms;
		try {
			rooms = JSONFile.getRooms();
			for (int i=0; i < rooms.size(); i++){
				final String selectedRoom = rooms.get(i);
				final Button button = new Button(getApplicationContext());
				button.setClickable(true);
				button.setText(selectedRoom);
				button.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						String buttonName = (String) button.getText();
						log("Se ha pulsado: "+ buttonName);
						createdManagementIntent(selectedRoom);
					}
			});
			_buttonList.add(button);
			 }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//---------------------------------------------------------------------
		//The buttons are added to tables
		for(int i=0; i<_buttonList.size()/2; i++)
        {
			TableRow tr = new TableRow(this.getBaseContext());
			_table1.addView(tr);
			_table1.addView(_buttonList.get(i));
        	
        }
        for(int i=(_buttonList.size()/2); i<_buttonList.size(); i++)
        {
        	TableRow tr = new TableRow(this.getBaseContext());
        	_table2.addView(tr);
        	_table2.addView(_buttonList.get(i));
        }
          
    }
	
	/**
	 * M�todo que ejecuta la activity lugares
	 * @param room 
	 */
	private void createdManagementIntent(String room) {
		try {
			Class<?> _class = Class.forName("ehc.net.ItemsActivity");
			Intent intent = new Intent( getApplicationContext(),_class );
			intent.putExtra("Room",room);
			startActivity( intent );
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     * M�todo para debugear
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
