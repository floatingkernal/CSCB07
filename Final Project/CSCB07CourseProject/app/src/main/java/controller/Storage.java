package controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import sql.SqlExecutor;
import travel.Client;
import travel.Flight;
import travel.Itinerary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Storage class to handle all interactions with stored information.
 * 
 * @author Cameron
 * 
 */
public class Storage implements Serializable {
  private SqlExecutor sqlExecutor;

  /**
   * Initialize a new <code>Storage</code> object with the default path.
   * 
   * @param context
   *          The application context to instantiate the SQL database
   * @throws StorageException
   *           if there is a problem using the default path
   *           (<code>IOException</code> etc.)
   */
  public Storage(Context context) throws StorageException {
    sqlExecutor = new SqlExecutor(context);
  }

  /**
   * b Load flights from a file at the given path.
   * 
   * @param stream
   *          The inputstream to the file
   * @throws StorageException
   *           Thrown when the specified file cannot be parsed or there is an
   *           issue with the file (IOException etc)
   */
  public void loadFlights(InputStream stream) throws StorageException {
    // Get the file contents
    String[] lines = readFile(stream);
    // Parse and save flights from lines found
    parseFlights(lines);
  }

  /**
   * Load flights from a formatted file at the given path and save to storage.
   * 
   * @param path
   *          to the formatted file
   * @throws StorageException
   *           if file cannot be read or if the file incorrectly formatted
   */
  public void loadFlights(String path) throws StorageException {
    // Get the file contents
    String[] lines = readFile(path);
    parseFlights(lines);
  }

  /**
   * Parse flight info from a given array of formatted(given in handout) strings.
   * 
   * @param lines
   *          Formatted string representing a single <code>Flight</code>'s info
   * @throws StorageException
   *           if file cannot be read or if the file incorrectly formatted
   */
  public void parseFlights(String[] lines) throws StorageException {
    // Iterate through the lines in the file and create a Flight with the info
    // from each line
    for (int i = 0; i < lines.length; i++) {
      String[] info = lines[i].split(";");
      try {
        addOrUpdateFlight(new Flight(info[0], info[3], Double.parseDouble(info[6]), info[1],
            info[2], info[4], info[5], Integer.parseInt(info[7])));
      } catch (Exception exception) {
        throw new StorageException("Cannot parse flight");
      }
    }
  }
  

  /**
   * Parse the flights from the Map with columns as keys and rows in an ordered.
   * ArrayList
   * 
   * @param flightInfo
   *          Map of columns mapped torow values
   * @return ArrayList of flight objects associated with the given map
   */
  private ArrayList<Flight> parseFlights(HashMap<String, ArrayList<String>> flightInfo) {
    // For each flight, get the Flight object associated with it and add it to
    // the List flights
    // If an parsing exception is thrown for any of flights, print
    // out the stack trace for it, continue attempting load the others
    ArrayList<Flight> flights = new ArrayList<>();
    for (int i = 0; i < flightInfo.get("flightNumber").size(); i++) {
      try {
        flights.add(new Flight(flightInfo.get("flightNumber").get(i),
            flightInfo.get("airline").get(i), Double.valueOf(flightInfo.get("cost").get(i)),
            flightInfo.get("departureDate").get(i), flightInfo.get("arrivalDate").get(i),
            flightInfo.get("origin").get(i), flightInfo.get("destination").get(i),
            Integer.parseInt(flightInfo.get("numSeats").get(i))));
      } catch (ParseException ex) {
        ex.printStackTrace();
      }
    }
    return flights;
  }

  /**
   * Get all itineraries for the user with given email
   * 
   * @param email
   *          Email of the desired User's inventories
   * @return Returns an array of <code>Itinerary</code>s for the given email,
   *         else returns null.
   */
  public Itinerary[] getItineraries(String email) {
    // Get a map of itineraries with column names as keys which map to an
    // ArrayList of ordered
    // row values
    HashMap<String, ArrayList<String>> itineraryInfo = getAllTableContentsWhere("itineraries",
        "clientEmail", email);
    ArrayList<Itinerary> itineraries = parseItineraries(itineraryInfo);
    // Create temporary array and use to cast itineraries List to array of
    // Itinerary
    Itinerary[] temp = new Itinerary[itineraries.size()];
    return itineraries.toArray(temp);
  }

