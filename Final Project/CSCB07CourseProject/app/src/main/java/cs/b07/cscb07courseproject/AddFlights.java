package cs.b07.cscb07courseproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.FileNotFoundException;

import controller.Backend;
import controller.StorageException;

/**
 * An activity to add flights to the database
 * @author Cameron
 */
public class AddFlights extends AppCompatActivity {

    private Backend backend;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flights);
        // Instantiate the backend with the application context
        backend = new Backend(getApplicationContext());
        Intent intent = getIntent();
        // Set the user, passed through the Intent
        backend.setUser((travel.User)intent.getExtras().get("user"));
        dialog = new AlertDialog.Builder(AddFlights.this).create();
    }
    public void addNewFlight(View view) {
        // Get the input values
        String flightNumber = ((EditText)findViewById(R.id.addflight_flightnum)).getText().toString();
        String airline = ((EditText)findViewById(R.id.addflight_airline)).getText().toString();
        String departureDate = ((EditText)findViewById(R.id.addflight_departdate)).getText().toString();
        String arrivalDate = ((EditText)findViewById(R.id.addflight_arivaldate)).getText().toString();
        String cost = ((EditText)findViewById(R.id.addflight_cost)).getText().toString();
        String origin = ((EditText)findViewById(R.id.addflight_origin)).getText().toString();
        String destination = ((EditText)findViewById(R.id.addflight_destination)).getText().toString();
        String seats = ((EditText)findViewById(R.id.addflight_numseats)).getText().toString();
        // Check none of the input fields are empty
        if (!(flightNumber.isEmpty() || airline.isEmpty() || departureDate.isEmpty()
                || cost.isEmpty() || origin.isEmpty() || destination.isEmpty() || seats.isEmpty())) {
            try {
                // Add the flight to the database
                backend.addFlight(flightNumber, airline, cost, departureDate, arrivalDate, origin,
                        destination, seats);
                // Alert the user that the flight has been added
                AlertDialog dialog = new AlertDialog.Builder(AddFlights.this).create();
                dialog.setTitle("Added Flight");
                dialog.setMessage("");
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            } catch (StorageException exception) {
                // Alert the user that the flight could not be added
                AlertDialog dialog = new AlertDialog.Builder(AddFlights.this).create();
                dialog.setTitle("Flight could not be added");
                dialog.setMessage("Flight could not be added: " + exception.getMessage());
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
    public final int FILE_SELECT_CODE = 0;
    public void addFlightFromFile(View view) {
        // Launch the android File Explorer to allow the user to select a
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Set the type to all files
        intent.setType("*/*");
        Intent finalIntent = Intent.createChooser(intent, "Select file with flight info");
        // Launch android File Explorer
        startActivityForResult(finalIntent, FILE_SELECT_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    try {
                        // Add flight to the database
                        backend.addFlight(getContentResolver().openInputStream(uri));
                        // Alert the user
                        dialog.setTitle("Success");
                        dialog.setMessage("Flights added successfully");
                        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog.show();
                    } catch (StorageException exception) {
                        // Alert the user that the file cannot be opened or formatted
                        dialog.setTitle("Could not open file");
                        dialog.setMessage("File either could not be opened or was not formatted" +
                                " correctly:" + exception.getMessage());
                        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog.show();
                    } catch (FileNotFoundException exception) {
                        // Alert the user that the file cannot be found
                        dialog.setTitle("Could not find file");
                        dialog.setMessage("File either could not be opened or was not formatted" +
                                "correctly:" + exception.getMessage());
                        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog.show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
