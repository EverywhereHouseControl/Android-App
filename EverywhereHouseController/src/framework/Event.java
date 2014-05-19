package framework;

import java.util.Date;

public class Event 
{
	//------------Variables-----------------------
	private String _name;
	private int _item;
	private String _creator;
	private String _year;
	private String _month;
	private String _day;
	private String _hour;
	private String _minute;
	private Date _date;
	//-----------------------------------
	
	public Event( String name, int item, String creator, Date date, String hour, String minute ) 
	{
		super();
		_name = name;
		_item = item;
		_creator = creator;
		setDate(_date);
		_hour = hour;
		_minute = minute;
	}
	
	public Event( String name, int item, String creator, String year,
			String month, String day, String hour, String minute ) 
	{
		super();
		_name = name;
		_item = item;
		_creator = creator;
		_year = year;
		_month = month;
		_day = day;
		_hour = hour;
		_minute = minute;
	}
	
	public String getName() 
	{
		return _name;
	}

	public void setName( String name ) 
	{
		_name = name;
	}

	public int getItem() 
	{
		return _item;
	}

	public void setItem( int item ) 
	{
		_item = item;
	}

	public String getCreator() 
	{
		return _creator;
	}

	public void setCreator( String creator ) 
	{
		_creator = creator;
	}

	public String getYear() 
	{
		return _year;
	}

	public void setYear( String year ) 
	{
		_year = year;
	}

	public String getMonth() 
	{
		return _month;
	}

	public void setMonth( String month ) 
	{
		_month = month;
	}

	public String getDay() 
	{
		return _day;
	}

	public void setDay( String day ) 
	{
		_day = day;
	}

	public String getHour() 
	{
		return _hour;
	}

	public void setHour( String hour ) 
	{
		_hour = hour;
	}

	public String getMinute() 
	{
		return _minute;
	}

	public void setMinute( String minute ) 
	{
		_minute = minute;
	}
	
	public Date getDate() 
	{
		return _date;
	}

	public void setDate( Date date ) 
	{
		_date = date;
	}
}
