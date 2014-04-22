package ehc.net;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;

import framework.JSON;
import framework.SpinnerEventContainer;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CreateNewEventActivity extends Activity {

	private SpinnerEventContainer servicesList;
	private boolean serviceSelected = false;
	private boolean textChanged = false;
	private boolean actionSelected = false;
	private int daySelected = 0;
	private int monthSelected = 0;
	private int yearSelected = 0;
	private int hourSelected = 0;
	private int minuteSelected = 0;

	static final int TIME_DIALOG = 500;
	static final int DATE_DIALOG = 999;

	private TextView dateSelected;
	private TextView timeSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_event_dialog);

		Calendar c = Calendar.getInstance();
		daySelected = c.get(Calendar.DAY_OF_MONTH);
		monthSelected = c.get(Calendar.MONTH);
		yearSelected = c.get(Calendar.YEAR);
		hourSelected = c.get(Calendar.HOUR);
		minuteSelected = c.get(Calendar.MINUTE);

		dateSelected = (TextView) findViewById(R.id.tv_date);
		timeSelected = (TextView) findViewById(R.id.tv_time);

		/**
		 * Spinner info : services
		 */
		final Spinner itemList = (Spinner) findViewById(R.id.sp_item);
		try {
			servicesList = JSON.getInstance(this).getItemsWithLocation();
			ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
					this, R.layout.spinner_simple_data,
					servicesList.getFullServiceInformation());
			itemList.setAdapter(spinnerAdapter);
			itemList.setSelection(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		itemList.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position != 0) {
					serviceSelected = true;
					final Spinner actionList = (Spinner) findViewById(R.id.sp_action);
					ArrayList<String> actions = JSON.getInstance(
							getApplicationContext()).getServices(
							servicesList.getHouse(position-1),
							servicesList.getRoom(position-1),
							servicesList.getService(position-1));
					ArrayAdapter<String> spinnerActionAdapter = new ArrayAdapter<String>(
							getApplicationContext(),
							R.layout.spinner_simple_data, actions);
					actionList.setAdapter(spinnerActionAdapter);
					actionList.setSelection(0);

					actionList
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int position, long id) {
									if ((position != 0) && (serviceSelected))
										actionSelected = true;
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> parent) {
									// TODO Auto-generated method stub

								}
							});

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		/**
		 * Spinner info : actions
		 */

		EditText et = (EditText) findViewById(R.id.event_name);
		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.toString().length() > 0)
					textChanged = true;
				else
					textChanged = false;

			}

		});

		/**
		 * Date and time buttons
		 */

		Button dateButton = (Button) findViewById(R.id.bt_select_date);
		dateButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// When button is clicked, dialog is closed.
				showDialog(DATE_DIALOG);
			}
		});

		Button timeButton = (Button) findViewById(R.id.bt_select_time);
		timeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// When button is clicked, dialog is closed.
				showDialog(TIME_DIALOG);
			}
		});

		/**
		 * Cancel and accept buttons
		 */
		Button cancelButton = (Button) findViewById(R.id.cancel_event);
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// When button is clicked, dialog is closed.
				finish();
			}
		});

		Button acceptButton = (Button) findViewById(R.id.create_event);
		acceptButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String date = (String) dateSelected.getText();
				String time = (String) timeSelected.getText();

				if (textChanged == false)
					Toast.makeText(getApplicationContext(), "Insert a name",
							Toast.LENGTH_LONG).show();
				else if (serviceSelected == false)
					Toast.makeText(getApplicationContext(), "Select a service",
							Toast.LENGTH_LONG).show();
				else if (date.equals((String) "No date selected"))
					Toast.makeText(getApplicationContext(), "Select a date",
							Toast.LENGTH_LONG).show();
				else if (time.equals((String) "No time selected"))
					Toast.makeText(getApplicationContext(), "Select a time",
							Toast.LENGTH_LONG).show();
			}
		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG:
			return new TimePickerDialog(this, timerPickerListener,
					hourSelected, minuteSelected, true);
		case DATE_DIALOG:
			return new DatePickerDialog(this, datePickerListener, yearSelected,
					monthSelected, daySelected);
		}

		return super.onCreateDialog(id);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			dateSelected.setText("Selected date: " + dayOfMonth + "-"
					+ monthOfYear + "-" + year);
			yearSelected = year;
			monthSelected = monthOfYear;
			daySelected = dayOfMonth;

		}
	};

	private TimePickerDialog.OnTimeSetListener timerPickerListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			timeSelected.setText("Selected time: " + hourOfDay + ":" + minute);
			hourSelected = hourOfDay;
			minuteSelected = minute;
		}
	};

}
