package ehc.net;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;
import serverConnection.Post;
import ehc.net.R;
import framework.SpinnerEventContainer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
	// ------------Variables-----------------------
	private SpinnerEventContainer _servicesList;
	private boolean _serviceSelected = false;
	private boolean _textChanged = false;
	private boolean _actionSelected = false;
	private int _daySelected = 0;
	private int _monthSelected = 0;
	private int _yearSelected = 0;
	private int _hourSelected = 0;
	private int _minuteSelected = 0;

	static final int TIME_DIALOG = 500;
	static final int DATE_DIALOG = 999;

	private String _servicename;
	private TextView _dateSelected;
	private TextView _timeSelected;
	private EditText _et;

	private Spinner _itemList;

	private String _house;
	private String _file;
	private ArrayList<String> _selectedService = new ArrayList<String>();
	private String _data;
	private ArrayList<String> _actions = null;

	private final String _ip = Post._ip;

	// ------------Variables-----------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_event_dialog);

		Calendar _c = Calendar.getInstance();
		_daySelected = _c.get(Calendar.DAY_OF_MONTH);
		_monthSelected = _c.get(Calendar.MONTH);
		_yearSelected = _c.get(Calendar.YEAR);
		_hourSelected = _c.get(Calendar.HOUR);
		_minuteSelected = _c.get(Calendar.MINUTE);

		_dateSelected = (TextView) findViewById(R.id.tv_date);
		_timeSelected = (TextView) findViewById(R.id.tv_time);

		/**
		 * Spinner info : services
		 */
		_itemList = (Spinner) findViewById(R.id.sp_item);
		try {
			_servicesList = JSON.getInstance(this).getItemsWithLocation();
			ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
					this, R.layout.spinner_simple_data,
					_servicesList.getFullServiceInformation());
			_itemList.setAdapter(spinnerAdapter);
			_itemList.setSelection(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		_itemList.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position != 0) {
					_serviceSelected = true;
					final Spinner _actionList = (Spinner) findViewById(R.id.sp_action);
					// ArrayList<String> actions = JSON.getInstance(
					// getApplicationContext()).getServices(
					// servicesList.getHouse(position - 1),
					// servicesList.getRoom(position - 1),
					// servicesList.getService(position - 1));
					int _service2 = parent.getItemAtPosition(position)
							.toString().indexOf("-");
					int _service1 = parent.getItemAtPosition(position)
							.toString().length();
					String _service = parent.getItemAtPosition(position)
							.toString().substring(_service2 + 2, _service1);
					_actions = setActionList(_service);

					ArrayAdapter<String> _spinnerActionAdapter = new ArrayAdapter<String>(
							getApplicationContext(),
							R.layout.spinner_simple_data, _actions);
					_actionList.setAdapter(_spinnerActionAdapter);
					_actionList.setSelection(0);

					_actionList
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int position, long id) {
									if ((position != 0) && (_serviceSelected)) {
										_actionSelected = true;
										_data = _actions.get(position)
												.toUpperCase();
									} else {
										_data = _actions.get(position)
												.toUpperCase();
									}

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

		_et = (EditText) findViewById(R.id.event_name);
		_et.addTextChangedListener(new TextWatcher() {

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
				if (s.toString().length() > 0) {
					_textChanged = true;
					_servicename = s.toString();
				} else
					_textChanged = false;
			}

		});

		/**
		 * Date and time buttons
		 */
		Button _dateButton = (Button) findViewById(R.id.bt_select_date);
		_dateButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// When button is clicked, dialog is closed.
				showDialog(DATE_DIALOG);
			}
		});

		Button _timeButton = (Button) findViewById(R.id.bt_select_time);
		_timeButton.setOnClickListener(new OnClickListener() {
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

		Button _acceptButton = (Button) findViewById(R.id.create_event);
		_acceptButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String _date = (String) _dateSelected.getText();
				String _time = (String) _timeSelected.getText();

				if (_textChanged == false)
					Toast.makeText(getApplicationContext(), "Insert a name",
							Toast.LENGTH_LONG).show();
				else if (_serviceSelected == false)
					Toast.makeText(getApplicationContext(), "Select a service",
							Toast.LENGTH_LONG).show();
				else if (_date.equals((String) "No date selected"))
					Toast.makeText(getApplicationContext(), "Select a date",
							Toast.LENGTH_LONG).show();
				else if (_time.equals((String) "No time selected"))
					Toast.makeText(getApplicationContext(), "Select a time",
							Toast.LENGTH_LONG).show();
				else if (_actions.get(0).toString()
						.equals("No actions for this service"))
					Toast.makeText(getApplicationContext(),
							"No valid service to save an event",
							Toast.LENGTH_LONG).show();
				else {
					_selectedService = parseService(_itemList.getSelectedItem()
							.toString());
					// new
					// SimpleActivityTask(getApplicationContext()).sendEvent(
					// selectedService[0],
					// selectedService[1],
					// selectedService[2],
					// "SEND",
					// "a",
					// dateSelected.getText() + " "
					// + timeSelected.getText(), et.getText()
					// .toString());
					Log.d("NEW EVENT", _selectedService.toString());
					Log.d("SEND EVENT", "CRASH");

					// It's load the profile's information.
					_file = JSON
							.loadUserInformation(CreateNewEventActivity.this);
					String _file2 = JSON
							.loadUserEnvironment(CreateNewEventActivity.this);

					try {
						JSONObject _obj = new JSONObject(_file2);
						JSONObject infoCasa = _obj.getJSONArray("houses ")
								.getJSONObject(0);
						_house = infoCasa.getString("name");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					sendEventConnection _connection = new sendEventConnection();
					_connection.execute();
				}

			}
		});

	}

	private ArrayList<String> parseService(String serv) {
		ArrayList<String> str = new ArrayList<String>();
		int service1 = serv.indexOf(':');
		int service2 = serv.indexOf('-');
		int service3 = serv.length();
		str.add(0, serv.substring(0, service1 - 1));
		str.add(1, serv.substring(service1 + 2, service2 - 1));
		str.add(2, serv.substring(service2 + 2, service3));
		return str;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG:
			return new TimePickerDialog(this, timerPickerListener,
					_hourSelected, _minuteSelected, false);
		case DATE_DIALOG:
			return new DatePickerDialog(this, datePickerListener,
					_yearSelected, _monthSelected, _daySelected);
		}

		return super.onCreateDialog(id);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			monthOfYear++;
			_dateSelected.setText("Selected date: " + dayOfMonth + "-"
					+ monthOfYear + "-" + year);
			_yearSelected = year;
			_monthSelected = monthOfYear + 1;
			_daySelected = dayOfMonth;
		}
	};

	private TimePickerDialog.OnTimeSetListener timerPickerListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			String _hourOfDayStr = null;
			String minuteStr = null;
			if (hourOfDay < 10)
				_hourOfDayStr = "0" + hourOfDay;
			else
				_hourOfDayStr = Integer.toString(hourOfDay);
			if (minute < 10)
				minuteStr = "0" + minute;
			else
				minuteStr = Integer.toString(minute);
			_timeSelected.setText("Selected time: " + _hourOfDayStr + ":"
					+ minuteStr);
			_hourSelected = hourOfDay;
			_minuteSelected = minute;
		}
	};

	private ArrayList<String> setActionList(String service) {
		ArrayList<String> actions = new ArrayList<String>();
		if (service.equals("LIGHTS") || service.equals("PLUG")) {
			actions.add("On");
			actions.add("Off");
		} else if (service.equals("TV") || service.equals("DVD")) {
			actions.add("Turn off");
			actions.add("Turn on");
			actions.add("Volume +");
			actions.add("Volume -");
			actions.add("Up");
			actions.add("Down");
			actions.add("Left");
			actions.add("Rigth");
			actions.add("Star");
			actions.add("One");
			actions.add("Two");
			actions.add("Three");
			actions.add("Four");
			actions.add("Five");
			actions.add("Six");
			actions.add("Seven");
			actions.add("Eight");
			actions.add("Nine");
			actions.add("Zero");
		} else if (service.equals("BLINDS")) {
			actions.add("Up");
			actions.add("Medium");
			actions.add("Down");
		} else if (service.equals("AIRCONDITIONING")) {
			actions.add("Up");
			actions.add("Down");
			actions.add("On");
			actions.add("Off");
		} else if (service.equals("DOOR")) {
			actions.add("Open");
			actions.add("Close");
		} else
			actions.add("No actions for this service");

		return actions;
	}

	// Background process
	private class sendEventConnection extends AsyncTask<String, String, String> {
		// ------------Variables-----------------------
		private ProgressDialog _pDialog;
		private String _message = "";
		private int _internalError = 0;

		// -----------------------------------



		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			_pDialog = new ProgressDialog(CreateNewEventActivity.this);
			// pDialog.setView(getLayoutInflater().inflate(R.layout.loading_icon_view,null));
			_pDialog.setMessage("Loading. Please wait...");
			_pDialog.setIndeterminate(false);
			_pDialog.setCancelable(false);
			_pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			try {
				// Query
				ArrayList<String> _parametros = new ArrayList<String>();
				JSONObject obj = new JSONObject(_file);

				_parametros.add("command");
				_parametros.add("createprogramaction");
				_parametros.add("username");
				_parametros.add(obj.getString("USERNAME"));
				_parametros.add("housename");
				_parametros.add(_selectedService.get(0));
				_parametros.add("roomname");
				_parametros.add(_selectedService.get(1).toUpperCase());
				_parametros.add("servicename");
				_parametros.add(_selectedService.get(2));
				_parametros.add("actionname");
				_parametros.add("SEND");
				_parametros.add("data");
				_parametros.add(_data);
				_parametros.add("start");
				_parametros
						.add(_dateSelected
								.getText()
								.toString()
								.subSequence(
										_dateSelected.getText().toString()
												.indexOf(':') + 1,
										_dateSelected.getText().toString()
												.length()).toString()
								+ _timeSelected
										.getText()
										.toString()
										.subSequence(
												_dateSelected.getText()
														.toString()
														.indexOf(':') + 1,
												_timeSelected.getText()
														.toString().length())
										.toString());
				_parametros.add("programname");
				_parametros.add(_servicename);

				JSONObject _data = Post.getServerData(_parametros, _ip);
				try {
					JSONObject _json_data = _data.getJSONObject("error");
					switch (_json_data.getInt("ERROR")) {
					case 0: {
						_message = _json_data.getString("ENGLISH");
						break;
					}
					default: {
						_internalError = _json_data.getInt("ERROR");
						_message = _json_data.getString("ENGLISH");
						break;
					}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// End call to PHP server
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog
			_pDialog.dismiss();
			if (_internalError != 0) {
				Toast.makeText(getBaseContext(), _message, Toast.LENGTH_SHORT)
						.show();
			} else {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateNewEventActivity.this);
				alertDialogBuilder.setTitle("You did it!");
				alertDialogBuilder.setMessage(
						"Needed re-login to see your changes")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										CreateNewEventActivity.this.finish();
									}
								});
				AlertDialog dialog = alertDialogBuilder.create();

				dialog.show();
			}

		}
	}

}
