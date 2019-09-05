package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Player;
import edu.gatech.seclass.crypto6300.models.User;

public class ViewStatisticsPlayerActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics_player);
        databaseHandler = new DatabaseHandler(this);

        ListView lv = findViewById(R.id.statistics_list);

        ArrayList<Player> playerList = User.getStatisticReport(databaseHandler);
        Collections.sort(playerList, new PlayerComparator());

        ArrayAdapter<Player> arrayAdapter = new PlayerListAdapterForPlayer(this, playerList);

        lv.setAdapter(arrayAdapter);
    }

    public void logOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void selectCryptogram(View view) {
        Intent intent = new Intent(this, SelectCryptogramActivity.class);
        startActivity(intent);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, HomePlayerActivity.class);
        startActivity(intent);
    }
}

// following code from: https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
class PlayerListAdapterForPlayer extends ArrayAdapter<Player> {
    public PlayerListAdapterForPlayer(Context context, ArrayList<Player> players) {
        super(context, 0, players);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Player player = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_player_for_player, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.text_view_first_name);
        TextView tvNumWon = convertView.findViewById(R.id.text_view_num_won);
        TextView tvNumLost = convertView.findViewById(R.id.text_view_num_lost);

        tvName.setText(player.getFirstName());
        tvNumWon.setText(Integer.toString(player.getNumWins()));
        tvNumLost.setText(Integer.toString(player.getNumLoss()));

        return convertView;
    }
}