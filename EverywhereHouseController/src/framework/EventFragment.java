package framework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ehc.net.Event;
import ehc.net.R;

public class EventFragment extends Fragment {

	public interface OnHeadlineSelectedListener {
		public Event getEventSelected();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.event_fragment, container, false);
	}
	
}