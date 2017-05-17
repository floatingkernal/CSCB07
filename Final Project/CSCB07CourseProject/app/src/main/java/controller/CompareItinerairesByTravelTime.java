package controller;

import travel.Itinerary;

import java.util.Comparator;


/**
 * Class to compare by travel time.
 * 
 * @author salmansharif
 *
 */
public class CompareItinerairesByTravelTime implements Comparator<Itinerary> {

  @Override
  public int compare(Itinerary it, Itinerary it2) {
    if (it == null || it2 == null) {
      throw new NullPointerException("Failed to compare " + it.toString() + " with "
            + it2.toString());
    } else if (!it.getClass().equals(it2.getClass())) {
      throw new ClassCastException(
          "Possible Class loader issue. Failed to compare " + it.toString() + " with "
            + it2.toString());
    }
    return compareTo(it, it2);
  }

  private int compareTo(Itinerary it, Itinerary it2) {
    String travel = it.getTravelTime();
    String travel2 = it2.getTravelTime();
    int compare = travel.compareTo(travel2);
    return compare;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof CompareItinerairesByTravelTime;
  }
}