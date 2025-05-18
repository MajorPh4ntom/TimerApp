package com.example.aplikacijaura;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class WorldClockAdapter extends RecyclerView.Adapter<WorldClockAdapter.ClockViewHolder> {

    private final ArrayList<WorldClock> clockList;

    public WorldClockAdapter(ArrayList<WorldClock> clocks) {
        this.clockList = clocks;
    }

    @Override
    public ClockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create vertical LinearLayout
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 24, 32, 24); // left, top, right, bottom
        layout.setBackgroundColor(0xFF262626); // dark background

        // Create city TextView
        TextView cityView = new TextView(parent.getContext());
        cityView.setTextSize(18);
        cityView.setTextColor(0xFFFFFFFF);
        cityView.setTypeface(null, Typeface.BOLD);

        // Create time TextView
        TextView timeView = new TextView(parent.getContext());
        timeView.setTextSize(32);
        timeView.setTextColor(0xFFFFFFFF);
        timeView.setPadding(0, 8, 0, 24);

        // Add views to layout
        layout.addView(cityView);
        layout.addView(timeView);

        return new ClockViewHolder(layout, cityView, timeView);
    }

    @Override
    public void onBindViewHolder(ClockViewHolder holder, int position) {
        WorldClock clock = clockList.get(position);
        String city = clock.getCityName();
        String timeZoneId = clock.getTimeZoneId();

        TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);

        // Format time as h:mm a (e.g., 9:41 AM)
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        sdf.setTimeZone(timeZone);
        String currentTime = sdf.format(new Date());

        holder.cityNameView.setText(city);
        holder.timeView.setText(currentTime);
    }

    @Override
    public int getItemCount() {
        return clockList.size();
    }

    static class ClockViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameView, timeView;

        ClockViewHolder(View itemView, TextView cityNameView, TextView timeView) {
            super(itemView);
            this.cityNameView = cityNameView;
            this.timeView = timeView;
        }
    }
}
