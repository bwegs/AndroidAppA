package myApp.location;

import myApp.androidappa.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EditLocationActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_locations);

	}
	
	public void addNewLocation(View v) {
		Intent intentAddNewLocation = new Intent(this, AddLocationActivity.class);
		startActivity(intentAddNewLocation);
	}

}
