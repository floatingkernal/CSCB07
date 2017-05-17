package travel;

import java.util.Date;

/**
 * Itinerary class.
 *
 * @author Cameron, Rene, Li
 *
 */
public class Itinerary implements java.io.Serializable {
  private static final long serialVersionUID = -2722938415991696833L;
  private Client client;
  private Flight[] flights;
  private String origin;
  private String destination;
  private double totalCost; //
  private String travelTime; //

  /**
   * Creates a new <code>Itinerary</code> with the given client, flights,
   * origin, destination, total cost, and travel time.
   *
   * @param client
   *          the object client of the new <code>Itinerary</code>
   * @param flights
   *          the list flights of the new <code>Itinerary</code>
   */
  public Itinerary(Client client, Flight[] flights) {
    this.client = client;
    this.flights = flights;
    this.origin = flights[0].getOrigin();
    this.destination = flights[flights.length - 1].getDestination();
    this.totalCost = sumFlights();
    this.travelTime = sumTravel();
  }

  /**
   * Count and return the total cost of the flights in the Flight list.
   *
   * @return the total cost of the flights of this <code>Itinerary</code>
   */
  private double sumFlights() {
    double tempsum = 0;

    for (int i = 0; i < this.flights.length; i++) {
      tempsum = tempsum + this.flights[i].getCost();
    }

    return tempsum;
  }

  /**
   * Count and return the total travel time of the time of the flights.
   *
   * @return the total travel time of the flights
   */
  public String sumTravel() {
    Date departureDate = this.flights[0].getDepartureDate();
    Date arrivalDate = this.flights[flights.length - 1].getArrivalDate();

    return String.format("%.2f",
        (arrivalDate.getTime() - departureDate.getTime()) / 1000.0 / 60 / 60);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String res = new String();

    for (Flight fl : flights) {
      res += fl.toString() + "\n";
    }

    String result = String.format("%.2f\n%s", totalCost, travelTime);
    ;

    return res + result;
  }

  /**
   * Return the object client of this <code>Itinerary</code>.
   *
   * @return the client of this <code>Itinerary</code>
   */
  public Client getClient() {
    return client;
  }

  /**
   * Change the client of this <code>Itinerary</code> to the given client
   * object.
   *
   * @param client
   *          the new client object for this <code>Itinerary</code>
   */
  public void setClient(Client client) {
    this.client = client;
  }

  /**
   * Return the destination location of this <code>Itinerary</code>.
   *
   * @return the destination <code>Itinerary</code>
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Change the destination location of this <code>Itinerary</code> to the given
   * destination.
   *
   * @param destination
   *          the new destination for this <code>Itinerary</code>
   */
  public void setDestination(String destination) {
    this.destination = destination;
  }

  /**
   * Return the flights of this <code>Itinerary</code>.
   *
   * @return the flights of this <code>Itinerary</code>
   */
  public Flight[] getFlights() {
    return flights;
  }

  /**
   * Return the origin location of this <code>Itinerary</code>.
   *
   * @return the origin of this <code>Itinerary</code>
   */
  public String getOrigin() {
    return origin;
  }

  /**
   * Change the origin location of this <code>Itinerary</code> to the given.
   * origin
   *
   * @param origin
   *          the new origin for this <code>Itinerary</code>
   */
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  /**
   * Return the total cost of this <code>Itinerary</code>.
   *
   * @return the total cost of this <code>Itinerary</code>
   */
  public double getTotalCost() {
    return totalCost;
  }

  /**
   * Change the total cost of this <code>Itinerary</code> to the given total
   * cost.
   *
   * @param totalCost
   *          the new total cost for this <code>Itinerary</code>
   */
  public void setTotalCost(double totalCost) {
    this.totalCost = totalCost;
  }

  /**
   * Return the travel time of this <code>Itinerary</code>.
   *
   * @return the travel time of this <code>Itinerary</code>
   */
  public String getTravelTime() {
    return travelTime;
  }

  /**
   * Change the travel time of this <code>Itinerary</code> to the given travel
   * time.
   *
   * @param travelTime
   *          the new travel time for this <code>Itinerary</code>
   */
  public void setTravelTime(String travelTime) {
    this.travelTime = travelTime;
  }
}