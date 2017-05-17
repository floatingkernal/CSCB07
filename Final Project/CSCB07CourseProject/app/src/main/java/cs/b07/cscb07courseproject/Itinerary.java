package cs.b07.cscb07courseproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controller.Backend;
import travel.Administrator;
import travel.Client;
import travel.Flight;
import travel.User;

/**
 * Itinerary Activity class for application.
 * @author Rene
 *
 */
public class Itinerary extends AppCompatActivity {

    private Backend backend;
    private User user;
    private List<Flight> flights;
    private travel.Itinerary itinerary;
    private Client[] clients;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        backend = new Backend(getApplicationContext());
        backend.setUser(user);


        // TODO: put list of flights in to flights variable
        this.itinerary = (travel.Itinerary) intent.getExtras().get("itinerary");
        flights = Arrays.asList(itinerary.getFlights());
        TextView itineraryInfo = (TextView) findViewById(R.id.itinerary_info);
        String txt = String.format("Traveling from %s to %s \n" +
                        "Travel time: %s \n" +
                        "Total Cost: $%.2f \n" +
                        "Departure Date and time: %s \n" +
                        "Arrival Date and time: %s",
                itinerary.getOrigin(), itinerary.getDestination(), itinerary.getTravelTime(),
                itinerary.getTotalCost(), new SimpleDateFormat("yyyy-MM-dd kk:mm")
                        .format(itinerary.getFlights()[0].getDepartureDate()),
                new SimpleDateFormat("yyyy-MM-dd kk:mm")
                        .format(itinerary.getFlights()[itinerary.getFlights().length -1].getArrivalDate()));
        itineraryInfo.setText(txt);
        ArrayAdapter<Flight> adapter = new Itinerary.CustomArrayAdapter();
        ListView listview = (ListView) findViewById(R.id.itinerary_listview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Flight flight = (Flight) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(view.getContext(), cs.b07.cscb07courseproject.Flight.class);
                // TODO: trasfer this flight to the next activity
                intent.putExtra("flight", flight);
                intent.putExtra("user", user);
                startActivity(intent);

            }
        });
        setDropdown();
    }

    private void setDropdown() {
        Spinner dropdown = (Spinner)findViewById(R.id.itinerary_spinner);
        this.clients = backend.getAllClients();
        String[] items = stringifyClients(clients);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter2);
        //dropdown.setVisibility(View.INVISIBLE);
        if (!(user instanceof Administrator)) {
            dropdown.setVisibility(View.INVISIBLE);
            //dropdown.setOnItemClickListener(this);
        }
    }

    /**
     * Books the itinerary for a client.
     *
     * @param view of the view being used
     */
    public void bookItinerary(View view) {
        // TODO: book this itinerary here
        if (user instanceof Client) {
            backend.bookItinerary(itinerary);
            AlertDialog dialog = new AlertDialog.Builder(Itinerary.this).create();
            dialog.setTitle("Itinerary booked");
            dialog.setMessage("Itinerary has been booked for: " + user.getEmail());
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();
        } else {
            Spinner spinner = (Spinner) findViewById(R.id.itinerary_spinner);
            client = backend.getClient(spinner.getSelectedItem().toString());
            itinerary.setClient(client);
            backend.bookItinerary(itinerary);
            AlertDialog dialog = new AlertDialog.Builder(Itinerary.this).create();
            dialog.setTitle("Itinerary booked");
            dialog.setMessage("Itinerary has been booked for: " + client.getEmail());
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();
        }
    }

    private String[] stringifyClients(Client[] clients) {
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < clients.length; i++) {
            res.add(clients[i].getEmail());
        }
        String[] temp = new String[res.size()];
        return res.toArray(temp);
    }


    private class CustomArrayAdapter extends ArrayAdapter<travel.Flight> {

        public CustomArrayAdapter() {
            super(Itinerary.this, R.layout.activity_flight_list_view, flights);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.activity_flight_list_view, parent, false);
            }

            // Find the flight
            travel.Flight currFlight = flights.get(position);

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
