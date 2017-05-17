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
import travel.Administrator;
import travel.User;

/**
 * Activity to change a user's password
 * @author Cameron
 */
public class ChangePass extends AppCompatActivity {

    private Backend backend;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        Intent intent = getIntent();
        // Instantiat the backend with the application context
        backend = new Backend(getApplicationContext());
        // Set the user
        user = (travel.User) intent.getExtras().get("user");
        backend.setUser(user);
    }

    public void changePassword(View view) {
        // Get user input
        String oldpass = ((EditText) findViewById(R.id.changepass_oldpass)).getText().toString();
        String user = this.user.getEmail();
        // Check user password is correct
        if (backend.login(user, oldpass)) {
            String newPass = ((EditText) findViewById(R.id.changepass_newpass)).getText().toString();
            String confirmPass = ((EditText) findViewById(R.id.changepass_confirmpass)).getText().toString();
            // Check password and confirmed pass are the same
            if (newPass.equals(confirmPass)) {
                // Set the user password in storage if there are no issues
                try {
                    backend.setUserPassword(user, newPass);
                     Intent intent;
                    // Determine next activity
                    if (this.user instanceof Administrator) {
                       intent = new Intent(this, MainMenuAdmin.class);
                    } else {
                        intent = new Intent(this, MainMenuClient.class);
                    }
                    intent.putExtra("user", this.user);
                    //Display success dialog
                    AlertDialog dialog = new AlertDialog.Builder(ChangePass.this).create();
                    dialog.setTitle("Success!");
                    dialog.setMessage("Password has been sucessfully changed!");
                    dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    startActivity(intent);
                    dialog.show();
                } catch (StorageException exception) {
                    // Warn user that the password they entered is invalid
                    AlertDialog dialog = new AlertDialog.Builder(ChangePass.this).create();
                    dialog.setTitle("Invalid Password");
                    dialog.setMessage("Password is invalid! Please try another.");
                    dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.show();
                }
            } else {
                // Warn user that passwords are not equal
                AlertDialog dialog = new AlertDialog.Builder(ChangePass.this).create();
                dialog.setTitle("Error");
                dialog.setMessage("Passwords are not equal!");
                dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }
        } else {
            // Warn user that the wrong password was used
            AlertDialog dialog = new AlertDialog.Builder(ChangePass.this).create();
            dialog.setTitle("Error");
            dialog.setMessage("Password is incorrect");
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
