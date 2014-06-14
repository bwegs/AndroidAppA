/*
 * Author - Ben Wegher
 * Date   - 6/10/2014
 * Class  - AddNewTextAlert.java
 * Description - This activity is a form that lets the user create a specialized
 * 				 text alert. After being validated, the relevant alert data is returned to 
 * 				 MainActivity and inserted into the SQLite Database - alerts table. 
 */


package myApp.androidappa;

import myApp.database.DatabaseHandler;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class AddNewTextAlert extends Activity {
	EditText alertName;
	EditText phoneAdd;
	// location
	EditText message;
	RadioButton enterRadio;
	RadioButton exitRadio;
	private DatabaseHandler db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_text_alert);
		alertName = (EditText) findViewById(R.id.editText1);
		phoneAdd = (EditText) findViewById(R.id.editTextPhone);
		message = (EditText) findViewById(R.id.editText4);
		enterRadio = (RadioButton) findViewById(R.id.radio0);
		exitRadio = (RadioButton) findViewById(R.id.radio1);
		db = new DatabaseHandler(this);
	}

	// onClick() method
	public void addAlert(View V) {
		// get user data and convert to Strings
		String name = alertName.getText().toString();
		String phone = phoneAdd.getText().toString();
		String text = message.getText().toString();
		String when;
		if(exitRadio.isChecked())
			when = "EXIT";
		else
			when = "ENTER";

		// Validate input
		if (checkEmpty(name)) {  // Ensure name is NOT empty
			Toast.makeText(AddNewTextAlert.this, "Give your alert a name!",
					Toast.LENGTH_LONG).show();
			return;
		} else if (checkEmpty(phone)) { // Ensure phone # is NOT empty
			Toast.makeText(AddNewTextAlert.this,
					"You forgot to enter a phone number", Toast.LENGTH_LONG)
					.show();
			return;
		// Check length of phone number
		} else if (phone.length() != 7 && phone.length() != 10) { 
			Toast.makeText(AddNewTextAlert.this,
					"That phone number isn't valid", Toast.LENGTH_LONG).show();
			return;
		} else if (!onlyNumeric(phone)) { // Ensure phone # contains only numbers
			Toast.makeText(AddNewTextAlert.this,
					"Phone number must contain only numbers " + phone,
					Toast.LENGTH_LONG).show();
			return;
		} else if (checkEmpty(text)) { // Ensure message is NOT empty
			Toast.makeText(AddNewTextAlert.this,
					"You need to enter a message to send to " + phone,
					Toast.LENGTH_LONG).show();
			return;
		}
		
		// check for duplicate alert name
		if(db.alertExists(name)) {
			Toast.makeText(AddNewTextAlert.this,
					"An alert with that name already exists! Please enter a new name.",
					Toast.LENGTH_LONG).show();
			return;
		}
		
		// TODO -- CHECK IF LOCATION EXISTS IN LOCATION TABLE!
		// location must be created before it can be used

		Intent intentMessage = new Intent();

		// Debugging toast
		Toast.makeText(AddNewTextAlert.this,
				"Added new text alert: " + name + " " + phone + " " + text,
				Toast.LENGTH_LONG).show();

		// Return alert data to MainActivity
		intentMessage.putExtra("TITLE", name);
		intentMessage.putExtra("CONTACT", phone);
		intentMessage.putExtra("ICON", Constants.TEXT);
		intentMessage.putExtra("MESSAGE", text);
		intentMessage.putExtra("WHEN", when);
		intentMessage.putExtra("LOCATION", 3);


		setResult(RESULT_OK, intentMessage);

		finish();
	}

	// Checks if the given string is empty
	private boolean checkEmpty(String s) {
		if (s.matches(""))
			return true;
		return false;
	}

	// Checks if the given string consists solely of numbers
	private boolean onlyNumeric(String s) {
		try {
			Long.parseLong(s);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}
	
	// launches a contact picker to select phone # from contacts
		public void launchContactPicker(View view) {
			Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
					Contacts.CONTENT_URI);
			startActivityForResult(contactPickerIntent,
					Constants.CONTACT_PICKER_RESULT);
		}

		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);

			if (resultCode == RESULT_OK) {
				if (requestCode == Constants.CONTACT_PICKER_RESULT) {

					// TODO -- Need to handle multiple phone #s
					// i.e. let user pick between them
					Cursor cursor = null;
		            String phone = "";
		            try {
		            	
		                Uri result = data.getData();
		                Log.v(Constants.DEBUG_TAG, "Got a contact result: "
		                        + result.toString());

		                // get the contact id from the Uri
		                String id = result.getLastPathSegment();

		                // query for everything phone
		                cursor = getContentResolver().query(Phone.CONTENT_URI,
		                        null, Phone.CONTACT_ID + "=?", new String[] { id },
		                        null);

		                int phoneInd = cursor.getColumnIndex(Phone.DATA);

		                // let's just get the first text
		                if (cursor.moveToFirst()) {
		                    phone = cursor.getString(phoneInd);
		                    Log.v(Constants.DEBUG_TAG, "Got phone: " + phone);
		                } else {
		                	//Toast.makeText(AddNewTextAlert.this, "No phone found for contact.", Toast.LENGTH_LONG).show();
		                    Log.w(Constants.DEBUG_TAG, "No results");
		                }
		            } catch (Exception e) {
		                Log.e(Constants.DEBUG_TAG, "Failed to get phone data", e);
		            } finally {
		                if (cursor != null) {
		                    cursor.close();
		                }
		                EditText phoneEntry = (EditText) findViewById(R.id.editTextPhone);
		                phoneEntry.setText(phone);
		                if (phone.length() == 0) {
		                    Toast.makeText(this, "No phone number found for contact.",
		                            Toast.LENGTH_LONG).show();
		                }

		            }
					
				} else {
					// gracefully handle failure
					Log.d(Constants.DEBUG_TAG, "Warning: activity result not ok");
				}
			}

		}

}