  /**
   * Returns all itineraries stored in the database.
   *
   * @return all itineraries in the database
   */
  public Itinerary[] getAllItineraries() {
    // Get all table contents for itineraries in a HashMap with columns as keys
    // and an ArrayList of
    // Strings with ordered row values
    HashMap<String, ArrayList<String>> itineraryInfo = getAllTableContents("itineraries");
    // Parse the itineraries into an ArrayList of Itinerary objects
    ArrayList<Itinerary> itineraries = parseItineraries(itineraryInfo);
    // Cast to an array of Itinerary and return
    Itinerary[] temp = new Itinerary[itineraries.size()];
    return itineraries.toArray(temp);
  }

  /**
   * Function to parse itineraries from an ArrayList of column Strings mapped to
   * an ArrayList of ordered rows.
   * 
   * @param itineraryInfo
   *          map of columns to row values
   * @return ArrayList of Itinerary objects representing the values in the given
   *         HashMap
   */
  public ArrayList<Itinerary> parseItineraries(HashMap<String, ArrayList<String>> itineraryInfo) {
    ArrayList<Itinerary> itineraries = new ArrayList<>();
    // For each Itinerary(row) in the table
    // (Columns have the same amount of rows so rows in "flights" column holds
    // for all)
    for (int i = 0; i < itineraryInfo.get("flights").size(); i++) {
      // Get a Client object associated with the client email
      Client client = getClient(itineraryInfo.get("clientEmail").get(i));
      // For each Flight id, get a Flight object associated with it and add it
      // to the List flights
      String[] flightIds = itineraryInfo.get("flights").get(i).split(";");
      ArrayList<Flight> flights = new ArrayList<>();
      for (int e = 0; e < flightIds.length; e++) {
        flights.add(getFlight(flightIds[e]));
      }
      // Create a new Itinerary with the Client object found and the List of
      // Flights found cast
      // to an array of Flight
      Flight[] temp = new Flight[flights.size()];
      itineraries.add(new Itinerary(client, flights.toArray(temp)));
    }
    return itineraries;
  }

  /**
   * Sets the password of the specified user with the specified password.
   * 
   * @param userEmail
   *          The desired user's email
   * @param password
   *          The desired password
   * @throws StorageException
   *           if the given user doesn't exist
   */
  public void setPassword(String userEmail, String password) throws StorageException {
    ContentValues values = new ContentValues();
    values.put("email", userEmail);
    values.put("password", password);
    // Update SQL table of clients with the new values for password
    sqlExecutor.updateRecords("clients", values, "email", userEmail);
  }

  /**
   * Returns the password of the specified user.
   * 
   * @param userEmail
   *          the user who's password will be returned
   * @return the password of the user
   */
  public String getPassword(String userEmail) {
    // If given email is an existing client, get a Client object associated with
    // it from storage
    if (getClient(userEmail) != null) {
      return sqlExecutor.selectRecords("clients", "email", userEmail).getString(7);
    } else {
      // If client does not exist
      return null;
    }
  }

  /**
   * Adds an <code>Itinerary</code> object to the database.
   * 
   * @param itinerary
   *          the <code>Itinerary</code> to add to the database.
   */
  public void addItinerary(Itinerary itinerary) {
    // Add the email to the values mapped to the key clientEmail
    ContentValues values = new ContentValues();
    values.put("clientEmail", itinerary.getClient().getEmail());
    // Create the String of flights(all flight IDs separated by a semi-colon
    Flight[] allFlights = itinerary.getFlights();
    String flights = "";
    for (int i = 0; i < allFlights.length; i++) {
      flights += allFlights[i].getFlightNumber() + ";";
    }
    // Remove trailing ';'
    flights = flights.substring(0, flights.length() - 1);
    // Add the String of flights to the values mapped to the key flights
    values.put("flights", flights);
    // Create a new entry in the database in the table of itineraries
    sqlExecutor.createRecords("itineraries", values);
  }

  /**
   * Get all flights from storage.
   * 
   * @return An array of <code>Flight</code>s
   * @throws StorageException
   *           Thrown when a file exception is found
   */
  public Flight[] getFlights() {
    // Get all the table of flights with columns as keys mapped to an ArrayList
    // of ordered rows
    HashMap<String, ArrayList<String>> flightInfo = getAllTableContents("flights");
    ArrayList<Flight> flights = parseFlights(flightInfo);
    Flight[] temp = new Flight[flightInfo.get("flightNumber").size()];
    return flights.toArray(temp);
  }

