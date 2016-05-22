package musclemetrics.musclemetricsandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by JerunTrajko on 1/15/16.
 */
public class one_excercise_activity extends AppCompatActivity {

    EditText One;
    EditText Two;
    EditText Three;
    EditText Four;
    EditText Five;
    EditText Six;
    EditText Seven;
    EditText Eight;
    EditText Nine;
    EditText Ten;
    SegmentedGroup typeSeg;
    SegmentedGroup setSeg;
    RadioButton burnout;
    RadioButton dropset;
    RadioButton standard;
    RadioButton pyramid;
    RadioButton fivexfive;
    RadioButton oneB;
    RadioButton twoB;
    RadioButton threeB;
    RadioButton fourB;
    RadioButton fiveB;
    RadioButton sixB;
    RadioButton sevenB;
    RadioButton eightB;
    RadioButton nineB;
    RadioButton tenB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Creating Layout ----------------------");
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.full_toolbar_one_excercise);

        One = (EditText) findViewById(R.id.editOne);
        Two = (EditText) findViewById(R.id.editTwo);
        Three = (EditText) findViewById(R.id.editThree);
        Four = (EditText) findViewById(R.id.editFour);
        Five = (EditText) findViewById(R.id.editFive);
        Six = (EditText) findViewById(R.id.editSix);
        Seven = (EditText) findViewById(R.id.editSeven);
        Eight = (EditText) findViewById(R.id.editEight);
        Nine = (EditText) findViewById(R.id.editNine);
        Ten = (EditText) findViewById(R.id.editTen);
        typeSeg = (SegmentedGroup) findViewById(R.id.segmented2);
        setSeg = (SegmentedGroup) findViewById(R.id.segmented3);
        burnout = (RadioButton) findViewById(R.id.burnout);
        dropset = (RadioButton) findViewById(R.id.dropset);
        standard = (RadioButton) findViewById(R.id.standard);
        pyramid = (RadioButton) findViewById(R.id.pyramid);
        fivexfive = (RadioButton) findViewById(R.id.fivexfive);
        oneB = (RadioButton) findViewById(R.id.one);
        twoB = (RadioButton) findViewById(R.id.two);
        threeB = (RadioButton) findViewById(R.id.three);
        fourB = (RadioButton) findViewById(R.id.four);
        fiveB = (RadioButton) findViewById(R.id.five);
        sixB = (RadioButton) findViewById(R.id.six);
        sevenB = (RadioButton) findViewById(R.id.seven);
        eightB = (RadioButton) findViewById(R.id.eight);
        nineB = (RadioButton) findViewById(R.id.nine);
        tenB = (RadioButton) findViewById(R.id.ten);

        setSeg.clearCheck();
        tenB.setChecked(true);
        setSeg.check(R.id.ten);


        typeSeg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == burnout.getId())
                {
                    //clear edit boxes, show all of them, and enable all radio buttons
                    setSeg.clearCheck();
                    clearEdits();
                    clearRadio();
                    radioEnabled();
                    showEdit();

                    burnout.setChecked(true);
                    dropset.setChecked(false);
                    standard.setChecked(false);
                    pyramid.setChecked(false);
                    fivexfive.setChecked(false);
                    setSeg.check(R.id.ten);
                    tenB.setChecked(true);
                }
                else if(checkedId == dropset.getId())
                {
                    setSeg.clearCheck();
                    clearEdits();
                    clearRadio();
                    radioEnabled();
                    showEdit();

                    burnout.setChecked(false);
                    dropset.setChecked(true);
                    standard.setChecked(false);
                    pyramid.setChecked(false);
                    fivexfive.setChecked(false);
                    setSeg.check(R.id.ten);
                    tenB.setChecked(true);
                }
                else if(checkedId == standard.getId())
                {
                    setSeg.clearCheck();
                    clearEdits();
                    clearRadio();
                    radioEnabled();
                    showEdit();

                    burnout.setChecked(false);
                    dropset.setChecked(false);
                    standard.setChecked(true);
                    pyramid.setChecked(false);
                    fivexfive.setChecked(false);
                    setSeg.check(R.id.ten);
                    tenB.setChecked(true);
                }
                else if(checkedId == pyramid.getId())
                {
                    setSeg.clearCheck();
                    clearEdits();
                    clearRadio();
                    radioEnabled();
                    showEdit();

                    burnout.setChecked(false);
                    dropset.setChecked(false);
                    standard.setChecked(false);
                    pyramid.setChecked(true);
                    setSeg.check(R.id.ten);
                    tenB.setChecked(true);
                    fivexfive.setChecked(false);
                }
                else // five x five
                {
                    setSeg.clearCheck();
                    clearEdits();
                    clearRadio();
                    radioEnabled();
                    showEdit();

                    burnout.setChecked(false);
                    dropset.setChecked(false);
                    standard.setChecked(false);
                    pyramid.setChecked(false);
                    fivexfive.setChecked(true);

                    setSeg.check(R.id.five);



                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.VISIBLE);
                    Three.setVisibility(View.VISIBLE);
                    Four.setVisibility(View.VISIBLE);
                    Five.setVisibility(View.VISIBLE);
                    Six.setVisibility(View.INVISIBLE);
                    Seven.setVisibility(View.INVISIBLE);
                    Eight.setVisibility(View.INVISIBLE);
                    Nine.setVisibility(View.INVISIBLE);
                    Ten.setVisibility(View.INVISIBLE);
                    oneB.setEnabled(false);
                    twoB.setEnabled(false);
                    threeB.setEnabled(false);
                    fourB.setEnabled(false);
                    fiveB.setEnabled(true);
                    sixB.setEnabled(false);
                    sevenB.setEnabled(false);
                    eightB.setEnabled(false);
                    nineB.setEnabled(false);
                    tenB.setEnabled(false);
                }
            }
        });

        setSeg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == oneB.getId())
                {
                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.INVISIBLE);
                    Three.setVisibility(View.INVISIBLE);
                    Four.setVisibility(View.INVISIBLE);
                    Five.setVisibility(View.INVISIBLE);
                    Six.setVisibility(View.INVISIBLE);
                    Seven.setVisibility(View.INVISIBLE);
                    Eight.setVisibility(View.INVISIBLE);
                    Nine.setVisibility(View.INVISIBLE);
                    Ten.setVisibility(View.INVISIBLE);
                }
                else if(checkedId == twoB.getId())
                {
                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.VISIBLE);
                    Three.setVisibility(View.INVISIBLE);
                    Four.setVisibility(View.INVISIBLE);
                    Five.setVisibility(View.INVISIBLE);
                    Six.setVisibility(View.INVISIBLE);
                    Seven.setVisibility(View.INVISIBLE);
                    Eight.setVisibility(View.INVISIBLE);
                    Nine.setVisibility(View.INVISIBLE);
                    Ten.setVisibility(View.INVISIBLE);
                }
                else if(checkedId == threeB.getId())
                {
                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.VISIBLE);
                    Three.setVisibility(View.VISIBLE);
                    Four.setVisibility(View.INVISIBLE);
                    Five.setVisibility(View.INVISIBLE);
                    Six.setVisibility(View.INVISIBLE);
                    Seven.setVisibility(View.INVISIBLE);
                    Eight.setVisibility(View.INVISIBLE);
                    Nine.setVisibility(View.INVISIBLE);
                    Ten.setVisibility(View.INVISIBLE);
                }
                else if(checkedId == fourB.getId())
                {
                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.VISIBLE);
                    Three.setVisibility(View.VISIBLE);
                    Four.setVisibility(View.VISIBLE);
                    Five.setVisibility(View.INVISIBLE);
                    Six.setVisibility(View.INVISIBLE);
                    Seven.setVisibility(View.INVISIBLE);
                    Eight.setVisibility(View.INVISIBLE);
                    Nine.setVisibility(View.INVISIBLE);
                    Ten.setVisibility(View.INVISIBLE);
                }
                else if(checkedId == fiveB.getId())
                {
                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.VISIBLE);
                    Three.setVisibility(View.VISIBLE);
                    Four.setVisibility(View.VISIBLE);
                    Five.setVisibility(View.VISIBLE);
                    Six.setVisibility(View.INVISIBLE);
                    Seven.setVisibility(View.INVISIBLE);
                    Eight.setVisibility(View.INVISIBLE);
                    Nine.setVisibility(View.INVISIBLE);
                    Ten.setVisibility(View.INVISIBLE);
                }
                else if(checkedId == sixB.getId())
                {
                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.VISIBLE);
                    Three.setVisibility(View.VISIBLE);
                    Four.setVisibility(View.VISIBLE);
                    Five.setVisibility(View.VISIBLE);
                    Six.setVisibility(View.VISIBLE);
                    Seven.setVisibility(View.INVISIBLE);
                    Eight.setVisibility(View.INVISIBLE);
                    Nine.setVisibility(View.INVISIBLE);
                    Ten.setVisibility(View.INVISIBLE);
                }
                else if(checkedId == sevenB.getId())
                {
                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.VISIBLE);
                    Three.setVisibility(View.VISIBLE);
                    Four.setVisibility(View.VISIBLE);
                    Five.setVisibility(View.VISIBLE);
                    Six.setVisibility(View.VISIBLE);
                    Seven.setVisibility(View.VISIBLE);
                    Eight.setVisibility(View.INVISIBLE);
                    Nine.setVisibility(View.INVISIBLE);
                    Ten.setVisibility(View.INVISIBLE);
                }
                else if(checkedId == eightB.getId())
                {
                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.VISIBLE);
                    Three.setVisibility(View.VISIBLE);
                    Four.setVisibility(View.VISIBLE);
                    Five.setVisibility(View.VISIBLE);
                    Six.setVisibility(View.VISIBLE);
                    Seven.setVisibility(View.VISIBLE);
                    Eight.setVisibility(View.VISIBLE);
                    Nine.setVisibility(View.INVISIBLE);
                    Ten.setVisibility(View.INVISIBLE);
                }
                else if(checkedId == nineB.getId())
                {
                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.VISIBLE);
                    Three.setVisibility(View.VISIBLE);
                    Four.setVisibility(View.VISIBLE);
                    Five.setVisibility(View.VISIBLE);
                    Six.setVisibility(View.VISIBLE);
                    Seven.setVisibility(View.VISIBLE);
                    Eight.setVisibility(View.VISIBLE);
                    Nine.setVisibility(View.VISIBLE);
                    Ten.setVisibility(View.INVISIBLE);
                }
                //ten
                else
                {
                    One.setVisibility(View.VISIBLE);
                    Two.setVisibility(View.VISIBLE);
                    Three.setVisibility(View.VISIBLE);
                    Four.setVisibility(View.VISIBLE);
                    Five.setVisibility(View.VISIBLE);
                    Six.setVisibility(View.VISIBLE);
                    Seven.setVisibility(View.VISIBLE);
                    Eight.setVisibility(View.VISIBLE);
                    Nine.setVisibility(View.VISIBLE);
                    Ten.setVisibility(View.VISIBLE);
                }
            }
        });

        //Set bottom toolbar buttons
        setBottomToolbar();

        //Set Top Toolbar
        setTopToolbar();
    }

    @Override
    public void onBackPressed() {
        Intent intentApp = new Intent(one_excercise_activity.this,
                one_workout_activity.class);
        startActivity(intentApp);
        finish();
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

    //Set Bottom Toolbar
    private void setBottomToolbar()
    {
        final ImageButton work = (ImageButton) findViewById(R.id.workoutButton);
        work.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(one_excercise_activity.this,
                        workout_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton cal = (ImageButton) findViewById(R.id.calendarButton);
        cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(one_excercise_activity.this,
                        calendar_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton pro = (ImageButton) findViewById(R.id.progressButton);
        pro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentApp = new Intent(one_excercise_activity.this,
                        progress_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton prof = (ImageButton) findViewById(R.id.profileButton);
        prof.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(one_excercise_activity.this,
                        profile_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

    }


    //Set Top Toolbar
    private void setTopToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Chosen Excercise");
        setSupportActionBar(toolbar);
    }

    public void clearEdits()
    {
        One.setText(null);
        Two.setText(null);
        Three.setText(null);
        Four.setText(null);
        Five.setText(null);
        Six.setText(null);
        Seven.setText(null);
        Eight.setText(null);
        Nine.setText(null);
    }

    public void clearRadio()
    {
        oneB.setChecked(false);
        twoB.setChecked(false);
        threeB.setChecked(false);
        fourB.setChecked(false);
        fiveB.setChecked(false);
        sixB.setChecked(false);
        sevenB.setChecked(false);
        eightB.setChecked(false);
        nineB.setChecked(false);
        tenB.setChecked(false);
    }

    public void radioEnabled()
    {
        oneB.setEnabled(true);
        twoB.setEnabled(true);
        threeB.setEnabled(true);
        fourB.setEnabled(true);
        fiveB.setEnabled(true);
        sixB.setEnabled(true);
        sevenB.setEnabled(true);
        eightB.setEnabled(true);
        nineB.setEnabled(true);
        tenB.setEnabled(true);
    }

    public void showEdit()
    {
        One.setVisibility(View.VISIBLE);
        Two.setVisibility(View.VISIBLE);
        Three.setVisibility(View.VISIBLE);
        Four.setVisibility(View.VISIBLE);
        Five.setVisibility(View.VISIBLE);
        Six.setVisibility(View.VISIBLE);
        Seven.setVisibility(View.VISIBLE);
        Eight.setVisibility(View.VISIBLE);
        Nine.setVisibility(View.VISIBLE);
        Ten.setVisibility(View.VISIBLE);
    }
}
