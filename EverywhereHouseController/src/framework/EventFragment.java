package framework;

import ehc.net.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.event_fragment, container, false);
    }
    
    public void setText(String text){
        TextView textView = (TextView) getView().findViewById(R.id.eventtext);
        textView.setText(text);
    }
}