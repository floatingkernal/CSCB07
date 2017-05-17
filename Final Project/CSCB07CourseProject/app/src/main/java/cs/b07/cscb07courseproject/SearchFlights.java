package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import controller.Backend;
import controller.StorageException;
import travel.User;

/** SearchFlights Activity class for application.
 * @author Salman
 *
 */
public class SearchFlights extends AppCompatActivity {

    private Backend backend;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flights);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        backend = new Backend(getApplicationContext());
    }

    /** Searches flights and launches FlightResults activity.
     * @param view of the view being used
     */
    public void searchFlights(View view) {
        Intent intent = new Intent(this, FlightResults.class);
        // TODO: search flights here and put into intent
        EditText origintext = (EditText) findViewById(R.id.flightsearch_origin);
        EditText destinationtxt = (EditText) findViewById(R.id.flightsearch_destination);
        EditText departDatetext = (EditText) findViewById(R.id.flightsearch_departuredate);
        String origin = origintext.getText().toString();
        String destination = destinationtxt.getText().toString();
        String departDate = departDatetext.getText() .toString();

        try {
            ArrayList<travel.Flight> flights = (ArrayList)backend.searchFlight(departDate, origin, destination);
            intent.putExtra("flights",flights);
            intent.putExtra("user", user);
            startActivity(intent);
        } catch (StorageException e) {
            e.printStackTrace();
        }



    }
}
