package myApp.androidappa;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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
	 	    startActivityForResult(intentAddNewAlert, 2);
		} 
		// else radio1 is filled then launch new email alert activity
		else {
			Intent intentAddNewAlert = new Intent(this,AddNewEmailAlert.class);
	 	    startActivityForResult(intentAddNewAlert, 2);
		}
		
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
 		super.onActivityResult(requestCode, resultCode, data);
 		
        if(null != data) {	 	
        	String item = data.getStringExtra("ITEM");
        	String priority = data.getStringExtra("PRIORITY");
        	String finished = data.getStringExtra("FINISHED");
        	if(finished.compareTo("TRUE") == 0)
        		item = "FINISHED: " + item;
        	else
        		item = priority + ": " + item;
        	//mAdapter.add(item);
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
