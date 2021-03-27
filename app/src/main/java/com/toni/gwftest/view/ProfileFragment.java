package com.toni.gwftest.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.toni.gwftest.R;
import com.toni.gwftest.httpsClient.GetMetersRequest;
import com.toni.gwftest.httpsClient.VolleyCallback;
import com.toni.gwftest.login.SharedPrefManager;
import com.toni.gwftest.login.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment  {

    private TextView email;
    private Button logout;
    private MainActivity mainActivity;
    private String totalsUrl;

    private LinearLayout meters, volume, errors, readouts;
    private ImageView imageMeters, imageVolume, imageErrors, imageReadout;

    private GetMetersRequest getMetersRequest;

    public ProfileFragment(){
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.profile_layout, container, false);

        mainActivity = (MainActivity) getActivity();

        getMetersRequest = new GetMetersRequest(mainActivity);
        totalsUrl = getString(R.string.get_totals_url);

        // Get User
        User user = SharedPrefManager.getInstance(mainActivity).getUser();

        // Display User email
        email = view.findViewById(R.id.email);
        email.setText(user.getUsername());

        // Totals
        meters = view.findViewById(R.id.meters);
        volume = view.findViewById(R.id.volume);
        errors = view.findViewById(R.id.errors);
        readouts = view.findViewById(R.id.readouts);

        // Set images
        imageMeters = meters.findViewById(R.id.total_image);
        imageMeters.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.meters_img));

        imageVolume = volume.findViewById(R.id.total_image);
        imageVolume.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.volume_img));

        imageErrors = errors.findViewById(R.id.total_image);
        imageErrors.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.errors_img));

        imageReadout = readouts.findViewById(R.id.total_image);
        imageReadout.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.readout_img));

        // Set Units
        TextView unit;
        unit = meters.findViewById(R.id.unit);
        unit.setText("Συνολικοί Μετρητές");

        unit = volume.findViewById(R.id.unit);
        unit.setText("Συνολική Κατανάλωση (m3)");

        unit = errors.findViewById(R.id.unit);
        unit.setText("Συνολικά Προβλήματα");

        unit = readouts.findViewById(R.id.unit);
        unit.setText("Συνολικές Μετρήσεις");

        // Get Totals
        getMetersRequest.getMeters(totalsUrl, new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject objson = new JSONObject(response);
                    setTotals(objson);
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                }
            }

            @Override
            public void onError(String result) {
                // TODO refresh token
            }
        });

        // Set button listener
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(mainActivity).logout();
            }
        });

        return view;
    }

    private void setTotals(JSONObject objson){
        TextView count;
        count = meters.findViewById(R.id.count);
        try {
            count.setText(objson.getString("meters"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        count = volume.findViewById(R.id.count);
        try {
            count.setText(objson.getString("volume"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        count = errors.findViewById(R.id.count);
        try {
            count.setText(objson.getString("errors"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        count = readouts.findViewById(R.id.count);
        try {
            count.setText(objson.getString("readouts"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}