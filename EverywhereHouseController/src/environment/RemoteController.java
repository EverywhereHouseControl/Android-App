package environment;

import serverConnection.SimpleActivityTask;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import ehc.net.R;

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
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND", "ONE");	
			}
		});
		
		ImageView _button_two = (ImageView) findViewById(R.id.two_remote);
		_button_two.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","TWO");				
			}
		});
		
		ImageView _button_three = (ImageView) findViewById(R.id.three_remote);
		_button_three.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","THREE");				
			}
		});
		
		ImageView _button_four = (ImageView) findViewById(R.id.four_remote);
		_button_four.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","FOUR");				
			}
		});
		
		ImageView _button_five = (ImageView) findViewById(R.id.five_remote);
		_button_five.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","FIVE");				
			}
		});
		
		ImageView _button_six = (ImageView) findViewById(R.id.six_remote);
		_button_six.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","SIX");				
			}
		});
		
		ImageView _button_seven = (ImageView) findViewById(R.id.seven_remote);
		_button_seven.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","SEVEN");				
			}
		});
		
		ImageView _button_eight = (ImageView) findViewById(R.id.eight_remote);
		_button_eight.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","EIGHT");				
			}
		});
		
		ImageView _button_nine = (ImageView) findViewById(R.id.nine_remote);
		_button_nine.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","NINE");				
			}
		});
		
		ImageView _button_zero = (ImageView) findViewById(R.id.zero_remote);
		_button_zero.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","ZERO");				
			}
		});
		
		ImageView _button_fav = (ImageView) findViewById(R.id.star_remote);
		_button_fav.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","FAV");				
			}
		});
		
		ImageView _button_up = (ImageView) findViewById(R.id.up_arrow_remote);
		_button_up.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","UP");				
			}
		});
		
		ImageView _button_down = (ImageView) findViewById(R.id.down_arrow_remote);
		_button_down.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","DOWN");				
			}
		});
		
		ImageView _button_left = (ImageView) findViewById(R.id.left_arrow_remote);
		_button_left.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","LEFT");				
			}
		});
		
		ImageView _button_play = (ImageView) findViewById(R.id.play_remote);
		_button_play.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","PLAY");				
			}
		});
		
		ImageView _button_right = (ImageView) findViewById(R.id.right_arrow_remote);
		_button_right.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","RIGHT");				
			}
		});
				
		ImageView _button_mute = (ImageView) findViewById(R.id.mute_remote);
		_button_mute.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","MUTE");				
			}
		});
		
		ImageView _button_setup = (ImageView) findViewById(R.id.config_remote);
		_button_setup.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","SETUP");				
			}
		});
		
		ImageView _button_power = (ImageView) findViewById(R.id.UserImage);
		_button_power.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","POWER");				
			}
		});
		
		ImageView _button_up_volumen = (ImageView) findViewById(R.id.up_volume_remote);
		_button_up_volumen.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","VOLUMEUP");				
			}
		});
		
		ImageView _button_down_volume = (ImageView) findViewById(R.id.down_volume_remote);
		_button_down_volume.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "SEND","VOLUMEDOWN");				
			}
		});
		
	}
	
	protected void onStop()
    {
    	super.onStop();
    }
	
	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	
}