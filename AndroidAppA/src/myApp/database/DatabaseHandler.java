/*
 * Author - Ben Wegher
 * Date   - 6/10/2014
 * Class  - DatabaseHandler.java
 * Description - This class specifies all the necessary methods for my SQLite database.
 * 				 Has 2 tables - alerts, locations. Currently alerts is the only one in use.
 * 
 */

package myApp.database;

import java.util.ArrayList;

import myApp.list.AlertListItem;
import myApp.location.Location;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database name
	private static final String DATABASE_NAME = "alertsManager.db";

	// Table names
	private static final String TABLE_ALERTS = "alerts";
	private static final String TABLE_LOCATIONS = "locations";

	// common column names
	private static final String KEY_NAME = "name";
	private static final String KEY_LOCATION_ID = "location_id";

	// alerts table columns names
	// private static final String KEY_ID = "id";
	private static final String KEY_CONTACT = "contact";
	private static final String KEY_MESSAGE = "message";
	private static final String KEY_TRIGGER = "trigger";
	private static final String KEY_TYPE = "type";

	// locations table columns names
	private static final String KEY_LAT = "latitude";
	private static final String KEY_LONG = "longitude";
	private static final String KEY_RAD = "radius";
	private static final String KEY_EXPIRE = "expiration";
	private static final String KEY_TRANSITION = "transition";

	// Create 'alerts' table string
	private static final String CREATE_ALERTS_TABLE = "CREATE TABLE "
			+ TABLE_ALERTS
			+ "("
			// + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ KEY_NAME + " TEXT PRIMARY KEY," + KEY_CONTACT + " TEXT,"
			+ KEY_LOCATION_ID + " INTEGER FOREIGN KEY," + KEY_MESSAGE
			+ " TEXT," + KEY_TRIGGER + " TEXT," + KEY_TYPE + " INTEGER" + ")";

	// Create 'locations' table string
	private static final String CREATE_LOCATIONS_TABLE = "CREATE TABLE "
			+ TABLE_LOCATIONS + "(" + KEY_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ KEY_NAME + " TEXT," + KEY_LAT + " REAL," + KEY_LONG + " REAL,"
			+ KEY_RAD + " REAL," + KEY_EXPIRE + " INTEGER," + KEY_TRANSITION
			+ " INTEGER" + ")";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// create tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_ALERTS_TABLE);
		db.execSQL(CREATE_LOCATIONS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations for 'alerts' table
	 */

	/**
	 * Adding a new alert
	 * */
	public void addAlert(AlertListItem alert) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, alert.getTitle()); // Alert Name
		values.put(KEY_CONTACT, alert.getContact()); // Alert contact
														// (phone/email/...)

		// locations should be stored in a separate table with unique IDs (here)
		// table itself will have corresponding ID, name, coordinates and radius
		values.put(KEY_LOCATION_ID, alert.getLocation()); // NEEDS TO BE CHANGED

		values.put(KEY_MESSAGE, alert.getMessage()); // Message to be sent
		values.put(KEY_TRIGGER, alert.getWhen()); // 'ENTER' or 'EXIT'
		values.put(KEY_TYPE, alert.getIcon()); // R.id of corresponding icon

		// Inserting Row
		db.insert(TABLE_ALERTS, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Getting single alert by name (Primary Key)
	 * */
	public AlertListItem getAlert(String name) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ALERTS, new String[] { KEY_NAME,
				KEY_CONTACT, KEY_LOCATION_ID, KEY_MESSAGE, KEY_TRIGGER,
				KEY_TYPE }, KEY_NAME + "=?", new String[] { name }, null, null,
				null, null);
		if (cursor != null)
			cursor.moveToFirst();

		AlertListItem alert = new AlertListItem(cursor.getString(0),
				cursor.getString(1), Integer.parseInt(cursor.getString(2)),
				cursor.getString(3), cursor.getString(4),
				Integer.parseInt(cursor.getString(5)));
		// return alert
		return alert;
	}

	/**
	 * Getting all alerts
	 * */
	public ArrayList<AlertListItem> getAllAlerts() {
		ArrayList<AlertListItem> alertList = new ArrayList<AlertListItem>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ALERTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				AlertListItem alert = new AlertListItem();
				alert.setTitle(cursor.getString(0));
				alert.setContact(cursor.getString(1));
				alert.setLocation(Integer.parseInt(cursor.getString(2)));
				alert.setMessage(cursor.getString(3));
				alert.setWhen(cursor.getString(4));
				alert.setIcon(Integer.parseInt(cursor.getString(5)));
				// Adding alert to list
				alertList.add(alert);
			} while (cursor.moveToNext());
		}

		// return alert list
		return alertList;
	}

	/**
	 * Updating a single alert
	 * */
	public int updateAlert(AlertListItem alert) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, alert.getTitle());
		values.put(KEY_CONTACT, alert.getContact());
		values.put(KEY_LOCATION_ID, alert.getLocation());
		values.put(KEY_MESSAGE, alert.getMessage());
		values.put(KEY_TRIGGER, alert.getWhen());
		values.put(KEY_TYPE, alert.getIcon());

		// updating row
		return db.update(TABLE_ALERTS, values, KEY_NAME + " = ?",
				new String[] { alert.getTitle() });
	}

	/**
	 * Deleting single alert
	 * */
	public void deleteAlert(AlertListItem alert) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ALERTS, KEY_NAME + " = ?",
				new String[] { alert.getTitle() });
		db.close();
	}

	/**
	 * Check if alert exists
	 * */
	public boolean alertExists(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("select 1 from " + TABLE_ALERTS
				+ " where name=?", new String[] { name });
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		db.close();
		return exists;
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations for 'locations' table
	 */

	/**
	 * Adding a new location
	 * */
	public void addLocation(Location location) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		//values.put(KEY_LOCATION_ID, location.getId()); // location_id -- automatically generated
		values.put(KEY_NAME, location.getName()); // location name
		values.put(KEY_LAT, location.getLatitude()); // latitude
		values.put(KEY_LONG, location.getLongitude()); // longitude
		values.put(KEY_RAD, location.getRadius()); // radius

		// Inserting Row
		db.insert(TABLE_LOCATIONS, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Getting all locations
	 * */
	public ArrayList<Location> getAllLocations() {
		ArrayList<Location> locationList = new ArrayList<Location>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				try {
					Location location = new Location(cursor.getString(0), // location id
							cursor.getString(1), // name
							Double.parseDouble(cursor.getString(2)), // latitude
							Double.parseDouble(cursor.getString(3)), // longitude
							Float.parseFloat(cursor.getString(4))); // radius

					// Adding location to list
					locationList.add(location);
				} catch (NumberFormatException e) {
					Log.e("ERROR",
							"error parsing value to String in location table - location '"
									+ cursor.getString(1) + "'");
				}
			} while (cursor.moveToNext());
		}

		// return location list
		return locationList;
	}

	/**
	 * Updating a single location
	 * */
	public int updateLocation(Location location) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LOCATION_ID, location.getId());
		values.put(KEY_NAME, location.getName());
		values.put(KEY_LAT, location.getLatitude());
		values.put(KEY_LONG, location.getLongitude());
		values.put(KEY_RAD, location.getRadius());

		// updating row
		return db.update(TABLE_LOCATIONS, values, KEY_LOCATION_ID + " = ?",
				new String[] { location.getId() });
	}

	/**
	 * Deleting single location
	 * 
	 * TODO --any geofences using this location need to be removed first
	 * */
	public void deleteLocation(Location location) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LOCATIONS, KEY_LOCATION_ID + " = ?",
				new String[] { location.getId() });
		db.close();
	}

}
