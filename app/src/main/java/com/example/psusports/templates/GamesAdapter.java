package com.example.psusports.templates;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.psusports.GameDetailsActivity;
import com.example.psusports.R;
import com.example.psusports.global.GlobalVariables;
import com.example.psusports.models.Game;
import com.example.psusports.models.Team;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {
    private static final String TAG = "GamesAdapter";
    private List<Game> gameList;
    private Context context;

    String name1;
    String name2;
    String image1;
    String image2;

    public GamesAdapter(List<Game> gameList, Context context) {
        this.gameList = gameList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {


        final Game game = gameList.get(i);

        myViewHolder.score1.setText(game.score1);
        myViewHolder.score2.setText(game.score2);
        myViewHolder.team1.setText(game.team1);
        myViewHolder.team2.setText(game.team2);

        Log.d(TAG, "size of teams: " + GlobalVariables.teamList.size());
        for(int j = 0; j < GlobalVariables.teamList.size(); j++){

            // Log.d(TAG, "ids:  " + GlobalVariables.teamList.get(i).id);
            // Log.d(TAG, "logo:  " + GlobalVariables.teamList.get(i).logo);
            if(game.team1.equalsIgnoreCase(GlobalVariables.teamList.get(j).id)){
                Log.d(TAG, "ids:  " + GlobalVariables.teamList.get(i).id);
                name1 = GlobalVariables.teamList.get(j).name;
                image1 = GlobalVariables.teamList.get(j).logo;
                myViewHolder.team1.setText(GlobalVariables.teamList.get(j).name);
                Picasso.get()
                        .load(GlobalVariables.teamList.get(j).logo)
                        .resize(100, 100)
                        .centerCrop()
                        .into(myViewHolder.image1);
            }

            if(game.team2.equalsIgnoreCase(GlobalVariables.teamList.get(j).id)){
                Log.d(TAG, "ids:  " + GlobalVariables.teamList.get(i).id);
                name2 = GlobalVariables.teamList.get(j).name;
                image2 = GlobalVariables.teamList.get(j).logo;
                myViewHolder.team2.setText(GlobalVariables.teamList.get(j).name);
                Picasso.get()
                        .load(GlobalVariables.teamList.get(j).logo)
                        .resize(100, 100)
                        .centerCrop()
                        .into(myViewHolder.image2);
            }

        }
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "size of teams: " + GlobalVariables.teamList.size());
                for(int j = 0; j < GlobalVariables.teamList.size(); j++){

                    // Log.d(TAG, "ids:  " + GlobalVariables.teamList.get(i).id);
                    // Log.d(TAG, "logo:  " + GlobalVariables.teamList.get(i).logo);
                    if(game.team1.equalsIgnoreCase(GlobalVariables.teamList.get(j).id)){
                        Log.d(TAG, "ids:  " + GlobalVariables.teamList.get(i).id);
                        name1 = GlobalVariables.teamList.get(j).name;
                        image1 = GlobalVariables.teamList.get(j).logo;
                        myViewHolder.team1.setText(GlobalVariables.teamList.get(j).name);
                        Picasso.get()
                                .load(GlobalVariables.teamList.get(j).logo)
                                .resize(100, 100)
                                .centerCrop()
                                .into(myViewHolder.image1);
                    }

                    if(game.team2.equalsIgnoreCase(GlobalVariables.teamList.get(j).id)){
                        Log.d(TAG, "ids:  " + GlobalVariables.teamList.get(i).id);
                        name2 = GlobalVariables.teamList.get(j).name;
                        image2 = GlobalVariables.teamList.get(j).logo;
                        myViewHolder.team2.setText(GlobalVariables.teamList.get(j).name);
                        Picasso.get()
                                .load(GlobalVariables.teamList.get(j).logo)
                                .resize(100, 100)
                                .centerCrop()
                                .into(myViewHolder.image2);
                    }

                }

                GlobalVariables.selectedGameIndex = 1;
                Intent intent = new Intent(context, GameDetailsActivity.class);
                intent.putExtra("image1", image1);
                intent.putExtra("image2", image2);
                intent.putExtra("name1", name1);
                intent.putExtra("name2", name2);
                GlobalVariables.selectedGame = gameList.get(i);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image1;
        TextView score1;
        TextView team1;
        ImageView image2;
        TextView score2;
        TextView team2;

        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image1 = itemView.findViewById(R.id.game_image_1);
            score1 = itemView.findViewById(R.id.game_score_1);
            team1 = itemView.findViewById(R.id.game_team_name_1);
            image2 = itemView.findViewById(R.id.game_image_2);
            score2 = itemView.findViewById(R.id.game_score_2);
            team2 = itemView.findViewById(R.id.game_team_name_2);
            linearLayout = itemView.findViewById(R.id.game_container);
        }
    }
}
