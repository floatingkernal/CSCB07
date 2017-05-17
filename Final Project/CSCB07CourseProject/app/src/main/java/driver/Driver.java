package driver;

import android.content.Context;

import controller.Backend;
import controller.Storage;
import controller.StorageException;
import travel.Flight;
import travel.Itinerary;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




/** A Driver used for autotesting the project backend. */
public class Driver {

  public static final long MIN_LAYOVER = 30 * 60; // 30 min in seconds
  public static final long MAX_LAYOVER = 6 * 60 * 60; // 6 hours in seconds

  private Backend backend;

  public Driver(Context context) {
    backend = new Backend(context);
  }

  private static Date toDate(String time) throws ParseException {
    DateFormat yyyyMMddhhmm = new SimpleDateFormat("yyyy-MM-dd");
    return yyyyMMddhhmm.parse(time);
  }

  /**
   * Uploads client information to the application from the file at the
   * given path.
   * @param path the path to an input csv file of client information with
   *     lines in the format:
   *     LastName;FirstNames;Email;Address;CreditCardNumber;ExpiryDate
   *     The ExpiryDate is stored in the format yyyy-MM-dd.
   */
  public void uploadClientInfo(String path) {
    try {
      backend.addClient(path);
    } catch (StorageException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Uploads flight information to the application from the file at the
   * given path.
   * @param path the path to an input csv file of flight information with 
   *     lines in the format: 
   *     Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination;Price
   *     The dates are in the format yyyy-MM-dd HH:mm; the price has exactly two
   *     decimal places.
   */
  public void uploadFlightInfo(String path) {
    try {
      backend.addFlight(path);
    } catch (StorageException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Returns the information stored for the client with the given email. 
   * @param email the email address of a client
   * @return the information stored for the client with the given email
   *     in this format:
   *     LastName;FirstNames;Email;Address;CreditCardNumber;ExpiryDate
   *     (the ExpiryDate is stored in the format yyyy-MM-dd)
   */
  public String getClient(String email) {

    // The code below gives you the format in which the auto-tester expects the output.
    
    try {
      return (backend.searchClient(email));
    } catch (StorageException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * Returns all flights that depart from origin and arrive at destination on
   * the given date. 
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight origin
   * @param destination a flight destination
   * @return the flights that depart from origin and arrive at destination
   *     on the given date formatted in exactly this format:
   *     Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination;Price
   *     The dates are in the format yyyy-MM-dd HH:mm; the price has exactly two
   *     decimal places. 
   * @throws ParseException if date cannot be parsed
   */
  public List<String> getFlights(String date, String origin, String destination)
      throws ParseException {
    // The code below gives you the format in which the auto-tester expects the output.
    
    try {
      List<Flight> flights = backend.searchFlight(date, origin, destination);
      List<String> res = new ArrayList<>();
      for (int i = 0; i < flights.size(); i++) {
        res.add(flights.get(i).toString());
      }
      return res;
    } catch (StorageException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * Returns all itineraries that depart from origin and arrive at destination on the given date. If
   * an itinerary contains two consecutive flights F1 and F2, then the destination of F1 should
   * match the origin of F2. To simplify our task, if there are more than MAX_LAYOVER hours or less
   * than MIN_LAYOVER between the arrival of F1 and the departure of F2, then we do not consider
   * this sequence for a possible itinerary.
   * 
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries that depart from origin and arrive at destination on the given date with
   *         valid layover. Each itinerary in the output should contain one line per flight, in the
   *         format: 
   *         Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination
   *         followed by total price (on its own line, exactly 2 decimal places),
   *         followed by total duration (on its own line, measured in hours with 
   *         exactly 2 decimal places).
   * @throws ParseException when date is not in the correct format yyyy-mm-dd
   */
  public List<String> getItineraries(String date, String origin, String destination) {

    List<String> itineraries = new ArrayList<>();    
    Date departDate = null;
    try {
      departDate = toDate(date);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    List<Itinerary> listItineraries = null;
    try {
      listItineraries = backend.getItineraries(departDate, origin, destination);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    for (int i = 0; i < listItineraries.size(); i++) {
      itineraries.add(listItineraries.get(i).toString());
    }
    return itineraries;
  }

  /**
   * Returns the same itineraries as getItineraries produces, but sorted according to total
   * itinerary cost, in non-decreasing order.
   * 
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries (sorted in non-decreasing order of total itinerary cost) in the same format
   *         as in getItineraries.
   */
  public String getItinerariesSortedByCost(String date, String origin, String destination) {
    String itineraries = new String();    
    Date departDate = null;
    try {
      departDate = toDate(date);
    } catch (ParseException e1) {
      e1.printStackTrace();
    }
    List<Itinerary> listItineraries = null;
    try {
      listItineraries = backend.getItineraries(departDate, origin, destination);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    backend.sortItinerariesByCost(listItineraries);
    for (int i = 0; i < listItineraries.size(); i++) {
      itineraries += listItineraries.get(i).toString() + "\n";
    }
    return itineraries;
  }

  /**
   * Returns the same itineraries as getItineraries produces, but sorted according
   * to total itinerary travel time, in non-decreasing order.
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries (sorted in non-decreasing order of total travel time) in the same format
   *         as in getItineraries.
   */
  public String getItinerariesSortedByTime(String date, String origin, String destination) {
    String itineraries = new String();    
    Date departDate = null;
    try {
      departDate = toDate(date);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    List<Itinerary> listItineraries = null;
    try {
      listItineraries = backend.getItineraries(departDate, origin, destination);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    backend.sortItinerariesByTravelTime(listItineraries);
    for (int i = 0; i < listItineraries.size(); i++) {
      itineraries += listItineraries.get(i).toString() + "\n";
    }
    return itineraries;
  }
}
