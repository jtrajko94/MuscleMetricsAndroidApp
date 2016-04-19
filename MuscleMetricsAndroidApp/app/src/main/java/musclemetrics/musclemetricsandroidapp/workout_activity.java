package musclemetrics.musclemetricsandroidapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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

import io.focusmotion.sdk.ConnectionError;
import io.focusmotion.sdk.Device;
import io.focusmotion.sdk.DeviceListener;

public class workout_activity extends AppCompatActivity implements DeviceListener {

    LineChart chart;
    ArrayList<String> xVals = new ArrayList<String>();
    private Handler mHandler = new Handler();
    private Spinner dropdown;
    private Toolbar toolbar;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.full_toolbar_work);
        System.out.println("Creating Workout View -----------------");
        String PACKAGE_NAME = getApplicationContext().getPackageName();
        System.out.println(PACKAGE_NAME);

        //Setting Bottom Toolbar
        setBottomToolbar();

        //Setting Top Toolbar
        setTopToolbar();

        //Setting spinner menu on top toolbar
        setTopSpinner();

        //Setting Circular Progress Bar
        setProgressBar();

        setChart();

        //Populate the chart with info
        setExcercise();

        chart.invalidate(); // refresh

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


        final ImageButton pebble = (ImageButton) findViewById(R.id.button3);
        pebble.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(workout_activity.this,
                        focus_motion_activity.class);
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

        xVals.add("Jan"); xVals.add("Feb"); xVals.add("Mar"); xVals.add("Apr");
        xVals.add("May"); xVals.add("Jun"); xVals.add("Jul"); xVals.add("Aug");
        xVals.add("Sept"); xVals.add("Oct"); xVals.add("Nov"); xVals.add("Dec");

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

    private void setExcercise()
    {
        ArrayList<Entry> excercisePoints = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(30.000f, 0); // 0 == quarter 1
        excercisePoints.add(c1e1);
        Entry c1e2 = new Entry(40.000f, 1); // 1 == quarter 2 ...
        excercisePoints.add(c1e2);
        Entry c1e3 = new Entry(50.000f, 2);
        excercisePoints.add(c1e3);
        Entry c1e4 = new Entry(50.000f, 3);
        excercisePoints.add(c1e4);
        Entry c1e5 = new Entry(52.000f, 4);
        excercisePoints.add(c1e5);
        Entry c1e6 = new Entry(55.000f, 5);
        excercisePoints.add(c1e6);
        Entry c1e7 = new Entry(60.000f, 6);
        excercisePoints.add(c1e7);
        Entry c1e8 = new Entry(62.000f, 7);
        excercisePoints.add(c1e8);
        Entry c1e9 = new Entry(63.000f, 8);
        excercisePoints.add(c1e9);
        Entry c1e10 = new Entry(66.000f, 9);
        excercisePoints.add(c1e10);
        Entry c1e11 = new Entry(70.000f, 10);
        excercisePoints.add(c1e11);
        Entry c1e12 = new Entry(75.000f, 11);
        excercisePoints.add(c1e12);
        // and so on ...

        LineDataSet setComp1 = new LineDataSet(excercisePoints, "Excercise");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        //chart.invalidate(); // refresh
    }

    private void setProgressBar()
    {
        mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);

        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus = mProgressStatus + 1;
                    try {
                        Thread.sleep(1000);
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

                String selectedItem =  adapter.getItemAtPosition(i).toString();
                Toast.makeText(getBaseContext(),selectedItem,
                        Toast.LENGTH_SHORT).show();
                //or this can be also right: selecteditem = level[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });
    }

    @Override
    public void onAvailableChanged(Device device, boolean b) {

    }

    @Override
    public void onConnectedChanged(Device device, boolean b) {

    }

    @Override
    public void onRecordingChanged(Device device, boolean b) {

    }

    @Override
    public void onDataReceived(Device device) {

    }

    @Override
    public void onConnectionFailed(Device device, ConnectionError connectionError, String s) {

    }
}