  /**
   * Get a flight with the given flightNumber else return null.
   * 
   * @param flightNumber
   *          Flight number of the desired flight
   * @return Flight with desired flightNumber, else null
   */
  private Flight getFlight(String flightNumber) {
    HashMap<String, ArrayList<String>> flightInfo = getAllTableContentsWhere("flights",
        "flightNumber", flightNumber);
    if (flightInfo.get("flightNumber").size() > 0) {
      return parseFlights(flightInfo).toArray(new Flight[1])[0];
    }
    return null;
  }


  /**
   * Adds a <code>Flight</code> to the database, or updates an existing one if a
   * <code>Flight</code> with the same flight number already exists.
   * 
   * @param flight
   *          The flight to be added or updated.
   */
  public void addOrUpdateFlight(Flight flight) {
    // Add all values to mapped to their associated column name
    ContentValues value = new ContentValues();
    value.put("flightNumber", flight.getFlightNumber());
    value.put("airline", flight.getAirline());
    value.put("cost", String.valueOf(flight.getCost()));
    value.put("departureDate",
        new SimpleDateFormat("yyyy-MM-dd kk:mm").format(flight.getDepartureDate()));
    value.put("arrivalDate",
        new SimpleDateFormat("yyyy-MM-dd kk:mm").format(flight.getArrivalDate()));
    value.put("origin", flight.getOrigin());
    value.put("destination", flight.getDestination());
    value.put("numSeats", flight.getNumSeats());
    // Add a new row to the database if not already added. Else, update that row
    // with given Flight
    // info.
    sqlExecutor.updateOrAddRecords("flights", value);
  }

  /**
   * . Read a file from the specified InputStream
   * 
   * @param stream
   *          Stream to the desired file
   * @return An array of the lines found in the file
   * @throws StorageException
   *           Thrown if the file cannot be read
   */
  private String[] readFile(InputStream stream) throws StorageException {
    try {
      return readFile(new BufferedReader(new InputStreamReader(stream)));
    } catch (Exception exception) {
      throw new StorageException("Cannot read file ");
    }
  }

  /**
   * Read a file from the given path.
   * 
   * @param path
   *          Path to the desired file
   * @return An array of lines found in the file
   * @throws StorageException
   *         if file cannot be read
   */
  private String[] readFile(String path) throws StorageException {
    try {
      return readFile(new BufferedReader(new FileReader(new File(path))));
    } catch (Exception exception) {
      throw new StorageException("Cannot read file " + path + ":" + exception.getMessage());
    }
  }

  /**
   * Read a file with with the given reader.
   * 
   * @param reader
   *          Desired Reader to read from
   * @return An array of the lines found in the file
   * @throws IOException
   *         if reading file causes exception.
   */
  private String[] readFile(BufferedReader reader) throws IOException {
    // Read each line in the file, add it to the ArrayList lines
    ArrayList<String> lines = new ArrayList<String>();
    String line;
    while ((line = reader.readLine()) != null) {
      lines.add(line);
    }
    reader.close();
    // Convert the list to an array of type String and return
    String[] ret = new String[1];
    return (lines.toArray(ret));
  }

  /**
   * Returns an array of all the <code>Client</code>s in the database.
   * 
   * @return The list of <code>Client</code> objects in the database.
   */
  public Client[] getClients() {
    // Get all the contents from the table clients with columns as keys mapped
    // to an ArrayList of
    // ordered row values
    HashMap<String, ArrayList<String>> clientInfo = getAllTableContents("clients");
    // Parse the clients into an ArrayList of Client objects
    ArrayList<Client> clients = parseClients(clientInfo);
    // Cast to an array of Client and return
    Client[] temp = new Client[clients.size()];
    return clients.toArray(temp);
  }

  /**
   * Returns the <code>Client</code> with the given email.
   * 
   * @param email
   *          the email of the <code>Client</code> to search for
   * @return returns the <code>Client</code> with the given email
   */
  public Client getClient(String email) {
    // Get the contents from the table of clients with columns as keys mapped to
    // an ArrayList of
    // ordered row values
    HashMap<String, ArrayList<String>> clientInfo = getAllTableContentsWhere("clients", "email",
        email);
    // If there is a client associated with it
    if (clientInfo.get("email").size() > 0) {
      // Get a client Object associated with it
      return parseClients(clientInfo).toArray(new Client[1])[0];
    } else {
      // If there client email is not associated with a client in the database
      return null;
    }
  }

