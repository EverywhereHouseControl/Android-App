package framework;

import java.util.ArrayList;

import ehc.net.R;
import ehc.net.R.id;
import ehc.net.R.layout;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Event> event;

	public EventAdapter(Context c, ArrayList<Event> e) {
		event = e;
		context = c;
	}

	@Override
	public int getCount() {
		return event.size();
	}

	@Override
	public Object getItem(int position) {
		return event.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater _infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = _infalInflater.inflate(R.layout.event_fragment, null);
		}

		TextView nameText = (TextView) convertView.findViewById(R.id.textView2);
		nameText.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		TextView itemText = (TextView) convertView.findViewById(R.id.forgotPassword);
		itemText.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		TextView createText = (TextView) convertView.findViewById(R.id.textView3);
		createText.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		TextView hourText = (TextView) convertView.findViewById(R.id.textView4);
		hourText.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		
		
		
		TextView name = (TextView) convertView.findViewById(R.id.fragment_name);
		name.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		TextView item = (TextView) convertView.findViewById(R.id.fragment_item);
		item.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		TextView creator = (TextView) convertView
				.findViewById(R.id.fragment_creator);
		creator.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));
		TextView hour = (TextView) convertView.findViewById(R.id.fragment_hour);
		hour.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"imagine_earth.ttf"));

		name.setText(event.get(position).getName());
		item.setText(Integer.toString(event.get(position).getItem()));
		creator.setText(event.get(position).getCreator());
		hour.setText(event.get(position).getHour() + ":"
				+ event.get(position).getMinute());

		return convertView;
	}

	public void clear() {
		event.clear();
	}

}