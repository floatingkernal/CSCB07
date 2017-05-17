package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import controller.Backend;
import travel.User;

/**MainMenuClient Activity class for application.
 * @author salmansharif
 *
 */
public class MainMenuClient extends AppCompatActivity {

    private Backend backend;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_client);
        Intent intent = getIntent();
        user = (User) intent.getExtras().get("user");
        backend = new Backend(getApplicationContext());
    }

    /** Signs out the client and goes to the login activity.
     * @param view of the view being used
     */
    public void signout(View view) {
        // TODO: sign out client here
        startActivity(new Intent(this, LogIn.class));
    }

    /** Launches ClientInfo activity.
     * @param view of the view being used
     */
    public void getInfo(View view) {
        Intent intent = new Intent(this, ClientInfo.class);
        intent.putExtra("user", user);
        intent.putExtra("desiredClient", user);
        startActivity(intent);
    }

    /** Launches SearchItineries activity.
     * @param view of the view being used
     */
    public void searchItineraries(View view) {
        Intent intent = new Intent(this, SearchItineraries.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /** Launches SearchFlights activity.
     * @param viewof the view being used
     */
    public void searchFlights(View view) {
        Intent intent = new Intent(this, SearchFlights.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /**Launches ViewBookedItineraries activity.
     * @param view of the view being used
     */
    public void viewBookedItineraries(View view) {
        Intent intent = new Intent(this, ViewBookedItineraries.class);
        intent.putExtra("user", user);
        intent.putExtra("client", user);
        startActivity(intent);
    }
}