  /**
   * Parse the given <code>Map</code> of columns mapped to an ordered list.
   * 
   * @param clientInfo
   *          ArrayList to parse
   * @return a <code>List</code> of <code>Client</code>s associated with the
   *         given <code>Map</code>
   */
  public ArrayList<Client> parseClients(HashMap<String, ArrayList<String>> clientInfo) {
    ArrayList<Client> clients = new ArrayList<Client>();
    // Create Client with the content in each row and add it to the ArrayList
    // clients
    for (int i = 0; i < clientInfo.get("email").size(); i++) {
      clients
          .add(new Client(clientInfo.get("email").get(i), clientInfo.get("personalAddress").get(i),
              clientInfo.get("creditCardNumber").get(i), clientInfo.get("expiry").get(i),
              clientInfo.get("firstName").get(i), clientInfo.get("lastName").get(i)));
    }
    return clients;
  }

  /**
   * Uploads a <code>Client</code> to the database using th given
   * <code>Stream</code>.
   * 
   * @param stream
   *          The stream to load a file from
   * @throws StorageException
   *           if file cannot be read or parsed
   */
  public void loadClients(InputStream stream) throws StorageException {
    // Get the lines in the file
    String[] lines = readFile(stream);
    // Parse and save the clients to the database
    parseClients(lines);
  }

  /**
   * Load new Clients from a formatted file at the given path. Overwrite if
   * already existing.
   * 
   * @param path
   *          Path to the formatted file
   * @throws StorageException
   *           If file cannot be read or parsed
   */
  public void loadClients(String path) throws StorageException {
    // Get the lines in the file
    String[] lines = readFile(path);
    // Parse and save clients to database
    parseClients(lines);
  }

  /**
   * Parse lines and add to clients table in database. Overwrite if already
   * existing
   * 
   * @param lines
   *          to parse and add to database
   */
  public void parseClients(String[] lines) {
    // For each line, add a value mapped to the associated column name
    for (int i = 0; i < lines.length; i++) {
      String[] info = lines[i].split(";");
      ContentValues values = new ContentValues();
      values.put("email", info[2]);
      values.put("expiry", info[5]);
      values.put("personalAddress", info[3]);
      values.put("creditCardNumber", info[4]);
      values.put("firstName", info[1]);
      values.put("lastName", info[0]);
      // Add or update the database with the parsed info
      sqlExecutor.updateOrAddRecords("clients", values);
    }
  }

  /**
   * Adds a <code>Client</code> to the database, or updates an existing one if a
   * <code>Client</code> with the same email already exists.
   * 
   * @param client
   *          The client to be added or updated.
   */
  public void addOrUpdateClient(Client client) {
    // Add table values mapped to their associated column name
    ContentValues value = new ContentValues();
    value.put("email", client.getEmail());
    value.put("personalAddress", client.getPersonalAddress());
    value.put("creditCardNumber", client.getCreditCardNumber());
    value.put("expiry", new SimpleDateFormat("yyyy-MM-dd").format(client.getExpiry()));
    value.put("firstName", client.getFirstName());
    value.put("lastName", client.getLastName());
    // Add or update database with table values
    sqlExecutor.updateOrAddRecords("clients", value);
  }

  /**
   * Get all the contents in the given table.
   * 
   * @param table
   *          name of table to use
   * @return HashMap with columns in table as keys mapping to an ArrayList of
   *         all the row values
   */
  private HashMap<String, ArrayList<String>> getAllTableContents(String table) {
    return getAllTableContentsWhere(table, null, null);
  }

  /**
   * Get all the contents in the given table satisfying the given condition.
   * 
   * @param table
   *          name of Table to use
   * @param where
   *          conditioning row
   * @param whereValue
   *          conditioning value
   * @return HashMap with columns in table as keys mapping to an ArrayList of
   *         all the row values
   */
  private HashMap<String, ArrayList<String>> getAllTableContentsWhere(String table, String where,
      String whereValue) {
    HashMap<String, ArrayList<String>> contents = new HashMap<String, ArrayList<String>>();
    // Get the records associated with the table and conditions
    Cursor allRecords = sqlExecutor.selectRecords(table, where, whereValue);
    if (allRecords == null) {
      return new HashMap<>();
    }
    // For each column, add a key to contents with the column name
    for (int i = 0; i < allRecords.getColumnCount(); i++) {
      contents.put(allRecords.getColumnName(i), new ArrayList<String>());
    }
    // add all row values to the ArrayList mapped to each column
    for (int i = 0; i < allRecords.getCount(); i++) {
      for (int e = 0; e < allRecords.getColumnCount(); e++) {
        contents.get(allRecords.getColumnName(e)).add(allRecords.getString(e));
      }
      // Move to next row
      allRecords.moveToNext();
    }
    return contents;
  }

}
