package com.toni.gwftest.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.toni.gwftest.view.MainActivity;

public class NetworkChangeReceiver {

        MainActivity mainActivity;

        public NetworkChangeReceiver(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        // Network Check
        public void registerNetworkCallback() {
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
                        @Override
                        public void onAvailable(Network network) {
                            Toast.makeText(mainActivity, "Network Established.", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onLost(Network network) {
                            Toast.makeText(mainActivity, "Network Lost.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }catch (Exception e){
                Log.d("Network CAllback", String.valueOf(e));
            }
        }

}
