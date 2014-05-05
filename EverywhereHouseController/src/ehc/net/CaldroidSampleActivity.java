package ehc.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import parserJSON.JSON;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import ehc.net.R;
import framework.Event;
import framework.EventAdapter;


@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends FragmentActivity {
	private CaldroidFragment caldroidFragment;
	private CaldroidFragment dialogCaldroidFragment;
	private ArrayList<Date> eventDays;
	private HashMap<Date, ArrayList<Event>> eventsPerDate;
	private EventAdapter adapter;

	/**
	 * Gets user events and then it will be stored in user's calendar
	 * 
	 * @throws ParseException
	 */
	private void setCustomResourceForDates() throws ParseException {
		Calendar cal = Calendar.getInstance();
		eventDays = new ArrayList<Date>();
		eventsPerDate = new HashMap<Date, ArrayList<Event>>();
		Date date = null;

		JSON events = JSON.getInstance(getApplicationContext());
		HashMap<String, Event> eventInfo = events.getEvents();

		// Getting all the information about user. This information has been
		// stored in loadJSONEvent (JSON Class)
		for (Entry<String, Event> entry : eventInfo.entrySet()) {
			boolean dateWithEvents = false;
			Event e = entry.getValue();
			date = entry.getValue().getDate();
			Date updateDate = new Date();
			ArrayList<Event> eventlist = new ArrayList<Event>();
			for (Date d : eventDays) {
				if (d.equals(date)) {
					dateWithEvents = true;
					for (Entry<Date, ArrayList<Event>> entry2 : eventsPerDate
							.entrySet()) {
						if (entry2.getKey().equals(date)) {
							updateDate = date;
							eventlist = entry2.getValue();
							eventlist.add(new Event(e.getName(), e.getItem(), e
									.getCreator(), date, e.getHour(), e
									.getMinute()));
						}
					}
				}
			}
			if (!dateWithEvents) {
				eventDays.add(date);
				eventlist = new ArrayList<Event>();
				eventlist.add(new Event(e.getName(), e.getItem(), e
						.getCreator(), date, e.getHour(), e.getMinute()));
				eventsPerDate.put(date, eventlist);
			} else {
				eventsPerDate.remove(updateDate);
				eventsPerDate.put(updateDate, eventlist);
			}
				
		}

		for (Date d : eventDays) {
			if (caldroidFragment != null) {
				caldroidFragment.setBackgroundResourceForDate(R.color.Blue,
						(Date) d);
				caldroidFragment.setTextColorForDate(R.color.White, (Date) d);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		// Setup caldroid fragment
		// **** If you want normal CaldroidFragment, use below line ****
		caldroidFragment = new CaldroidFragment();

		// If Activity is created after rotation
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

			// Uncomment this to customize startDayOfWeek
			// args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
			// CaldroidFragment.TUESDAY); // Tuesday
			caldroidFragment.setArguments(args);
		}

		try {
			setCustomResourceForDates();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				updateEventList(getApplicationContext(), date);
			}

			@Override
			public void onChangeMonth(int month, int year) {
				String text = "month: " + month + " year: " + year;
//				Toast.makeText(getApplicationContext(), text,
//						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClickDate(Date date, View view) {
//				Toast.makeText(getApplicationContext(),
//						"Long click " + formatter.format(date),
//						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCaldroidViewCreated() {
				if (caldroidFragment.getLeftArrowButton() != null) {
//					Toast.makeText(getApplicationContext(),
//							"Caldroid view is created", Toast.LENGTH_SHORT)
//							.show();
				}
			}

		};
		
		/** Fragment Dialog creation **/
		
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.ll_event);
		
		TextView txt = (TextView) findViewById(R.id.event_list_title);
		Typeface font = Typeface.createFromAsset(getAssets(), "imagine_earth.ttf");
		txt.setTypeface(font);
		
		ImageButton ib = (ImageButton) ll.findViewById(R.id.create_event_button);
		ib.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//				DialogFragment dialog = CreateUserFragmentDialog.newInstance();
//				dialog.show(ft, "dialog"); 

					startActivity(new Intent(getApplicationContext(), CreateNewEventActivity.class));
			}
		});
		
		
		

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);

	}

	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}

		if (dialogCaldroidFragment != null) {
			dialogCaldroidFragment.saveStatesToKey(outState,
					"DIALOG_CALDROID_SAVED_STATE");
		}
	}

	private boolean hasEvent(Date date) {
		for (Date d : eventDays)
			if (d.equals(date))
				return true;
		return false;
	}

	private ArrayList<Event> getEvents(Date d) {
		return eventsPerDate.get(d);
	}

	private void updateEventList(Context c, Date date) {
		if (hasEvent(date)) {
			adapter = new EventAdapter(this, getEvents(date));
			ListView list = (ListView) findViewById(R.id.eventList);
			list.setAdapter(adapter);
		} else {
			adapter = new EventAdapter(this, new ArrayList<Event>());
			ListView list = (ListView) findViewById(R.id.eventList);
			list.setAdapter(adapter);
		}
			
	}

}
