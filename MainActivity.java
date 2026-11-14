package com.trading.locker;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText etPin;
    private Button btnUnlock;
    private TextView tvStatus;
    private SharedPreferences prefs;
    private Handler handler = new Handler();
    
    private static final String CORRECT_PIN = "4321";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPin = findViewById(R.id.etPin);
        btnUnlock = findViewById(R.id.btnUnlock);
        tvStatus = findViewById(R.id.tvStatus);
        prefs = getSharedPreferences("TradingPrefs", MODE_PRIVATE);

        checkUsageStatsPermission();

        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPin = etPin.getText().toString();
                if (enteredPin.equals(CORRECT_PIN)) {
                    Toast.makeText(MainActivity.this, "Unlocked Successfully!", Toast.LENGTH_SHORT).show();
                    // Reset lock state or extend time here
                } else {
                    Toast.makeText(MainActivity.this, "Wrong PIN! Default is 4321", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkUsageStatsPermission() {
        try {
            AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());
            if (mode != AppOpsManager.MODE_ALLOWED) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
