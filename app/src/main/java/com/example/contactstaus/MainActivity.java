package com.example.contactstaus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView statusTextView;
    private ToggleButton featureToggle;
    private TelephonyManager telephonyManager;
    private PhoneStateListener phoneStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.statusTextView);
        featureToggle = findViewById(R.id.featureToggle);

        // Load status message and feature state from SharedPreferences
        loadStatusAndFeatureState();

        Button statusButton = findViewById(R.id.statusButton);
        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle setting status message
                // You can open a dialog or another activity to input the status message
                // For simplicity, we'll set a default message
                String statusMessage = "Do Not Disturb";
                statusTextView.setText("Status: " + statusMessage);

                // Save the status message to SharedPreferences
                saveStatusAndFeatureState(statusMessage, featureToggle.isChecked());
            }
        });

        featureToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle feature activation/deactivation
            saveStatusAndFeatureState(statusTextView.getText().toString(), isChecked);
        });

        // Initialize the TelephonyManager and PhoneStateListener
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);

                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // Incoming call is detected
                    if (featureToggle.isChecked()) {
                        // If the feature is enabled, send a notification with the status message
                        sendNotification(statusTextView.getText().toString());
                    }
                }
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void loadStatusAndFeatureState() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String statusMessage = preferences.getString("statusMessage", "Status: Not Set");
        boolean featureEnabled = preferences.getBoolean("featureEnabled", false);

        statusTextView.setText(statusMessage);
        featureToggle.setChecked(featureEnabled);
    }

    private void saveStatusAndFeatureState(String statusMessage, boolean featureEnabled) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("statusMessage", statusMessage);
        editor.putBoolean("featureEnabled", featureEnabled);
        editor.apply();
    }

    private void sendNotification(String message) {
        // Implement code to send a notification with the provided message
        // You can use NotificationCompat to create and display the notification
        // For simplicity, I'll show a Toast notification in this example
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
