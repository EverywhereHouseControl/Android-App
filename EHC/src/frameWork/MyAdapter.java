package frameWork;

import places.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter 
{
	//------Variables----------
	private int _position;
	//-------------------------
	
    public MyAdapter( FragmentManager fm ) 
    {
        super( fm );
        _position = -1;
    }

    @Override
    public int getCount() 
    {
        return 7;
    }

    @Override
    public Fragment getItem( int _position ) 
    {
        //Crear una lista de vistas para quitar el switch
    	switch ( _position ) 
        {
        	case 0:
        		_position = 0;
        		return new LivingRoomMenu();
            
        	case 1:
        		_position = 1;
        		return new KitchenMenu();
            
        	case 2:
        		_position = 2;
        		return new BathRoomMenu();

        	case 3:
        		_position = 3;
        		return new RoomMenu();
        	
        	case 4:
        		_position = 4;
        		return new TerraceMenu();
        		
        	case 5:
        		_position = 5;
        		return new GardenMenu();
        		
        	case 6:
        		_position = 6;
        		return new GarageMenu();
        		
        	default:
        		return null;
        }
    }
    
    /**
     * 
     * @return un entero con el índice de la vista actual
     */
    public int getPosition()
    {
    	return _position;
    }
}
