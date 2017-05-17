package cs.b07.cscb07courseproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;

import controller.Backend;
import controller.StorageException;
import travel.Administrator;
import travel.User;

/**
 * An activity to view and, if an admin is logged in, update flight info
 * @author Cameron
 */
public class Flight extends AppCompatActivity {

    private Backend backend;
    private User user;
    private travel.Flight flight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);
        Intent intent = getIntent();
        // Get the current logged-in user from the intent
        user = (User) intent.getExtras().get("user");
        backend = new Backend(getApplicationContext());
        backend.setUser(user);
        // Prefill user info into EditTexts in the activity
        flight = (travel.Flight) getIntent().getExtras().get("flight");
        ((EditText)findViewById(R.id.flight_airline)).setText(flight.getAirline());
        ((EditText)findViewById(R.id.flight_arivaldate)).setText(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(flight.getArrivalDate()));
        ((EditText)findViewById(R.id.flight_cost)).setText("" + flight.getCost());
        ((EditText)findViewById(R.id.flight_departdate)).setText(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(flight.getDepartureDate()));
        ((EditText)findViewById(R.id.flight_destination)).setText(flight.getDestination());
        ((EditText)findViewById(R.id.flight_num)).setText(flight.getFlightNumber());
        ((EditText)findViewById(R.id.flight_origin)).setText(flight.getOrigin());
        ((EditText)findViewById(R.id.flight_numseats)).setText(String.valueOf(flight.getNumSeats()));
        showUpdateFlightBtn();
    }
    private void showUpdateFlightBtn() {
        Button button = (Button) findViewById(R.id.flight_updatebtn);
        if (!(user instanceof Administrator)) {
            // If not an admin, do not display edit button
            button.setVisibility(View.INVISIBLE);
            // Do not allow input from fields
            ((EditText)findViewById(R.id.flight_airline)).setFocusable(false);
            ((EditText)findViewById(R.id.flight_arivaldate)).setFocusable(false);
            ((EditText)findViewById(R.id.flight_cost)).setFocusable(false);
            ((EditText)findViewById(R.id.flight_departdate)).setFocusable(false);
            ((EditText)findViewById(R.id.flight_destination)).setFocusable(false);
            ((EditText)findViewById(R.id.flight_num)).setBackgroundColor(00000);
            ((EditText)findViewById(R.id.flight_origin)).setFocusable(false);
            ((EditText)findViewById(R.id.flight_numseats)).setFocusable(false);
        }
    }

    public void updateFlight(View view) {
        // Get user's input
        String flightNumber = ((EditText)findViewById(R.id.flight_num)).getText().toString();
        String airline = ((EditText)findViewById(R.id.flight_airline)).getText().toString();
        String departureDate = ((EditText)findViewById(R.id.flight_departdate)).getText().toString();
        String arrivalDate = ((EditText)findViewById(R.id.flight_arivaldate)).getText().toString();
        String cost = ((EditText)findViewById(R.id.flight_cost)).getText().toString();
        String origin = ((EditText)findViewById(R.id.flight_origin)).getText().toString();
        String destination = ((EditText)findViewById(R.id.flight_destination)).getText().toString();
        String seats = ((EditText)findViewById(R.id.flight_numseats)).getText().toString();
        // Varify none of the inputs are empty
        if (!(flightNumber.isEmpty() || airline.isEmpty() || departureDate.isEmpty()
                || cost.isEmpty() || origin.isEmpty() || destination.isEmpty() || seats.isEmpty())) {
            // Attempt to add the flight to the database, if success, dispaly a dialog to tell
            // the user. then navigate to searchflight activity
            try {
                backend.addFlight(flightNumber, airline, cost, departureDate, arrivalDate, origin,
                        destination, seats);
                AlertDialog dialog = new AlertDialog.Builder(Flight.this).create();
                dialog.setTitle("Flight Updated");
                dialog.setMessage("Flight updated successfully");
                final Intent intent = new Intent(this, SearchFlights.class);
                intent.putExtra("user", this.user);
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(intent);
                            }
                        });
                dialog.show();
            // If an exeception occurs, warn the user with the issue in an AlertDialog
            } catch (StorageException exception) {
                AlertDialog dialog = new AlertDialog.Builder(Flight.this).create();
                dialog.setTitle("Flight Update Failed");
                dialog.setMessage("Flight could not be updated: " +exception.getMessage());
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }
        }
    }
}
