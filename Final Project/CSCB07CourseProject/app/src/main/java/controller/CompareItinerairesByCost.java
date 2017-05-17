package controller;

import travel.Itinerary;

import java.util.Comparator;


/**
 * Class to compare costs.
 * 
 * @author salmansharif
 *
 */
public class CompareItinerairesByCost implements Comparator<Itinerary> {
  
  @Override
  public int compare(Itinerary it, Itinerary it2) {
    if (it == null || it2 == null) {
      throw new NullPointerException("Failed to compare " + it.toString() + " with "
          + it2.toString());
    } else if (!it.getClass().equals(it2.getClass())) {
      throw new ClassCastException("Possible Class loader issue. Failed to compare " + it.toString()
          + " with " + it2.toString());
    }
    return compareTo(it, it2);
  }
  
  private int compareTo(Itinerary it, Itinerary it2) {
    double cost = it.getTotalCost();
    double cost2 = it2.getTotalCost();
    if (cost < cost2) {
      return -1;
    } else if (cost > cost2) {
      return 1;
    } else {
      return 0;
    }
  }
  
  @Override
  public boolean equals(Object obj) {
    return obj instanceof CompareItinerairesByCost;
  }
}
