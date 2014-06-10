package myApp.androidappa;

import myApp.database.DatabaseHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddNewTextAlert extends Activity {
	EditText alertName;
	EditText phoneAdd;
	// location
	EditText message;
	RadioButton arriveRadio;
	RadioButton leaveRadio;
	private DatabaseHandler db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_text_alert);
		alertName = (EditText) findViewById(R.id.editText1);
		phoneAdd = (EditText) findViewById(R.id.editText2);
		message = (EditText) findViewById(R.id.editText4);
		arriveRadio = (RadioButton) findViewById(R.id.radio0);
		leaveRadio = (RadioButton) findViewById(R.id.radio1);
		db = new DatabaseHandler(this);
	}

	public void addAlert(View V) {
		// get user data and convert to Strings
		String name = alertName.getText().toString();
		String phone = phoneAdd.getText().toString();
		String text = message.getText().toString();

		// Validate input
		if (checkEmpty(name)) {
			Toast.makeText(AddNewTextAlert.this, "Give your alert a name!",
					Toast.LENGTH_LONG).show();
			return;
		} else if (checkEmpty(phone)) {
			Toast.makeText(AddNewTextAlert.this,
					"You forgot to enter a phone number", Toast.LENGTH_LONG)
					.show();
			return;
		} else if (phone.length() != 7 && phone.length() != 10) {
			Toast.makeText(AddNewTextAlert.this,
					"That phone number isn't valid", Toast.LENGTH_LONG).show();
			return;
		} else if (!onlyNumeric(phone)) {
			Toast.makeText(AddNewTextAlert.this,
					"Phone number must contain only numbers " + phone,
					Toast.LENGTH_LONG).show();
			return;
		} else if (checkEmpty(text)) {
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

		Intent intentMessage = new Intent();

		// Debugging toast
		Toast.makeText(AddNewTextAlert.this,
				"Added new text alert: " + name + " " + phone + " " + text,
				Toast.LENGTH_LONG).show();

		intentMessage.putExtra("TITLE", name);
		intentMessage.putExtra("CONTACT", phone);
		intentMessage.putExtra("ICON", R.drawable.ic_action_chat);
		intentMessage.putExtra("MESSAGE", text);

		// if(arriveRadio.isChecked()) {
		// intentMessage.putExtra("WHEN", "ENTER");
		// } else {
		// intentMessage.putExtra("WHEN", "LEAVE");
		// }

		// put the message in Intent
		// intentMessage.putExtra("ALERT", name);

		setResult(Constants.TEXT, intentMessage);

		finish();
	}

	private boolean checkEmpty(String s) {
		if (s.matches(""))
			return true;
		return false;
	}

	private boolean onlyNumeric(String s) {
		try {
			Long.parseLong(s);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;

	}

}
