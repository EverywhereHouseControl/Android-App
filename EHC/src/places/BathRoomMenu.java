package places;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ehcontrol.net.R;

@SuppressLint( "ValidFragment" )
public class BathRoomMenu extends Fragment 
{ 
	public BathRoomMenu(){}
 
    @Override
    public void onCreate( Bundle savedInstanceState ) 
    {
        super.onCreate( savedInstanceState );
        Log.e( "Test", "hello" );
    }
 
    @Override
    public void onActivityCreated( Bundle savedInstanceState ) 
    {
        super.onActivityCreated( savedInstanceState );
    }
 
    /**Crea la vista ligando el archivo .XML al rootView
     *@return la vista del baño 
     **/
    public View onCreateView( LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState ) 
    {
    	ViewGroup rootView = ( ViewGroup ) inflater.inflate( R.layout.view_bathroom_menu, container, false );
        return rootView;
    }
}