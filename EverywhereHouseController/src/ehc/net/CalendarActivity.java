package ehc.net;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarActivity extends Activity {

	CalendarView calendar;
	TextView tv;
	private int year;
	private int month;
	private int day;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view);
		findViews();
		final Calendar c = Calendar.getInstance();

		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
		

	}

	private void findViews() {
		calendar = (CalendarView) findViewById(R.id.calendarView1);
		tv = (TextView) findViewById(R.id.calendar_text);
		calendar.setOnDateChangeListener(new OnDateChangeListener() {

			public void onSelectedDayChange(CalendarView view, int y,
					int m, int dayOfMonth) {
				year=y;
				month=m;
				day=dayOfMonth;
				updateDisplay();
			}
		});

	}

	private void updateDisplay() {
		tv.setText(new StringBuilder().append(year).append("-").append(month)
				.append("-").append(day));

	}


}
