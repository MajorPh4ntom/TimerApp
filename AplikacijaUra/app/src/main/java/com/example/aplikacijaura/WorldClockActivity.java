package com.example.aplikacijaura;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.TimeZone;

public class WorldClockActivity extends AppCompatActivity {

    RecyclerView clockRecyclerView;
    Button addClockBtn;
    WorldClockAdapter adapter;
    ArrayList<WorldClock> clocks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_clock);

        clockRecyclerView = findViewById(R.id.clockRecyclerView);
        addClockBtn = findViewById(R.id.addClockBtn);

        adapter = new WorldClockAdapter(clocks);
        clockRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        clockRecyclerView.setAdapter(adapter);

        addClockBtn.setOnClickListener(v -> showAddClockDialog());

        Button backToMainBtn = findViewById(R.id.backToMainBtn);
        backToMainBtn.setOnClickListener(v -> {
            finish(); // Closes this activity and returns to the previous one (MainActivity)
        });
    }

    private void showAddClockDialog() {
        // Get list of available timezone IDs for user to choose from
        String[] timeZones = TimeZone.getAvailableIDs();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Timezone");

        // Use a searchable dialog or simple list dialog
        builder.setItems(timeZones, (dialog, which) -> {
            String selectedTimeZone = timeZones[which];
            // Use last part of timezone as city for simplicity
            String cityName = selectedTimeZone.contains("/") ? selectedTimeZone.split("/")[1].replace('_', ' ') : selectedTimeZone;
            clocks.add(new WorldClock(cityName, selectedTimeZone));
            adapter.notifyDataSetChanged();
        });

        builder.show();
    }
}
