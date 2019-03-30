package com.bullach.android.bakingapp.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    /**
     * Checks for network availability.
     * <br>
     * @param context the context
     * @return whether connected to the network (<code>true</code>) or not (<code>false</code>)
     */
    public static boolean checkNetworkAvailability(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
