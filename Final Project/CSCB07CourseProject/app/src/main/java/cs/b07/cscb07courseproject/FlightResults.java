package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.net.Uri;
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

import controller.Backend;
import travel.Flight;
import travel.User;

public class FlightResults extends AppCompatActivity {

    private Backend backend;
    private User user;

    private ArrayList<Flight> flights;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_results);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        backend = new Backend(getApplicationContext());

        // put search flight results into flights
        this.flights = (ArrayList<Flight>) intent.getExtras().get("flights");
        if (this.flights == null) {
            this.flights = new ArrayList<Flight>();
        } else {
        }

        ArrayAdapter<Flight> adapter = new CustomArrayAdapter();
        ListView listview = (ListView) findViewById(R.id.flightresults_listview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Flight flight = (Flight) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(view.getContext(), cs.b07.cscb07courseproject.Flight.class);
                // TODO: transfer this flight to the next activity
                intent.putExtra("flight", flight);
                intent.putExtra("user", user);
                startActivity(intent);

            }
        });
    }


    private class CustomArrayAdapter extends ArrayAdapter<Flight> {

        public CustomArrayAdapter() {
            super(FlightResults.this, R.layout.activity_flight_list_view, flights);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.activity_flight_list_view, parent, false);
            }

            // Find the flight
            Flight currFlight = flights.get(position);

            // Fill the view
            TextView Airline = (TextView) itemView.findViewById(R.id.flightlistview_airline);
            Airline.setText(currFlight.getAirline());

            TextView orgToDes = (TextView) itemView.findViewById(R.id.
                    flightlistview_origindestination);
            orgToDes.setText(currFlight.getOrigin() + " To " + currFlight.getDestination());

            TextView flightNum = (TextView) itemView.findViewById(R.id.flightlistview_flightnum);
            flightNum.setText(currFlight.getFlightNumber());

            TextView cost = (TextView) itemView.findViewById(R.id.flightlistview_cost);
            cost.setText("$" + currFlight.getCost());

            TextView departDate = (TextView) itemView.findViewById(R.id.flightlistview_departdate);
            departDate.setText(new SimpleDateFormat("yyyy-MM-dd kk:mm")
                    .format(currFlight.getDepartureDate()));

            return itemView;
        }
    }
}
