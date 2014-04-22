package framework;

import java.util.ArrayList;

public class SpinnerEventContainer {

	private ArrayList<SpinnerEventInfo> list;

	public SpinnerEventContainer() {
		super();
		list = new ArrayList<SpinnerEventContainer.SpinnerEventInfo>();
	}

	public void add(String room, String service, String house) {
		list.add(new SpinnerEventInfo(room, service, house));
	}

	public SpinnerEventInfo get(int i) {
		return list.get(i);
	}

	public void remove(int i) {
		list.remove(i);
	}

	public ArrayList<String> getFullServiceInformation() {
		ArrayList<String> fullInfo = new ArrayList<String>();
		fullInfo.add(SpinnerEventInfo.getDefaultSpinnerRegister());
		for (SpinnerEventInfo s : list) {
			fullInfo.add(s.getSpinnerRegister());
		}
		return fullInfo;
	}

	public String getHouse(int index){
		return list.get(index).getHouse();
	}
	
	public String getRoom(int index){
		return list.get(index).getRoom();
	}
	
	public String getService(int index){
		return list.get(index).getService();
	}
	
	private static class SpinnerEventInfo {

		private String room;
		private String service;
		private String house;

		public SpinnerEventInfo(String room, String service, String house) {
			super();
			this.room = room;
			this.service = service;
			this.house = house;
		}

		public String getSpinnerRegister() {			
			return house + " : " + room + " - " + service;
		}
		
		static String getDefaultSpinnerRegister(){
			return "Select a service.";
		}
		
		public String getHouse(){
			return house;
		}
		
		public String getRoom(){
			return room;
		}
		
		public String getService(){
			return service;
		}

	}

}
