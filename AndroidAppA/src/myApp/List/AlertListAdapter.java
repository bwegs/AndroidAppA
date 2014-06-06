package myApp.List;

import java.util.ArrayList;

import myApp.androidappa.R;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<AlertListItem> alertItems;
	
	public AlertListAdapter(Context context, ArrayList<AlertListItem> alertItems){
		this.context = context;
		this.alertItems = alertItems;
	}
	
	public void add(AlertListItem item) {
		alertItems.add(item);
	}

	@Override
	public int getCount() {
		return alertItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return alertItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.alert_list_item, null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtContact = (TextView) convertView.findViewById(R.id.contact);
         
        imgIcon.setImageResource(alertItems.get(position).getIcon());        
        txtTitle.setText(alertItems.get(position).getTitle());
        txtContact.setText(alertItems.get(position).getContact());
        
        // displaying count
        // check whether it set visible or not
//        if(alertItems.get(position).getCounterVisibility()){
//        	txtCount.setText(navDrawerItems.get(position).getCount());
//        }else{
//        	// hide the counter view
//        	txtCount.setVisibility(View.GONE);
//        }
        
        return convertView;
	}

}
