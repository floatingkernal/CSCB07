package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import controller.Backend;
import travel.Itinerary;
import travel.User;

public class ItineraryResults extends AppCompatActivity {

    private Backend backend;
    private List<Itinerary> itineraries;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_results);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        backend = new Backend(getApplicationContext());
        // put search itinerary results into itineraries

        itineraries = (ArrayList<Itinerary>) intent.getExtras().get("itineraries");
        fillListView();

    }

    public void sortByTime(View view) {
        backend.sortItinerariesByTravelTime(itineraries);
        fillListView();
    }

    public void sortByCost(View view) {
        backend.sortItinerariesByCost(itineraries);
        fillListView();
    }

    private void fillListView() {
        ArrayAdapter<Itinerary> adapter = new CustomArrayAdapter();
        ListView listview = (ListView) findViewById(R.id.itineraryresults_listview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                travel.Itinerary itinerary = (travel.Itinerary) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(view.getContext(), cs.b07.cscb07courseproject.Itinerary.class);
                // TODO: transfer this Itinerary to the next activity
                intent.putExtra("itinerary", itinerary);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    private class CustomArrayAdapter extends ArrayAdapter<Itinerary> {

        public CustomArrayAdapter() {
            super(ItineraryResults.this, R.layout.activity_itinerary_list_view, itineraries);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.activity_itinerary_list_view, parent, false);
            }

            // Find the flight
            Itinerary currItinerary = itineraries.get(position);

            // Fill the view
            TextView traveltime = (TextView) itemView.findViewById(R.id.itinerarylistview_traveltime);
            traveltime.setText("Travel Time: " + currItinerary.sumTravel());

            TextView orgToDes = (TextView) itemView.findViewById(R.id.itinerarylistview_orgtodes);
            orgToDes.setText(currItinerary.getOrigin() + " To " + currItinerary.getDestination());

            TextView flightNum = (TextView) itemView.findViewById(R.id.itinerarylistview_numflights);
            flightNum.setText("Number of Flights: " + currItinerary.getFlights().length);

            TextView cost = (TextView) itemView.findViewById(R.id.itinerarylistview_cost);
            cost.setText("$" + currItinerary.getTotalCost());

            TextView departDate = (TextView) itemView.findViewById(R.id.itinerarylistview_departdate);
            departDate.setText(new SimpleDateFormat("yyyy-MM-dd kk:mm")
                    .format(currItinerary.getFlights()[0].getDepartureDate()));

            return  itemView;
        }
    }
}