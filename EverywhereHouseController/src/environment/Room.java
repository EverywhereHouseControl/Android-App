package environment;

/**
 * Class which behaviour will be implemented in child classes of child classes. 
 * Defines the contents of a room
 * @author Colin
 *
 */
public abstract class Room {
	String _description= "Undefined";

	/**
	 * Get the room's and item's description. This method will be implemented in child classes.
	 * @return
	 */
	public abstract String getDescription();
	
	/**
	 * Set the room's description.
	 * @param description
	 */
	public void setDescription(String description){
		_description = description;
	}
	
			
}
