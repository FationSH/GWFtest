package com.toni.gwftest.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.toni.gwftest.R;
import com.toni.gwftest.httpsClient.GetMetersRequest;
import com.toni.gwftest.httpsClient.VolleyCallback;
import com.toni.gwftest.utils.MyDividerItemDecoration;
import com.toni.gwftest.utils.RecyclerTouchListener;
import com.toni.gwftest.view.model.Meter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MetersFragment extends Fragment {

    private ArrayList<Meter> meterList;
    private MainActivity mainActivity;
    private String meterUrl;
    private GetMetersRequest getMetersRequest;

    protected MeterAdapter meterAdapter;
    private RecyclerView viewMeters;
    private TextView emptyMeters;

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

        meterList = new ArrayList<>();
        meterUrl = getString(R.string.get_meters_url);
        getMetersRequest = new GetMetersRequest(mainActivity);

        // Get Meters
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
            public void onError(String result) {
                // TODO refresh token
            }
        });

        return view;
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
                meterList.add(meter);
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
    public void recView(){
        meterAdapter = new MeterAdapter(getContext(), meterList);
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

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }
}
