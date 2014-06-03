package myApp.androidappa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddNewTextAlert extends Activity {
	EditText editTextMessage;
	RadioButton arriveRadio;
	RadioButton leaveRadio;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_text_alert);
        editTextMessage=(EditText)findViewById(R.id.editText1);
        arriveRadio = (RadioButton)findViewById(R.id.radio0);
        leaveRadio = (RadioButton)findViewById(R.id.radio1);

    }
    
	public void addItem(View V)
	{
		// get the Entered  message
		String item = editTextMessage.getText().toString();
		Intent intentMessage = new Intent();
		
		// Debugging toast
		Toast.makeText(AddNewTextAlert.this,
					   "Added new text alert: " + item,
					   Toast.LENGTH_LONG).show();
		
		
		if(arriveRadio.isChecked()) {
			intentMessage.putExtra("WHEN", "ENTER");
		} else {
			intentMessage.putExtra("WHEN", "LEAVE");
		} 
		
		// put the message in Intent
		intentMessage.putExtra("ITEM", item);

		setResult(2,intentMessage);
	
        finish();
	}
}
