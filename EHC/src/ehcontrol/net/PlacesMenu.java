package ehcontrol.net;

import frameWork.MyAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.HorizontalScrollView;
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
        
        /*_mainPlacesView.setOnTouchListener( new View.OnTouchListener() 
        {

            @Override
            public boolean onTouch( View v, MotionEvent event ) 
            {	
            	log("touch");
            	centerViews( event );
                return false;
            }
        });*/
 
    }
    /**
     * Se encarga del desplazamiento entre las vistas.
     * @param event
     */
    /*private void ChangesViews( MotionEvent event )
    {
		
    	if ( event.getAction() == MotionEvent.ACTION_UP ) 
    	{ 
    		
    		LinearLayout linearLayout = ( ( LinearLayout ) _hsc.findViewById( R.id.viewLivingRoomMenu ) );
    		LinearLayout listViews = ( ( LinearLayout ) _hsc.findViewById( R.id.listViews ) );
    		
    		if( ( _hsc.getScrollX() > ( ( linearLayout.getWidth() / 2 ) + ( linearLayout.getWidth() * ( _numView - 1 ) ) ) ) &&
        			( _numView < listViews.getChildCount() ) )
    		{
    			
    			_hsc.setScrollX( linearLayout.getWidth() * _numView );
    			_numView ++;
    			centerView();
    		}
    		else    		
    		if( ( _hsc.getScrollX() < ( ( linearLayout.getWidth() - ( linearLayout.getWidth() / 2 ) ) + ( linearLayout.getWidth() * ( _numView - 2 ) ) ) ) &&
    			( _numView != 1 )	)
    		{
    			_numView --;
    			_hsc.setScrollX( linearLayout.getWidth() * ( _numView - 1 ) );
    			centerView();
    		}
    		else    		
    		{
    			_hsc.setScrollX( linearLayout.getWidth() * ( _numView - 1 ) );
    			log( "Mal desplazamiento" );
    		}		
    	}
    
    }
    */
    /**
     * Desplaza el scrollView de la vista destino al principio de la misma. 
    **//*
    private void centerViews( MotionEvent event )
    {
    	if ( event.getAction() == MotionEvent.ACTION_UP )
    	{
    		switch( _mAdapter.getPosition() )
    		{
    			case 0:
    				_svlr.setScrollY( 0 );
    				_svk.setScrollY( 0 );
    				break;
    		
    			case 1:
    				_svlr.setScrollY( 0 );
    				_svk.setScrollY( 0 );
    				_svb.setScrollY( 0 );
    				break;
    		
    			case 2:
    				_svk.setScrollY( 0 );
    				_svb.setScrollY( 0 );
    				break;
        		
    			default:
    				break;
    		}
    	}
    }*/
    
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
