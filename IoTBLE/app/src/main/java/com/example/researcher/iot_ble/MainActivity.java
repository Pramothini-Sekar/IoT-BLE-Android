package com.example.researcher.iot_ble;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean scanning = false;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;


    private class BleList extends BaseAdapter{
        private ArrayList<BluetoothDevice> devices;
        private ArrayList<Integer> RSSIs;
        private LayoutInflater inflater;
        private ListView listView;

        public BleList(){
            super();
            devices = new ArrayList<BluetoothDevice>();
            RSSIs = new ArrayList<Integer>();
            inflater = ((Activity) MainActivity.this).getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device,int rssi){
            if(!devices.contains(device)){
                devices.add(device);
                RSSIs.add(rssi);
            }
            else{
                RSSIs.set(devices.indexOf(device),rssi);
            }
        }

        public void clear(){
            devices.clear();
            RSSIs.clear();
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(android.R.layout.two_line_list_item, null);
                viewHolder.deviceName = (TextView) convertView.findViewById(android.R.id.text1);
                viewHolder.deviceRssi = (TextView) convertView.findViewById(android.R.id.text2);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (devices.get(position).getName() != null) {
                String deviceName = devices.get(position).getName();
                String deviceAddress = devices.get(position).getAddress();
                int content = devices.get(position).describeContents();
                int rssi = RSSIs.get(position);
                Double distance = calculateDistance(rssi);

                viewHolder.deviceName.setText(String.valueOf(deviceName) + " => " + String.valueOf(deviceAddress));
                viewHolder.deviceRssi.setText(String.valueOf(distance) + " metres");
            }
                return convertView;

            }

    }

    static class ViewHolder{
        TextView deviceName;
        TextView deviceRssi;
    }


    ToggleButton toggleButton;

    ListView listView;
    BleList bleList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Requesting permission for Bluetooth connection
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.BLUETOOTH},1);

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        //Checking if phone has bluetooth enabled
        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()){
            Toast.makeText(this, "Please turn on Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        }


        bleList = new BleList();
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(bleList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String MACAddr = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(MainActivity.this,OfferActivity.class);
                intent.putExtra("MACAddr",MACAddr);
                startActivity(intent);

            }
        });

        toggleButton = (ToggleButton)findViewById(R.id.toggleButton);
        toggleButton.setTextOff("Not scanning anymore");
        toggleButton.setTextOn("Scanning right now");
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    bluetoothAdapter.startLeScan(leScanCallback);

                }
                else{
                    bluetoothAdapter.stopLeScan(leScanCallback);
                    bleList.clear();
                    bleList.notifyDataSetChanged();
                }
            }
        });
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d("scan",device.getName() + " RSSI :" + rssi + " Record " + scanRecord);
            bleList.addDevice(device,rssi);
            bleList.notifyDataSetChanged();
        }
    };


    private double calculateDistance(int rssi){
        Double distance = Math.pow(10.0,(-92.0-rssi)/20.0);
        Double toBeTruncated = new Double(String.valueOf(distance));

        Double truncatedDistance = BigDecimal.valueOf(toBeTruncated)
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        return truncatedDistance;
    }



}