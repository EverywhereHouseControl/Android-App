package places;

import ehcontrol.net.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint( "ValidFragment" )
public class LivingRoomMenu extends Fragment
{    
	
	public LivingRoomMenu() {}
 
    @Override
    public void onCreate( Bundle savedInstanceState ) 
    {
        super.onCreate( savedInstanceState );
    }
 
    @Override
    public void onActivityCreated( Bundle savedInstanceState ) 
    {
        super.onActivityCreated( savedInstanceState );
    }
 
    /**Crea la vista ligando el archivo .XML al rootView
     *@return la vista del salón 
     **/
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState ) 
    {
    	ViewGroup rootView = ( ViewGroup ) inflater.inflate( R.layout.view_livingroom_menu, container, false );
        
    	rootView.findViewById( R.id.buttonTvLR ).setOnClickListener( new View.OnClickListener() 
    	{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    	try 
					{
						Class<?> _clazz = Class.forName( "items.Tv" );
						Intent _intent = new Intent(getActivity(),_clazz );
						startActivity( _intent );
					} 
					catch ( ClassNotFoundException e ) 
					{
						e.printStackTrace();
					}
			 }
		});
    	
    	return rootView;
    }    
}
