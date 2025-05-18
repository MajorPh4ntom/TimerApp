package com.example.aplikacijaura;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timerText, alarmTimeText;
    private EditText inputTime;
    private Button startTimerBtn, stopTimerBtn, setAlarmBtn, openWorldClockBtn;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timerText = findViewById(R.id.timerText);
        inputTime = findViewById(R.id.inputTime);
        startTimerBtn = findViewById(R.id.startTimerBtn);
        stopTimerBtn = findViewById(R.id.stopTimerBtn);
        setAlarmBtn = findViewById(R.id.setAlarmBtn);
        openWorldClockBtn = findViewById(R.id.openWorldClockBtn);
        alarmTimeText = findViewById(R.id.alarmTimeText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        startTimerBtn.setOnClickListener(v -> startTimer());
        stopTimerBtn.setOnClickListener(v -> stopTimer());
        setAlarmBtn.setOnClickListener(v -> setAlarm());
        openWorldClockBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WorldClockActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notifications won't show without permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startTimer() {
        String input = inputTime.getText().toString().trim();
        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter time in seconds.", Toast.LENGTH_SHORT).show();
            return;
        }

        long seconds;
        try {
            seconds = Long.parseLong(input);
            if (seconds <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input. Enter a positive number.", Toast.LENGTH_SHORT).show();
            return;
        }

        long millis = seconds * 1000;
        countDownTimer = new CountDownTimer(millis, 1000) {
            public void onTick(long millisUntilFinished) {
                int sec = (int) (millisUntilFinished / 1000);
                int min = sec / 60;
                sec = sec % 60;
                timerText.setText(String.format(Locale.getDefault(), "%02d:%02d", min, sec));
            }

            public void onFinish() {
                timerText.setText("00:00");
                stopTimerBtn.setEnabled(false);
                startTimerBtn.setEnabled(true);
                inputTime.setText("");
            }
        };
        countDownTimer.start();
        isTimerRunning = true;
        startTimerBtn.setEnabled(false);
        stopTimerBtn.setEnabled(true);
    }

    private void stopTimer() {
        if (isTimerRunning && countDownTimer != null) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
        timerText.setText("00:00");
        startTimerBtn.setEnabled(true);
        stopTimerBtn.setEnabled(false);
    }

    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);

                    long triggerTime = calendar.getTimeInMillis();
                    if (triggerTime < System.currentTimeMillis()) {
                        triggerTime += 24 * 60 * 60 * 1000;
                    }

                    Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            MainActivity.this, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager != null) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                        Toast.makeText(MainActivity.this, "Alarm set!", Toast.LENGTH_SHORT).show();

                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String formattedTime = timeFormat.format(calendar.getTime());
                        alarmTimeText.setText("Alarm set for: " + formattedTime);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);

        timePickerDialog.show();
    }
}
