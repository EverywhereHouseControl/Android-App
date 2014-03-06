package environment;

public abstract class Room {
	String _description= "Sin definir";
	
	public String getDescription(){
		return _description;
	}
	
	public void setDescription(String description){
		_description = description;
	}

}
