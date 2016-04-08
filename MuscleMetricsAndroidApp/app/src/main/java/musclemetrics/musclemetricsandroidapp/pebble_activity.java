package musclemetrics.musclemetricsandroidapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by JerunTrajko on 3/27/16.
 */
public class pebble_activity extends AppCompatActivity {

    private ListView listView;
    private BluetoothAdapter mBluetoothAdapter;
    public TextView textView;
    public String uuid;
    PebbleKit.PebbleDataReceiver dataReceiver;

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
        //Get the pebble paired to this device
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

        //Start the app!
        PebbleKit.startAppOnPebble(getApplicationContext(), UUID.fromString("4eb9f670-e798-4ce5-918f-db6c38b23846"));

        //Set up the receiver to get data from pebble
        setPebbleReceiver();

        //Send data to pebble
        final Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Log.d("Sending Data", "Sending...");
                sendPebbleInfo();
            }
        });

        //Open the app and begin receiving data from pebble
        final Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Log.d("Closing App", "Closing...");
                PebbleKit.closeAppOnPebble(getApplicationContext(), UUID.fromString("4eb9f670-e798-4ce5-918f-db6c38b23846"));

                Log.d("Starting App", "Starting...");
                PebbleKit.startAppOnPebble(getApplicationContext(), UUID.fromString("4eb9f670-e798-4ce5-918f-db6c38b23846"));
            }
        });
    }

    //Send data to the pebble
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

        final UUID appUuid = UUID.fromString("4eb9f670-e798-4ce5-918f-db6c38b23846");

        // Send the dictionary
        PebbleKit.sendDataToPebble(getApplicationContext(), appUuid, dict);
        Log.d("Sending", "Info sent to pebble");
        //Log.d("uuid", "UUID is " + uuid);
        boolean connected = PebbleKit.isWatchConnected(getApplicationContext());
        Log.d("connected", Boolean.toString(connected));
    }

    //Get data from the pebble
    public void setPebbleReceiver()
    {
        //UUID appUuid = UUID.fromString(uuid);
        UUID appUuid = UUID.fromString("4eb9f670-e798-4ce5-918f-db6c38b23846");
        dataReceiver = new PebbleKit.PebbleDataReceiver(appUuid) {

            @Override
            public void receiveData(Context context, int transaction_id,
                                    PebbleDictionary dict) {
                Log.d("Pebble Receive", "Received Data");

                // If the tuple is present...
                Long x = dict.getInteger(0);
                Long y = dict.getInteger(1);
                Long z = dict.getInteger(2);
                if(x != null && y != null && z != null) {
                    // Read the integer value
                    int xval = x.intValue();
                    int yval = y.intValue();
                    int zval = z.intValue();
                    Log.d("Received Value X: ", Integer.toString(xval));
                    Log.d("Received Value Y: ", Integer.toString(yval));
                    Log.d("Received Value Z: ", Integer.toString(zval));
                }

                // A new AppMessage was received, tell Pebble
                PebbleKit.sendAckToPebble(context, transaction_id);
            }

        };
    }

    @Override
    public void onResume() {
        super.onResume();

        // Register the receiver
        PebbleKit.registerReceivedDataHandler(getApplicationContext(), dataReceiver);
    }
}
