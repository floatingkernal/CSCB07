package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import travel.*;
import travel.Itinerary;

/** ViewBookedItineraries Activity class for application.
 * @author Salman
 *
 */
public class ViewBookedItineraries extends AppCompatActivity {

    private ArrayList<Itinerary> itineraries;
    private Backend backend;
    private User user;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booked_itineraries);
        Intent intent = getIntent();
        user = (travel.User)intent.getExtras().get("user");
        backend = new Backend(getApplicationContext());
        backend.setUser((travel.User)intent.getExtras().get("user"));
        client = (Client)intent.getExtras().get("client");
        itineraries = convListToArrayList(backend.getBookedItineraries(client));
        setup();
    }
    private ArrayList<Itinerary> convListToArrayList(List<Itinerary> list) {
        Itinerary[] array = (Itinerary[])(list.toArray(new Itinerary[list.size()]));
        ArrayList<Itinerary> res = new ArrayList<>();
        if (!(list.isEmpty())) {
            for (int i = 0; i < list.size(); i++) {
                res.add(list.get(i));
            }
        }
        return res;
    }
    
    protected void setup() {
        ArrayAdapter<travel.Itinerary> adapter = new ViewBookedItineraries.CustomArrayAdapter();
        ListView listview = ((ListView)findViewById(R.id.bookeditinerary_listview));
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

    private class CustomArrayAdapter extends ArrayAdapter<travel.Itinerary> {
	
        /**
         * Constructor for array adapter.
         */
        public CustomArrayAdapter() {
            super(ViewBookedItineraries.this, R.layout.activity_itinerary_list_view, itineraries);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.activity_itinerary_list_view, parent, false);
            }

            // Find the flight
            travel.Itinerary currItinerary = itineraries.get(position);

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