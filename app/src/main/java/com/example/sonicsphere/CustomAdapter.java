package com.example.sonicsphere;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sonicsphere.R;

public class CustomAdapter extends BaseAdapter {

    private final String[] localDataSet;
    private final int[] imageResIds;

    public CustomAdapter(String[] dataSet, int[] imageResIds) {
        this.localDataSet = dataSet;
        this.imageResIds = imageResIds;
    }

    @Override
    public int getCount() {
        return localDataSet.length;
    }

    @Override
    public Object getItem(int position) {
        return localDataSet[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_adapter, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.getTextView().setText(localDataSet[position]);
        viewHolder.getImageView().setImageResource(imageResIds[position]);

        return convertView;
    }

    static class ViewHolder {
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            textView = view.findViewById(R.id.textView);
            imageView = view.findViewById(R.id.imageView3);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
