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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by JerunTrajko on 1/15/16.
 */
public class progress_activity extends AppCompatActivity {

    LineChart chart1;
    //private Handler mHandler = new Handler();

    BarChart chart2;
    //private Handler mHandler2 = new Handler();


    SegmentedGroup segmentedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Starting Progress ----------------------");
        overridePendingTransition(0, 0);;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_toolbar_prog);

        //Set bottom toolbar
        setBottomToolbar();

        //set top toolbar
        setTopToolbar();

        //set first chart
        setWeight();
        setCalories();


        final RadioButton compR = (RadioButton) findViewById(R.id.comp);
        final RadioButton sndR = (RadioButton) findViewById(R.id.snd);
        //final RadioButton wncR = (RadioButton) findViewById(R.id.wnc);

        segmentedGroup = (SegmentedGroup) findViewById(R.id.segmented2);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == compR.getId())
                {
                    setComp();
                    Toast.makeText(getBaseContext(), "Comp",
                            Toast.LENGTH_SHORT).show();
                }
                else if(checkedId == sndR.getId())
                {

                    Toast.makeText(getBaseContext(), "sndR",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    setWeight();
                    setCalories();
                    Toast.makeText(getBaseContext(), "wncR",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


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

    //set bottom toolbar
    private void setBottomToolbar()
    {
        final ImageButton work = (ImageButton) findViewById(R.id.workoutButton);
        work.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(progress_activity.this,
                        workout_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton lib = (ImageButton) findViewById(R.id.libraryButton);
        lib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(progress_activity.this,
                        library_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton cal = (ImageButton) findViewById(R.id.calendarButton);
        cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(progress_activity.this,
                        calendar_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton prof = (ImageButton) findViewById(R.id.profileButton);
        prof.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(progress_activity.this,
                        profile_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });
    }

    private void setTopToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Progress");
        setSupportActionBar(toolbar);
    }

    private void setWeight()
    {
        chart1 = (LineChart) findViewById(R.id.chart1);
        //Set the datapoints
        ArrayList<Entry> entries2 = new ArrayList<Entry>();
        entries2.add(new Entry(100f, 0));
        entries2.add(new Entry(120f, 1));
        entries2.add(new Entry(30f, 2));
        entries2.add(new Entry(40f, 3));
        entries2.add(new Entry(200f, 4));
        entries2.add(new Entry(100f, 5));
        entries2.add(new Entry(130f, 6));
        entries2.add(new Entry(100f, 7));
        entries2.add(new Entry(120f, 8));
        entries2.add(new Entry(30f, 9));
        entries2.add(new Entry(40f, 10));
        entries2.add(new Entry(200f, 11));

        LineDataSet dataSet = new LineDataSet(entries2, "Weight");

        XAxis xAxis = chart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);

        YAxis yAxis = chart1.getAxisLeft();
        yAxis.setTextSize(10f); // set the textsize
        //yAxis.setAxisMaxValue(800f); // the axis maximum is 100
        yAxis.setTextColor(Color.BLACK);
        yAxis.setLabelCount(6, false);
        //yAxis.setDrawLabels(false);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Jan");
        labels.add("Feb");
        labels.add("Mar");
        labels.add("Apr");
        labels.add("May");
        labels.add("Jun");
        labels.add("Jul");
        labels.add("Aug");
        labels.add("Sep");
        labels.add("Oct");
        labels.add("Nov");
        labels.add("Dec");

        LineData data = new LineData(labels, dataSet);
        chart1.setData(data);
        chart1.setDescription("My Weight");
        chart1.invalidate(); // refresh
    }

    private void setCalories()
    {
        chart2 = (BarChart) findViewById(R.id.chart2);
        //Set the datapoints
        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();
        entries2.add(new BarEntry(100f, 0));
        entries2.add(new BarEntry(120f, 1));
        entries2.add(new BarEntry(30f, 2));
        entries2.add(new BarEntry(40f, 3));
        entries2.add(new BarEntry(200f, 4));
        entries2.add(new BarEntry(100f, 5));
        entries2.add(new BarEntry(130f, 6));

        BarDataSet dataSet = new BarDataSet(entries2, "Calories Burned");

        XAxis xAxis = chart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);

        YAxis yAxis = chart2.getAxisLeft();
        yAxis.setTextSize(10f); // set the textsize
        //yAxis.setAxisMaxValue(800f); // the axis maximum is 100
        yAxis.setTextColor(Color.BLACK);
        yAxis.setLabelCount(6, false);
        //yAxis.setDrawLabels(false);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Sun");
        labels.add("Mon");
        labels.add("Tues");
        labels.add("Wed");
        labels.add("Thurs");
        labels.add("Fri");
        labels.add("Sat");

        BarData data = new BarData(labels, dataSet);
        chart2.setData(data);
        chart2.setDescription("Calories Burned");
        chart2.invalidate(); // refresh
    }

    private void setComp()
    {
        chart1 = (LineChart) findViewById(R.id.chart1);
        //Set the datapoints
        ArrayList<Entry> entries2 = new ArrayList<Entry>();
        entries2.add(new Entry(50, 0));
        entries2.add(new Entry(60f, 1));
        entries2.add(new Entry(30f, 2));
        entries2.add(new Entry(20f, 3));
        entries2.add(new Entry(20f, 4));
        entries2.add(new Entry(100f, 5));
        entries2.add(new Entry(120f, 6));
        entries2.add(new Entry(120f, 7));
        entries2.add(new Entry(110f, 8));
        entries2.add(new Entry(60f, 9));
        entries2.add(new Entry(80f, 10));
        entries2.add(new Entry(100f, 11));

        LineDataSet dataSet = new LineDataSet(entries2, "Body Fat");

        XAxis xAxis = chart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);

        YAxis yAxis = chart1.getAxisLeft();
        yAxis.setTextSize(10f); // set the textsize
        //yAxis.setAxisMaxValue(800f); // the axis maximum is 100
        yAxis.setTextColor(Color.BLACK);
        yAxis.setLabelCount(6, false);
        //yAxis.setDrawLabels(false);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Jan");
        labels.add("Feb");
        labels.add("Mar");
        labels.add("Apr");
        labels.add("May");
        labels.add("Jun");
        labels.add("Jul");
        labels.add("Aug");
        labels.add("Sep");
        labels.add("Oct");
        labels.add("Nov");
        labels.add("Dec");

        LineData data = new LineData(labels, dataSet);
        chart1.setData(data);
        chart1.setDescription("Body Fat");
        chart1.invalidate(); // refresh
    }
}
