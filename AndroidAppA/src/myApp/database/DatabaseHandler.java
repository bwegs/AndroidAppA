package myApp.database;

import myApp.list.AlertListItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database name
    private static final String DATABASE_NAME = "alertsManager";
 
    // Table name
    private static final String TABLE_ALERTS = "alerts";
 
    // Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_WHEN = "when";
    private static final String KEY_ICON = "icon";
	
	public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_ALERTS_TABLE = "CREATE TABLE " + TABLE_ALERTS + "("
                + KEY_NAME + " TEXT,"
				+ KEY_CONTACT + " TEXT,"
				+ KEY_LOCATION + " TEXT,"
				+ KEY_MESSAGE + " TEXT,"
				+ KEY_WHEN + " TEXT,"
                + KEY_ICON + " INTEGER" + ")";
        db.execSQL(CREATE_ALERTS_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTS);
 
        // Create tables again
        onCreate(db);
	}
	
	/**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
	
	// Adding a new alert
    void addAlert(AlertListItem alert) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, alert.getTitle()); // Alert Name
        values.put(KEY_CONTACT, alert.getContact()); // Alert contact (phone/email/...)
        
        // locations should be stored in a separate table with unique IDs (here)
        // table itself will have corresponding ID, name, coordinates and radius
        values.put(KEY_LOCATION, alert.getContact()); // NEEDS TO BE CHANGED
        
        values.put(KEY_MESSAGE, alert.getMessage());  // Message to be sent
        values.put(KEY_WHEN, alert.getWhen());  // 'ENTER' or 'EXIT'
        values.put(KEY_ICON, alert.getIcon());  // R.id of corresponding icon
 
        // Inserting Row
        db.insert(TABLE_ALERTS, null, values);
        db.close(); // Closing database connection
    }
	
 // Getting single alert
    AlertListItem getAlert(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_ALERTS, new String[] { KEY_NAME,
                KEY_CONTACT, KEY_LOCATION, KEY_MESSAGE, KEY_WHEN, KEY_ICON }, KEY_NAME + "=?",
                new String[] { name }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        AlertListItem alert = new AlertListItem(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), Integer.parseInt(cursor.getString(5)));
        // return alert
        return alert;
    }

}
