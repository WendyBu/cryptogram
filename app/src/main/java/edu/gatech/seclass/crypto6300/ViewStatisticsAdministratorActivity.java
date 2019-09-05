package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Player;
import edu.gatech.seclass.crypto6300.models.User;
import edu.gatech.seclass.crypto6300.utils.Enums;

public class ViewStatisticsAdministratorActivity extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics_administrator);
        ListView lv = findViewById(R.id.statistics_list);
        databaseHandler = new DatabaseHandler(this);

        ArrayList<Player> playerList = User.getStatisticReport(databaseHandler);
        Collections.sort(playerList, new PlayerComparator());

        ArrayAdapter<Player> arrayAdapter = new PlayerListAdapterForAdmin(this, playerList);

        lv.setAdapter(arrayAdapter);
    }

    public void addCryptogram(View view) {
        Intent intent = new Intent(this, AddCryptogramActivity.class);
        startActivity(intent);
    }

    public void addPlayer(View view) {
        Intent intent = new Intent(this, AddPlayerActivity.class);
        startActivity(intent);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, HomeAdministratorActivity.class);
        startActivity(intent);
    }

    public void logOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

// following code from: https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
class PlayerListAdapterForAdmin extends ArrayAdapter<Player> {
    public PlayerListAdapterForAdmin(Context context, ArrayList<Player> players) {
        super(context, 0, players);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Player player = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_player_for_admin, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.text_view_first_name);
        TextView tvNumWon = convertView.findViewById(R.id.text_view_num_won);
        TextView tvNumLost = convertView.findViewById(R.id.text_view_num_lost);
        TextView tvUsername = convertView.findViewById(R.id.text_view_username);
        TextView tvDifficulty = convertView.findViewById(R.id.text_view_difficulty);

        tvName.setText(player.getFirstName());
        tvNumWon.setText(Integer.toString(player.getNumWins()));
        tvNumLost.setText(Integer.toString(player.getNumLoss()));
        tvUsername.setText(player.getUserName());
        tvDifficulty.setText(player.getDifficultyCategory().toString());

        return convertView;
    }
}