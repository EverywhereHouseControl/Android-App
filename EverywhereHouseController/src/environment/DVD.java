package environment;

public class DVD extends RoomDecorator {
	
	String _item="DVD";
	String _name;
	private Room _room;
	
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

}
