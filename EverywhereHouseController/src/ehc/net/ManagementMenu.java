package ehc.net;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import framework.JSON;

public class ManagementMenu extends SherlockActivity {//Activity{
	//-----------Variables-----------
	private TableLayout _table1;
	private TableLayout _table2;
	private ArrayList<Button> _buttonList = new ArrayList<Button>();
	private JSON _JSONFile;
	private ActionBar _ab;
	//-------------------------------
	
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
	    	 log("Tamaño botón :" + Integer.toString(_genericButton.getHeight()) +" "+ Integer.toString(_genericButton.getWidth()));
	    	 
	    	 //It applies the previous dimension for the other buttons
	    	 for(int i=0; i<_buttonList.size(); i++)
	         {
	    		 _buttonList.get(i).setHeight(_genericButton.getHeight());
	    		 _buttonList.get(i).setWidth(_genericButton.getWidth());		 
	         }
	    	 //It is deleted the button 'genericButton'
	    	 _table1.getChildAt(0).setVisibility(8);
	    	 
	    }
	    super.onWindowFocusChanged(hasFocus);
	}
	
	@Override
    protected void onCreate( Bundle savedInstanceState ) 
    {   
		super.onCreate( savedInstanceState );
        setContentView( R.layout.management_menu_view );
     
        _table1 = (TableLayout) findViewById(R.id.table1);
                
        _table2 = (TableLayout) findViewById(R.id.table2);
          
         //----------------ActionBar-----
         _ab = getSupportActionBar();
         _ab.setDisplayShowHomeEnabled(false);
         _ab.setDisplayUseLogoEnabled(false);
         _ab.setDisplayShowTitleEnabled(false);
         //-------------------------------

         //-----------------It Reads config.json-----------------

        _JSONFile = JSON.getInstance(getApplicationContext());
		ArrayList<String> _rooms;
		try 
		{
			_rooms = _JSONFile.getRooms();
			log("Número de habitaciones: "+ _rooms);
			for (int i=0; i < _rooms.size(); i++)
			{
				final String selectedRoom = _rooms.get(i);
				final Button _button = new Button(getApplicationContext());
				_button.setClickable(true);
				
				_button.setTextColor(R.drawable.button_text_color);
				_button.setTextSize(25);
				_button.setText(selectedRoom);
				_button.setBackgroundResource(R.drawable.button_config);
				
				_button.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						String buttonName = (String) _button.getText();
						log("Se ha pulsado: "+ buttonName);
						createdManagementIntent(selectedRoom);
					}
			});
			_buttonList.add(_button);
			 }
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//---------------------------------------------------------------------
		
		//The buttons are added to tables
		for(int i=0; i<_buttonList.size(); i++)//i<_buttonList.size()/2; i++)
        {
			TableRow tr = new TableRow(this.getBaseContext());
			if((i % 2) == 0)
			{
				_table1.addView(tr);
				_table1.addView(_buttonList.get(i));
			}
			else
			{
				_table2.addView(tr);
	        	_table2.addView(_buttonList.get(i));
			}        	
        }
		/*
        for(int i=(_buttonList.size()/2); i<_buttonList.size(); i++)
        {
        	TableRow tr = new TableRow(this.getBaseContext());
        	_table2.addView(tr);
        	_table2.addView(_buttonList.get(i));
        }  */     
    }
	
	
	@Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
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
