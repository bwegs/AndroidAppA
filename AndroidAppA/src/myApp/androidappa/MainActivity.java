package myApp.androidappa;

import java.util.ArrayList;

import myApp.database.DatabaseHandler;
import myApp.list.AlertListAdapter;
import myApp.list.AlertListItem;

import android.support.v4.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import com.bugsense.trace.BugSenseHandler;

public class MainActivity extends ListActivity {

	private ArrayList<AlertListItem> alertListItems;
	private AlertListAdapter mAdapter;
	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(MainActivity.this, "b52296ad");
		setContentView(R.layout.activity_main);
		Log.d("Check: ", "Attempting to insert...");
		db = new DatabaseHandler(this);

		// create an ArrayList of AlertListItems
		alertListItems = new ArrayList<AlertListItem>();

		// load saved alerts from local storage into alertListItems
		alertListItems = db.getAllAlerts();

		// set the list adapter
		mAdapter = new AlertListAdapter(this, alertListItems);
		setListAdapter(mAdapter);
		db.close();

		android.app.ActionBar action = getActionBar();
		action.show();

		registerForContextMenu(getListView());
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.delete:
			AlertListItem deleteMe = (AlertListItem) mAdapter
					.getItem((int) info.id);

			// ********** TODO Confirmation or Undo feature
			db.deleteAlert(deleteMe); // remove alert from database (don't have
										// to do this)
			mAdapter.delete(deleteMe); // remove from adapter

			mAdapter.notifyDataSetChanged();

			Toast.makeText(MainActivity.this,
					deleteMe.getTitle() + " was deleted.", Toast.LENGTH_LONG)
					.show();
			return true;
		case R.id.edit:
			// Intent emailIntent = new
			// Intent(android.content.Intent.ACTION_SEND);

			// startActivity(emailIntent);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
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



	// triggered as onClick() event of 'Add New Alert' button
	public void addNewAlert(View v) {
		RadioButton rb;
		rb = (RadioButton) findViewById(R.id.radio0);

		// if radio0 is filled then launch new text alert activity
		if (rb.isChecked()) {
			Intent intentAddNewAlert = new Intent(this, AddNewTextAlert.class);
			startActivityForResult(intentAddNewAlert, Constants.TEXT);
		}
		// else radio1 is filled then launch new email alert activity
		else {
			Intent intentAddNewAlert = new Intent(this, AddNewEmailAlert.class);
			startActivityForResult(intentAddNewAlert, Constants.EMAIL);
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			// 
			if (requestCode == Constants.EMAIL || requestCode == Constants.TEXT) {
				String title = data.getStringExtra("TITLE");
				String contact = data.getStringExtra("CONTACT");
				int icon = data.getIntExtra("ICON", 0);
				int location = data.getIntExtra("LOCATION", -1);
				String message = data.getStringExtra("MESSAGE");
				String when = data.getStringExtra("WHEN");

				// needs a title, icon, contact, message to preview
				AlertListItem addMe = new AlertListItem(title, contact,
						location, message, when, icon);

				db.addAlert(addMe);	 // add the alert to our database
				mAdapter.add(addMe); // add the alert to the list adapter
				mAdapter.notifyDataSetChanged(); // notify the list adapter
			} 
			
		// result was not OK	
		} else {
			
			Log.w("Warning", "Warning: activity result not ok");
		}
	}

	public void launchSettings() {
		Intent intentLaunchSettings = new Intent(this, SettingsActivity.class);
		
		startActivity(intentLaunchSettings);
	}

	public void launchLocations() {
		Intent intentEditLocations = new Intent(this,
				EditLocationsActivity.class);
		startActivity(intentEditLocations);
	}
	

    

}
