package ehcontrol.net;

import frameWork.MyAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import ehcontrol.net.R;

public class PlacesMenu extends FragmentActivity
{
	//----------Variables------------
		//Salón
	private LinearLayout _livingRoom;
	private ScrollView _svlr;
	private Button _lrTV;
		//Cocina
	private LinearLayout _kitchen;
	private ScrollView _svk;
		//Cocina
	private LinearLayout _bathRoom;
	private ScrollView _svb;
	//-------------------------------
	private MyAdapter _mAdapter;
    private ViewPager _mPager;
    private LinearLayout _mainPlacesView;
	//--------------------------------
	
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
    	super.onCreate( savedInstanceState );
        setContentView( R.layout.view_main_places_menu);
                
        /**
         * ------------------------------------
         * Ligadura:  variable <- componente XML
         *-------------------------------------
         */
    	//Componentes view Salón
    	//_livingRoom = ( LinearLayout ) findViewById( R.id.livingRoomMenu );
    	//_svlr = ( ScrollView ) findViewById( R.id.svLivingRoomMenu );
        
    	//Componentes view Cocina
    	//_kitchen = ( LinearLayout ) findViewById( R.id.kitchenMenu );
    	//_svk = ( ScrollView ) findViewById( R.id.svKitchenMenu );
    	//Componentes view Baño
    	//_bathRoom = ( LinearLayout ) findViewById( R.id.bathroomMenu );
    	//_svb = ( ScrollView ) findViewById( R.id.svBathRoomMenu );
    	//Contenedor de las vistas 
    	//_mainPlacesView = ( LinearLayout ) findViewById( R.id.mainPlacesMenu );
    
        
        
        
    	// Crea un adaptador con los fragmentos que vamos a mostrar en el ViewPager
        _mAdapter = new MyAdapter(getSupportFragmentManager());
        
        _mPager = (ViewPager) findViewById(R.id.pager);
        _mPager.setAdapter(_mAdapter);
 
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
