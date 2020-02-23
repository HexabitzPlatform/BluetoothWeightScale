package com.hexabitz.bluetoothweightscale;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothDevices extends AppCompatActivity {

  private ListView listView;
  View parentLayout;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bluetooth_devices);
    parentLayout = findViewById(android.R.id.content);


    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Refreshing Devices...", Snackbar.LENGTH_LONG)
                .setAction("Refresh", null).show();
        refresh();
      }
    });

    listView = findViewById(R.id.listView);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView device = view.findViewById(android.R.id.text1);

        String[] values = device.getText().toString().split("/");
        String DeviceName = values[0].trim();
        String DeviceAddress = values[1].trim();
        Intent MainActivityIntent = new Intent(BluetoothDevices.this, MainActivity.class);
        MainActivityIntent.putExtra("DeviceName",DeviceName);
        MainActivityIntent.putExtra("DeviceAddress",DeviceAddress);
        startActivity(MainActivityIntent);
      }
    });
    refresh();
  }

  private void refresh() {
    try {

      BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
      Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

      List<String> s = new ArrayList<>();
      for (BluetoothDevice bt : pairedDevices)
        s.add(bt.getName() + " / " + bt.getAddress());


      listView.setAdapter(new ArrayAdapter<>(getApplication(),
          android.R.layout.simple_list_item_1, s));
    }
    catch (Exception e ) {
      Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_LONG)
          .setAction("IOException", null).show();

    }
  }
}
