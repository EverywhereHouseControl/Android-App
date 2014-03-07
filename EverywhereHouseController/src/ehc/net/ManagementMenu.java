package ehc.net;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
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
	//-------------------------------
	
	/**
	 * M�todo que se ejcuta cuando la vista ha sido cargada.
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) 
	{
	    // TODO Auto-generated method stub
	    super.onWindowFocusChanged(hasFocus);
	    if (hasFocus) 
	    {
	    	//Ligadura del bot�n 'genericButton' a una varible para obtener sus dimensiones
	    	 final Button _genericButton = (Button) findViewById(R.id.genericButton);
	    	 log("Tama�o bot�n :" + Integer.toString(_genericButton.getHeight()) +" "+ Integer.toString(_genericButton.getWidth()));
	     
	    	 //Se aplican las dimensiones del bot�n 'generciButton' a los dem�s botones
	    	 for(int i=0; i<_buttonList.size(); i++)
	         {
	    		 _buttonList.get(i).setHeight(_genericButton.getHeight());
	    		 _buttonList.get(i).setWidth(_genericButton.getWidth());
	         }
	    	 //Se elimina el bot�n 'genericButton'
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

         //-----------------Lectura del archivo config.json-----------------
         JSON parserJSON = new JSON(this.getBaseContext());
         //parserJSON.loadJSON(this.getBaseContext());
         
		try {
			ArrayList<String> rooms = parserJSON.getRooms();
	         for (int i=0; i < rooms.size(); i++){
	        	final Button button = new Button(this);
	        	button.setClickable(true);
	        	button.setText(rooms.get(i));
	        	button.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						String buttonName = (String) button.getText();
						log("Se ha pulsado: "+ buttonName);			
					}
				});
	        	_buttonList.add(button);
	         } 
		} catch (JSONException e) {
			e.printStackTrace();
		}
     
		
		//Se a�aden los botones a las tablas de la vista
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
		
        //-------------------end-View----------------------------------------------
        
        /**
         * ------------------------------------
         * Ligadura:  variable <- componente XML
         *-------------------------------------
         */
        /*
        _livingRoom = ( Button ) findViewById( R.id.buttonLivingRoom );
        
        _livingRoom.setOnClickListener( new View.OnClickListener() 
        {
			
			@Override
			public void onClick( View v ) 
			{
				log( "Bot�n gesti�n pulsado" );
				createdManagementIntent();
			}
		});
		*/    
    }
	
	/**
	 * M�todo que ejecuta la activity lugares
	 */
	private void createdManagementIntent(String buttonName)
	{/*
		try 
		{
			Class<?> _clazz = Class.forName( "ehc.net.PlacesMenu" );
			Intent _intent = new Intent( this,_clazz );
			//Paso el nombre del bot�n que sido pulsado para redirigirla la siguiente
			//Activity a la vista correcta.
			_intent.putExtra("buttonName", buttonName);
			startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}*/
	}
	
	/**
     * M�todo para debugear
     * @param _text
     */
    private void log( String _text )
    {
    	Log.d("Acci�n :", _text);
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
