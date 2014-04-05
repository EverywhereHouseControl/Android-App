package ehc.net;

import hirondelle.date4j.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CalendarHelper;

import framework.EventFragment;
import framework.JSON;

@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends FragmentActivity {
	private boolean undo = false;
	private CaldroidFragment caldroidFragment;
	private CaldroidFragment dialogCaldroidFragment;
	private TextView tvEvent;
	private ArrayList<Date> eventDays;

	/**
	 * Gets user events and then it will be stored in user's calendar
	 * 
	 * @throws ParseException
	 */
	private void setCustomResourceForDates() throws ParseException {
		Calendar cal = Calendar.getInstance();
		eventDays = new ArrayList<Date>();
		JSON events = JSON.getInstance(getApplicationContext());
		HashMap<String, ArrayList<Object>> event = events.getEvents();

		Date date = null;

		// Getting all the information about user. This information has been
		// stored in loadJSONEvent (JSON Class)
		for (Entry<String, ArrayList<Object>> entry : event.entrySet()) {
			int index = 0;
			List<Object> l = entry.getValue();
			String name = (String) l.get(0);
			int item = (Integer) l.get(1);
			String created = (String) l.get(2);
			String dateFormat = (String) l.get(3) + "-" + (String) l.get(4)
					+ "-" + (String) l.get(5);
			date = CalendarHelper.getDateFromString(dateFormat, "yyyy-MM-dd");
			eventDays.add(date);
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
		FragmentTransaction e = getSupportFragmentManager().beginTransaction();
		final EventFragment ef = new EventFragment();
		e.replace(R.id.event_fragment, ef);
		e.commit();

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				ef.setText(date.toString());
			}

			@Override
			public void onChangeMonth(int month, int year) {
				String text = "month: " + month + " year: " + year;
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClickDate(Date date, View view) {
				Toast.makeText(getApplicationContext(),
						"Long click " + formatter.format(date),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCaldroidViewCreated() {
				if (caldroidFragment.getLeftArrowButton() != null) {
					Toast.makeText(getApplicationContext(),
							"Caldroid view is created", Toast.LENGTH_SHORT)
							.show();
				}
			}

		};

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

	private void eventLoader() {

	}

}
