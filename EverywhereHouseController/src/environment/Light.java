package environment;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class Light extends RoomDecorator{
	
	String _item="Light";
	String _name;
	private Room _room; 
	List<View> _elementList = null;
	
	public Light(){}
	
	public Light(Room room, String name){
		_room=room;
		this._name=name;
	}
	
	@Override
	public String getDescription(){
		return _room.getDescription() + "+ light ";
	}
	
	@Override
	public void setDescription(String description){
		_description = description;
	}


	@Override
	public void setView(Context c, LinearLayout ll) {
		setViewBoolean(c, ll);
		setViewFloat(c, ll);		
	}
	
	/**
	 * Define how to be represented the boolean value (On/Off)
	 * @param c
	 * @param ll
	 */
	private void setViewBoolean(Context c, LinearLayout ll) {
		CheckBox checkbox = new CheckBox(c);
		checkbox.setClickable(true);
		checkbox.setText("On/Off");
		ll.addView(checkbox);		
	}

	/**
	 * Define how to be represented the intensity of the light.
	 * @param c
	 * @param ll
	 */
	private void setViewFloat(Context c, LinearLayout ll) {
		SeekBar seekbar = new SeekBar(c);
		ll.addView(seekbar);
	}

	
}
