package environment;

public abstract class RoomDecorator extends Room {
	String _type;
	
	RoomDecorator(){}
	
	RoomDecorator(String type){
		_type=type;
		_description=type;
	}
}
