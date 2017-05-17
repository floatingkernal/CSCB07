package sql;

/**
 * A class to execute SQL queries on the Sqlite database
 * Created by Cameron on 11/23/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

public class SqlExecutor implements Serializable {
  private SQLiteDatabase database;
  private MyDatabaseHelper dbHelper;
  public static final HashMap<String, String[]> columns = new HashMap<String, String[]>();

  /**
   * Create a new <code>SqlExecutor</code> object attached to the given context.
   * 
   * @param context
   *          The current application context
   */
  public SqlExecutor(Context context) {
    dbHelper = new MyDatabaseHelper(context);
    database = dbHelper.getWritableDatabase();
    columns.put("clients", new String[] { "id", "email", "personalAddress", "creditCardNumber",
        "expiry", "firstName", "lastName", "password" });
    columns.put("flights", new String[] { "id", "flightNumber", "airline", "cost", "departureDate",
        "arrivalDate", "origin", "destination", "numSeats" });
    columns.put("itineraries", new String[] { "id", "clientEmail", "flights" });

  }

  /**
   * Create a record in the given table with column values in
   * <code>ContentValues</code> values.
   * 
   * @param table
   *          The table to add to
   * @param values
   *          Values to add
   * @return Sql Affected rows
   */
  public long createRecords(String table, ContentValues values) {
    return database.insert(table, null, values);
  }

  /**
   * Update a record in the given table.
   * 
   * @param table
   *          The table to modify
   * @param values
   *          The new values of the desired row
   * @param where
   *          The conditioning column
   * @param whereValue
   *          The conditioning value
   * @return Number of rows affected
   */
  public int updateRecords(String table, ContentValues values, String where, String whereValue) {
    return database.update(table, values, where + "='" + whereValue + "'", null);
  }

  /**
   * Update rows if they exist(Using default unique values to condition), else
   * add a new row.
   * 
   * @param table
   *          The table to add/update
   * @param values
   *          The values to add/update
   */
  public void updateOrAddRecords(String table, ContentValues values) {
    updateOrAddRecords(table, values, columns.get(table)[1],
        values.getAsString(columns.get(table)[1]));
  }

  /**
   * Update rows using the given where and whereValue as conditions. If row does
   * not exist, add.
   * 
   * @param table
   *          Table to add/update
   * @param values
   *          Values to add/update
   * @param where
   *          The conditioning column
   * @param whereValue
   *          The conditioning value
   */
  public void updateOrAddRecords(String table, ContentValues values, String where,
      String whereValue) {
    int affected = updateRecords(table, values, where, whereValue);
    if (affected == 0) {
      createRecords(table, values);
    }
  }

  /**
   * Select records using the default unique identifier (email, flightNumber
   * etc).
   * 
   * @param table
   *          The table to select from
   * @return Cursor with all values returned from the query
   */
  public Cursor selectRecords(String table) {
    return selectRecords(table, null, null);
  }

  /**
   * Select records using the default unique identifier (email, flightNumber
   * etc).
   * 
   * @param table
   *          The table to select from
   * @param where
   *          The conditioning column
   * @param whereValue
   *          The conditioning value
   * @return Cursor with all values returned from the query
   */
  public Cursor selectRecords(String table, String where, String whereValue) {
    Cursor mcursor;
    if (where != null && whereValue != null) {
      mcursor = database.query(true, table, columns.get(table), where + "='" + whereValue + "'",
          null, null, null, null, null);
    } else {
      mcursor = database.query(true, table, columns.get(table), null, null, null, null, null, null);
    }
    if (mcursor != null) {
      mcursor.moveToFirst();
    }
    return mcursor; // iterate to get each value.
  }
}
