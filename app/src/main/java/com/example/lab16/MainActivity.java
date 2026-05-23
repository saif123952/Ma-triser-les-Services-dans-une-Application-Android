package com.example.lab16;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView labelDuration;
    private Button startControl, stopControl;
    private TrackingService monitorService;
    private boolean connected = false;

    private final ServiceConnection monitorConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackingService.ServiceControl control = (TrackingService.ServiceControl) service;
            monitorService = control.getBoundService();
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        labelDuration = findViewById(R.id.displayTimer);
        startControl = findViewById(R.id.launchBtn);
        stopControl = findViewById(R.id.terminateBtn);

        startControl.setOnClickListener(v -> executeStart());
        stopControl.setOnClickListener(v -> executeStop());
    }

    private void executeStart() {
        Intent intent = new Intent(this, TrackingService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        bindService(intent, monitorConnection, Context.BIND_AUTO_CREATE);
    }

    private void executeStop() {
        Intent intent = new Intent(this, TrackingService.class);
        intent.setAction("ACTION_TERMINATE");
        stopService(intent);

        if (connected) {
            unbindService(monitorConnection);
            connected = false;
        }
        labelDuration.setText("00:00");
    }

    @Override
    protected void onDestroy() {
        if (connected) {
            unbindService(monitorConnection);
        }
        super.onDestroy();
    }
}
