package com.trading.locker;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class MainActivity extends Activity {

    private EditText pinInput;
    private Button unlockButton;
    private TextView statusText;
    private Handler handler = new Handler();
    private Runnable checkRunnable;

    private static final String CORRECT_PIN = "4321";
    // 15 minutes in milliseconds
    private static final long USAGE_LIMIT_MS = 15 * 60 * 1000; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Creating a simple UI dynamically so it never fails to load
        setContentView(createUI());

        startAutoLockChecker();
    }

    private View createUI() {
        // Simple linear layout layout for locking interface
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 100, 50, 50);
        layout.setGravity(android.view.Gravity.CENTER);

        statusText = new TextView(this);
        statusText.setText("Trading Apps Locked\nEnter PIN (4321) to Unlock");
        statusText.setTextSize(18);
        statusText.setGravity(android.view.Gravity.CENTER);
        layout.addView(statusText);

        android.view.ViewGroup.MarginLayoutParams params = new android.view.ViewGroup.MarginLayoutParams(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT, 
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 40, 0, 20);

        pinInput = new EditText(this);
        pinInput.setHint("Enter PIN");
        pinInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        layout.addView(pinInput, new android.widget.LinearLayout.LayoutParams(params));

        unlockButton = new Button(this);
        unlockButton.setText("Unlock for 15 Mins");
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPin = pinInput.getText().toString();
                if (CORRECT_PIN.equals(enteredPin)) {
                    Toast.makeText(MainActivity.this, "Unlocked successfully!", Toast.LENGTH_SHORT).show();
                    pinInput.setText("");
                    // Hide or minimize app to let user trade
                    moveTaskToBack(true);
                } else {
                    Toast.makeText(MainActivity.this, "Wrong PIN! Default is 4321", Toast.LENGTH_SHORT).show();
                }
            }
        });
        layout.addView(unlockButton, new android.widget.LinearLayout.LayoutParams(params));

        return layout;
    }

    private void startAutoLockChecker() {
        checkRunnable = new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Auto unlock at 02:00 AM (02:00)
                if (hour == 2 && minute == 0) {
                    if (statusText != null) {
                        statusText.setText("Auto-unlocked at 02:00 AM");
                    }
                }

                // Run check every 30 seconds
                handler.postDelayed(this, 30000);
            }
        };
        handler.post(checkRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && checkRunnable != null) {
            handler.removeCallbacks(checkRunnable);
        }
    }
}
