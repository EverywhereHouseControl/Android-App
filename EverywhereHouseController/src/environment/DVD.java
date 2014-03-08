package environment;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class DVD extends RoomDecorator {
	
	String _item="DVD";
	String _name;
	List<View> _elementList = null;
	
	public DVD(){}
	
	public DVD(Room room, String name){
		_room=room;
		this._name=name;
	}
	
	@Override
	public String getDescription(){
		return _room.getDescription() + "+ DVD ";
	}
	
	@Override
	public void setDescription(String description){
		_description = description;
	}

	
	//	Later, we should load the dvd's controller
	@Override
	public void setView(Context c, LinearLayout ll){
		CheckBox checkbox = new CheckBox(c);
		checkbox.setClickable(true);
		checkbox.setText("On/Off");
		ll.addView(checkbox);
	}

}
