package com.tests.titouan.bluetoothtests;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Set;


public class BluetoothClientActivity extends Activity {

    private ArrayList<String> devices = new ArrayList<>();
    private ListView lvDevices;
    private TextView textView;
    private BluetoothAdapter blueAdapter;
    private BroadcastReceiver receiver;


    private ArrayAdapter<String> devicesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_client);

        lvDevices = (ListView) findViewById(R.id.devices);
        textView = (TextView) findViewById(R.id.textView);

        blueAdapter = BluetoothAdapter.getDefaultAdapter();

        if(blueAdapter != null){
            blueAdapter.enable();
            textView.setText("Enabled");

            Set<BluetoothDevice> bluetoothDevices = blueAdapter.getBondedDevices();
            for(BluetoothDevice device : bluetoothDevices){
                devices.add(device.getName());
            }

            devicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, devices);
            lvDevices.setAdapter(devicesAdapter);

            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();

                    if(BluetoothDevice.ACTION_FOUND.equals(action)){
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        String deviceName = (device.getName() != null ? device.getName() : device.getAddress());
                        Log.e("Device", deviceName);
                        devices.add(deviceName);
                        devicesAdapter.notifyDataSetChanged();
                    }
                }
            };

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(blueAdapter != null){
            blueAdapter.cancelDiscovery();
            unregisterReceiver(receiver);
            blueAdapter.disable();
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.discover:
                Log.e("Discover", "try");
                blueAdapter.startDiscovery();
                break;
        }
    }
}
