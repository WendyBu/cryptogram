package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Player;
import edu.gatech.seclass.crypto6300.utils.Enums;

import static edu.gatech.seclass.crypto6300.utils.ValidationUtils.isNameValid;
import static edu.gatech.seclass.crypto6300.utils.ValidationUtils.isPasswordValid;
import static edu.gatech.seclass.crypto6300.utils.ValidationUtils.isUsernameValid;

public class AddPlayerActivity extends AppCompatActivity {
    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private Spinner mDifficultyCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        setDifficultyCategoryDropDownValues();
    }

    /**
     * This method populates the dropdown values for difficulty categories (easy, medium and hard).
     * Understood code in https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
     */
    private void setDifficultyCategoryDropDownValues(){
        Spinner difficultyCategoryDropDown = findViewById(R.id.spinner_difficulty_category);
        String[] difficultyCategories = new String[]{"EASY", "MEDIUM", "HARD"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, difficultyCategories);
        difficultyCategoryDropDown.setAdapter(adapter);
    }

    public void addCryptogram(View view) {
        Intent intent = new Intent(this, AddCryptogramActivity.class);
        startActivity(intent);
    }

    public void viewStatistics(View view) {
        Intent intent = new Intent(this, ViewStatisticsAdministratorActivity.class);
        startActivity(intent);
    }
    public void logOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void backHome(View view) {
        Intent intent = new Intent(this, HomeAdministratorActivity.class);
        startActivity(intent);
    }
    /**
     * This method saves the player object into the database.
     * @param view view
     */
    public void savePlayer(View view) {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        mUsernameView = findViewById(R.id.username);
        mPasswordView = findViewById(R.id.password);
        mFirstNameView = findViewById(R.id.first_name);
        mLastNameView = findViewById(R.id.last_name);
        mDifficultyCategory = findViewById(R.id.spinner_difficulty_category);

        final String firstName = mFirstNameView.getText().toString();
        final String lastName = mLastNameView.getText().toString();
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String difficultyCategory = mDifficultyCategory.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        } else {
            //Check if username already exists
            if(databaseHandler.checkPlayerAccount(username,null)){
                mUsernameView.setError(getString(R.string.error_existing_username));
                focusView = mUsernameView;
                cancel = true;
            }
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //Check for a valid first name
        if(TextUtils.isEmpty(firstName)){
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        } else if(!isNameValid(firstName)){
            mFirstNameView.setError(getString(R.string.error_invalid_first_name));
            focusView = mFirstNameView;
            cancel = true;
        }

        //Check for a valid last name
        if(TextUtils.isEmpty(lastName)){
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        } else if(!isNameValid(lastName)){
            mLastNameView.setError(getString(R.string.error_invalid_last_name));
            focusView = mLastNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt to save and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Try to save user in database.
            Player player = new Player(firstName, lastName, username, password, Enums.DifficultyCategory.valueOf(difficultyCategory));
            if(databaseHandler.createPlayer(player)){
                Toast.makeText(this, "Player was saved to database." , Toast.LENGTH_SHORT).show();
                // Home activity for Administrator is shown.
                goToHome();
            }
            else
                Toast.makeText(this, "Error occurred. Player was not saved to database." , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method shows the Home activity for Administrator.
     */
    private void goToHome(){
        Intent intent = new Intent(this, HomeAdministratorActivity.class);
        startActivity(intent);
    }
}
