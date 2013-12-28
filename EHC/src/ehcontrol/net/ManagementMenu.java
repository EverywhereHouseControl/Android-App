package ehcontrol.net;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ManagementMenu extends Activity
{
	//-----------Variables-----------
	private Button _livingRoom;
	//-------------------------------
	
	@Override
    protected void onCreate( Bundle savedInstanceState ) 
    {   
		super.onCreate( savedInstanceState );
        setContentView( R.layout.view_management_menu );
        /**
         * ------------------------------------
         * Ligadura:  variable <- componente XML
         *-------------------------------------
         */
        _livingRoom = ( Button ) findViewById( R.id.buttonLivingRoom );
        
        _livingRoom.setOnClickListener( new View.OnClickListener() 
        {
			
			@Override
			public void onClick( View v ) 
			{
				log( "Botón gestión pulsado" );
				createdManagementIntent();
			}
		});    
    }
	
	/**
	 * Método que ejecuta la activity lugares
	 */
	private void createdManagementIntent()
	{
		try 
		{
			Class<?> _clazz = Class.forName( "ehcontrol.net.PlacesMenu" );
			Intent _intent = new Intent( this,_clazz );
			startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
	}
	
	/**
     * Método para debugear
     * @param _text
     */
    private void log( String _text )
    {
    	Log.d("Acción :", _text);
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
