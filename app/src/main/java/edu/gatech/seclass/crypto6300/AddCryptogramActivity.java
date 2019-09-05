package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Cryptogram;

public class AddCryptogramActivity extends AppCompatActivity {

    // Set UI reference
    private AutoCompleteTextView cryptogramNameView;
    private AutoCompleteTextView solutionPhaseView;
    private AutoCompleteTextView maxAttempEasyView;
    private AutoCompleteTextView maxAttempMediumView;
    private AutoCompleteTextView maxAttempHardView;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cryptogram);

        // Initial UI reference
        cryptogramNameView = findViewById(R.id.cryptogram_name);
        solutionPhaseView = findViewById(R.id.unencrypted_phrase);
        maxAttempEasyView = findViewById(R.id.max_number_solution_attempts_easy);
        maxAttempMediumView = findViewById(R.id.max_number_solution_attempts_medium);
        maxAttempHardView = findViewById(R.id.max_number_solution_attempts_hard);
        databaseHandler = new DatabaseHandler(this);

    }

    public void addPlayer(View view) {
        Intent intent = new Intent(this, AddPlayerActivity.class);
        startActivity(intent);
    }

    public void viewStatistics(View view) {
        Intent intent = new Intent(this, ViewStatisticsAdministratorActivity.class);
        startActivity(intent);
    }

    public void saveCryptogram(View view) {

        if (validateInput()) {
            Cryptogram cryptogram = createCryptogram();
            saveCryptogramToDB(cryptogram);
            backToAdminHome(view);
        }
    }
    public void logOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void backToAdminHome(View view) {
        Intent intent = new Intent(this, HomeAdministratorActivity.class);
        startActivity(intent);
    }

    private void saveCryptogramToDB(Cryptogram c) {

        StringBuffer errorMsg = new StringBuffer();

        if (!databaseHandler.createCryptogram(c, errorMsg)) {
            Toast.makeText(this, errorMsg.toString() , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cryptogram was saved to database." , Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput() {

        validateCryptogramName(cryptogramNameView);
        validateSolutionPhase(solutionPhaseView);
        validateMaxAttemp(maxAttempEasyView);
        validateMaxAttemp(maxAttempMediumView);
        validateMaxAttemp(maxAttempHardView);

        return TextUtils.isEmpty(cryptogramNameView.getError()) &&
                TextUtils.isEmpty(solutionPhaseView.getError()) &&
                TextUtils.isEmpty(maxAttempEasyView.getError()) &&
                TextUtils.isEmpty(maxAttempMediumView.getError()) &&
                TextUtils.isEmpty(maxAttempHardView.getError());
    }
    private void validateCryptogramName(AutoCompleteTextView view) {
        view.setError(null);
        final String cryptogramName = view.getText().toString();
        if (cryptogramName.isEmpty()) {
            view.setError("Name can't be empty.");
        } else if(databaseHandler.doesCryptogramExist(cryptogramName)){
            view.setError("Name already exists.");
        }
    }
    private void validateSolutionPhase(AutoCompleteTextView view) {
        view.setError(null);
        if (view.getText().toString().isEmpty()) {
            view.setError("Phase can't be empty");
        }
    }
    private void validateMaxAttemp(AutoCompleteTextView view) {

        view.setError(null);
        try {
            if (view.getText().toString().length() == 0) {
                view.setError("Need at least one Number");
            }
            if (Integer.parseInt(view.getText().toString()) <= 0) {
                view.setError("Max Attemp should be > 0");
            }
        } catch (NumberFormatException e) {
            view.setError("Need Number");
        }
    }
    private Cryptogram createCryptogram() {
        return new Cryptogram(
                cryptogramNameView.getText().toString(),
                solutionPhaseView.getText().toString(),
                new int[]{Integer.parseInt(maxAttempEasyView.getText().toString()),
                          Integer.parseInt(maxAttempMediumView.getText().toString()),
                          Integer.parseInt(maxAttempHardView.getText().toString())}
        );
    }
}
