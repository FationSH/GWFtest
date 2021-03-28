package com.toni.gwftest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.toni.gwftest.R;
import com.toni.gwftest.httpsClient.GetMetersRequest;
import com.toni.gwftest.httpsClient.RefreshToken;
import com.toni.gwftest.httpsClient.VolleyCallback;
import com.toni.gwftest.utils.MyDividerItemDecoration;
import com.toni.gwftest.utils.RecyclerTouchListener;
import com.toni.gwftest.view.model.Meter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MetersFragment extends Fragment {

    private Map<String, Meter> meterList;
    private MainActivity mainActivity;
    private String meterUrl;
    private GetMetersRequest getMetersRequest;

    protected MeterAdapter meterAdapter;
    private RecyclerView viewMeters;
    private TextView emptyMeters;

    private ImageButton search;
    private EditText searchable;

    public MetersFragment(){
        // Required empty public constructor
    }

    public static MetersFragment newInstance() {
        return new MetersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("CutPasteId")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.meters_layout, container, false);

        mainActivity = (MainActivity) getActivity();

        emptyMeters = view.findViewById(R.id.empty_view);

        viewMeters = view.findViewById(R.id.recycler_view_meters);
        viewMeters = view.findViewById(R.id.recycler_view_meters);

        meterList = new HashMap<>();
        meterUrl = getString(R.string.get_meters_url);
        getMetersRequest = new GetMetersRequest(mainActivity);

        // Get Meters
        getMeters();

        // Set search listener
        searchable = view.findViewById(R.id.search_bar_edit_text);
        search = view.findViewById(R.id.search_bar_enter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String srch = searchable.getText().toString();
                if (meterList.containsKey(srch))
                    new HandleSearch(meterList.get(srch));
            }
        });

        return view;
    }

    private class HandleSearch {
        public HandleSearch(Meter meter) {
            // Set selected Meter
            mainActivity.selectedMeter = meter;

            // Pop up a window with meter's info
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
            View layout = inflater.inflate(R.layout.info_layout, null);

            TextView vals;
            vals = layout.findViewById(R.id.msp_value);
            vals.setText(meter.getMpName());

            vals = layout.findViewById(R.id.meter_value);
            vals.setText(meter.getMeterId());

            vals = layout.findViewById(R.id.module_value);
            vals.setText(meter.getCommModSerial() +" "+ meter.getCommModType());

            vals = layout.findViewById(R.id.volume_value);
            vals.setText(meter.getVolume());

            //Get the devices screen density to calculate correct pixel sizes
            float density = mainActivity.getResources().getDisplayMetrics().density;
            // create a focusable PopupWindow with the given layout and correct size
            final PopupWindow pw = new PopupWindow(layout, (int) density * 280, (int) density * 310, true);

            pw.setOutsideTouchable(true);
            // display the pop-up in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
        }
    }

    private void getMetersList(JSONArray array){
        JSONObject obj;
        for (int i = 0; i < array.length(); i++) {
            try {
                obj = array.getJSONObject(i);
                @SuppressLint("SimpleDateFormat") Meter meter = new Meter(obj.getString("lat"), obj.getString("lng"),
                        obj.getString("mp_name"), obj.getString("meter_id"),
                        obj.getString("comm_mod_type"), obj.getString("comm_mod_serial"),
                        obj.getString("last_entry"), obj.getString("volume"),
                        obj.getString("battery_lifetime"), obj.getJSONObject("state"));
                meterList.put(meter.getMeterId(), meter);
                mainActivity.selectedMeter = meter;     // Keep the last as selected to plot on map
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (meterList.size() == 0) {
            emptyMeters.setVisibility(View.VISIBLE);
        } else {
            emptyMeters.setVisibility(View.GONE);
            recView();
        }
    }

    /**
     * Initialize RecyclerView in MeterFragment
     * Add Listener
     * - onClick select the meter
     * - onLongClick do nothing
     */
    private void recView(){
        meterAdapter = new MeterAdapter(getContext(), new ArrayList<>(meterList.values()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        viewMeters.setLayoutManager(mLayoutManager);
        viewMeters.setItemAnimator(new DefaultItemAnimator());
        viewMeters.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        viewMeters.setAdapter(meterAdapter);

        // refreshing the list
        meterAdapter.notifyDataSetChanged();

        viewMeters.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                viewMeters, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, final int position) {
                TextView sel = view.findViewById(R.id.meter_id);
                String srch = sel.getText().toString();
                if (meterList.containsKey(srch))
                    new HandleSearch(meterList.get(srch));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getMeters(){
        // Check internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            Toast.makeText(getContext(), "No internet.", Toast.LENGTH_SHORT).show();
            emptyMeters.setVisibility(View.VISIBLE);
        } else {

            Fragment metFrag = this;
            final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

            getMetersRequest.getMeters(meterUrl, new VolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONArray array = new JSONArray(response);
                        getMetersList(array);
                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                    }
                }

                @Override
                public void onError(VolleyError result) {
                    // Refresh token and get data
                    RefreshToken refreshToken = new RefreshToken(mainActivity);
                    refreshToken.refreshToken(getString(R.string.refresh_url), metFrag, ft);
                    getMeters();
                }
            });
        }
    }

}
