package cs.b07.cscb07courseproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import controller.Backend;
import travel.*;

/** SearchItineray Activity class for application.
 * @author salmansharif
 *
 */
public class SearchItineraries extends AppCompatActivity {

    private Backend backend;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_itineraries);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        backend = new Backend(getApplicationContext());
        backend.setUser(user);
    }

    /** Searches for itinerary and launches the ItinearyResults class.
     * @param view of the view being used.
     */
    public void searchItineraries(View view) {
        Intent intent = new Intent(this, ItineraryResults.class);

        // TODO: search itinerary here and put it into the intent

        Date departureDate;
        String origin = ((EditText)findViewById(R.id.itinerarysearch_origin)).getText().toString();
        String destination = ((EditText)findViewById(R.id.itinerarysearch_destination)).getText().toString();
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        String dep = ((EditText)findViewById(R.id.itinerarysearch_departuredate)).getText().toString();
        try {
            departureDate = fm.parse(dep);
            ArrayList<travel.Itinerary> itineraries = (ArrayList)backend.getItineraries(departureDate, origin, destination);
            Log.d("SEARCHITINER:", String.valueOf(itineraries.size()));
            intent.putExtra("itineraries",  itineraries);
            intent.putExtra("user", user);
            startActivity(intent);
        } catch (ParseException e) {
            e.printStackTrace();
            AlertDialog dialog = new AlertDialog.Builder(SearchItineraries.this).create();
            dialog.setTitle("Invalid Date");
            dialog.setMessage("Date must be in the format of yyyy-MM-dd");
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
