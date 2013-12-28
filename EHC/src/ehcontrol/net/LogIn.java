package ehcontrol.net;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogIn extends Activity 
{
	//------------Variables-----------------------
	private Button _buttonLog;
	private EditText _user;
	private EditText _password;
	//-------------------------------------------------
	
    @Override
    protected void onCreate( Bundle savedInstanceState ) 
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_log_in );
        
        /**
         * ------------------------------------
         * Ligadura:  variable <- componente XML
         *-------------------------------------
         **/
        _buttonLog = ( Button ) findViewById( R.id.buttonLogin );
        _user = ( EditText ) findViewById( R.id.idText );
        _password = ( EditText ) findViewById( R.id.passwordText );
       
        
        _buttonLog.setOnClickListener( new View.OnClickListener() 
        {	
			@Override
			public void onClick( View _v ) 
			{
				log( "Botón login pulsado" );
				createdIntent();
			}
		});
    }
    
    /**
     * -----------------------------------------
     * Ejecuta la activity Menu Principal
     * -----------------------------------------
     */
    private void createdIntent()
    {
    	try {
			Class<?> _clazz = Class.forName( "ehcontrol.net.MainMenu" );
			Intent _intent = new Intent( this,_clazz );
			startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
    }
    
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.log_in, menu );
        return true;
    }
    
    /**
     * Método para debugear
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
