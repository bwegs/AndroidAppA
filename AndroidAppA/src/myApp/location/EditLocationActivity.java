package myApp.location;

import java.util.ArrayList;

import myApp.androidappa.Constants;
import myApp.androidappa.R;
import myApp.database.DatabaseHandler;
import myApp.geofence.GeofenceUtils;
import myApp.list.LocationListAdapter;
import myApp.list.LocationListItem;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class EditLocationActivity extends ListActivity {

	private ArrayList<LocationListItem> locationListItems;
	private LocationListAdapter mAdapter;
	private DatabaseHandler db;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_locations);

		db = new DatabaseHandler(this);

		// create an ArrayList of LocationListItems
		locationListItems = new ArrayList<LocationListItem>();

		// load saved locations from local storage into locationListItems
		locationListItems = db.getAllLocations();

		if(locationListItems.size() == 0) {
			Log.w("Warning", "locationListItems is empty");
		} else
			Log.w("Warning", "locationListItems is not empty");
		// set the list adapter
		mAdapter = new LocationListAdapter(this, locationListItems);
		setListAdapter(mAdapter);
		db.close();
		
	}
	
	// onClick() for Add New Location button
	public void addNewLocation(View v) {
		Intent intentAddNewLocation = new Intent(this, AddLocationActivity.class);
		startActivityForResult(intentAddNewLocation, Constants.LOCATION);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			if (requestCode == Constants.LOCATION) {
				String name = data.getStringExtra("NAME");
				String address = data.getStringExtra("ADDRESS");
				double longitude = data.getDoubleExtra("LONGITUDE", GeofenceUtils.INVALID_FLOAT_VALUE);
				double latitude = data.getDoubleExtra("LATITUDE", GeofenceUtils.INVALID_FLOAT_VALUE);
				float radius = data.getFloatExtra("RADIUS", GeofenceUtils.INVALID_FLOAT_VALUE);

				// needs a name, address and radius to preview
				LocationListItem addMe = new LocationListItem(name, latitude,
						longitude, radius);
				addMe.setAddress(address);

				db.addLocation(addMe);	 // add the location to our database
				mAdapter.add(addMe); // add the location to the list adapter
				mAdapter.notifyDataSetChanged(); // notify the list adapter
				Log.w("Warning", "EditLocation - onActivityResult ran");
			} //else if(requestCode == Constants.UPDATE) {
//				// restart activity so the updated alert appears
//				finish();
//				startActivity(getIntent());
//			}
			
		// result was not OK	
		} else {
			
			Log.w("Warning", "Warning: activity result not ok");
		}
		
	}

}
