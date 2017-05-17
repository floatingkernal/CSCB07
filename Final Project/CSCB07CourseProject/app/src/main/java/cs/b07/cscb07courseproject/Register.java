package cs.b07.cscb07courseproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import controller.Backend;
import controller.StorageException;

/**
 * Activity to register an account
 * @author Cameron
 */
public class Register extends AppCompatActivity {

    private Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        backend = new Backend(getApplicationContext());
    }

    /** Registers new client and launches MainMenuClient activity.
     * @param view of the view being used
     */
    public void register(View view) {
        // get user's input
        String email = ((EditText)findViewById(R.id.register_email)).getText().toString();
        String personalAddress = ((EditText)findViewById(R.id.register_address)).getText().toString();
        String creditCardNumber = ((EditText)findViewById(R.id.register_creditcard)).getText().toString();
        String expiryString = ((EditText)findViewById(R.id.register_expirydate)).getText().toString();
        String firstName = ((EditText)findViewById(R.id.register_firstname)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.register_lastname)).getText().toString();
        String password = ((EditText)findViewById(R.id.register_password)).getText().toString();
        String confirmPassword = ((EditText)findViewById(R.id.regester_confirm_pass)).getText().toString();
        if (password.equals(confirmPassword)) {
            try {
                // Add client to storage
                backend.addClient(email, personalAddress, creditCardNumber, expiryString, firstName, lastName);
                try {
                    // Set the client's password
                    backend.setUserPassword(email, password);
                    Intent intent = new Intent(this, MainMenuClient.class);
                    //pass the new user
                    intent.putExtra("user", backend.getClient(email));
                    startActivity(intent);
                } catch (StorageException exception) {
                    // Alert the inputted password is invalid
                    AlertDialog dialog = new AlertDialog.Builder(Register.this).create();
                    dialog.setTitle("Invalid Password");
                    dialog.setMessage("You're new password is invalid. Please try another.");
                    dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.show();
                }
            } catch (Exception e) {
                // Alert the user that the inputted date is improperly formatted
                AlertDialog dialog = new AlertDialog.Builder(Register.this).create();
                dialog.setTitle("Invalid Date");
                dialog.setMessage("Date is not formatted correctly");
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }

        } else {
            // Alert the user that te password and the confirmed password are not equal
            AlertDialog dialog = new AlertDialog.Builder(Register.this).create();
            dialog.setTitle("Password Mismatch");
            dialog.setMessage("Passwords do not match!");
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