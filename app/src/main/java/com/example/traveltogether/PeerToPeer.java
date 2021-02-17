package com.example.traveltogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class PeerToPeer extends AppCompatActivity implements View.OnClickListener {

    Button register, discover, stopDiscovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peer_to_peer);

        register = findViewById(R.id.registerP2P);
        register.setOnClickListener(this);

        discover = findViewById(R.id.discoverP2P);
        discover.setOnClickListener(this);

        stopDiscovery = findViewById(R.id.stopDiscovery);
        stopDiscovery.setOnClickListener(this);
    }


    void registerService(String serviceName) throws UnknownHostException {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(serviceName);
        serviceInfo.setServiceType("_http._tcp.");
        serviceInfo.setHost(InetAddress.getLocalHost());
        serviceInfo.setPort(8181);

        NsdManager.RegistrationListener mRegistrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                System.out.println("11111111111111111129");
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                System.out.println("11111111111111111134");
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                System.out.println("11111111111111111139");
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                System.out.println("11111111111111111144");
            }
        };

        NsdManager mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    HashMap<InetAddress, String > p2pServices = new HashMap<InetAddress, String>();

    void actOnServiceFound(NsdServiceInfo serviceInfo){
        p2pServices.put(serviceInfo.getHost(), serviceInfo.getServiceName());

    }

    void discoverP2PServices(){
        NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {
        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            System.out.println("222222222222222222222222267");
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            System.out.println("222222222222222222222222272");
        }

        @Override
        public void onDiscoveryStarted(String serviceType) {
            System.out.println("222222222222222222222222277");
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            System.out.println("222222222222222222222222282");
        }

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            System.out.println("222222222222222222222222287");
            actOnServiceFound(serviceInfo);
            System.out.println(serviceInfo);
        }

        @Override
        public void onServiceLost(NsdServiceInfo serviceInfo) {
            System.out.println("222222222222222222222222293");
        }
    };

        NsdManager mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

        mNsdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerP2P:
                try {
                    registerService("new location");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.discoverP2P:
                discoverP2PServices();
                break;
            case R.id.stopDiscovery:
//                mNsdManager.stopServiceDiscovery(mDiscoveryListener);
                break;
        }
    }
}