package ehc.net;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CreateUserFragmentDialog extends DialogFragment{
	
    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static CreateUserFragmentDialog newInstance() {
    	CreateUserFragmentDialog f = new CreateUserFragmentDialog();
        return f;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_event_dialog, container, false);
        EditText et = (EditText) v.findViewById(R.id.event_name);
        
        // Watch for button clicks.
        Button button = (Button)v.findViewById(R.id.cancel_event);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, dialog is closed.
                getDialog().dismiss();
            }
        });

        return v;
    }


}
