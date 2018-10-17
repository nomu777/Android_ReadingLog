package com.example.nomuradaichi.readinglog;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by nomuradaichi on 2018/05/03.
 */


public class ReadingLogAdapter extends RealmBaseAdapter<ReadingLog> {

    private static class ViewHolder {
        TextView title;
        TextView author;
        TextView publisher;
        TextView endDate;
    }

    public ReadingLogAdapter(@Nullable OrderedRealmCollection<ReadingLog> data) {
        super(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.content_main, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.author = (TextView) convertView.findViewById(R.id.author);
            viewHolder.publisher = (TextView) convertView.findViewById(R.id.publisher);
            viewHolder.endDate = (TextView) convertView.findViewById(R.id.endDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ReadingLog readingLog = adapterData.get(position);
        viewHolder.title.setText(readingLog.getTitle());
        viewHolder.author.setText(readingLog.getAuthor());
        viewHolder.publisher.setText(readingLog.getPublisher());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String formatDate = sdf.format(readingLog.getEndDate());
        viewHolder.endDate.setText(formatDate);
        return convertView;
    }
}
