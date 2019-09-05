package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Cryptogram;
import edu.gatech.seclass.crypto6300.models.GameProgress;
import edu.gatech.seclass.crypto6300.models.GlobalClass;
import edu.gatech.seclass.crypto6300.models.Player;
import edu.gatech.seclass.crypto6300.utils.Enums;

import static edu.gatech.seclass.crypto6300.models.GameProgress.generateEncryptedPhrase;

public class PlayCryptogramActivity extends AppCompatActivity {

    private TextView encryptedPhraseView;
    private AutoCompleteTextView solutionAttemptView;
    private TextView numberSolutionAttemptsRemainView;

    private GlobalClass globalClass;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_cryptogram);

        encryptedPhraseView = findViewById(R.id.encrypted_phrase);
        solutionAttemptView = findViewById(R.id.solution_attempt);
        numberSolutionAttemptsRemainView = findViewById(R.id.number_solution_attempts);

        globalClass = (GlobalClass) getApplicationContext();
        databaseHandler = new DatabaseHandler(this);

        loadScreen();
    }

    public void logOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void viewStatistics(View view) {
        Intent intent = new Intent(this, ViewStatisticsPlayerActivity.class);
        startActivity(intent);
    }
    public void selectCryptogram(View view) {
        Intent intent = new Intent(this, SelectCryptogramActivity.class);
        startActivity(intent);
    }

    public void submit(View view) {
        GameProgress progress = createProgressFromScreen();
        Enums.CryptogramStatus status = verifySolution(progress);
        saveProgress(progress);
        updateScreenByStatus(status, progress);
    }
    public void backHome(View view) {
        Intent intent = new Intent(this, HomePlayerActivity.class);
        startActivity(intent);
    }
    private void updateScreenByStatus(Enums.CryptogramStatus status, GameProgress progress) {
        switch (status) {
            case WIN:
                Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_SHORT).show();
                Intent intentWin = new Intent(this, SelectCryptogramActivity.class);
                startActivity(intentWin);
                break;
            case LOSS:
                Toast.makeText(this, "YOU LOST!!!", Toast.LENGTH_SHORT).show();
                Intent intentLoss = new Intent(this, SelectCryptogramActivity.class);
                startActivity(intentLoss);
                break;
            case INPROGRESS:
                Toast.makeText(this, "Try Again!!!", Toast.LENGTH_SHORT).show();
                loadScreen();
                break;
        }

    }
    private void saveProgress(GameProgress progress) {
        StringBuffer errorMsg = new StringBuffer();
        databaseHandler.updateProgress(progress, errorMsg);
        if (!errorMsg.toString().isEmpty()) {
            Toast.makeText(this, "Failed to save game progress: " + errorMsg.toString() , Toast.LENGTH_SHORT).show();
        }
    }
    private GameProgress createProgressFromScreen() {
        GameProgress progress = databaseHandler.getProgressList(globalClass.getCurrentPlayerName(), globalClass.getCurrentCryptogram()).get(0);
        updateProgressFromScreen(progress);
        return progress;
    }
    private void updateProgressFromScreen(GameProgress progress) {
        progress.setPotentialSolution(solutionAttemptView.getText().toString());
    }
    private Enums.CryptogramStatus verifySolution(GameProgress progress) {
        Cryptogram cryptogram = databaseHandler.getCryptogramList(globalClass.getCurrentCryptogram()).get(0);
        progress.setCurrentPlayCount(progress.getCurrentPlayCount() + 1);

        if (solutionAttemptView.getText().toString().equals(cryptogram.getSolutionPhrase())) {
            // WIN!
            progress.setCryptogramStatus(Enums.CryptogramStatus.WIN);
        } else {
            int attempt = loadMaxNumAttempt() - progress.getCurrentPlayCount();

            if (attempt <= 0) {
                // LOSS!
                progress.setCryptogramStatus(Enums.CryptogramStatus.LOSS);
            }
        }
        return progress.getCryptogramStatus();
    }

    private void loadScreen() {
        ArrayList<GameProgress> progressList = databaseHandler.getProgressList(globalClass.getCurrentPlayerName(), globalClass.getCurrentCryptogram());
        GameProgress progress;
        if (progressList.size() != 1) {
            Toast.makeText(this, "Create New Game Progress." , Toast.LENGTH_SHORT).show();
            progress = createNewProgress(globalClass.getCurrentPlayerName(), globalClass.getCurrentCryptogram());
            saveProgress(progress);

        } else {
            progress = progressList.get(0);
        }
        loadProgressOnScreen(progress);
    }
    private GameProgress createNewProgress(String playerName, String cryptogramName) {
        Cryptogram cryptogram = databaseHandler.getCryptogramList(cryptogramName).get(0);
        String encryptedPhrase = generateEncryptedPhrase(cryptogram.getSolutionPhrase());
        return new GameProgress(
                playerName,
                cryptogramName,
                "",
                Enums.CryptogramStatus.INPROGRESS,
                0,
                encryptedPhrase);
    }

    private void loadProgressOnScreen(GameProgress progress) {
        encryptedPhraseView.setText(progress.getEncryptedPhrase());
        solutionAttemptView.setText(progress.getPotentialSolution());
        int attempt = loadMaxNumAttempt() - progress.getCurrentPlayCount();
        numberSolutionAttemptsRemainView.setText(Integer.toString(attempt));
    }

    private int loadMaxNumAttempt() {
        Player player = databaseHandler.getPlayersList(globalClass.getCurrentPlayerName()).get(0);
        Cryptogram cryptogram = databaseHandler.getCryptogramList(globalClass.getCurrentCryptogram()).get(0);
        return maxSolutionAttempt(player.getDifficultyCategory(), cryptogram);
    }

    private int maxSolutionAttempt(Enums.DifficultyCategory d, Cryptogram c) {
        switch (d) {
            case EASY:
                return c.getMaxAllowedAttempts()[0];
            case MEDIUM:
                return c.getMaxAllowedAttempts()[1];
            case HARD:
                return c.getMaxAllowedAttempts()[2];
        }

        return 0;
    }
}
