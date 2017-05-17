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
import travel.User;

/**
 * Activity to add clients
 * @author Cameron
 */
public class AddClient extends AppCompatActivity {

    private Backend backend;
    private User user;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        Intent intent = getIntent();
        // Create backend instance with the application context
        backend = new Backend(getApplicationContext());
        // Set the user, passed through the Intent
        user = (User) intent.getExtras().get("user");
        backend.setUser(user);
        dialog = new AlertDialog.Builder(AddClient.this).create();
    }

    public void addClient(View view) {
        // Get all input values
        String email = ((EditText)findViewById(R.id.addclient_email)).getText().toString();
        String personalAddress = ((EditText)findViewById(R.id.addclient_address)).getText().toString();
        String creditCardNumber = ((EditText)findViewById(R.id.addclient_creditcard)).getText().toString();
        String expiryString = ((EditText)findViewById(R.id.addclient_expirydate)).getText().toString();
        String firstName = ((EditText)findViewById(R.id.addclient_firstname)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.addclient_lastname)).getText().toString();
        String password = ((EditText)findViewById(R.id.addclient_password)).getText().toString();
        String confirmPassword = ((EditText)findViewById(R.id.addclient_confirm_pass)).getText().toString();
        // If no inputs are empty
        if (!(email.isEmpty() || personalAddress.isEmpty() || creditCardNumber.isEmpty() ||
         expiryString.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty())) {
            // If the password and the confirm password are equal
            if (password.equals(confirmPassword)) {
                try {
                    // Add the client to the database
                    backend.addClient(email, personalAddress, creditCardNumber, expiryString, firstName, lastName);
                    try {
                        // Set the password of the user
                        backend.setUserPassword(email, password);
                        // Alert the user that the client has been added
                        dialog.setTitle("Success");
                        dialog.setMessage("Client successfully created");
                        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog.show();
                    } catch (StorageException exception) {
                        // Password cannot be set, alert the user
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
                    // Date incorrectly formatted, alert user
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
                // Passwords do not match, alert user
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
        } else {
            // Atleast one field is empty, alert user
            dialog.setTitle("Fields Empty");
            dialog.setMessage("Please fill in all fields");
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();
        }

    }
    public final int FILE_SELECT_CODE=0;
    public void addClientFromFile(View view) {
        // Let user choose a file with the Android File Explorer
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Set type of file to all files
        intent.setType("*/*");
        Intent finalIntent = Intent.createChooser(intent, "Select file with clients");
        // Launch the File Explorer
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
                        // Add the clients in the file to the database
                        backend.addClient(getContentResolver().openInputStream(uri));
                        // Alert the user of success
                        dialog.setTitle("Success");
                        dialog.setMessage("Clients added successfully");
                        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog.show();
                    } catch (StorageException exception) {
                        // Alert the user that the file could not be loaded
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
                        // Alert the user that the file could not be found
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
