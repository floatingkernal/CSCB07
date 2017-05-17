package controller;

import android.content.Context;
import driver.Driver;
import travel.Client;
import travel.Flight;
import travel.Itinerary;
import travel.User;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Backend class. Facilitates communication between Front-end and storage.
 * 
 * @author Rene, Salman, Cameron
 *
 */
public class Backend {

  private User user;
  private Flight[] flights;
  private Storage data;

  /**
   * Initializes new <code>Backend</code> object with specified storage.
   * 
   * @param context
   *          The application context to initialize the SQL database with stored
   *          data.
   */
  public Backend(Context context) {
    try {
      this.data = new Storage(context);
      flights = this.data.getFlights();
    } catch (StorageException exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Faciliatates booking an itinerary: adds given itinerary to storage.
   * 
   * @param itinerary
   *          Itinerary object to add
   */
  public void bookItinerary(Itinerary itinerary) {
    data.addItinerary(itinerary);
  }

  /**
   * Get a <code>List</code> of the itineraries booked for the given.
   * <code>Client</code>
   * 
   * @param client
   *          desired <code>Client</code> to recieve itineraries for
   * @return List of itineraries booked by the given <code>Client</code>
   */
  public List<Itinerary> getBookedItineraries(Client client) {
    List<Itinerary> res = Arrays.asList(data.getItineraries(client.getEmail()));
    if (res == null || res.isEmpty()) {
      res = new ArrayList<Itinerary>();
    }
    return res;
  }

  public Client[] getAllClients() {
    return data.getClients();
  }

  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Returns a list of itineraries with specified information.
   * 
   * @param departureDate
   *          The date of departure being searched for.
   * @param origin
   *          The origin of the flight.
   * @param destination
   *          The destination of the flight.
   * @return List of all itineraries with the specified date, origin, and
   *         destination.
   * @throws ParseException
   *           <code>ParseException</code> for invalid date.
   */
  public List<Itinerary> getItineraries(Date departureDate, String origin, String destination)
      throws ParseException {
    User us;
    if (!(user instanceof Client)) {
      us = new Client();
    } else {
      us = this.user;
    }
    // get all possible itineraries
    ArrayList<LinkedList<Flight>> possibleItineraries = getAllPossibleItineraries(departureDate,
        origin, destination);
    // remove any itineraries that do not end at destination
    removeItinerariesWithoutDestination(possibleItineraries, destination);
    // remove any cycling itinerary
    removeCyclingItineraries(possibleItineraries);
    // make the array list into an array of itineraries

    return makeToListOfItinerary(possibleItineraries, us);
  }

  private List<Itinerary> makeToListOfItinerary(ArrayList<LinkedList<Flight>> itineraries,
      User user) throws ParseException {
    ArrayList<Itinerary> res = new ArrayList<>();
    // loop over each link list
    for (int i = 0; i < itineraries.size(); i++) {
      // create array of flight and new itinerary
      Flight[] flights = itineraries.get(i).toArray(new Flight[itineraries.get(i).size()]);
      Itinerary itinerary = new Itinerary((Client) user, flights);
      res.add(itinerary);
    }
    return res;
  }

  private void removeCyclingItineraries(ArrayList<LinkedList<Flight>> itineraryList) {
    // loop over each itinerary
    for (int i = 0; i < itineraryList.size(); i++) {
      LinkedList<Flight> itineray = itineraryList.get(i);
      boolean cycleOccurred = false;
      // create a temporary list that tract each destination
      ArrayList<String> destinations = new ArrayList<>();
      // loop through each flight,
      for (int j = 0; j < itineray.size(); j++) {
        Flight flight = itineray.get(j);
        // check if destination of this flight already occurred
        if (destinations.contains(flight.getDestination())) {
          // if destination already occurred, flag this itinerary
          cycleOccurred = true;
        } else {
          // else add this flights destination to list of destinations.
          destinations.add(flight.getDestination());
        }
      }
      // if cycle occurred, remove this itinerary
      if (cycleOccurred) {
        itineraryList.remove(i);
      }
    }
  }

  private void removeItinerariesWithoutDestination(ArrayList<LinkedList<Flight>> itineraryList,
      String destination) {
    // loop over each list of itineraries,
    for (int i = 0; i < itineraryList.size(); i++) {
      LinkedList<Flight> itinerary = itineraryList.get(i);
      // get the last flight in the itinerary
      Flight lastFlight = itinerary.peekLast();
      // check if it does not have the same destination and remove it form this
      // list
      boolean checkDestination = lastFlight.getDestination().equals(destination);
      if (!(checkDestination)) {
        itineraryList.remove(i);
      }
    }
  }

  private ArrayList<LinkedList<Flight>> getAllPossibleItineraries(Date departureDate, String origin,
      String destination) {
    // base case: no flights at origin, departure date out of range, origin ==
    // destination
    // return an empty array of flights.
    ArrayList<LinkedList<Flight>> res = new ArrayList<>();
    ArrayList<Flight> flightsWithThisOriginAndDepartDate = getsFlightsWithOriginAndDepartDate(
        departureDate, origin);
    if (origin == destination || flightsWithThisOriginAndDepartDate.isEmpty()) {
      return res;
    } else {
      // loop over each flight
      for (int i = 0; i < flightsWithThisOriginAndDepartDate.size(); i++) {
        // get list of flight connecting this flight
        Flight flight = flightsWithThisOriginAndDepartDate.get(i);
        Date nextDepart = null;
        nextDepart = new Date(flight.getArrivalDate().getTime() + Driver.MIN_LAYOVER * 1000);
        String nextOrigin = flight.getDestination();
        ArrayList<LinkedList<Flight>> connectingFlights = getAllPossibleItineraries(nextDepart,
            nextOrigin, destination);
        // if the connecting flights is empty, create a new linked list with
        // this flight as the head
        // add it to res
        if (connectingFlights.isEmpty()) {
          LinkedList<Flight> connectedFlights = new LinkedList<>();
          connectedFlights.add(flight);
          res.add(connectedFlights);
        } else {
          // add this flight to each head of the flight and add each connected
          // flights to res
          for (int j = 0; j < connectingFlights.size(); j++) {
            LinkedList<Flight> connectedFlights = connectingFlights.get(j);
            connectedFlights.addFirst(flight);
            res.add(connectedFlights);
          }
        }
      }
      return res;
    }
  }

  private ArrayList<Flight> getsFlightsWithOriginAndDepartDate(Date departDate, String origin) {
    ArrayList<Flight> res = new ArrayList<>();
    Date flightDepartBefore = new Date(Driver.MAX_LAYOVER * 1000 + departDate.getTime());
    // loop through every flights in the array of flights.
    if (flights != null) {
      for (int i = 0; i < flights.length; i++) {
        // get flight i and its depart date
        Flight flight = flights[i];
        Date flightDepartDate = flight.getDepartureDate();
        // check if flight i is in range of departDate, if so add it to res
        boolean checkOrigin = flight.getOrigin().equals(origin);
        boolean checkDepartDate = (flightDepartDate.after(departDate)
            && flightDepartDate.before(flightDepartBefore)) || flightDepartDate.equals(departDate);
        if (checkOrigin && checkDepartDate) {
          res.add(flight);
        }
      }
    }
    return res;
  }

  /**
   * Sorts itinerary by TravelTime.
   * 
   * @param itineraries
   *          needs to be List of Flights
   */
  public void sortItinerariesByTravelTime(List<Itinerary> itineraries) {
    java.util.Collections.sort(itineraries, new CompareItinerairesByTravelTime());
  }

  /**
   * Sorts itinerary by Cost.
   * 
   * @param itineraries
   *          needs to be List of Flights
   */
  public void sortItinerariesByCost(List<Itinerary> itineraries) {
    java.util.Collections.sort(itineraries, new CompareItinerairesByCost());
  }

  /**
   * Searches for all available <code>Flight</code> given a specific
   * departureDate, an origin, and destination.
   * 
   * @param departureDate
   *          date of departure of the requested <code>Flight</code>.
   * @param origin
   *          place of origin of the requested <code>Flight</code>.
   * @param destination
   *          destination of the requested <code>Flight</code>.
   * @return A list of flights matching the given input with all information of
   *         those flights.
   * @throws StorageException
   *           <code>StorageException</code> thrown if file was not loaded.
   */
  public List<Flight> searchFlight(String departureDate, String origin, String destination)
      throws StorageException {
    // Initialize empty list of flights.
    List<Flight> flightList = new ArrayList<Flight>();
    DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
    Date dep;
    try {
      dep = fm.parse(departureDate);
      // Loops through all flights.
      for (int i = 0; i < this.flights.length; i++) {
        // Check if input matches flight information.
        if (fm.parse(fm.format(this.flights[i].getDepartureDate())).equals(dep)
            && this.flights[i].getOrigin().equals(origin)
            && this.flights[i].getDestination().equals(destination)) {
          // Creates a temporary String to add to the list of flights with
          // required input.
          Flight tempFlight = this.flights[i];
          flightList.add(tempFlight);
        }
      }
      return flightList;
    } catch (ParseException ex) {
      // Returns null if exception.
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * Searches for the specified client with a given e-mail.
   * 
   * @param email
   *          the e-mail of the client to search for.
   * @return the client with all their information in the system.
   * @throws StorageException
   *           <code>StorageException</code> thrown if a user does not exist.
   */
  public String searchClient(String email) throws StorageException {
    // Temporary null assignment for client. Later used for Exception.
    Client cl = null;
    // Initializes a list of client retrieved from <code>Storage</code>.
    Client[] clients = this.data.getClients();
    // Loops through all clients.
    for (int i = 0; i < clients.length; i++) {
      // If e-mails are equals, cl takes on that <code>Client</code> object.
      if (clients[i].getEmail().equals(email)) {
        cl = clients[i];
      }
    }
    // If after all operations cl is still null, new exception is thrown.
    if (cl == null) {
      throw new StorageException("A user with that e-mail does not exist.");
    }
    return (cl.getLastName() + ";" + cl.getFirstName() + ";" + cl.getEmail() + ";"
        + cl.getPersonalAddress() + ";" + cl.getCreditCardNumber() + ";"
        + new SimpleDateFormat("yyyy-MM-dd").format(cl.getExpiry()));

  }

  /**
   * Add <code>Flight</code> to the list of possible flights with a given text
   * file. Only administrators can do this.
   *
   * @param stream
   *          The text file location of the <code>Flight</code>(s) to be added.
   * @throws StorageException
   *           <code>StorageException</code> thrown from <code>Storage</code> if
   *           no path.
   */
  public void addFlight(InputStream stream) throws StorageException {
    this.data.loadFlights(stream);
    this.flights = this.data.getFlights();
  }

  public void addFlight(String path) throws StorageException {
    this.data.loadFlights(path);
    this.flights = this.data.getFlights();
  }

  /**
   * Add <code>Flight</code> to the list of possible flights with a given text
   * file. Only administrators can do this.
   *
   * @param flightNumber
   *          flight number of the <code>Flight</code> to be added
   * @param airline
   *          the airline of the <code>Flight</code> to be added
   * @param cost
   *          the cost of the <code>Flight</code> to be added
   * @param departDate
   *          the departure date of the <code>Flight</code> to be added
   * @param arrivalDate
   *          the arrival date of the <code>Flight</code> to be added
   * @param origin
   *          the origin of the <code>Flight</code> to be added
   * @param destination
   *          the destination of the <code>Flight</code> to be added
   * @param numSeats
   *          the number of seats of the <code>Flight</code> to be added
   * @throws StorageException
   *           <code>StorageException</code> thrown from <code>Storage</code> if
   *           necessary
   */
  public void addFlight(String flightNumber, String airline, String cost, String departDate,
      String arrivalDate, String origin, String destination, String numSeats)
      throws StorageException {
    try {
      this.data.addOrUpdateFlight(new Flight(flightNumber, airline, Double.valueOf(cost),
          departDate, arrivalDate, origin, destination, Integer.parseInt(numSeats)));
      this.flights = this.data.getFlights();
    } catch (ParseException exception) {
      throw new StorageException("Cannot parse flights:" + exception.getMessage());
    }
  }

  /**
   * Add <code>Client</code> to the list of clients with a given text file. Only
   * administrators can do this.
   *
   * @param stream
   *          The text file location of the <code>Client</code>(s) to be added.
   * @throws StorageException
   *           <code>StorageException</code> thrown from <code>Storage</code> if
   *           no path.
   */
  public void addClient(InputStream stream) throws StorageException {
    this.data.loadClients(stream);
  }

  public void addClient(String path) throws StorageException {
    this.data.loadClients(path);
  }

  /**
   * Add <code>Client</code> to the list of clients with the given parameters.
   * Only administrators can do this.
   * 
   * @param email
   *          email of the <code>Client</code> to be added
   * @param personalAddress
   *          personal address of the <code>Client</code> to be added
   * @param creditCardNumber
   *          credit card number of the <code>Client</code> to be added
   * @param expiryString
   *          expiration date of the <code>Client</code> to be added
   * @param firstName
   *          first name of the <code>Client</code> to be added
   * @param lastName
   *          last name of the <code>Client</code> to be added
   */
  public void addClient(String email, String personalAddress, String creditCardNumber,
      String expiryString, String firstName, String lastName) {
    this.data.addOrUpdateClient(
        new Client(email, personalAddress, creditCardNumber, expiryString, firstName, lastName));
  }

  public void addOrUpdateClient(Client client) {
    this.data.addOrUpdateClient(client);
  }

  /**
   * Returns the <code>Client</code> given a specific email.
   * 
   * @param email
   *          email of the <code>Client</code> being searched
   * @return the <code>Client</code> corresponding to the email
   */
  public Client getClient(String email) {
    return this.data.getClient(email);
  }

  /**
   * Returns a boolean on whether the user and password match that stored in the
   * database.
   * 
   * @param user
   *          the user logging in
   * @param pass
   *          the password of the user logging in
   * @return true if the credentials match, and false otherwise
   */
  public boolean login(String user, String pass) {
    String correctPass = this.data.getPassword(user);
    if (correctPass != null) {
      return correctPass.equals(pass);
    }
    return false;
  }

  /**
   * Sets a password of the specified user. Throws StorageException if any
   * issues, such as user not existing is encountered.
   * 
   * @param user
   *          user having the password set
   * @param pass
   *          password to set
   * @throws StorageException
   *           Throws StorageException if an issue occurs.
   */
  public void setUserPassword(String user, String pass) throws StorageException {
    this.data.setPassword(user, pass);
  }

}
