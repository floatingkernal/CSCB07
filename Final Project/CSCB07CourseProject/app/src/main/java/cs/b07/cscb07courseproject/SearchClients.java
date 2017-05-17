package cs.b07.cscb07courseproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import controller.Backend;
import travel.Client;
import travel.User;

/**SearchClients Activity class for application.
 * @author Cameron
 */
public class SearchClients extends AppCompatActivity {

    private Backend backend;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_clients);
        Intent intent = getIntent();
        // Get the logged-in user
        user = (travel.User) getIntent().getExtras().get("user");
        // Instantiate the backend and set the User
        backend = new Backend(getApplicationContext());
        backend.setUser((travel.User)intent.getExtras().get("user"));
    }

    /** Searches Client and launches ClientInfo activity.
     * @param view of the view being used.
     */
    public void searchClient(View view) {
        // get the inputted user email
        String email = ((EditText)findViewById(R.id.clientsearch_email)).getText().toString();
        Client desiredClient;
        // Check that there is a client associated with that email
        if ((desiredClient = backend.getClient(email)) != null) {
            // Get the client and pass it to be displayed
            Intent intent = new Intent(this, ClientInfo.class);
            // pass logged-in user
            intent.putExtra("user", user);
            intent.putExtra("desiredClient", desiredClient);
            startActivity(intent);
        } else {
            // Alert user that the email suplied does not belong to a user
            AlertDialog dialog = new AlertDialog.Builder(SearchClients.this).create();
            dialog.setTitle("Invalid user email");
            dialog.setMessage("User with that email does not exist");
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
