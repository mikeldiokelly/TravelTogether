package com.example.traveltogether;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerToPeerActivity extends AppCompatActivity {

    TextView connectionStatusP2P, readMessageTextView, deviceNameMessage;
    Button wifiSwitch, discoverPeers, sendButtonP2P;
    ListView peersListView;
    EditText writeMsgToPeer;

    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel channel;

    BroadcastReceiver receiver;
    IntentFilter intentFilter;

    List<WifiP2pDevice> peers = new ArrayList<>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    Socket socket;

    Server server;
    Client client;
    boolean isHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peer_to_peer);

        initializeInterface();
        exqListener();
    }

    private void exqListener() {
        wifiSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(intent, 1);
            }
        });

        discoverPeers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    ActivityCompat.requestPermissions(PeerToPeerActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    return;
                }
                wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        connectionStatusP2P.setText(R.string.discoveryStarted);
                    }

                    @Override
                    public void onFailure(int reason) {
                        connectionStatusP2P.setText(R.string.discoveryNotStarted);
                    }
                });
            }
        });

        deviceNameMessage.setText(String.format("you are broadcasted as %s to the users near you, it is advisable to change the device name in settings>general to reflect the subject of your broadcast", Settings.Global.getString(getApplicationContext().getContentResolver(), "device_name")));

        peersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WifiP2pDevice device = deviceArray[position];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(PeerToPeerActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    return;
                }
                wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        connectionStatusP2P.setText(String.format("%s%s", getString(R.string.connected), device.deviceName));
                    }

                    @Override
                    public void onFailure(int reason) {
                        connectionStatusP2P.setText(String.format("%s%s", getString(R.string.errorInConnectionP2P), device.deviceName));
                    }
                });
            }
        });

        sendButtonP2P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                String msg = writeMsgToPeer.getText().toString();

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(msg!= null && isHost){
                            server.write(msg.getBytes());
                        }
                        else if(msg != null && !isHost){
                            client.write(msg.getBytes());
                        }
                    }
                });
            }
        });
    }

    private void initializeInterface() {
        connectionStatusP2P = findViewById(R.id.connectionStatusP2P);
        readMessageTextView = findViewById(R.id.readMsgSentByPeer);
        deviceNameMessage = findViewById(R.id.deviceNameMessage);
        wifiSwitch = findViewById(R.id.wifiSwitch);
        discoverPeers = findViewById(R.id.discoverPeers);
        sendButtonP2P = findViewById(R.id.sendButtonP2P);
        peersListView = findViewById(R.id.peersListView);
        writeMsgToPeer = findViewById(R.id.writeMsgToPeer);

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        receiver = new WifiDirectBroadcastReceiver(wifiP2pManager, channel, this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {

        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            System.out.println(wifiP2pDeviceList.getDeviceList());
            if(!wifiP2pDeviceList.getDeviceList().equals(peers))
            {
                peers.clear();
                peers.addAll(wifiP2pDeviceList.getDeviceList());

                deviceNameArray = new String[wifiP2pDeviceList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[wifiP2pDeviceList.getDeviceList().size()];
                int index = 0;

                for(WifiP2pDevice device: wifiP2pDeviceList.getDeviceList()){
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceNameArray);
                peersListView.setAdapter(adapter);

                if(peers.size() == 0){
                    connectionStatusP2P.setText(R.string.noDeviceFound);
                }
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;
            if(info.groupFormed && info.isGroupOwner){
                isHost = true;
                server = new Server();
                server.start();
                connectionStatusP2P.setText("Host");
            }
            else if(info.groupFormed){
                isHost = false;
                client = new Client(groupOwnerAddress);
                client.start();
                connectionStatusP2P.setText("Client");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public class Server extends Thread{
        ServerSocket serverSocket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try{
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            }
            catch (IOException e){
                e.printStackTrace();
            }

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    byte[] buffer = new byte[1024];
                    int bytes;

                    while(socket != null){
                        try {
                            bytes = inputStream.read(buffer);
                            if(bytes > 0){
                                int finalBytes = bytes;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String tempMsg = new String(buffer, 0, finalBytes);
                                        readMessageTextView.setText(tempMsg);
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }
    }

    public class Client extends Thread {
        String hostAdd;
        private InputStream inputStream;
        private OutputStream outputStream;

        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Client(InetAddress hostAddress){
            hostAdd = hostAddress.getHostAddress();
            socket = new Socket();

        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            }
            catch (IOException e){
                e.printStackTrace();
            }

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    byte[] buffer = new byte[1024];
                    int bytes;

                    while(socket!=null){
                        try{
                            bytes = inputStream.read(buffer);
                            if(bytes>0){
                                int finalBytes = bytes;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String tempMsg = new String(buffer, 0, finalBytes);
                                        readMessageTextView.setText(tempMsg);
                                    }
                                });
                            }
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                }
            });

        }
    }
}