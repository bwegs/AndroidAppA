package myApp.androidappa;

import myApp.database.DatabaseHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddNewEmailAlert extends Activity {
	private DatabaseHandler db;
	EditText alertName;
	EditText emailAdd;
	// location
	EditText message;
	RadioButton enterRadio;
	RadioButton exitRadio;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_email_alert);
		alertName = (EditText) findViewById(R.id.editText1);
		emailAdd = (EditText) findViewById(R.id.editText2);
		message = (EditText) findViewById(R.id.editText4);
		enterRadio = (RadioButton) findViewById(R.id.radio0);
		exitRadio = (RadioButton) findViewById(R.id.radio1);
		db = new DatabaseHandler(this);
	}

	public void addAlert(View V) {

		// get user data and convert to Strings
		String name = alertName.getText().toString();
		String email = emailAdd.getText().toString();
		String text = message.getText().toString();
		String when;
		if(exitRadio.isChecked())
			when = "EXIT";
		else
			when = "ENTER";

		// Validate user input!
		if (checkEmpty(name)) {
			Toast.makeText(AddNewEmailAlert.this, "Give your alert a name!",
					Toast.LENGTH_LONG).show();
			return;
		} else if (checkEmpty(email)) {
			Toast.makeText(AddNewEmailAlert.this,
					"You forgot to enter an email address", Toast.LENGTH_LONG)
					.show();
			return;
		} else if (!email.contains("@") || !email.contains(".")) {
			Toast.makeText(AddNewEmailAlert.this,
					"That email address isn't valid", Toast.LENGTH_LONG).show();
			return;
		} else if (checkEmpty(text)) {
			Toast.makeText(AddNewEmailAlert.this,
					"You need to enter a message to send to " + email,
					Toast.LENGTH_LONG).show();
			return;
		}

		// check for duplicate alert name
		if (db.alertExists(name)) {
			Toast.makeText(AddNewEmailAlert.this,
					"An alert with that name already exists! Please enter a new name.",
					Toast.LENGTH_LONG).show();
			return;
		}

		Intent intentMessage = new Intent();

		// Debugging toast
		Toast.makeText(AddNewEmailAlert.this,
				"Added new email alert: " + name + " " + email + " " + text,
				Toast.LENGTH_LONG).show();

		intentMessage.putExtra("TITLE", name);
		intentMessage.putExtra("CONTACT", email);
		intentMessage.putExtra("ICON", R.drawable.ic_action_email);
		intentMessage.putExtra("MESSAGE", text);
		intentMessage.putExtra("WHEN", when);
		intentMessage.putExtra("LOCATION", 4);

		// if(arriveRadio.isChecked()) {
		// intentMessage.putExtra("WHEN", "ENTER");
		// } else {
		// intentMessage.putExtra("WHEN", "LEAVE");
		// }

		// put the message in Intent
		// intentMessage.putExtra("ITEM", item);

		setResult(Constants.EMAIL, intentMessage);

		finish();
	}

	private boolean checkEmpty(String s) {
		if (s.matches(""))
			return true;
		return false;
	}
}
