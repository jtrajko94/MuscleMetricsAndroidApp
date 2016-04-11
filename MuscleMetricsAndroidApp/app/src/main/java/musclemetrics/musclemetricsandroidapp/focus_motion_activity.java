package musclemetrics.musclemetricsandroidapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
import io.focusmotion.sdk.MovementType;
import io.focusmotion.sdk.PathType;
import io.focusmotion.sdk.pebble.PebbleDevice;

/**
 * Created by JerunTrajko on 1/15/16.
 */
public class focus_motion_activity extends AppCompatActivity implements DeviceListener {

    //public static String biceps = MovementType.BICEPCURLS;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("onCreate", "onCreate Start");
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
            // populate spinner with analysis types
            m_analyzerSpinner = (Spinner) findViewById(R.id.analysis_spinner);
            String[] labels = new String[]
                    {
                            "Single movement",
                            "Multiple movement"
                    };
            ArrayAdapter analysisAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);
            analysisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            m_analyzerSpinner.setAdapter(analysisAdapter);

            m_analyzerSpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                        {
                            m_movementSpinner.setEnabled(pos != 1);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
        }

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
        Log.d("onCreate", "onCreate End");
    }

    @Override
    protected void onDestroy()
    {
        Log.d("onDestroy", "onDestroy");
        FocusMotion.shutdown();

        super.onDestroy();
    }

    @Override
    protected void onStart()
    {
        Log.d("onStart", "on Start");
        super.onStart();

        Device.onStart();

        updateStatusLabel();
        updateConnectButton();
        updateStartButton();
    }

    @Override
    protected void onStop()
    {
        Log.d("onStop", "on Stop");
        Device.onStop();

        super.onStop();
    }


    ////////////////////////////////////////

    // UI
    private Button m_startButton;
    private Button m_connectButton;
    private Spinner m_analyzerSpinner;
    private Spinner m_movementSpinner;
    private TextView m_resultsLabel;
    private TextView m_statusLabel;

    private Device m_device; // the current device


    ////////////////////////////////////////
    // button handlers

    private void onConnectButtonPressed()
    {
        Log.d("onConnectButtonPressed", "Start");
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
        Log.d("onConnectButtonPressed", "End");
    }

    private void onStartButtonPressed()
    {
        Log.d("onStartButtonPressed", "Start");
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
        Log.d("onStartButtonPressed", "End");
    }

    ////////////////////////////////////////

    private void updateStatusLabel()
    {
        Log.d("updateStatusLabel", "start");
        if (m_device != null)
        {
            m_statusLabel.setText(String.format("%s: %s", m_device.getName(), (m_device.isConnected() ? "connected" : "disconnected")));
        }
        else
        {
            m_statusLabel.setText(R.string.no_devices);
        }
        Log.d("updateStatusLabel", "end");
    }

    private void updateConnectButton()
    {
        Log.d("updateConnectButton", "start");
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
        Log.d("updateConnectButton", "end");
    }

    private void updateStartButton()
    {
        Log.d("updateStartButton", "start");
        if (m_device != null && m_device.isConnected())
        {
            m_startButton.setEnabled(true);

            if (m_device.isRecording())
            {
                m_startButton.setText(R.string.stop_recording);
            }
            else
            {
                m_startButton.setText(R.string.start_recording);
            }
        }
        else
        {
            m_startButton.setEnabled(false);
        }
        Log.d("updateStartButton", "end");
    }

    ////////////////////////////////////////
    // analysis

    private void analyze()
    {
        // get the data that has been sent from the device

        DeviceOutput data = Device.getLastConnectedDevice().getOutput();
        /*
        MovementAnalyzer analyzer = MovementAnalyzer.createSingleMovementAnalyzer("bicepcurls");
        Log.d("analyze", "here2");
        analyzer.analyze(data);
        Log.d("analyze", "here3");
        AnalyzerResult result = analyzer.getResult(0);
        Log.d("analyze", "here31");
        if(result.repCount > 0)
        {
            Log.d("analyze", "here32");
            showResults(analyzer);
        }
        Log.d("analyze", "here34");
        showResults(analyzer);
        Log.d("analyze", "here4");
        analyzer.destroy();
        */
        // which movement type?
        int movementPos = m_movementSpinner.getSelectedItemPosition();
        Log.d("analyze", Integer.toString(movementPos));
        String movementType = null;
        if (movementPos > 0)
        {
            Log.d("analyze", "movement type set");
            movementType = FocusMotion.getMovementType(movementPos-1);
        }

        Log.d("analyze", "here1");
        // which analyzer?
        //analyzerPos is null always
        int analyzerPos = m_analyzerSpinner.getSelectedItemPosition();
        Log.d("analyze", "here11");
        MovementAnalyzer analyzer = null;
        Log.d("analyze", "here12");
        //breaks here
        switch (analyzerPos)
        {
            case 0:
                Log.d("analyze", "here13");
                analyzer = MovementAnalyzer.createSingleMovementAnalyzer(movementType);
                Log.d("analyze", "here131");
                break;

            case 1:
                Log.d("analyze", "here14");
                analyzer = MovementAnalyzer.createMultipleMovementAnalyzer();
                break;
        }

        Log.d("analyze", "here2");

        analyzer.analyze(data);

        Log.d("analyze", "here3");
        showResults(analyzer);
        Log.d("analyze", "here4");
        analyzer.destroy();
        Log.d("analyze", "end");
    }

    private void showResults(MovementAnalyzer analyzer)
    {
        Log.d("showResults", "start");
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
                }
                text += "\n";
            }
            m_resultsLabel.setText(text);
        }
        else
        {
            m_resultsLabel.setText(R.string.no_results);
        }
        Log.d("showResults", "end");
    }


    ////////////////////////////////////////
    // DeviceListener

    @Override
    public void onAvailableChanged(Device device, boolean available)
    {
        Log.d("onAvailableChanged", "start");
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
        Log.d("onAvailableChanged", "end");
    }

    @Override
    public void onConnectedChanged(Device device, boolean connected)
    {
        Log.d("onConnectedChanged", "start");
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
        Log.d("onConnectedChanged", "end");
    }

    @Override
    public void onRecordingChanged(Device device, boolean recording)
    {
        Log.d("onRecordingChanged", "start");
        updateStartButton();
        if (!recording)
        {
            analyze();
        }
        else
        {
            m_resultsLabel.setText("");
        }
        Log.d("onRecordingChanged", "end");
    }

    @Override
    public void onDataReceived(Device device)
    {
        Log.d("onDataReceived", "start");
    }

    @Override
    public void onConnectionFailed(Device device, ConnectionError error, String message)
    {
        Log.d("onConnectionFailed", "start");
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
        Log.d("onConnectionFailed", "end");
    }
}
