package com.example.gamebacklog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<Game> gameList;

    public GameAdapter(List<Game> newGameList) {
        this.gameList = newGameList;
    }

    @NonNull
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_row, viewGroup, false);
        return new GameAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder viewHolder, int i) {
        Game game = gameList.get(i);
        viewHolder.updateUI(game);
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public void swapList (List<Game> list) {
        gameList = list;

        if (list != null) {
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView gameTitle;
        private TextView gamePlatform;
        private TextView gameStatus;
        private TextView gameDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gamePlatform = itemView.findViewById(R.id.gamePlatform);
            gameStatus = itemView.findViewById(R.id.gameStatus);
            gameDate = itemView.findViewById(R.id.gameDate);
        }

        public void updateUI(Game item) {
            gameTitle.setText(item.getTitle());
            gamePlatform.setText(item.getPlatform());
            gameStatus.setText(item.getStatus());
            gameDate.setText(item.getDate());
        }
    }
}
