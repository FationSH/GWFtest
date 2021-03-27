package com.toni.gwftest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.toni.gwftest.R;
import com.toni.gwftest.view.model.Meter;

import java.util.ArrayList;

/**
 * This class is responsible for the recycleView shown in MeterFragment
 */
public class MeterAdapter extends RecyclerView.Adapter<MeterAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Meter> meterList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView meter_id;
        public TextView comm_mod_type;
        public TextView comm_mod_serial;

        public MyViewHolder(View view) {
            super(view);

            meter_id = view.findViewById(R.id.meter_id);
            comm_mod_type = view.findViewById(R.id.comm_mod_type);
            comm_mod_serial = view.findViewById(R.id.comm_mod_serial);
        }
    }

    public MeterAdapter(Context context, ArrayList<Meter> meterList) {
        this.context = context;

        this.meterList = meterList;
    }

    @Override
    public MeterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meter_object_layout, parent, false);

        return new MeterAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(MeterAdapter.MyViewHolder holder, int position) {
        String meter_id = meterList.get(position).getMeterId();
        String comm_mod_type = meterList.get(position).getCommModType();
        String comm_mod_serial = meterList.get(position).getCommModSerial();

        holder.meter_id.setText(meter_id);
        holder.comm_mod_type.setText(comm_mod_type);
        holder.comm_mod_serial.setText(comm_mod_serial);
    }

    @Override
    public int getItemCount() {
        return meterList.size();
    }

}