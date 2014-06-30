package myApp.location;

import java.io.IOException;
import java.util.List;

import myApp.androidappa.R;
import myApp.database.DatabaseHandler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddLocationActivity extends Activity {
	private GoogleMap googleMap;
	private EditText address;
	private Circle circle;
	private Marker marker;
	private double latitude;
	private double longitude;
	private float radius = 400;

	private DatabaseHandler db;
	private EditText locationName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_locations);

		// get db handler
		db = new DatabaseHandler(this);
		
		locationName = (EditText) findViewById(R.id.editText1);

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
			// googleMap.setMyLocationEnabled(true);

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

	// TODO -- allow user to change circle radius -- between 200 & ???m

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
									.getLongitude()), 14));

			latitude = location.getLatitude();
			longitude = location.getLongitude();
			// add a circle around current location
			circle = googleMap.addCircle(new CircleOptions()
					.center(new LatLng(latitude, longitude)).radius(radius)
					.strokeColor(Color.RED).fillColor(Color.TRANSPARENT));

			// add a marker at current / last known loc
			marker = googleMap.addMarker(new MarkerOptions().position(
					new LatLng(latitude, longitude)).title(
					"Last Known Location"));
			marker.showInfoWindow();
		}
	}

	// onClick() method of 'find' button - searches GoogleMap fragment
	public void go(View v) {
		address = (EditText) findViewById(R.id.editText2);

		String check = address.getText().toString();
		if (!check.equals("")) {
			List<Address> myList;
			Geocoder gc = new Geocoder(getBaseContext());
			try {
				myList = gc
						.getFromLocationName(address.getText().toString(), 1);
				if (myList.size() > 0) {
					Address a = myList.get(0);
					latitude = a.getLatitude();
					longitude = a.getLongitude();
					googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(latitude, longitude), 14));
					// if a circle exists remove it and add a new one
					if (circle != null && circle.isVisible()) {
						circle.remove();
						circle = googleMap.addCircle(new CircleOptions()
								.center(new LatLng(latitude, longitude))
								.radius(radius).strokeColor(Color.RED)
								.fillColor(Color.TRANSPARENT));
					}
					// if a marker exists remove it and add a new one
					if (marker != null && marker.isVisible()) {
						marker.remove();
						marker = googleMap.addMarker(new MarkerOptions()
								.position(new LatLng(latitude, longitude)));
					}
				} else {
					Toast.makeText(this, "Sorry, couldn't find that location",
							Toast.LENGTH_LONG).show();
				}
			} catch (IOException e) {
				Toast.makeText(this, "Sorry, couldn't find that location",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "You didn't enter a location!",
					Toast.LENGTH_SHORT).show();
		}
	}

	// decrease radius of circle
	public void shrink(View v) {
		if (radius > 100)
			radius -= 50;

		// redraw circle
		// if a circle exists remove it and add a new one
		if (circle != null && circle.isVisible()) {
			circle.remove();
			circle = googleMap.addCircle(new CircleOptions()
					.center(new LatLng(latitude, longitude)).radius(radius)
					.strokeColor(Color.RED).fillColor(Color.TRANSPARENT));
		}
	}

	// increase radius of circle
	public void grow(View v) {
		if (radius < 1500)
			radius += 50;

		// redraw circle
		// if a circle exists remove it and add a new one
		if (circle != null && circle.isVisible()) {
			circle.remove();
			circle = googleMap.addCircle(new CircleOptions()
					.center(new LatLng(latitude, longitude)).radius(radius)
					.strokeColor(Color.RED).fillColor(Color.TRANSPARENT));
		}
	}

	public void addLocation(View v) {
		// if user input is valid then we can add the alert
		if (isInputValid()) {
			// get location name
			String name = locationName.getText().toString();

			String address = "No address found.";
			
			List<Address> myList;
			Geocoder gc = new Geocoder(getBaseContext());
			try {
				myList = gc.getFromLocation(latitude, longitude, 1);
				if(myList.size() > 0) {
					Address temp = myList.get(0);
					StringBuilder s = new StringBuilder();
					int i = 0;
					while(temp.getAddressLine(i) != null) {
						s.append(temp.getAddressLine(i));
						s.append("\n");
						i++;
					}
					if(s.length() > 0)
						address = s.toString();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// check for duplicate location name
			if (db.locationExists(name)) {
				Toast.makeText(
						this,
						"A location with that name already exists! Please enter a new name.",
						Toast.LENGTH_LONG).show();
				return;
			}

			// Debugging toast
			Toast.makeText(
					this,
					"Added new location: " + name + " latitude = " + latitude
							+ " longitude = " + longitude + " radius = "
							+ radius + " Address: " + address, Toast.LENGTH_LONG).show();

			Intent intentMessage = new Intent();

			intentMessage.putExtra("NAME", name);
			intentMessage.putExtra("ADDRESS", address);
			intentMessage.putExtra("LONGITUDE", longitude);
			intentMessage.putExtra("LATITUDE", latitude);
			intentMessage.putExtra("RADIUS", radius);
			
			setResult(RESULT_OK, intentMessage);
			Log.w("Warning", "AddLocationActivity -- after setResult");
			finish();
		}
	}

	public boolean isInputValid() {
		String name = locationName.getText().toString();

		if (name.equals("")) {
			Toast.makeText(this,
					"Please enter a name for this location.",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (radius < 100 || radius > 1500) {
			Toast.makeText(this, "Invalid radius = " + radius,
					Toast.LENGTH_LONG).show();
			return false;
		} else if (longitude < -180 || longitude > 180 || latitude < -90  || latitude > 90) {
			Toast.makeText(this,
					"Invalid long " + longitude + " or lat " + latitude,
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

}
