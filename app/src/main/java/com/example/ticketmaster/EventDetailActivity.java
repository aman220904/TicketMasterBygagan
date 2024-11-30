package com.example.ticketmaster;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity {

    private TextView eventName, eventDate, eventPrice, eventUrl;
    private ImageView eventImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        eventName = findViewById(R.id.eventName);
        eventDate = findViewById(R.id.eventDate);
        eventPrice = findViewById(R.id.eventPrice);
        eventUrl = findViewById(R.id.eventUrl);
        eventImage = findViewById(R.id.eventImage);

        Event event = (Event) getIntent().getSerializableExtra("event");

        eventName.setText(event.getEventName());
        eventDate.setText(event.getStartDate());
        eventPrice.setText(event.getPriceRange());
        eventUrl.setText(event.getUrl());
        Picasso.get().load(event.getImageUrl()).into(eventImage);
    }
}
