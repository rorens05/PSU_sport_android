package com.example.psusports.templates;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psusports.GamesActivity;
import com.example.psusports.R;
import com.example.psusports.global.GlobalVariables;
import com.example.psusports.models.Sport;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.MyViewHolder> {

    private List<Sport> sportList;
    private Context context;

    public SportAdapter(List<Sport> sportList, Context context) {
        this.sportList = sportList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_sports, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        Sport sport = sportList.get(i);
        Picasso.get()
                .load(sport.logo)
                .resize(120, 120)
                .centerCrop()
                .into(myViewHolder.image);
        myViewHolder.name.setText(sport.name);
        myViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, sportList.get(i).name + " selected", Toast.LENGTH_SHORT).show();
                GlobalVariables.selectedSport = sportList.get(i);
                context.startActivity(new Intent(context, GamesActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return sportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        LinearLayout container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cv_sport_image);
            name = itemView.findViewById(R.id.cv_sport_name);
            container = itemView.findViewById(R.id.sport_container);
        }
    }
}
