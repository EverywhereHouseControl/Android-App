package framework;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import ehc.net.R;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context context;
    private Map<String, List<String>> laptopCollections;
    private List<String> laptops;
 
    public ExpandableListAdapter(Context context, List<String> laptops,
    		Map<String, List<String>> laptopCollections) {
        this.context = context;
        this.laptops = laptops;
        this.laptopCollections = laptopCollections;
    }
 
    public Object getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
    }
 
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {

 
        if (convertView == null) {
            LayoutInflater inflater =  (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_item, null);
        }
 
        TextView item = (TextView) convertView.findViewById(R.id.childname);
        
        String itemName = laptops.get(groupPosition);
        item.setText(itemName);
        
        if (itemName.equals("float")){
        	SeekBar sb = (SeekBar) convertView.findViewById(R.id.float_value);
        	sb.setVisibility(convertView.VISIBLE);
        } else {
        	SeekBar sb = (SeekBar) convertView.findViewById(R.id.float_value);
        	sb.setVisibility(convertView.VISIBLE);
        	ToggleButton tg = (ToggleButton) convertView.findViewById(R.id.boolean_value);
        	tg.setVisibility(convertView.VISIBLE); 
        }        
        return convertView;
    }
 
    public int getChildrenCount(int groupPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).size();
    }
 
    public Object getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }
 
    public int getGroupCount() {
        return laptops.size();
    }
 
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.groupname);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }
 
    public boolean hasStableIds() {
        return true;
    }
 
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}