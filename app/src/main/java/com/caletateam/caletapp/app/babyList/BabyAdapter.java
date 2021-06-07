package com.caletateam.caletapp.app.babyList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caletateam.caletapp.R;

import java.util.List;

class BabyAdapter extends BaseAdapter {

    Context context;
    List<BabyModel> babys;
    private static LayoutInflater inflater = null;

    public BabyAdapter(Context context, List<BabyModel> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.babys = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return babys.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return babys.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return babys.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.babyprofile, null);
        ImageView img = vi.findViewById(R.id.babyimg);

        img.setImageResource((Integer) babys.get(position).getPhoto());
        TextView text = (TextView) vi.findViewById(R.id.text);
        text.setText(babys.get(position).getName());
        return vi;
    }
}