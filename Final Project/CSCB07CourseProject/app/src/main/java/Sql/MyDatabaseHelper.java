package sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import controller.Constants;

/**
 * A class to create the database and access basic SQL functions.
 * 
 * @author Cameron
 */
public class MyDatabaseHelper extends SQLiteOpenHelper implements java.io.Serializable {

  private static final int DATABASE_VERSION = 2;
  // Database creation sql statement to create all required tables
  private static final String[] DEFAULT_CREATION = new String[] {
      "create table clients( id integer primary key AUTOINCREMENT, email VARCHAR, "
          + "personalAddress VARCHAR, creditCardNumber VARCHAR, expiry VARCHAR, "
          + "firstName VARCHAR, lastName VARCHAR, password VARCHAR);",
      "create table flights(id integer primary key AUTOINCREMENT, flightNumber VARCHAR,"
          + "airline VARCHAR, cost VARCHAR, departureDate VARCHAR, "
          + "arrivalDate VARCHAR, origin VARCHAR, destination VARCHAR, numSeats VARCHAR)",
      "create table itineraries(id integer primary key AUTOINCREMENT, "
          + "clientEmail VARCHAR,flights VARCHAR)" };

  // Start the database with the given context
  public MyDatabaseHelper(Context context) {
    super(context, Constants.DATABASE_NAME, null, DATABASE_VERSION);
  }

  // Method is called during creation of the database
  @Override
  public void onCreate(SQLiteDatabase database) {
    for (int i = 0; i < DEFAULT_CREATION.length; i++) {
      database.execSQL(DEFAULT_CREATION[i]);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
  }
}