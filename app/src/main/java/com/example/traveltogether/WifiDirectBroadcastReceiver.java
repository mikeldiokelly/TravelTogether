package com.example.traveltogether;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;

import androidx.core.app.ActivityCompat;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    private final WifiP2pManager wifiP2pManager;
    private final WifiP2pManager.Channel channel;
    private final PeerToPeerActivity peerToPeerActivity;

    public WifiDirectBroadcastReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, PeerToPeerActivity peerToPeerActivity) {
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.peerToPeerActivity = peerToPeerActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            //check to see if wifi is enabled
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            //call WifiP2PManager.requestPeers() to get current peers
            if (wifiP2pManager != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(peerToPeerActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, (int) 1);
                    return;
                }
                wifiP2pManager.requestPeers(channel, peerToPeerActivity.peerListListener);
            }
        }
        else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            //Respond to new connection or disconnection
            if(wifiP2pManager != null){
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if(networkInfo.isConnected()){
                    wifiP2pManager.requestConnectionInfo(channel, peerToPeerActivity.connectionInfoListener);
                }
                else {
                    peerToPeerActivity.connectionStatusP2P.setText("Not connected");
                }
            }
        }
    }
}
