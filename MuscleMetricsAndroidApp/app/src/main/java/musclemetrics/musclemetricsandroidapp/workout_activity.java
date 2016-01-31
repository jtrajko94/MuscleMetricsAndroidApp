package musclemetrics.musclemetricsandroidapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class workout_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.full_toolbar_work);
        System.out.println("Creating Workout View -----------------");

        //Setting Bottom Toolbar
        setBottomToolbar();

        //Setting Top Toolbar
        setTopToolbar();

        //Setting spinner menu on top toolbar
        setTopSpinner();

        //Setting Circular Progress Bar
        setProgressBar();

        setChart();

        setBenchPress();
        setSquat();

        //setDeadlift();
        //setPress();
        //setRow();

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setBottomToolbar()
    {
        final Button lib = (Button) findViewById(R.id.libraryButton);
        lib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(workout_activity.this,
                        library_activity.class);
                startActivity(intentApp);
            }
        });

        final Button cal = (Button) findViewById(R.id.calendarButton);
        cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(workout_activity.this,
                        calendar_activity.class);
                startActivity(intentApp);
                // Perform action on click
            }
        });

        final Button pro = (Button) findViewById(R.id.progressButton);
        pro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(workout_activity.this,
                        progress_activity.class);
                startActivity(intentApp);
                // Perform action on click
            }
        });

        final Button prof = (Button) findViewById(R.id.profileButton);
        prof.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(workout_activity.this,
                        profile_activity.class);
                startActivity(intentApp);
                // Perform action on click
            }
        });
    }

    private void setTopToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setChart()
    {
        chart = (LineChart) findViewById(R.id.chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
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
        l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "Bench", "Squat", "Deadlift", "Press", "Row" });
    }

    private void setBenchPress()
    {
        ArrayList<Entry> benchPoints = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
        benchPoints.add(c1e1);
        Entry c1e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
        benchPoints.add(c1e2);
        Entry c1e3 = new Entry(120.000f, 2); // 0 == quarter 1
        benchPoints.add(c1e3);
        Entry c1e4 = new Entry(130.000f, 3); // 1 == quarter 2 ...
        benchPoints.add(c1e4);
        Entry c1e5 = new Entry(135.000f, 4); // 0 == quarter 1
        benchPoints.add(c1e5);
        Entry c1e6 = new Entry(140.000f, 5); // 1 == quarter 2 ...
        benchPoints.add(c1e6);
        Entry c1e7 = new Entry(140.000f, 6); // 0 == quarter 1
        benchPoints.add(c1e7);
        Entry c1e8 = new Entry(143.000f, 7); // 1 == quarter 2 ...
        benchPoints.add(c1e8);
        Entry c1e9 = new Entry(145.000f, 8); // 0 == quarter 1
        benchPoints.add(c1e9);
        Entry c1e10 = new Entry(150.000f, 9); // 1 == quarter 2 ...
        benchPoints.add(c1e10);
        Entry c1e11 = new Entry(150.000f, 10); // 0 == quarter 1
        benchPoints.add(c1e11);
        Entry c1e12 = new Entry(155.000f, 11); // 1 == quarter 2 ...
        benchPoints.add(c1e12);
        // and so on ...

        LineDataSet setComp1 = new LineDataSet(benchPoints, "Bench");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        //chart.invalidate(); // refresh
    }

    private void setSquat()
    {
        ArrayList<Entry> benchPoints = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
        benchPoints.add(c1e1);
        Entry c1e2 = new Entry(130.000f, 1); // 1 == quarter 2 ...
        benchPoints.add(c1e2);
        Entry c1e3 = new Entry(150.000f, 2); // 0 == quarter 1
        benchPoints.add(c1e3);
        Entry c1e4 = new Entry(160.000f, 3); // 1 == quarter 2 ...
        benchPoints.add(c1e4);
        Entry c1e5 = new Entry(160.000f, 4); // 0 == quarter 1
        benchPoints.add(c1e5);
        Entry c1e6 = new Entry(180.000f, 5); // 1 == quarter 2 ...
        benchPoints.add(c1e6);
        Entry c1e7 = new Entry(190.000f, 6); // 0 == quarter 1
        benchPoints.add(c1e7);
        Entry c1e8 = new Entry(195.000f, 7); // 1 == quarter 2 ...
        benchPoints.add(c1e8);
        Entry c1e9 = new Entry(200.000f, 8); // 0 == quarter 1
        benchPoints.add(c1e9);
        Entry c1e10 = new Entry(230.000f, 9); // 1 == quarter 2 ...
        benchPoints.add(c1e10);
        Entry c1e11 = new Entry(250.000f, 10); // 0 == quarter 1
        benchPoints.add(c1e11);
        Entry c1e12 = new Entry(255.000f, 11); // 1 == quarter 2 ...
        benchPoints.add(c1e12);
        // and so on ...

        LineDataSet setComp1 = new LineDataSet(benchPoints, "Squat");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        //chart.invalidate(); // refresh
    }

    private void setDeadlift()
    {
        ArrayList<Entry> benchPoints = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
        benchPoints.add(c1e1);
        Entry c1e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
        benchPoints.add(c1e2);
        Entry c1e3 = new Entry(120.000f, 2); // 0 == quarter 1
        benchPoints.add(c1e3);
        Entry c1e4 = new Entry(130.000f, 3); // 1 == quarter 2 ...
        benchPoints.add(c1e4);
        Entry c1e5 = new Entry(135.000f, 4); // 0 == quarter 1
        benchPoints.add(c1e5);
        Entry c1e6 = new Entry(140.000f, 5); // 1 == quarter 2 ...
        benchPoints.add(c1e6);
        Entry c1e7 = new Entry(140.000f, 6); // 0 == quarter 1
        benchPoints.add(c1e7);
        Entry c1e8 = new Entry(143.000f, 7); // 1 == quarter 2 ...
        benchPoints.add(c1e8);
        Entry c1e9 = new Entry(145.000f, 8); // 0 == quarter 1
        benchPoints.add(c1e9);
        Entry c1e10 = new Entry(150.000f, 9); // 1 == quarter 2 ...
        benchPoints.add(c1e10);
        Entry c1e11 = new Entry(150.000f, 10); // 0 == quarter 1
        benchPoints.add(c1e11);
        Entry c1e12 = new Entry(250.000f, 11); // 1 == quarter 2 ...
        benchPoints.add(c1e12);
        // and so on ...

        LineDataSet setComp1 = new LineDataSet(benchPoints, "Bench");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.invalidate(); // refresh
    }

    private void setRow()
    {
        ArrayList<Entry> benchPoints = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
        benchPoints.add(c1e1);
        Entry c1e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
        benchPoints.add(c1e2);
        Entry c1e3 = new Entry(120.000f, 2); // 0 == quarter 1
        benchPoints.add(c1e3);
        Entry c1e4 = new Entry(130.000f, 3); // 1 == quarter 2 ...
        benchPoints.add(c1e4);
        Entry c1e5 = new Entry(135.000f, 4); // 0 == quarter 1
        benchPoints.add(c1e5);
        Entry c1e6 = new Entry(140.000f, 5); // 1 == quarter 2 ...
        benchPoints.add(c1e6);
        Entry c1e7 = new Entry(140.000f, 6); // 0 == quarter 1
        benchPoints.add(c1e7);
        Entry c1e8 = new Entry(143.000f, 7); // 1 == quarter 2 ...
        benchPoints.add(c1e8);
        Entry c1e9 = new Entry(145.000f, 8); // 0 == quarter 1
        benchPoints.add(c1e9);
        Entry c1e10 = new Entry(150.000f, 9); // 1 == quarter 2 ...
        benchPoints.add(c1e10);
        Entry c1e11 = new Entry(150.000f, 10); // 0 == quarter 1
        benchPoints.add(c1e11);
        Entry c1e12 = new Entry(250.000f, 11); // 1 == quarter 2 ...
        benchPoints.add(c1e12);
        // and so on ...

        LineDataSet setComp1 = new LineDataSet(benchPoints, "Bench");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.invalidate(); // refresh
    }

    private void setPress()
    {
        ArrayList<Entry> benchPoints = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
        benchPoints.add(c1e1);
        Entry c1e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
        benchPoints.add(c1e2);
        Entry c1e3 = new Entry(120.000f, 2); // 0 == quarter 1
        benchPoints.add(c1e3);
        Entry c1e4 = new Entry(130.000f, 3); // 1 == quarter 2 ...
        benchPoints.add(c1e4);
        Entry c1e5 = new Entry(135.000f, 4); // 0 == quarter 1
        benchPoints.add(c1e5);
        Entry c1e6 = new Entry(140.000f, 5); // 1 == quarter 2 ...
        benchPoints.add(c1e6);
        Entry c1e7 = new Entry(140.000f, 6); // 0 == quarter 1
        benchPoints.add(c1e7);
        Entry c1e8 = new Entry(143.000f, 7); // 1 == quarter 2 ...
        benchPoints.add(c1e8);
        Entry c1e9 = new Entry(145.000f, 8); // 0 == quarter 1
        benchPoints.add(c1e9);
        Entry c1e10 = new Entry(150.000f, 9); // 1 == quarter 2 ...
        benchPoints.add(c1e10);
        Entry c1e11 = new Entry(150.000f, 10); // 0 == quarter 1
        benchPoints.add(c1e11);
        Entry c1e12 = new Entry(250.000f, 11); // 1 == quarter 2 ...
        benchPoints.add(c1e12);
        // and so on ...

        LineDataSet setComp1 = new LineDataSet(benchPoints, "Bench");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.invalidate(); // refresh
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
        String[] items = new String[]{"","Option 1", "Option 2", "Option 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
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
}
