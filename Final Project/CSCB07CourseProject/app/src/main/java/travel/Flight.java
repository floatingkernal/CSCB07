package travel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * Flight class.
 *
 * @author Cameron, Rene, Li
 */
public class Flight implements java.io.Serializable {
  private static final long serialVersionUID = -985503000837056357L;
  private DateFormat yyyyMMddhhmm = new SimpleDateFormat("yyyy-MM-dd kk:mm");
  private String flightNumber;
  private String airline;
  private double cost;
  private Date departureDate;
  private Date arrivalDate;
  private String origin;
  private String destination;
  private int numSeats;

  /**
   * Create a new <code>/Flight</code> with the given flight number, airline,
   * cost, departure date, arrival date, origin and destination.
   *
   * @param flightNumber
   *          the flight number of the new <code>/Flight</code>
   * @param airline
   *          the airline company of the new <code>/Flight</code>
   * @param cost
   *          the cost of the new <code>/Flight</code>
   * @param departureDate
   *          the departure date of the new <code>/Flight</code>
   * @param arrivalDate
   *          the arrival date of the new <code>/Flight</code>
   * @param origin
   *          the origin location of the new <code>/Flight</code>
   * @param destination
   *          the destination location of the new <code>/Flight</code>
   * @param numSeats
   *          the number of seats of the new <code>Flight</code>
   * @throws ParseException
   *           to check if the date information formats are appropriate
   */
  public Flight(String flightNumber, String airline, double cost, String departureDate,
      String arrivalDate, String origin, String destination, int numSeats) throws ParseException {
    this.flightNumber = flightNumber;
    this.airline = airline;
    this.cost = cost;
    this.departureDate = yyyyMMddhhmm.parse(departureDate);
    this.arrivalDate = yyyyMMddhhmm.parse(arrivalDate);
    this.origin = origin;
    this.destination = destination;
    this.numSeats = numSeats;
  }

  private String dateToString(Date date) {
    return (yyyyMMddhhmm.format(date));
  }

  /**
   * Count the difference between the arrival date time and departure date time.
   * Return the total travel time of this <code>/Flight</code>.
   *
   * @return the total travel time of this <code>/Flight</code>
   */
  public String gettravelTime() {
    Long seconds = (this.departureDate.getTime() - this.arrivalDate.getTime()) / 1000;
    Long minutes = seconds / 60;
    Long hours = minutes / 60;
    String travelTime = hours.toString() + ":" + minutes.toString().substring((1));

    return travelTime;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return flightNumber + ";" + dateToString(departureDate) + ";" + dateToString(arrivalDate) + ";"
        + airline + ";" + origin + ";" + destination;
  }

  /**
   * Return the airline company of this <code>/Flight</code>.
   *
   * @return the airline of this <code>/Flight</code>
   */
  public String getAirline() {
    return airline;
  }

  /**
   * Change the airline company of this <code>/Flight</code> to the given.
   * airline company
   *
   * @param airline
   *          the new airline for this <code>/Flight</code>
   */
  public void setAirline(String airline) {
    this.airline = airline;
  }

  /**
   * Return the arrival date information of this <code>/Flight</code>.
   *
   * @return the arrival date of this <code>/Flight</code>
   */
  public Date getArrivalDate() {
    return this.arrivalDate;
  }

  /**
   * Change the arrival date of this <code>/Flight</code> to the given arrival.
   * date
   *
   * @param arrivalDate
   *          the new arrival date for this <code>/Flight</code>
   */
  public void setArrivalDate(Date arrivalDate) {
    this.arrivalDate = arrivalDate;
  }

  /**
   * Return the cost of this <code>/Flight</code>.
   *
   * @return the cost of this <code>/Flight</code>
   */
  public double getCost() {
    return cost;
  }

  /**
   * Change the cost of this <code>/Flight</code> to the given cost.
   *
   * @param cost
   *          the new cost for this <code>/Flight</code>
   */
  public void setCost(double cost) {
    this.cost = cost;
  }

  /**
   * Return the departure date information of this <code>/Flight</code>.
   *
   * @return the departure date of this <code>/Flight</code>
   */
  public Date getDepartureDate() {
    return this.departureDate;
  }

  /**
   * Change the departure date of this <code>/Flight</code> to the given
   * departure date.
   *
   * @param departureDate
   *          the new departure date of this <code>/Flight</code>
   */
  public void setDepartureDate(Date departureDate) {
    this.departureDate = departureDate;
  }

  /**
   * Return the destination information of this <code>/Flight</code>.
   *
   * @return the destination of this <code>/Flight</code>
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Change the destination of this <code>/Flight</code> to the given.
   * destination
   *
   * @param destination
   *          the new destination of this <code>/Flight</code>
   */
  public void setDestination(String destination) {
    this.destination = destination;
  }

  /**
   * Return the flight number of this <code>/Flight</code>.
   *
   * @return the flight number of this <code>/Flight</code>
   */
  public String getFlightNumber() {
    return flightNumber;
  }

  /**
   * Change the flight number of this <code>Flight</code> to the given flight
   * number.
   *
   * @param flightNumber
   *          the new flight number for this <code>Flight</code>
   */
  public void setFlightNumber(String flightNumber) {
    this.flightNumber = flightNumber;
  }

  /**
   * Return the origin location of this <code>Flight</code>.
   *
   * @return the origin of this <code>/Flight</code>
   */
  public String getOrigin() {
    return origin;
  }

  /**
   * Change the origin of this <code>/Flight</code> to the given origin.
   *
   * @param origin
   *          the new origin for this <code>/Flight</code>
   */
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  /**
   * Return the number of seats of this <code>Flight</code>.
   *
   * @return the number of seats of this <code>Flight</code>
   */
  public int getNumSeats() {
    return numSeats;
  }

  /**
   * Change the number of seats of this <code>Flight</code>.
   *
   * @param numSeats
   *          the new number of seats for this <code>Flight</code>
   */
  public void setNumSeats(int numSeats) {
    this.numSeats = numSeats;
  }
}