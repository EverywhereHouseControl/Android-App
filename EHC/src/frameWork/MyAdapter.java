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
    }

    @Override
    public int getCount() 
    {
        return 3;
    }

    @Override
    public Fragment getItem( int _position ) 
    {
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
