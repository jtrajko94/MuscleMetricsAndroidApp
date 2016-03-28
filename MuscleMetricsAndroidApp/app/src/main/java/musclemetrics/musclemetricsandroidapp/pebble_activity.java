package musclemetrics.musclemetricsandroidapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by JerunTrajko on 3/27/16.
 */
public class pebble_activity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> mDeviceList = new ArrayList<String>();
    private BluetoothAdapter mBluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    public TextView textView;
    public String uuid;

    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Starting Progress ----------------------");
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pebble);
        listView = (ListView) findViewById(R.id.list);

        //Set up the bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            textView.setText("Device Doesnt have bluetooth");
        }
        //If bluetooth is not enabled, enable it
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        //Get the paired device
        else
        {
            getPairedDevices();
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_LONG).show();
                getPairedDevices();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "User canceled", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getPairedDevices()
    {
        ArrayList <String> arrayList = new ArrayList<String>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                arrayList.add(device.getName() + "\n" + device.getAddress() + "\n");
                Log.d("here", device.getName());
                Log.d("uuid", device.getUuids()[0].toString());
                //we will need some sort of restriction to make sure its the pebble
                uuid = device.getUuids()[0].toString();
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        sendPebbleInfo();
    }

    public void sendPebbleInfo()
    {
        // Create a new dictionary
        PebbleDictionary dict = new PebbleDictionary();

        // The key representing a contact name is being transmitted
        final int AppKeyContactName = 0;
        final int AppKeyAge = 1;

        // Get data from the app
        final String contactName = "Jerun";
        final int age = 21;

        // Add data to the dictionary
        dict.addString(AppKeyContactName, contactName);
        dict.addInt32(AppKeyAge, age);

        final UUID appUuid = UUID.fromString(uuid);

        // Send the dictionary
        PebbleKit.sendDataToPebble(getApplicationContext(), appUuid, dict);
        Log.d("Sending", "Info sent to pebble");
        Log.d("uuid", "UUID is " + uuid);
    }
}
