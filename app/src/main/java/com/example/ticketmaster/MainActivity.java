package com.example.ticketmaster;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private EditText cityEditText, radiusEditText;
    private Button searchButton;
    private static final String API_KEY = "HylTNwr7lJv53y3ocDv9c4CWSZKxROwR7opQJJwM0tKxlsF0kjDpFei4";
    private static final String PREFS_NAME = "TicketMasterPrefs";
    private static final String LAST_CITY_KEY = "lastCity";
    private DatabaseHelper databaseHelper;  // To interact with SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        radiusEditText = findViewById(R.id.radiusEditText);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);
        databaseHelper = new DatabaseHelper(this);

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);

        // Load the last searched city from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lastCity = sharedPreferences.getString(LAST_CITY_KEY, "");
        cityEditText.setText(lastCity);

        searchButton.setOnClickListener(v -> {
            String city = cityEditText.getText().toString();
            String radius = radiusEditText.getText().toString();

            if (!city.isEmpty() && !radius.isEmpty()) {
                searchEvents(city, radius);
                // Save the city to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LAST_CITY_KEY, city);
                editor.apply();
            } else {
                Toast.makeText(MainActivity.this, "Please enter a city and radius.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchEvents(String city, String radius) {
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=HylTNwr7lJv53y3ocDv9c4CWSZKxROwR7opQJJwM0tKxlsF0kjDpFei4" + API_KEY +
                "&city=" + city + "&radius=" + radius;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray events = response.getJSONObject("_embedded").getJSONArray("events");
                            eventList.clear();
                            for (int i = 0; i < events.length(); i++) {
                                JSONObject eventObject = events.getJSONObject(i);
                                String eventName = eventObject.getString("name");
                                String startDate = eventObject.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                String priceRange = eventObject.optString("priceRanges", "Not Available");
                                String url = eventObject.getString("url");
                                String imageUrl = eventObject.getJSONObject("images").getJSONObject("loading").getString("url");

                                Event event = new Event(eventName, startDate, priceRange, url, imageUrl);
                                eventList.add(event);
                            }
                            eventAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing events.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error fetching events.", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
