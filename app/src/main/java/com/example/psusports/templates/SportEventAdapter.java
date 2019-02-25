package com.example.psusports.templates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psusports.R;
import com.example.psusports.models.SportEvent;

import java.util.List;

public class SportEventAdapter extends
        RecyclerView.Adapter<SportEventAdapter.MyViewHolder>{

    private List<SportEvent> eventList;
    private Context context;

    public SportEventAdapter(List<SportEvent> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView eventName;
        TextView date;
        TextView venue;
        TextView organizer;
        LinearLayout listContainer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.tv_list_event_name);
            date = itemView.findViewById(R.id.tv_list_event_date);
            venue = itemView.findViewById(R.id.tv_list_event_venue);
            organizer = itemView.findViewById(R.id.tv_list_event_organizer);
            listContainer = itemView.findViewById(R.id.event_container);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        SportEvent event = eventList.get(i);
        myViewHolder.eventName.setText(event.name);
        myViewHolder.date.setText(event.event_date);
        myViewHolder.venue.setText(event.venue);
        myViewHolder.organizer.setText(event.organizer);
        myViewHolder.listContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, eventList.get(i).name + " was touched", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
