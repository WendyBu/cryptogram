package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomePlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_player);
    }

    public void logOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void selectCryptogram(View view) {
        Intent intent = new Intent(this, SelectCryptogramActivity.class);
        startActivity(intent);
    }

    public void playCryptogram(View view) {
        Intent intent = new Intent(this, PlayCryptogramActivity.class);
        startActivity(intent);
    }

    public void viewStatistics(View view) {
        Intent intent = new Intent(this, ViewStatisticsPlayerActivity.class);
        startActivity(intent);
    }
}
