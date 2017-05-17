package cs.b07.cscb07courseproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import controller.Backend;
import controller.Constants;
import travel.Administrator;

/** Login Activity for the application.
 * @author Cameron
 */
public class LogIn extends AppCompatActivity {

    private Backend backend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        backend = new Backend(getApplicationContext());
    }

    /** Logs in the user and launches main menu for admin or client.
     * @param view of the view being used
     */
    public void login(View view) {
        // Get user input
        String username = ((EditText) findViewById(R.id.login_username)).getText().toString();
        String password = ((EditText) findViewById(R.id.login_password)).getText().toString();
        // Check if admin
        if ((Constants.ADMINEMAIL.equals(username) && Constants.ADMINPASS.equals(password))) {
            // Login as admin, go to admin menu and instatiate Administrator User object
            Intent intent = new Intent(this, MainMenuAdmin.class);
            Administrator admin = new Administrator(Constants.ADMINEMAIL);
            backend.setUser(admin);
            intent.putExtra("user", admin);
            startActivity(intent);
        } else {
            if (backend.login(username, password)) {
                // Log the user in, get the user's associated Client object and pass it
                Intent intent = new Intent(this, MainMenuClient.class);
                intent.putExtra("user", backend.getClient(username));
                startActivity(intent);
            } else {
                // Alert login failed
                AlertDialog dialog = new AlertDialog.Builder(LogIn.this).create();
                dialog.setTitle("Invalid Login");
                dialog.setMessage("Incorrect username or password");
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

    public void loginRegister(View view) {
        // Navigate to register
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
