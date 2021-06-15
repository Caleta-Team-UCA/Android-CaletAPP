package com.caletateam.caletapp.app.EventList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.babyList.BabyModel;

import java.util.List;

public class EventAdapter extends BaseAdapter {

    Context context;
    List<EventModel> events;
    private static LayoutInflater inflater = null;

    public EventAdapter(Context context, List<EventModel> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.events = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return events.get(position).getEventID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.event_element, null);
        //ImageView img = vi.findViewById(R.id.babyimg);

        //img.setImageResource((Integer) events.get(position).getPhoto());
        //TextView text = (TextView) vi.findViewById(R.id.text);
        //text.setText(babys.get(position).getName());
        return vi;
    }
}
