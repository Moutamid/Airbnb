package com.moutamid.airbnb.constant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moutamid.airbnb.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {
    static Dialog dialog;
    static Dialog loading_dialog;
    public static final int RC_SIGN_IN = 9001;
    public static final String USER = "user";
    public static final String KEY = "KEY";
    public static final String API_KEY = "API_KEY";
    public static final String Leaving = "Leaving";
    public static final String GOING = "GOING";
    public static final String TRIP = "TRIP";
    public static final String departureDate = "departureDate";
    public static final String returingDate = "returingDate";
    public static final String INCOMING = "incoming";
    public static final String ONEWAY_TRIP = "https://api.flightapi.io/onewaytrip/";
    public static final String ROUND_TRIP = "https://api.flightapi.io/roundtrip/";
    public static final String PAUSE_STATUS = "PAUSE_STATUS";
    public static final String Reservations = "Reservations";
    public static final String MODEL = "model";
    public static final String CHAT = "chats";
    public static final String CHAT_LIST = "chats_list";
    public static final String Rating = "Rating";
    public static final String phone = "phone";
    public static final String WISHLIST = "WISHLIST";
    public static final String ProductImages = "hotel_images";
    public static final String SPACE = "space";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String TIME = "hh:mm aa";
    public static final String DATE = "dd MMM";
    public static final String DEPARTURE_FORMAT = "EEE, dd MMM yyyy";
    public static final String API_DATE = "yyyy-MM-dd";

    public static String getOnewayTrip() {
        return ONEWAY_TRIP + Stash.getString(API_KEY, "6705069c810e2baf0592b595") + "/" + Stash.getString(Leaving) + "/" + Stash.getString(GOING) + "/" + getAPI_DATE(Stash.getLong(departureDate)) + "/1/0/0/Economy/USD";
    }

    public static String getRoundTrip() {
        return ROUND_TRIP + Stash.getString(API_KEY, "6705069c810e2baf0592b595") + "/" + Stash.getString(Leaving) + "/" + Stash.getString(GOING) + "/" + getAPI_DATE(Stash.getLong(departureDate)) + "/" + getAPI_DATE(Stash.getLong(returingDate)) + "/1/0/1/Economy/USD";
    }

    public static String getFormatedDate(long date) {
        return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(date);
    }

    public static String getAPI_DATE(long date) {
        return new SimpleDateFormat(API_DATE, Locale.getDefault()).format(date);
    }

    public static String getDepartureFormat(long date) {
        return new SimpleDateFormat(DEPARTURE_FORMAT, Locale.getDefault()).format(date);
    }

    public static String getFormatedTime(long date) {
        return new SimpleDateFormat(TIME, Locale.getDefault()).format(date);
    }

    public static String getFormatedYear(long date) {
        return new SimpleDateFormat(YEAR_FORMAT, Locale.getDefault()).format(date);
    }

    public static String getDate(long date) {
        return new SimpleDateFormat(DATE, Locale.getDefault()).format(date);
    }

    public static void initDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public static void showDialog() {
        dialog.show();
    }

    public static void dismissDialog() {
        dialog.dismiss();
    }

    public static void checkApp(Activity activity) {
        String appName = "airbnb";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
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

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("airbnb");
        db.keepSynced(true);
        return db;
    }

    public static StorageReference storageReference(String auth) {
        StorageReference sr = FirebaseStorage.getInstance().getReference().child("airbnb").child(auth);
        return sr;
    }

}
