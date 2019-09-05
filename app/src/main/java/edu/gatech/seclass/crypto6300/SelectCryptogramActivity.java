package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Cryptogram;
import edu.gatech.seclass.crypto6300.models.CryptogramDisplayItem;
import edu.gatech.seclass.crypto6300.models.GameProgress;
import edu.gatech.seclass.crypto6300.models.GlobalClass;
import edu.gatech.seclass.crypto6300.models.Player;
import edu.gatech.seclass.crypto6300.utils.Enums;

public class SelectCryptogramActivity extends AppCompatActivity {

    private ListView cryptogramListView;
    private GlobalClass globalClass;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cryptogram);
        globalClass = (GlobalClass) getApplicationContext();
        databaseHandler = new DatabaseHandler(this);

        cryptogramListView = findViewById(R.id.cryptogramListView);

        displayCryptograms();
    }

    public void logOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void playCryptogram() {
        Intent intent = new Intent(this, PlayCryptogramActivity.class);
        startActivity(intent);
    }

    public void viewStatistics(View view) {
        Intent intent = new Intent(this, ViewStatisticsPlayerActivity.class);
        startActivity(intent);
    }
    public void backHome(View view) {
        Intent intent = new Intent(this, HomePlayerActivity.class);
        startActivity(intent);
    }
    private void displayCryptograms() {
        ArrayList<CryptogramDisplayItem> cryptogramDisplayItems = getValidCryptogramsForDisplay();
        ArrayAdapter<CryptogramDisplayItem> arrayAdapter = new CryptogramListAdapter(this, cryptogramDisplayItems);

        cryptogramListView.setAdapter(arrayAdapter);
        cryptogramListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            CryptogramDisplayItem selectedCryptoname = (CryptogramDisplayItem) cryptogramListView.getItemAtPosition(position);
            globalClass.setCurrentCryptogram(selectedCryptoname.getName());

            playCryptogram();
            }
        });
    }
    private ArrayList<CryptogramDisplayItem> getValidCryptogramsForDisplay() {

        ArrayList<Cryptogram> cryptogramsList = databaseHandler.getCryptogramList(null);

        ArrayList<CryptogramDisplayItem> cryptogramDisplayItems = new ArrayList<>();

        ArrayList<GameProgress> progresses = databaseHandler.getProgressList(globalClass.getCurrentPlayerName(), null);
        Iterator<Cryptogram> i = cryptogramsList.iterator();
        while (i.hasNext()) {
            Cryptogram cryptogram = i.next();
            Iterator<GameProgress> j = progresses.iterator();
            boolean flag = true;
            while (j.hasNext()) {
                GameProgress progress = j.next();
                if (cryptogram.getCryptogramName().equals(progress.getCurrentCryptogram())) {
                    flag = false;
                    if (progress.getCryptogramStatus() == Enums.CryptogramStatus.INPROGRESS) {
                        cryptogramDisplayItems.add(new CryptogramDisplayItem(cryptogram.getCryptogramName(), "in-progress"));
                    }
                }
            }
            if (flag) {
                cryptogramDisplayItems.add(new CryptogramDisplayItem(cryptogram.getCryptogramName(), "unstarted"));
            }
        }
        return cryptogramDisplayItems;
    }

}

// following code from: https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
class CryptogramListAdapter extends ArrayAdapter<CryptogramDisplayItem> {
    public CryptogramListAdapter(Context context, ArrayList<CryptogramDisplayItem> statuses) {
        super(context, 0, statuses);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CryptogramDisplayItem displayItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cryptogram, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.cryptogram_name);
        TextView tvStatus = convertView.findViewById(R.id.cryptogram_status);


        tvName.setText(displayItem.getName());
        tvStatus.setText(displayItem.getStatus());

        return convertView;
    }
}