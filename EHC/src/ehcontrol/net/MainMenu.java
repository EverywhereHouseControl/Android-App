package ehcontrol.net;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity
{
	//---------Variables----------------
	private Button _buttonProfile;
	private Button _buttonManagement;
	private Button _buttonEvent;
	private Button _buttonConfig;
	//-------------------------------
	
	@Override
    protected void onCreate( Bundle savedInstanceState ) 
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_main_menu );
        /**
         * ------------------------------------
         * Ligadura:  variable <- componente XML
         *-------------------------------------
         */
        _buttonProfile = ( Button ) findViewById( R.id.buttonProfile );
        _buttonManagement = ( Button ) findViewById( R.id.buttonManagement );
        _buttonEvent = ( Button ) findViewById( R.id.buttonEvent );
        _buttonConfig = ( Button ) findViewById( R.id.buttonConfig );
        
        
        _buttonManagement.setOnClickListener( new View.OnClickListener() 
        {
			
			@Override
			public void onClick( View v ) 
			{
				log( "Bot�n gesti�n pulsado" );
				createdManagementIntent();
			}
		});
        
    }
	
	/**
	 * M�todo que ejecuta la activity gesti�n
	 */
	private void createdManagementIntent()
	{
		try {
			Class<?> _clazz = Class.forName( "ehcontrol.net.ManagementMenu" );
			Intent _intent = new Intent( this,_clazz );
			startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
	}
	
	 /**
     * M�todo para debugear
     * @param _text
     */
    private void log( String _text )
    {
    	Log.d( "Acci�n :", _text );
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
