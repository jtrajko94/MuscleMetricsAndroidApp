package musclemetrics.musclemetricsandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by JerunTrajko on 5/21/16.
 */

public class MyCustomAdapterSearchExcercises extends BaseAdapter implements ListAdapter {
    private ArrayList<excercise_entry> list = new ArrayList<excercise_entry>();
    private Context context;



    public MyCustomAdapterSearchExcercises(ArrayList<excercise_entry> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.text_search_activity, null);
        }

        //Handle TextView and display string from your list
        TextView name = (TextView)view.findViewById(R.id.list_item_string);
        name.setText(list.get(position).activity_name);

        TextView muscles = (TextView)view.findViewById(R.id.primary_muscle);
        muscles.setText("Primary Muscle: " + list.get(position).activity_primary_muscles);

        //Handle buttons and add onClickListeners
        ImageButton addBtn = (ImageButton)view.findViewById(R.id.add_btn);
        addBtn.setFocusable(false);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intentApp = new Intent(context,
                        one_excercise_activity.class);
                context.startActivity(intentApp);
                ((Activity)context).finish();
            }
        });

        return view;
    }
}