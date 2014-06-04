package myApp.androidappa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddNewEmailAlert extends Activity {
	EditText alertName;
	EditText emailAdd;
	// location
	EditText message;
	RadioButton arriveRadio;
	RadioButton leaveRadio;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_email_alert);
        alertName = (EditText)findViewById(R.id.editText1);
        emailAdd = (EditText)findViewById(R.id.editText2);
        message = (EditText)findViewById(R.id.editText4);
        arriveRadio = (RadioButton)findViewById(R.id.radio0);
        leaveRadio = (RadioButton)findViewById(R.id.radio1);
    }
    
	public void addAlert(View V)
	{
		
		// get user data and convert to Strings
		String name = alertName.getText().toString();
		String email = emailAdd.getText().toString();
		String text = message.getText().toString();
		
		// **reminder** also need to check for duplicate names
		if(checkEmpty(name)) { 
			Toast.makeText(AddNewEmailAlert.this,
					   "Give your alert a name!",
					   Toast.LENGTH_LONG).show();
			return;
		} else if (checkEmpty(email)) {
			Toast.makeText(AddNewEmailAlert.this,
					   "You forgot to enter an email address",
					   Toast.LENGTH_LONG).show();
			return;
		} else if (!email.contains("@") || !email.contains(".")) {
			Toast.makeText(AddNewEmailAlert.this,
					   "That email address isn't valid",
					   Toast.LENGTH_LONG).show();
			return;
		} else if (checkEmpty(text)) {
			Toast.makeText(AddNewEmailAlert.this,
					   "You need to enter a message to send to " + email,
					   Toast.LENGTH_LONG).show();
			return;
		}
		
		Intent intentMessage = new Intent();
		
		// Debugging toast
		Toast.makeText(AddNewEmailAlert.this,
					   "Added new email alert: " + name + " " + email + " " + text,
					   Toast.LENGTH_LONG).show();
		
		
//		if(arriveRadio.isChecked()) {
//			intentMessage.putExtra("WHEN", "ENTER");
//		} else {
//			intentMessage.putExtra("WHEN", "LEAVE");
//		} 
		
		// put the message in Intent
		//intentMessage.putExtra("ITEM", item);

		//setResult(2,intentMessage);
	
        //finish();
	}
	
	private boolean checkEmpty(String s) {
		if(s.matches(""))
			return true;
		return false;
	}
}
