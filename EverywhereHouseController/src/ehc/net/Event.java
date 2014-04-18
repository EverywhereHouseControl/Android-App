package ehc.net;

import java.util.Date;

public class Event {
	private String name;
	private int item;
	private String creator;
	private String year;
	private String month;
	private String day;
	private String hour;
	private String minute;
	private Date date;
	
	public Event(String name, int item, String creator, Date date, String hour, String minute) {
		super();
		this.name = name;
		this.item = item;
		this.creator = creator;
		this.setDate(date);
		this.hour = hour;
		this.minute = minute;
	}
	
	public Event(String name, int item, String creator, String year,
			String month, String day, String hour, String minute) {
		super();
		this.name = name;
		this.item = item;
		this.creator = creator;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
