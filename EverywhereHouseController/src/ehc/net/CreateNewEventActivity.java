package ehc.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import parserJSON.JSON;
import serverConnection.Post;
import serverConnection.SimpleActivityTask;

import ehc.net.R;
import framework.SpinnerEventContainer;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.opengl.Visibility;
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

	private String servicename;
	private TextView dateSelected;
	private TextView timeSelected;
	private EditText et;

	private Spinner itemList;
	
	private Post _post;
	private String _house;
	private String _file;
	private ArrayList<String> selectedService = new ArrayList<String>();
	private String _data;

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
		itemList = (Spinner) findViewById(R.id.sp_item);
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
					// ArrayList<String> actions = JSON.getInstance(
					// getApplicationContext()).getServices(
					// servicesList.getHouse(position - 1),
					// servicesList.getRoom(position - 1),
					// servicesList.getService(position - 1));
					int service2 = parent.getItemAtPosition(position)
							.toString().indexOf("-");
					int service1 = parent.getItemAtPosition(position)
							.toString().length();
					String service = parent.getItemAtPosition(position)
							.toString().substring(service2 + 2, service1);
					final ArrayList<String> actions = setActionList(service);

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
										int position, long id) 
								{
									if ((position != 0) && (serviceSelected))
									{
										actionSelected = true;
										_data = actions.get(position).toUpperCase();
									}
									else
									{
										_data = actions.get(position).toUpperCase();
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

		et = (EditText) findViewById(R.id.event_name);
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
				if (s.toString().length() > 0){
					textChanged = true;
					servicename = s.toString();
				}				
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
				else {
					selectedService = parseService(itemList
							.getSelectedItem().toString());
//					new SimpleActivityTask(getApplicationContext()).sendEvent(
//							selectedService[0],
//							selectedService[1],
//							selectedService[2],
//							"SEND",
//							"a",
//							dateSelected.getText() + " "
//									+ timeSelected.getText(), et.getText()
//									.toString());
					Log.d("NEW EVENT",selectedService.toString());
					Log.d("SEND EVENT","CRASH");
					parser();
					_post = new Post();
					sendEventConnection _connection = new sendEventConnection();
			    	_connection.execute();	
				}

			}
		});

	}

	private ArrayList<String> parseService(String serv) {
		ArrayList<String> str = new ArrayList<String>();
		Log.d("SERVICE",serv);
		int service0 = serv.indexOf(' ');
		int service1 = serv.indexOf(':');
		Log.d("SERVICE",Integer.toString(service1));
		int service2 = serv.indexOf('-');
		Log.d("SERVICE",Integer.toString(service2));
		int service3 = serv.length();
		Log.d("SERVICE",Integer.toString(service3));
		str.add(0, serv.substring(0,service1-1));
		Log.d("SERVICE",str.get(0)+"_");
		str.add(1, serv.substring(service1+2,service2-1));
		Log.d("SERVICE",str.get(1)+"_");
		str.add(2, serv.substring(service2+2,service3));
		Log.d("SERVICE",str.get(2)+"_");
		return str;
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
			monthOfYear++;
			dateSelected.setText("Selected date: " + dayOfMonth + "-"
					+ monthOfYear + "-" + year);
			yearSelected = year;
			monthSelected = monthOfYear+1;
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
	

	private ArrayList<String> setActionList(String service) {
		ArrayList<String> actions = new ArrayList<String>();
		if (service.equals("LIGHTS")) {
			actions.add("On");
			actions.add("Off");
		} else if (service.equals("TV")) {
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
		}

		return actions;
	}
	
	
	private void parser() {
		String _file2;
		try {
			InputStream _is = openFileInput("profileInformation.json");
			int _size = _is.available();
			byte[] buffer = new byte[_size];
			_is.read(buffer);
			_is.close();
			this._file = new String(buffer, "UTF-8");

			_is = openFileInput("configuration.json");
			_size = _is.available();
			buffer = new byte[_size];
			_is.read(buffer);
			_is.close();
			_file2 = new String(buffer, "UTF-8");
			JSONObject _obj = new JSONObject(_file2);
			JSONObject infoCasa = _obj.getJSONArray("houses").getJSONObject(0);
			_house = infoCasa.getString("name");
		} catch (IOException _ex) {
			_ex.printStackTrace();
		} catch (Exception _ex) {
			_ex.printStackTrace();
		}
	}
	
	// Background process
    private class sendEventConnection extends AsyncTask<String, String, String>
    {    	
    	private ProgressDialog _pDialog;
    	private String _message = "";
    	private int _internalError = 0;
    	/**
    	 * Message "Loading"
    	 */
    	protected void onPreExecute() 
    	{  
    		super.onPreExecute();
            _pDialog = new ProgressDialog(CreateNewEventActivity.this);
            //pDialog.setView(getLayoutInflater().inflate(R.layout.loading_icon_view,null));
            _pDialog.setMessage("Loading. Please wait...");
            _pDialog.setIndeterminate(false);
            _pDialog.setCancelable(false);
            _pDialog.show();          
        }
    	
    	@Override
		protected String doInBackground(String... arg0) 
		{
			try 
			{		             
				//Query
				ArrayList<String> _parametros = new ArrayList<String>();
				JSONObject obj = new JSONObject(_file);
				
				_parametros.add("command");
				_parametros.add("createprogramaction");
				_parametros.add("username");
				_parametros.add(obj.getString("USERNAME"));
				_parametros.add("housename");
				_parametros.add(selectedService.get(0));
				_parametros.add("roomname");
				_parametros.add(selectedService.get(1).toUpperCase());
				_parametros.add("servicename");
				_parametros.add(selectedService.get(2));
				_parametros.add("actionname");
				_parametros.add("SEND");
				_parametros.add("data");
				_parametros.add(_data);
				_parametros.add("start");
				_parametros.add(dateSelected.getText().toString().subSequence(dateSelected.getText().toString().indexOf(':')+1, dateSelected.getText().toString().length()).toString() 
								+
								timeSelected.getText().toString().subSequence(dateSelected.getText().toString().indexOf(':')+1, timeSelected.getText().toString().length()).toString());
				_parametros.add("programname");
				_parametros.add(servicename);
				Log.d("PARAMETROS",_parametros.toString());
			String prrr = _parametros.toString();
				JSONObject _data = _post.getServerData(_parametros,"http://5.231.69.226/EHControlConnect/index.php");//"http://192.168.2.147/EHControlConnect/index.php");//"");
				Log.d("DATA",_data.toString());
				
				try 
				{
					JSONObject _json_data = _data.getJSONObject("error");
					switch(_json_data.getInt("ERROR"))
					{
						case 0:
						{
							_message = _json_data.getString("ENGLISH");					
							break;
						}
						default:
						{
							_internalError = _json_data.getInt("ERROR");
							_message = _json_data.getString("ENGLISH");
							break;
						}
					}
				
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				
//				if (_data != null && _data.length() > 0) 
//				{				
//					JSONObject _json_data = _data.getJSONObject("result");
//					//log(json_data.toString());
//					
//					if (_json_data.getInt("IDUSER")==0) 
//					{ 
//						log("Incorrect user. ");
//					}
//					else
//					{ 	
//						//Save the profile's information.
//						saveProfileInfo(_json_data);
//						//Save the house's configuration
//						saveConfig(_json_data.get("JSON"));
//						//Activate the next Activity("MainMenu")
//						createdIntent();
//					}				
//				}
//				else 
//				{
//					log("JSON, ERROR ");
//					log(_data.toString());
//				}			 
			 }
			catch (Exception _e) 
			 {
			 	_e.printStackTrace();
			 }
			 // End call to PHP server
			return null;
		}
    	
		protected void onPostExecute(String file_url) 
		{
            // dismiss the dialog
            _pDialog.dismiss();
            if(_internalError!=0)Toast.makeText(getBaseContext(), _message, Toast.LENGTH_SHORT).show();
		}
    }
    
}
