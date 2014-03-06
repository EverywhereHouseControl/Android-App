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
	         for (int i=0; i < rooms.size(); i++)
	        	 log("Habitacion" + rooms.get(i));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         //--------------------------------------------------
         
		//-----------------------------------------------------------------------------
		//Ejemplo de configuraci�n.
        int num_bathrooms 	= 2;
		int num_rooms		= 2;
		int num_livingrooms	= 1;
		int num_kitchens	= 1;
		int num_terraces	= 2;
		int num_gardens		= 1;
		int num_garages		= 1;
		int num_places		= num_bathrooms + num_rooms	+ num_livingrooms + num_kitchens + 	num_terraces + num_gardens + num_garages;
		//-----------------------------------------------------------------------------
		
		log("N�mero de lugares: " + Integer.toString(num_places));
		
		//Se inserta los botones a la lista '_buttonList'
		for(int i=0; i<num_places; i++)
		{
			final Button B = new Button(this);
			B.setClickable(true);
		
			if(num_livingrooms!=0)
			{
				B.setText("Sal�n" + Integer.toString(num_livingrooms - (num_livingrooms-1)));
				num_livingrooms--;
			}
			else if(num_kitchens!=0)
			{
				B.setText("Cocina" + Integer.toString(num_kitchens - (num_kitchens-1)));
				num_kitchens--;
			}
			else if(num_rooms!=0)
			{
				B.setText("Habitaci�n" + Integer.toString(num_rooms - (num_rooms-1)));
				num_rooms--;
			}
			else if(num_bathrooms!=0)
			{
				B.setText("Ba�o" + Integer.toString(num_bathrooms - (num_bathrooms-1)));
				num_bathrooms--;
			}
			else if(num_terraces!=0)
			{
				B.setText("Terraza" + Integer.toString(num_terraces - (num_terraces-1)));
				num_terraces--;
			}
			else if(num_gardens!=0)
			{
				B.setText("Jard�n" + Integer.toString(num_gardens - (num_gardens-1)));
				num_gardens--;
			}
			else if(num_garages!=0)
			{
				B.setText("Garaje" + Integer.toString(num_garages - (num_garages-1)));
				num_garages--;
			}
			
			//Oyente asignado a cada bot�n
			B.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					String buttonName = (String) B.getText();
					//Se pasa el nombre del bot�n que ha sido pulsado
					log("Se ha pulsado: "+buttonName);			
					//createdManagementIntent(buttonName);
				}
			});
			
			_buttonList.add(i, B);
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
