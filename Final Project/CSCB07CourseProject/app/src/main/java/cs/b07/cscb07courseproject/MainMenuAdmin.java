package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import controller.Backend;
import travel.User;

/**MainMenuAdmin Activity for this application.
 * @author salmansharif
 *
 */
public class MainMenuAdmin extends AppCompatActivity {

    private Backend backend;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_admin);
        Intent intent = getIntent();
        user = (User) intent.getExtras().get("user");
        backend = new Backend(getApplicationContext());
    }

    /** Launches SearchFlights activity.
     * @param view of the view being used
     */
    public void searchFlights(View view) {
        Intent intent = new Intent(this, SearchFlights.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /** Launches SearchItinearies activity.
     * @param view of the view being used
     */
    public void searchItineraies(View view) {
        Intent intent = new Intent(this, SearchItineraries.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /** Launches SearchClients activity.
     * @param view of the view being used
     */
    public void searchClient(View view) {
        Intent intent = new Intent(this, SearchClients.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /** Launches AddFlights activity.
     * @param view of the view being used
     */
    public void addFlights(View view) {
        Intent intent = new Intent(this, AddFlights.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /** Launches AddClient activity.
     * @param view of the view being used
     */
    public void addClients(View view) {
        Intent intent = new Intent(this, AddClient.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /** Signs out the admin and goes to login activity.
     * @param view of the view being used
     */
    public void signout(View view) {
        //// TODO: sign out admin here
        startActivity(new Intent(this, LogIn.class));
    }

}
