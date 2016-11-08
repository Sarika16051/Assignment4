package com.example.sarika.todolist;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarika on 11/6/2016.
 */
public class TaskViewAdapter extends PagerAdapter {

    static List<ToDo> todoList = new ArrayList<>();
    Context mContext;
    LayoutInflater mLayoutInflater;

    public TaskViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    // Returns the number of pages to be displayed in the ViewPager.
    @Override
    public int getCount() {
        return todoList.size();
    }

    // Returns true if a particular object (page) is from a particular page
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // This method should create the page for the given position passed to it as an argument.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate the layout for the page
        View itemView = mLayoutInflater.inflate(R.layout.display_task_details, container, false);
        // Find and populate data into the page (i.e set the image)
        TextView title = (TextView) itemView.findViewById(R.id.titleDisplay);
        TextView details = (TextView) itemView.findViewById(R.id.detailDisplay);
        TextView time = (TextView) itemView.findViewById(R.id.timeDisplay);

        title.setText(todoList.get(position).getTitle());
        details.setText(todoList.get(position).getDetail());
        time.setText(todoList.get(position).getTime());


        // ...
        // Add the page to the container
        container.addView(itemView);
        // Return the page
        return itemView;
    }

    // Removes the page from the container for the given position.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
