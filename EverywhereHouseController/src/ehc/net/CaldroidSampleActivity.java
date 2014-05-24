package ehc.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import parserJSON.JSON;

import adapters.EventAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import ehc.net.R;
import framework.Event;
import gcmService.GCMIntentService;


@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends FragmentActivity 
{
	//------------Variables-----------------------
	private CaldroidFragment _caldroidFragment;
	private CaldroidFragment _dialogCaldroidFragment;
	private ArrayList<Date> _eventDays;
	private HashMap<Date, ArrayList<Event>> _eventsPerDate;
	private EventAdapter _adapter;
	private String _currentHouse;
	//-----------------------------------	
	/**
	 * Gets user events and then it will be stored in user's calendar
	 * 
	 * @throws ParseException
	 */
	private void setCustomResourceForDates() throws ParseException 
	{
		_currentHouse = getIntent().getExtras().getString( "House" );
		
		Calendar _cal = Calendar.getInstance();
		_eventDays = new ArrayList<Date>();
		_eventsPerDate = new HashMap<Date, ArrayList<Event>>();
		Date _date = null;

		JSON events = JSON.getInstance(getApplicationContext());
		HashMap<String, Event> eventInfo = events.getEvents();

		// Getting all the information about user. This information has been
		// stored in loadJSONEvent (JSON Class)
		for ( Entry<String, Event> _entry : eventInfo.entrySet() ) 
		{
			boolean dateWithEvents = false;
			Event _e = _entry.getValue();
			_date = _entry.getValue().getDate();
			Date _updateDate = new Date();
			ArrayList<Event> _eventlist = new ArrayList<Event>();
			for ( Date _d : _eventDays ) 
			{
				if ( _d.equals( _date ) ) 
				{
					dateWithEvents = true;
					for (Entry<Date, ArrayList<Event>> _entry2 : _eventsPerDate.entrySet()) 
					{
						if ( _entry2.getKey().equals( _date ) ) 
						{
							_updateDate = _date;
							_eventlist = _entry2.getValue();
							_eventlist.add (new Event( _e.getName(), _e.getItem(), _e.getCreator(), _date, _e.getHour(), _e.getMinute() ) );
						}
					}
				}
			}
			if ( !dateWithEvents ) 
			{
				_eventDays.add( _date );
				_eventlist = new ArrayList<Event>();
				_eventlist.add( new Event(_e.getName(), _e.getItem(), _e.getCreator(), _date, _e.getHour(), _e.getMinute() ) );
				_eventsPerDate.put( _date, _eventlist );
			} 
			else 
			{				
				_eventsPerDate.remove( _updateDate );
				_eventsPerDate.put (_updateDate, _eventlist );
			}
				
		}

		for (Date _d : _eventDays) 
		{
			if ( _caldroidFragment != null ) 
			{
				_caldroidFragment.setBackgroundResourceForDate( R.color.Blue, ( Date ) _d );
				_caldroidFragment.setTextColorForDate( R.color.White, ( Date ) _d );
			}
		}
	}

	@Override
	protected void onCreate( Bundle savedInstanceState ) 
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		final SimpleDateFormat _formatter = new SimpleDateFormat( "dd MMM yyyy" );
		// Setup caldroid fragment
		// **** If you want normal CaldroidFragment, use below line ****
		_caldroidFragment = new CaldroidFragment();

		// If Activity is created after rotation
		if ( savedInstanceState != null ) 
		{
			_caldroidFragment.restoreStatesFromKey( savedInstanceState, "CALDROID_SAVED_STATE" );
		}
		// If activity is created from fresh
		else 
		{
			Bundle _args = new Bundle();
			Calendar _cal = Calendar.getInstance();
			_args.putInt( CaldroidFragment.MONTH, _cal.get( Calendar.MONTH ) + 1 );
			_args.putInt( CaldroidFragment.YEAR, _cal.get( Calendar.YEAR ) );
			_args.putBoolean( CaldroidFragment.ENABLE_SWIPE, true );
			_args.putBoolean( CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true );

			// Uncomment this to customize startDayOfWeek
			// args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
			// CaldroidFragment.TUESDAY); // Tuesday
			_caldroidFragment.setArguments( _args );
		}

		try 
		{
			setCustomResourceForDates();
		} catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Attach to the activity
		FragmentTransaction _t = getSupportFragmentManager().beginTransaction();
		_t.replace( R.id.calendar1, _caldroidFragment );
		_t.commit();

		// Setup listener
		final CaldroidListener _listener = new CaldroidListener() 
		{

			@Override
			public void onSelectDate( Date date, View view ) 
			{
				updateEventList(getApplicationContext(), date);
			}

			@Override
			public void onChangeMonth( int month, int year ) 
			{
				String text = "month: " + month + " year: " + year;
//				Toast.makeText(getApplicationContext(), text,
//						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClickDate( Date date, View view ) 
			{
//				Toast.makeText(getApplicationContext(),
//						"Long click " + formatter.format(date),
//						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCaldroidViewCreated() 
			{
				if ( _caldroidFragment.getLeftArrowButton() != null ) 
				{
//					Toast.makeText(getApplicationContext(),
//							"Caldroid view is created", Toast.LENGTH_SHORT)
//							.show();
				}
			}

		};
		
		/** Fragment Dialog creation **/
		
		LinearLayout _ll = ( LinearLayout ) this.findViewById( R.id.ll_event );
		
		TextView _txt = ( TextView ) findViewById( R.id.event_list_title );
		Typeface _font = Typeface.createFromAsset( getAssets(), "imagine_earth.ttf" );
		_txt.setTypeface( _font );
		
		ImageButton _ib = ( ImageButton ) _ll.findViewById( R.id.create_event_button );
		_ib.setOnClickListener( new OnClickListener() 
		{
			
			@Override
			public void onClick( View v ) 
			{
//				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//				DialogFragment dialog = CreateUserFragmentDialog.newInstance();
//				dialog.show(ft, "dialog"); 
				
				Class<?> _clazz = null;
				try {
					_clazz = Class.forName( "ehc.net.CreateNewEventActivity" );
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent _intent = new Intent( CaldroidSampleActivity.this, _clazz );
				_intent.putExtra( "House", _currentHouse );
				startActivity( _intent );
			}
		});
	
		// Setup Caldroid
		_caldroidFragment.setCaldroidListener(_listener);

	}

	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState( Bundle outState ) 
	{		
		// TODO Auto-generated method stub
		super.onSaveInstanceState( outState );

		if ( _caldroidFragment != null ) 
		{
			_caldroidFragment.saveStatesToKey( outState, "CALDROID_SAVED_STATE" );
		}

		if (_dialogCaldroidFragment != null ) 
		{
			_dialogCaldroidFragment.saveStatesToKey( outState, "DIALOG_CALDROID_SAVED_STATE" );
		}
	}

	private boolean hasEvent( Date date ) 
	{
		for ( Date _d : _eventDays )
			if ( _d.equals( date ) )
				return true;
		return false;
	}

	private ArrayList<Event> getEvents( Date d ) 
	{
		return _eventsPerDate.get( d );
	}

	private void updateEventList( Context c, Date date ) 
	{
		if ( hasEvent( date ) ) 
		{
			_adapter = new EventAdapter( this, getEvents( date ), _currentHouse );
			ListView _list = ( ListView ) findViewById( R.id.eventList );
			_list.setAdapter(_adapter);
		} 
		else 
		{
			_adapter = new EventAdapter( this, new ArrayList<Event>(), _currentHouse );
			ListView _list = ( ListView ) findViewById( R.id.eventList );
			_list.setAdapter( _adapter );
		}			
	}
	
	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		try 
		{
			Class<?> _clazz = Class.forName( "ehc.net.MainMenu" );
			Intent _intent = new Intent( this, _clazz );
			_intent.putExtra( "House", _currentHouse );
			startActivity( _intent );
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		}
	}
}
