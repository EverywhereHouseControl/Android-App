package environment;

public class Light extends RoomDecorator{
	
	String _item="Light";
	String _name;
	private Room _room;
	
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

}
