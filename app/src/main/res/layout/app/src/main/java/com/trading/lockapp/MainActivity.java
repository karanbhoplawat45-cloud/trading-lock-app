package com.trading.lockapp;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Switch switchMt5, switchXm, switchTv;
    private TextView statusText;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchMt5 = findViewById(R.id.switch_mt5);
        switchXm = findViewById(R.id.switch_xm);
        switchTv = findViewById(R.id.switch_tv);
        statusText = findViewById(R.id.status_text);

        prefs = getSharedPreferences("TradingLockPrefs", MODE_PRIVATE);

        // Load saved switch states
        switchMt5.setChecked(prefs.getBoolean("lock_mt5", true));
        switchXm.setChecked(prefs.getBoolean("lock_xm", true));
        switchTv.setChecked(prefs.getBoolean("lock_tv", true));

        // Save states on change
        switchMt5.setOnCheckedChangeListener((buttonView, isChecked) -> prefs.edit().putBoolean("lock_mt5", isChecked).apply());
        switchXm.setOnCheckedChangeListener((buttonView, isChecked) -> prefs.edit().putBoolean("lock_xm", isChecked).apply());
        switchTv.setOnCheckedChangeListener((buttonView, isChecked) -> prefs.edit().putBoolean("lock_tv", isChecked).apply());

        // Check Usage Stats Permission
        if (!hasUsageStatsPermission()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "कृपया ऐप को 'Usage Access' परमिशन दें", Toast.LENGTH_LONG).show();
        }
    }

    private boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
