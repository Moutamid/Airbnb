package com.moutamid.airbnb.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.airbnb.adapters.FlightsAdapter;
import com.moutamid.airbnb.databinding.ActivityFlightsResultBinding;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.models.FlightsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FlightsResultActivity extends AppCompatActivity {
    ActivityFlightsResultBinding binding;
    ArrayList<FlightsModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlightsResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> {
            finish();
        });

        Constants.initDialog(this);
        Constants.showDialog();

        list = new ArrayList<>();

        binding.recyler.setLayoutManager(new LinearLayoutManager(this));
        binding.recyler.setHasFixedSize(false);

        if (Stash.getString(Constants.TRIP) == "oneway"){
            getData(Constants.getOnewayTrip());
        } else {
            getData(Constants.getRoundTrip());
        }

    }

    private void getData(String trip) {
        new Thread(() -> {
            URL google = null;
            try {
                google = new URL(trip);
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                Log.d("TAG", "compress: ERROR: " + e.toString());
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            Log.d("TAG", "data: " + htmlData);
                runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(htmlData);
                        JSONArray legs = json.getJSONArray("legs");
                        JSONObject filters = json.getJSONObject("filters");
                        JSONArray alliances = filters.getJSONArray("alliances");
                        JSONArray airlines = json.getJSONArray("airlines");
                        for (int i=0; i< legs.length(); i++) {
                            JSONObject obj = legs.getJSONObject(i);
                            FlightsModel model = new FlightsModel();
                            model.setDepartureTime(obj.getString("departureTime"));
                            model.setArrivalTime(obj.getString("arrivalTime"));
                            model.setDuration(obj.getString("duration"));
                            model.setDepartureAirportCode(obj.getString("departureAirportCode"));
                            model.setArrivalAirportCode(obj.getString("arrivalAirportCode"));
                            model.setStopoverCode(obj.getString("stopoverCode"));
                            model.setAllianceCodes(obj.getJSONArray("allianceCodes").getString(0));
                            model.setAirlines(airlines.getJSONObject(airlines.length()-1).getString("name"));
                            JSONObject all = alliances.getJSONObject(obj.getJSONArray("allianceCodes").length()-1);
                            model.setPrice(all.getJSONObject("price").getDouble("totalAmountUsd"));

                            list.add(model);
                        }
                        FlightsAdapter adapter = new FlightsAdapter(FlightsResultActivity.this, list);
                        binding.recyler.setAdapter(adapter);
                        Constants.dismissDialog();
                    } catch (JSONException e) {
                        Constants.dismissDialog();
                        Toast.makeText(this, "Your request quota has reached its maximum limits.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
        }).start();
    }
}