package cs.b07.cscb07courseproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;

import controller.Backend;
import travel.Client;
import travel.User;

/**
 * Activity to view client info
 * @author Cameron
 */
public class ClientInfo extends AppCompatActivity {

    private Backend backend;
    private User user;
    private Client desiredClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info);
        Intent intent = getIntent();
        // Instantiate the backend
        backend = new Backend(getApplicationContext());
        user = (travel.User) getIntent().getExtras().get("user");
        backend.setUser(user);
        // Prefill user info into EditTexts in the activity
        desiredClient = (Client) getIntent().getExtras().get("desiredClient");
        ((EditText)findViewById(R.id.clientinfo_firstname)).setText(desiredClient.getFirstName());
        ((EditText)findViewById(R.id.clientinfo_lastname)).setText(desiredClient.getLastName());
        ((EditText)findViewById(R.id.clientinfo_address)).setText(desiredClient.getPersonalAddress());
        ((EditText)findViewById(R.id.clientinfo_email)).setText(desiredClient.getEmail());
        ((EditText)findViewById(R.id.clientinfo_expiry)).setText(new SimpleDateFormat("yyyy-MM-dd").format(desiredClient.getExpiry()));
        ((EditText)findViewById(R.id.clientinfo_creditcard)).setText(desiredClient.getCreditCardNumber());
    }

    public void changePassword(View view) {
        Intent intent = new Intent(this, ChangePass.class);
        // pass the user
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void updateInfo(View view) {
        // Get user input
        String firstName = ((EditText)findViewById(R.id.clientinfo_firstname)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.clientinfo_lastname)).getText().toString();
        String address = ((EditText)findViewById(R.id.clientinfo_address)).getText().toString();
        String email = ((EditText)findViewById(R.id.clientinfo_email)).getText().toString();
        String expiry = ((EditText)findViewById(R.id.clientinfo_expiry)).getText().toString();
        String creditCard = ((EditText)findViewById(R.id.clientinfo_creditcard)).getText().toString();
        // Update info in storage
        backend.addOrUpdateClient(new travel.Client(((travel.Client)getIntent().getExtras().get("desiredClient")).getEmail(),address, creditCard, expiry, firstName, lastName));
        // Alert user of successfull change
        AlertDialog dialog = new AlertDialog.Builder(ClientInfo.this).create();
        dialog.setTitle("Client info updated");
        dialog.setMessage("Information updated successfully!");
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    public void viewBookedItineraries(View view) {
        Intent intent = new Intent(this, ViewBookedItineraries.class);
        // pass the desiredClient and the logged-in user
        intent.putExtra("client", desiredClient);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
