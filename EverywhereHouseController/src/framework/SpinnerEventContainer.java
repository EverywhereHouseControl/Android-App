package framework;

import java.util.ArrayList;

public class SpinnerEventContainer 
{
	//------------Variables-----------------------
	private ArrayList<SpinnerEventInfo> _list;
	//-----------------------------------

	public SpinnerEventContainer() 
	{
		super();
		_list = new ArrayList<SpinnerEventContainer.SpinnerEventInfo>();
	}

	public void add( String room, String service, String house ) 
	{
		_list.add( new SpinnerEventInfo( room, service, house ) );
	}

	public SpinnerEventInfo get( int i ) 
	{
		return _list.get(i);
	}

	public void remove( int i ) 
	{
		_list.remove(i);
	}

	public ArrayList<String> getFullServiceInformation() 
	{
		ArrayList<String> _fullInfo = new ArrayList<String>();
		_fullInfo.add(SpinnerEventInfo.getDefaultSpinnerRegister());
		for (SpinnerEventInfo _s : _list) 
		{
			_fullInfo.add( _s.getSpinnerRegister() );
		}
		return _fullInfo;
	}

	public String getHouse( int index )
	{
		return _list.get(index).getHouse();
	}
	
	public String getRoom( int index )
	{
		return _list.get(index).getRoom();
	}
	
	public String getService(int index)
	{
		return _list.get( index ).getService();
	}
	
	private static class SpinnerEventInfo 
	{
		//------------Variables-----------------------
		private String _room;
		private String _service;
		private String _house;
		//-----------------------------------

		public SpinnerEventInfo( String room, String service, String house ) 
		{
			super();
			_room = room;
			_service = service;
			_house = house;
		}

		public String getSpinnerRegister() 
		{			
			return _house + " : " + _room + " - " + _service;
		}
		
		static String getDefaultSpinnerRegister()
		{
			return "Select a service.";
		}
		
		public String getHouse()
		{
			return _house;
		}
		
		public String getRoom()
		{
			return _room;
		}
		
		public String getService()
		{
			return _service;
		}

	}

}
