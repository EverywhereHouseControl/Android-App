package environment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import ehc.net.R;
import framework.SimpleActivityTask;

public class RemoteController extends Activity 
{

	private String _currentRoom;
	private String _servicename;
	private Context _context;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_control_view);
		_context = this.getBaseContext();
		setListeners();
		_servicename=this.getIntent().getStringExtra("Service");
		_currentRoom = this.getIntent().getStringExtra("Room");
	}
	
	private void setListeners()
	{
		ImageView _button_one = (ImageView) findViewById(R.id.one_remote);
		_button_one.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR", "UNO");	
			}
		});
		
		ImageView _button_two = (ImageView) findViewById(R.id.two_remote);
		_button_two.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","DOS");				
			}
		});
		
		ImageView _button_three = (ImageView) findViewById(R.id.three_remote);
		_button_three.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","TRES");				
			}
		});
		
		ImageView _button_four = (ImageView) findViewById(R.id.four_remote);
		_button_four.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","CUATRO");				
			}
		});
		
		ImageView _button_five = (ImageView) findViewById(R.id.five_remote);
		_button_five.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","CINCO");				
			}
		});
		
		ImageView _button_six = (ImageView) findViewById(R.id.six_remote);
		_button_six.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","SEIS");				
			}
		});
		
		ImageView _button_seven = (ImageView) findViewById(R.id.seven_remote);
		_button_seven.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","SIETE");				
			}
		});
		
		ImageView _button_eight = (ImageView) findViewById(R.id.eight_remote);
		_button_eight.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","OCHO");				
			}
		});
		
		ImageView _button_nine = (ImageView) findViewById(R.id.nine_remote);
		_button_nine.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","NUEVE");				
			}
		});
		
		ImageView _button_zero = (ImageView) findViewById(R.id.zero_remote);
		_button_zero.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","CERO");				
			}
		});
		
		ImageView _button_fav = (ImageView) findViewById(R.id.star_remote);
		_button_fav.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","FAV");				
			}
		});
		
		ImageView _button_up = (ImageView) findViewById(R.id.up_arrow_remote);
		_button_up.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","UP");				
			}
		});
		
		ImageView _button_down = (ImageView) findViewById(R.id.down_arrow_remote);
		_button_down.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","DOWN");				
			}
		});
		
		ImageView _button_left = (ImageView) findViewById(R.id.left_arrow_remote);
		_button_left.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","LEFT");				
			}
		});
		
		ImageView _button_play = (ImageView) findViewById(R.id.play_remote);
		_button_play.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","PLAY");				
			}
		});
		
		ImageView _button_right = (ImageView) findViewById(R.id.right_arrow_remote);
		_button_right.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","RIGHT");				
			}
		});
				
		ImageView _button_mute = (ImageView) findViewById(R.id.mute_remote);
		_button_mute.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","MUTE");				
			}
		});
		
		ImageView _button_setup = (ImageView) findViewById(R.id.config_remote);
		_button_setup.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","SETUP");				
			}
		});
		
		ImageView _button_power = (ImageView) findViewById(R.id.on_off_remote);
		_button_power.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","POWER");				
			}
		});
		
	}
	
	protected void onStop()
    {
    	super.onStop();
    	onBackPressed();
    }
	
	
}