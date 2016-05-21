package musclemetrics.musclemetricsandroidapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.BoolRes;
import android.support.annotation.IntegerRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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
import io.focusmotion.sdk.FocusMotionService;
import io.focusmotion.sdk.MovementAnalyzer;
import io.focusmotion.sdk.pebble.PebbleDevice;

//TODO: App should close when the onStop method is called (track why it doesnt)
//TODO: Add the progress bar thread again
public class workout_activity extends AppCompatActivity implements DeviceListener {

    LineChart chart;
    ArrayList<String> xVals = new ArrayList<String>();
    private Handler mHandler = new Handler();
    private Spinner dropdown;
    private Toolbar toolbar;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private TextView repsCompleted;

    private Button m_startButton;
    private Button m_connectButton;
    private TextView m_statusLabel;
    private PebbleDevice m_device; // the current device


    //Change these with every excercise
    private String excerciseType = "bicepcurls";
    private int numReps = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.full_toolbar_work);
        System.out.println("Creating Workout View -----------------");

        repsCompleted = (TextView) findViewById(R.id.repText);

        //Setting Top Toolbar
        setTopToolbar();

        //Setting spinner menu on top toolbar
        setTopSpinner();

        //Setting Circular Progress Bar
        setProgressBar(0);

        setChart();

        //Populate the chart with info
        setExcercise(0);

        chart.invalidate(); // refresh

        m_startButton = (Button) findViewById(R.id.record);
        m_startButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        onStartButtonPressed();
                    }
                } );
        m_connectButton = (Button) findViewById(R.id.connect);
        m_connectButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        onConnectButtonPressed();
                    }
                } );
        m_statusLabel = (TextView) findViewById(R.id.textView17);

        Config config = new Config();

        // This is your API key; keep it secret!
        Log.d("initialized FM", "FM all good");
        if (!FocusMotion.startup(config, "4taUjX7OKnx86EMdkJlEXjzTYhiPYk6e", this))
        {
            throw new Error("Could not initialize FocusMotion SDK");
        }

        // initialize general device support
        PebbleDevice.addListener(this);

        // initialize Pebble support
        // the UUID is for the "simple" Pebble app, defined in fm/src/samples/simple/pebble/appinfo.json
        UUID uuid = UUID.fromString("4eb9f670-e798-4ce5-918f-db6c38b23846");
        PebbleDevice.startup(this, uuid);

        //Setting Bottom Toolbar
        setBottomToolbar();

        updateStatusLabel();
        updateConnectButton();
        updateStartButton();
    }

    @Override
    protected void onDestroy()
    {
        //FocusMotion.shutdown();

        super.onDestroy();
    }

    @Override
    protected void onStart()
    {
        Log.d("onStart", "first");
        Log.d("IsInitialized?", Boolean.toString(FocusMotion.isInitialized()));
        super.onStart();

        PebbleDevice.onStart();

        updateStatusLabel();
        updateConnectButton();
        updateStartButton();
    }

    @Override
    protected void onStop()
    {
        Log.d("onStop", "stop");
        PebbleDevice.onStop();
        //FocusMotion.shutdown();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.workout_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setBottomToolbar()
    {
        final ImageButton lib = (ImageButton) findViewById(R.id.libraryButton);
        lib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(workout_activity.this,
                        library_activity.class);
                m_device = (PebbleDevice)PebbleDevice.getAvailableDevices().get(0);
                m_device.connect();
                if (m_device != null && m_device.isConnected())
                {
                        Log.d("onConnect", "disconnect");
                        m_device.disconnect();
                        //PebbleDevice.shutdown();
                }
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
                //overridePendingTransition(0, 0);
            }
        });

        final ImageButton cal = (ImageButton) findViewById(R.id.calendarButton);
        cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(workout_activity.this,
                        calendar_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton pro = (ImageButton) findViewById(R.id.progressButton);
        pro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(workout_activity.this,
                        progress_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton prof = (ImageButton) findViewById(R.id.profileButton);
        prof.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(workout_activity.this,
                        profile_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intentApp);
                finish();
            }
        });


        final ImageButton info = (ImageButton) findViewById(R.id.button3);
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(workout_activity.this,
                        excercise_muscle_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intentApp);
                finish();
            }
        });
    }

    private void setTopToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Workout");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void setChart()
    {
        chart = (LineChart) findViewById(R.id.chart);
        chart.setDescription("One Rep Maxes with Time");
        chart.animateX(5000);
        chart.animateY(5000);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.argb(255, 12, 194, 247));
        xAxis.setDrawAxisLine(true);
        xAxis.setLabelsToSkip(0);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(10f); // set the textsize
        //yAxis.setAxisMaxValue(800f); // the axis maximum is 100
        yAxis.setTextColor(Color.BLACK);
        yAxis.setLabelCount(6, false);
        //yAxis.setDrawLabels(false);

        xVals.add("May"); xVals.add("Jun"); xVals.add("Jul"); xVals.add("Aug");
        xVals.add("Sep"); xVals.add("Oct"); xVals.add("Nov"); xVals.add("Dec");
        xVals.add("Jan"); xVals.add("Feb"); xVals.add("March"); xVals.add("Apr");

        Legend l = chart.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis

        // set custom labels and colors
        int[] color = new int[1];
        color[0] = Color.argb(255, 12, 194, 247);
        String excercise = "Bicep Curls with Dumbbell";
        String [] moves = new String[1];
        moves[0] = excercise;
        l.setCustom(color, moves);
    }

    private void setExcercise(float last)
    {
        ArrayList<Entry> excercisePoints = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(3.000f, 0); // 0 == quarter 1
        excercisePoints.add(c1e1);
        Entry c1e2 = new Entry(5.000f, 1); // 1 == quarter 2 ...
        excercisePoints.add(c1e2);
        Entry c1e3 = new Entry(7.000f, 2);
        excercisePoints.add(c1e3);
        Entry c1e4 = new Entry(7.000f, 3);
        excercisePoints.add(c1e4);
        Entry c1e5 = new Entry(8.000f, 4);
        excercisePoints.add(c1e5);
        Entry c1e6 = new Entry(9.000f, 5);
        excercisePoints.add(c1e6);
        Entry c1e7 = new Entry(9.300f, 6);
        excercisePoints.add(c1e7);
        Entry c1e8 = new Entry(9.500f, 7);
        excercisePoints.add(c1e8);
        Entry c1e9 = new Entry(9.700f, 8);
        excercisePoints.add(c1e9);
        Entry c1e10 = new Entry(9.900f, 9);
        excercisePoints.add(c1e10);
        Entry c1e11 = new Entry(10.000f, 10);
        excercisePoints.add(c1e11);
        Entry c1e12 = new Entry(last, 11);
        excercisePoints.add(c1e12);
        // and so on ...

        LineDataSet setComp1 = new LineDataSet(excercisePoints, "Excercise");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.invalidate(); // refresh
    }

    private void setProgressBar(double per)
    {
        Log.d("setting progress bar", Double.toString(per));
        mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgressStatus = 0;
        final double percent = per*100;
        Log.d("setting progress bar", Double.toString(percent));

        //Start the progress bar at 0
        mHandler.post(new Runnable() {
            public void run() {
                mProgress.setProgress(0);
            }
        });

        // Start lengthy operation in a background thread to increase the slider
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < percent) {
                    mProgressStatus = mProgressStatus + 1;
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }
            }
        }).start();
    }

    private void setTopSpinner()
    {
        dropdown = (Spinner)findViewById(R.id.spinner_nav);
        String[] items = new String[]{"Workout Info","Create a Workout", "My Workouts"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                //String selectedItem =  adapter.getItemAtPosition(i).toString();
                //Toast.makeText(getBaseContext(),Integer.toString(i),
                      //Toast.LENGTH_SHORT).show();
                if(i == 1) //create workout
                {
                    Intent intentApp = new Intent(workout_activity.this,
                            all_workouts_activity.class);
                    intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentApp.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intentApp);
                    finish();
                }
                //or this can be also right: selecteditem = level[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });
    }

    private void onConnectButtonPressed()
    {
        Log.d("onConnect", "start");
        if (m_device != null)
        {
            if (!m_device.isConnected())
            {
                Log.d("onConnect", "connect");
                m_device.connect();
            }
            else
            {
                Log.d("onConnect", "disconnect");
                m_device.disconnect();
                //m_device.stopRecording();
            }
        }

        updateConnectButton();
    }

    private void onStartButtonPressed()
    {
        Log.d("onStart", "First");
        if (m_device != null)
        {
            if (m_device.isRecording())
            {
                Log.d("onStart", "First if");
                m_device.stopRecording();
            }
            else
            {
                Log.d("onStart", "Second if");
                m_device.startRecording();
            }
        }
    }

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
        Log.d("updateConnectButton", "first");
        if (m_device != null)
        {
            if (m_device.isConnected())
            {
                Log.d("updateConnectButton", "1 if");
                m_connectButton.setText(R.string.disconnect);
                m_connectButton.setEnabled(true);
            }
            else if (m_device.isConnecting())
            {
                Log.d("updateConnectButton", "2 if");
                m_connectButton.setText(R.string.connecting);
                m_connectButton.setEnabled(false);
            }
            else
            {
                Log.d("updateConnectButton", "3 if");
                m_connectButton.setEnabled(true);
                m_connectButton.setText(R.string.connect);
            }
        }
        else
        {
            Log.d("updateConnectButton", "big else");
            m_connectButton.setText(R.string.connect);
            m_connectButton.setEnabled(false);
        }
    }

    private void updateStartButton()
    {
        m_device = (PebbleDevice)PebbleDevice.getAvailableDevices().get(0);
        Log.d("updateStartButton", "first");
        if (m_device != null && m_device.isConnected())
        {
            Log.d("updateStartButton", "if 1");
            m_startButton.setEnabled(true);

            if (m_device.isRecording())
            {
                Log.d("updateStartButton", "if 2");
                m_startButton.setText(R.string.stop_recording);
                m_connectButton.setEnabled(false);
            }
            else
            {
                Log.d("updateStartButton", "if 3");
                m_startButton.setText(R.string.start_recording);
                m_connectButton.setEnabled(true);
            }
        }
        else
        {
            Log.d("updateStartButton", "if 4");
            m_startButton.setEnabled(false);
        }
    }

    private void analyze()
    {
        DeviceOutput data = PebbleDevice.getLastConnectedDevice().getOutput();
        String movementType = excerciseType;
        MovementAnalyzer analyzer = MovementAnalyzer.createSingleMovementAnalyzer(movementType);
        analyzer.analyze(data);
        showResults(analyzer);
        analyzer.destroy();
    }

    private void showResults(MovementAnalyzer analyzer)
    {
        if (analyzer.getNumResults() > 0)
        {
            String text = "";
            AnalyzerResult result = analyzer.getResult(0);
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
            Log.d("Results:", text);
            Toast.makeText(getBaseContext(),"Excercise Completed!",
                    Toast.LENGTH_SHORT).show();
            repsCompleted.setText(result.repCount + " Reps");
            float weight = (float)10;
            float one = (float)1;
            float repCount = (float)result.repCount;
            float div = repCount/(float)30;
            float temp = one + div;
            float oneRepMax = (weight * temp);
            setExcercise(oneRepMax);
            double percentCompleted = (double) result.repCount/numReps;
            if(percentCompleted > 1) {
                percentCompleted = 1;
            }
            setProgressBar(percentCompleted);

        }
        else
        {
            setProgress(0);
            repsCompleted.setText("0 Reps");
        }
    }



    @Override
    public void onAvailableChanged(Device device, boolean available)
    {
        Log.d("on Available Changed", "start");
        if (available && m_device == null)
        {
            Log.d("on Available Changed", "first if");
            // didn't have a device before; set this to the current one
            m_device = (PebbleDevice)device;
        }

        if (!available && m_device == device)
        {
            Log.d("on Available Changed", "second if");
            // just lost our current device; get another one, if possible
            m_device = (PebbleDevice)Device.getAvailableDevices().get(0);
        }

        updateConnectButton();
        updateStatusLabel();
    }

    @Override
    public void onConnectedChanged(Device device, boolean connected)
    {
        Log.d("on connected changed", "start");
        if (!connected)
        {
            List<Device> availableDevices = Device.getAvailableDevices();
            if (availableDevices.isEmpty())
            {
                Log.d("on connected changed", "first if");
                m_device = null;
            }
            else
            {
                Log.d("on connected changed", "second if");
                // choose the next device
                int index = availableDevices.indexOf(device);
                ++index;
                if (index >= availableDevices.size())
                {
                    index = 0;
                }
                m_device = (PebbleDevice)availableDevices.get(index);
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
            Log.d("Notice", "Cleared results");
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
        /*
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
        */
        Toast.makeText(getBaseContext(),"Connection Timed Out!",
                Toast.LENGTH_SHORT).show();

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