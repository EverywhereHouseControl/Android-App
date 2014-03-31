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
		ImageView button_one = (ImageView) findViewById(R.id.one_remote);
		button_one.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR", "UNO");	
			}
		});
		
		ImageView button_two = (ImageView) findViewById(R.id.two_remote);
		button_two.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","DOS");				
			}
		});
		
		ImageView button_three = (ImageView) findViewById(R.id.three_remote);
		button_three.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","TRES");				
			}
		});
		
		ImageView button_four = (ImageView) findViewById(R.id.four_remote);
		button_four.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","CUATRO");				
			}
		});
		
		ImageView button_five = (ImageView) findViewById(R.id.five_remote);
		button_five.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","CINCO");				
			}
		});
		
		ImageView button_six = (ImageView) findViewById(R.id.six_remote);
		button_six.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","SEIS");				
			}
		});
		
		ImageView button_seven = (ImageView) findViewById(R.id.seven_remote);
		button_seven.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","SIETE");				
			}
		});
		
		ImageView button_eight = (ImageView) findViewById(R.id.eight_remote);
		button_eight.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","OCHO");				
			}
		});
		
		ImageView button_nine = (ImageView) findViewById(R.id.nine_remote);
		button_nine.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","NUEVE");				
			}
		});
		
		ImageView button_zero = (ImageView) findViewById(R.id.zero_remote);
		button_zero.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","CERO");				
			}
		});
		
		ImageView button_fav = (ImageView) findViewById(R.id.star_remote);
		button_fav.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","FAV");				
			}
		});
		
		ImageView button_up = (ImageView) findViewById(R.id.up_arrow_remote);
		button_up.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","UP");				
			}
		});
		
		ImageView button_down = (ImageView) findViewById(R.id.down_arrow_remote);
		button_down.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","DOWN");				
			}
		});
		
		ImageView button_left = (ImageView) findViewById(R.id.left_arrow_remote);
		button_left.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","LEFT");				
			}
		});
		
		ImageView button_play = (ImageView) findViewById(R.id.play_remote);
		button_play.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","PLAY");				
			}
		});
		
		ImageView button_right = (ImageView) findViewById(R.id.right_arrow_remote);
		button_right.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","RIGHT");				
			}
		});
				
		ImageView button_mute = (ImageView) findViewById(R.id.mute_remote);
		button_mute.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","MUTE");				
			}
		});
		
		ImageView button_setup = (ImageView) findViewById(R.id.config_remote);
		button_setup.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","SETUP");				
			}
		});
		
		ImageView button_power = (ImageView) findViewById(R.id.on_off_remote);
		button_power.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{	
				new SimpleActivityTask(_context).sendAction(_currentRoom, _servicename, "ENVIAR","POWER");				
			}
		});
		
	}
}