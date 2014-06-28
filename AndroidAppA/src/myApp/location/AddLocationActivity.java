package myApp.location;

import java.io.IOException;
import java.util.List;

import myApp.androidappa.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddLocationActivity extends Activity {
	GoogleMap googleMap;
	Location lastKnownLocation;
	private EditText address;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_locations);

		// Getting Google Play availability status
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			// Getting reference to the SupportMapFragment of activity_main.xml
			MapFragment fm = (MapFragment) getFragmentManager()
					.findFragmentById(R.id.map);

			// Getting GoogleMap object from the fragment
			googleMap = fm.getMap();

			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);

			// Getting LocationManager object from System Service
			// LOCATION_SERVICE
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting Current Location
			Location location = locationManager.getLastKnownLocation(provider);

			if (location != null) {
				// center the map on user
				centerMapOnMyLocation();
			}
		}
	}

	private void centerMapOnMyLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();

		Location location = locationManager
				.getLastKnownLocation(locationManager.getBestProvider(criteria,
						false));

		if (location != null) {
			googleMap
					.animateCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(location.getLatitude(), location
									.getLongitude()), 15));
		}
	}

	public void go(View v) {
		address = (EditText) findViewById(R.id.editText2);

		String check = address.getText().toString();
		if (!check.equals("")) {
			List<Address> myList;
			Geocoder gc = new Geocoder(getApplicationContext());
			try {
				myList = gc
						.getFromLocationName(address.getText().toString(), 1);
				if (myList.size() > 0) {
					Address a = myList.get(0);
					double lat = a.getLatitude();
					double lng = a.getLongitude();
					googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(lat, lng), 15));
				} else {
					Toast.makeText(this, "Sorry, couldn't find that location",
							Toast.LENGTH_LONG).show();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Sorry, couldn't find that location",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "You didn't enter a location!",
					Toast.LENGTH_SHORT).show();
		}
	}

}
