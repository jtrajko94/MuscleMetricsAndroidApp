package musclemetrics.musclemetricsandroidapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import io.focusmotion.sdk.AnalyzerResult;
import io.focusmotion.sdk.Config;
import io.focusmotion.sdk.ConnectionError;
import io.focusmotion.sdk.Device;
import io.focusmotion.sdk.DeviceListener;
import io.focusmotion.sdk.DeviceOutput;
import io.focusmotion.sdk.FocusMotion;
import io.focusmotion.sdk.MovementAnalyzer;
import io.focusmotion.sdk.pebble.PebbleDevice;

/**
 * Created by JerunTrajko on 1/15/16.
 */
public class focus_motion_activity extends AppCompatActivity implements DeviceListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.focus_motion);

        m_startButton = (Button) findViewById(R.id.start_button);
        m_startButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        onStartButtonPressed();
                    }
                } );
        m_connectButton = (Button) findViewById(R.id.connect_button);
        m_connectButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        onConnectButtonPressed();
                    }
                } );

        m_resultsLabel = (TextView) findViewById(R.id.results_label);
        m_resultsLabel.setMovementMethod(new ScrollingMovementMethod());
        m_statusLabel = (TextView) findViewById(R.id.status_label);


        // initialize FocusMotion SDK
        Config config = new Config();

        // This is your API key; keep it secret!
        if (!FocusMotion.startup(config, "4taUjX7OKnx86EMdkJlEXjzTYhiPYk6e", this))
        {
            throw new Error("Could not initialize FocusMotion SDK");
        }

        // initialize general device support
        Device.addListener(this);

        // initialize Pebble support
        // the UUID is for the "simple" Pebble app, defined in fm/src/samples/simple/pebble/appinfo.json
        UUID uuid = UUID.fromString("4eb9f670-e798-4ce5-918f-db6c38b23846");
        PebbleDevice.startup(this, uuid);

        m_resultsLabel.setText("");
        updateStatusLabel();
        updateConnectButton();
        updateStartButton();

        {
            // populate spinner with movement types
            m_movementSpinner = (Spinner) findViewById(R.id.movement_spinner);
            ArrayList<String> labels = new ArrayList<>();

            labels.add("(unknown)");

            int numMovements = FocusMotion.getNumMovements();
            for (int i = 0; i < numMovements; ++i)
            {
                labels.add(FocusMotion.getMovementDisplayName(FocusMotion.getMovementType(i)));
            }

            ArrayAdapter movementAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);
            movementAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            m_movementSpinner.setAdapter(movementAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intentApp = new Intent(focus_motion_activity.this,
                workout_activity.class);
        startActivity(intentApp);
        finish();
    }

    @Override
    protected void onDestroy()
    {
        FocusMotion.shutdown();

        super.onDestroy();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Device.onStart();

        updateStatusLabel();
        updateConnectButton();
        updateStartButton();
    }

    @Override
    protected void onStop()
    {
        Device.onStop();

        super.onStop();
    }


    ////////////////////////////////////////

    // UI
    private Button m_startButton;
    private Button m_connectButton;
    //private Spinner m_analyzerSpinner;
    private Spinner m_movementSpinner;
    private TextView m_resultsLabel;
    private TextView m_statusLabel;

    private Device m_device; // the current device


    ////////////////////////////////////////
    // button handlers

    private void onConnectButtonPressed()
    {
        if (m_device != null)
        {
            if (!m_device.isConnected())
            {
                m_device.connect();
            }
            else
            {
                m_device.disconnect();
            }
        }

        updateConnectButton();
    }

    private void onStartButtonPressed()
    {
        if (m_device != null)
        {
            if (m_device.isRecording())
            {
                m_device.stopRecording();
            }
            else
            {
                m_device.startRecording();
            }
        }
    }

    ////////////////////////////////////////

    private void updateStatusLabel()
    {
        if (m_device != null)
        {
            m_statusLabel.setText(String.format("%s: %s", m_device.getName(), (m_device.isConnected() ? "connected" : "disconnected")));
        }
        else
        {
            m_statusLabel.setText(R.string.no_devices);
        }
    }

    private void updateConnectButton()
    {
        if (m_device != null)
        {
            if (m_device.isConnected())
            {
                m_connectButton.setText(R.string.disconnect);
                m_connectButton.setEnabled(true);
            }
            else if (m_device.isConnecting())
            {
                m_connectButton.setText(R.string.connecting);
                m_connectButton.setEnabled(false);
            }
            else
            {
                m_connectButton.setEnabled(true);
                m_connectButton.setText(R.string.connect);
            }
        }
        else
        {
            m_connectButton.setText(R.string.connect);
            m_connectButton.setEnabled(false);
        }
    }

    private void updateStartButton()
    {
        if (m_device != null && m_device.isConnected())
        {
            m_startButton.setEnabled(true);

            if (m_device.isRecording())
            {
                m_startButton.setText(R.string.stop_recording);
                m_connectButton.setEnabled(false);
            }
            else
            {
                m_startButton.setText(R.string.start_recording);
                m_connectButton.setEnabled(true);
            }
        }
        else
        {
            m_startButton.setEnabled(false);
        }
    }

    ////////////////////////////////////////
    // analysis

    private void analyze()
    {
        DeviceOutput data = Device.getLastConnectedDevice().getOutput();

        // which movement type?
        int movementPos = m_movementSpinner.getSelectedItemPosition();
        String movementType = null;
        if (movementPos > 0)
        {
            movementType = FocusMotion.getMovementType(movementPos-1);
        }
        MovementAnalyzer analyzer = null;
        analyzer = MovementAnalyzer.createSingleMovementAnalyzer(movementType);
        analyzer.analyze(data);
        showResults(analyzer);
        analyzer.destroy();
    }

    private void showResults(MovementAnalyzer analyzer)
    {
        if (analyzer.getNumResults() > 0)
        {
            String text = "";
            for (int i = 0; i < analyzer.getNumResults(); ++i)
            {
                AnalyzerResult result = analyzer.getResult(i);
                if (result.movementType.equals("resting"))
                {
                    text += String.format("Resting: %.2fs\n", result.duration);
                }
                else
                {
                    text += String.format(
                            "%s\n" +
                                    "  %d reps\n" +
                                    "  duration %.2fs\n" +
                                    "  rep time %.2f (%.2f-%.2f)\n" +
                                    "  variation %.2f\n" +
                                    "  ref variation %.2f\n" +
                                    "  ref rep time %.2f\n",
                            FocusMotion.getMovementDisplayName(result.movementType),
                            result.repCount,
                            result.duration,
                            result.meanRepTime, result.minRepTime, result.maxRepTime,
                            result.internalVariation,
                            result.referenceVariation,
                            result.referenceRepTime);
                    sendPebbleInfo(result.repCount, FocusMotion.getMovementDisplayName(result.movementType));
                }
                text += "\n";
            }
            m_resultsLabel.setText(text);
        }
        else
        {
            m_resultsLabel.setText(R.string.no_results);
        }
    }


    ////////////////////////////////////////
    // DeviceListener

    @Override
    public void onAvailableChanged(Device device, boolean available)
    {
        if (available && m_device == null)
        {
            // didn't have a device before; set this to the current one
            m_device = device;
        }

        if (!available && m_device == device)
        {
            // just lost our current device; get another one, if possible
            m_device = Device.getAvailableDevices().get(0);
        }

        updateConnectButton();
        updateStatusLabel();
    }

    @Override
    public void onConnectedChanged(Device device, boolean connected)
    {
        if (!connected)
        {
            List<Device> availableDevices = Device.getAvailableDevices();
            if (availableDevices.isEmpty())
            {
                m_device = null;
            }
            else
            {
                // choose the next device
                int index = availableDevices.indexOf(device);
                ++index;
                if (index >= availableDevices.size())
                {
                    index = 0;
                }
                m_device = availableDevices.get(index);
            }
        }

        updateConnectButton();
        updateStartButton();
        updateStatusLabel();
    }

    @Override
    public void onRecordingChanged(Device device, boolean recording)
    {
        updateStartButton();
        if (!recording)
        {
            analyze();
        }
        else
        {
            m_resultsLabel.setText("");
        }
    }

    @Override
    public void onDataReceived(Device device)
    {
        //Data has been received, don't need to do anything
    }

    @Override
    public void onConnectionFailed(Device device, ConnectionError error, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle("Connection failed!");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                } );
        builder.create().show();

        updateConnectButton();
        updateStatusLabel();
    }

    //Send data to the pebble
    public void sendPebbleInfo(int rep, String workout)
    {
        // Create a new dictionary
        PebbleDictionary dict = new PebbleDictionary();

        // The key representing a contact name is being transmitted
        final int repKey = 0;
        final int workoutKey = 1;

        // Add data to the dictionary
        dict.addString(workoutKey, workout);
        dict.addInt32(repKey, rep);

        final UUID appUuid = UUID.fromString("4eb9f670-e798-4ce5-918f-db6c38b23846");

        // Send the dictionary
        PebbleKit.sendDataToPebble(getApplicationContext(), appUuid, dict);
    }
}
