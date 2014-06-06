package myApp.androidappa;


import java.util.ArrayList;

import myApp.List.AlertListAdapter;
import myApp.List.AlertListItem;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ListActivity {

	private ArrayList<AlertListItem> alertListItems;
    private AlertListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// need to load saved alerts from local storage into alertListItems
		alertListItems = new ArrayList<AlertListItem>();
		// hard coded for testing purposes
		alertListItems.add(new AlertListItem("Finish Lab 4", R.drawable.ic_action_email, "mom@gmail.com",
				"hey mom, just wanted to let you know that I finished lab 4!"));
		alertListItems.add(new AlertListItem());
		alertListItems.add(new AlertListItem("Test long title and message", R.drawable.ic_action_chat, "9706914011",
				"hey mom, it's me, Bob, just wanted to text you a really long message" +
				"because I love you and I want you to know that I appreciate everything" +
				"that you've ever done for me, which is a lot, so thank you.. also this is an" +
				"awesome run on sentence."));
		
		mAdapter = new AlertListAdapter(this, alertListItems);
		setListAdapter(mAdapter);
		
		android.app.ActionBar action = getActionBar();
		action.show();
		
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			launchSettings();
			return true;
		} else if (id == R.id.action_locations) {
			launchLocations();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	// triggered as onClick() event of 'Add New Alert' button
	public void addNewAlert(View v) {
		RadioButton rb;
		rb = (RadioButton) findViewById(R.id.radio0);
		
		// if radio0 is filled then launch new text alert activity
		if(rb.isChecked()) {
			Intent intentAddNewAlert = new Intent(this,AddNewTextAlert.class);
	 	    startActivityForResult(intentAddNewAlert, Constants.TEXT);
		} 
		// else radio1 is filled then launch new email alert activity
		else {
			Intent intentAddNewAlert = new Intent(this,AddNewEmailAlert.class);
	 	    startActivityForResult(intentAddNewAlert, Constants.EMAIL);
		}
		
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
 		super.onActivityResult(requestCode, resultCode, data);
 		
        if(data != null) {	 	
        	String title = data.getStringExtra("TITLE");
        	String contact = data.getStringExtra("CONTACT");
        	int icon = data.getIntExtra("ICON", 0);
        	String message = data.getStringExtra("MESSAGE");
        	
//        	if(requestCode == Constants.EMAIL)
//        		icon = R.drawable.ic_action_email;
//        	else
//        		icon = R.drawable.ic_action_chat;
        	
        	// needs a title, icon and contact
        	mAdapter.add(new AlertListItem(title, icon, contact, message));
        	mAdapter.notifyDataSetChanged();
        }
 	}
	
	public void launchSettings() {
		Toast.makeText(getApplicationContext(), "launched settings page", 
				Toast.LENGTH_SHORT).show();
	}
	
	public void launchLocations() {
		Toast.makeText(getApplicationContext(), "launched locations page", 
				Toast.LENGTH_SHORT).show();
	}
	

}
