package framework;

import org.json.JSONException;

import parserJSON.JSON;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import ehc.net.R;

public class CreateUserFragmentDialog extends DialogFragment 
{
	//------------Variables-----------------------
	private SpinnerEventContainer servicesList;
	private boolean serviceSelected = false;
	private boolean textChanged = false;
	//-----------------------------------

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
	 */
	static CreateUserFragmentDialog newInstance() 
	{
		CreateUserFragmentDialog _f = new CreateUserFragmentDialog();
		return _f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
	{
        View _v = inflater.inflate( R.layout.create_event_dialog, container, false );
     

        /**
         * Spinner info
         */
        final Spinner _itemList = ( Spinner ) _v.findViewById( R.id.sp_item );
        try 
        {
        	servicesList = JSON.getInstance( getActivity().getApplicationContext() ).getItemsWithLocation();
	        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>( this.getActivity(), R.layout.spinner_simple_data, servicesList.getFullServiceInformation() );
	        _itemList.setAdapter( spinnerAdapter );
	        _itemList.setSelection( 0 );
		} 
        catch ( JSONException e ) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        _itemList.setOnItemSelectedListener( new OnItemSelectedListener() 
        {

			@Override
			public void onItemSelected( AdapterView<?> parent, View view,
					int position, long id ) 
			{
				if ( position != 0 )
					serviceSelected = true;
				
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent ) 
			{
				// TODO Auto-generated method stub
				
			}
		});
        
        /**
         * Cancel and accept buttons
         */        
        Button cancelButton = ( Button )_v.findViewById( R.id.cancel_event );
        cancelButton.setOnClickListener( new OnClickListener() 
        {
            public void onClick( View v ) 
            {
                // When button is clicked, dialog is closed.
                getDialog().dismiss();
            }
        });
        EditText _et = ( EditText ) _v.findViewById( R.id.event_name ); 
        _et.addTextChangedListener( new TextWatcher() 
        {
			
			@Override
			public void onTextChanged( CharSequence s, int start, int before, int count ) 
			{

			}

			@Override
			public void beforeTextChanged( CharSequence s, int start, int count, int after ) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged( Editable s ) 
			{
				// TODO Auto-generated method stub
				if ( s.toString().length()>0 ) 					
					textChanged = true;				
				else 
					textChanged = false;				
				
			}

		});
        
        Button _acceptButton = ( Button )_v.findViewById( R.id.create_event );
        _acceptButton.setOnClickListener( new OnClickListener() 
        {
            public void onClick( View v ) 
            {
            	if ( serviceSelected == false )
            		Toast.makeText( getActivity().getApplicationContext(), "Select a service", Toast.LENGTH_LONG ).show();    
            	if ( textChanged == false )
            		Toast.makeText( getActivity().getApplicationContext(), "Insert a name", Toast.LENGTH_LONG ).show();    
            }
        });

        return _v;
    }
}
