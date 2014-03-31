package environment;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Abstract class of decorator's pattern.
 * @author Colin
 *
 */
public abstract class RoomDecorator extends Room 
{
	String _type;
	protected Room _room;
	
	RoomDecorator(){}
	
	RoomDecorator(String type)
	{
		_type=type;
		_description=type;
	}
		
	/**
	 * It will load the respective view in the LinearLayout. 
	 * @param c
	 * @param ll
	 */
	public abstract void setView(Context c, LinearLayout ll);
}
