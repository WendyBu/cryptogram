package edu.gatech.seclass.crypto6300;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Administrator;
import edu.gatech.seclass.crypto6300.models.GlobalClass;
import edu.gatech.seclass.crypto6300.utils.Enums;

import static edu.gatech.seclass.crypto6300.utils.ValidationUtils.isPasswordValid;
import static edu.gatech.seclass.crypto6300.utils.ValidationUtils.isUsernameValid;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private DatabaseHandler databaseHandler;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo1", "aaaaaaaa"
    };

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private RadioButton radioButtonSelectedUserType; //Player or Administrator
    private Button mLogInButton;
    private GlobalClass globalClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        globalClass = (GlobalClass) getApplicationContext();
        databaseHandler = new DatabaseHandler(this);
        // Set up the login form.
        mUsernameView = findViewById(R.id.username);
        mPasswordView = findViewById(R.id.password);
        radioButtonSelectedUserType = findViewById(R.id.radio_admin);
        mLogInButton = findViewById(R.id.log_in_button);

        init();

    }

    private void init() {
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    verifyInput();
                    return true;
                }
                return false;
            }
        });
        mLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mUsernameView.setText(DUMMY_CREDENTIALS[0]);
        mPasswordView.setText(DUMMY_CREDENTIALS[1]);
        databaseHandler.createAdministrator(new Administrator(DUMMY_CREDENTIALS[0], DUMMY_CREDENTIALS[1]));

    }
    /**
     * Attempts to sign in the account.
     * If there are form errors (invalid username, missing password, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (verifyInput()) {
            goToHomeScreen();
        }
    }
    private boolean verifyInput() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String userType = radioButtonSelectedUserType.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;

        } else {
            if(userType.equals(Enums.UserType.Player.toString())){
                //User is a player. Check if player's username already exists.
                if(!databaseHandler.checkPlayerAccount(username, null)) {
                    mUsernameView.setError(getString(R.string.error_no_such_user));
                    focusView = mUsernameView;
                    cancel = true;
                } else if (!databaseHandler.checkPlayerAccount(username, password)) {
                    mUsernameView.setError(getString(R.string.account_not_match));
                    focusView = mUsernameView;
                    cancel = true;
                }
            }
            else {
                //User is an administrator. Check if administrator's username already exists.
                if(!databaseHandler.checkAdminAccount(username, null)) {
                    mUsernameView.setError(getString(R.string.no_such_admin_account));
                    focusView = mUsernameView;
                    cancel = true;
                } else if (!databaseHandler.checkAdminAccount(username, password)) {
                    mUsernameView.setError(getString(R.string.account_not_match));
                    focusView = mUsernameView;
                    cancel = true;
                }
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        }

        return !cancel;
    }

    /**
     * When user clicks 'Log in' button in 'Log in as' activity, this method goes to the activity home page depending of the user type:
     * - if user selected Player then this method goes to the activity Home for players
     * - if user selected Administrator then this method goes to the activity Home for administrators
     * To do this, we understood explanation of tutorial https://www.youtube.com/watch?v=bgIUdb-7Rqo
     */
    public void goToHomeScreen(){
        //Get selected user type (Player or Administrator)
        String userType = radioButtonSelectedUserType.getText().toString();


        if(userType.equals(Enums.UserType.Player.toString())){
            globalClass.setAdmin(false);
            globalClass.setCurrentPlayerName(mUsernameView.getText().toString());
            //Go to Home activity for player
            Intent intent = new Intent(this, HomePlayerActivity.class);
            startActivity(intent);
        }
        else {
            globalClass.setAdmin(true);
            globalClass.setCurrentPlayerName(mUsernameView.getText().toString());
            //Go to Home activity for administrator
            Intent intent = new Intent(this, HomeAdministratorActivity.class);
            startActivity(intent);
        }
    }
    /**
     * This method is executed when user selects Player or Administrator in the 'Log in as' screen.
     * Foolowing video was watched to understand how to work with radio group and radio buttons: https://www.youtube.com/watch?v=fwSJ1OkK304
     */
    public void checkRadioButton(View view) {
        RadioGroup radioGroupUserTypes = findViewById(R.id.radio_group_user_types);
        int checkedRadioButtonId = radioGroupUserTypes.getCheckedRadioButtonId();
        radioButtonSelectedUserType = findViewById(checkedRadioButtonId);
        Toast.makeText(this, "User type is " + radioButtonSelectedUserType.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * This method clears out the username and password entered by user.
     * @param view view
     */
    public void clear(View view) {
        mUsernameView.setText("");
        mPasswordView.setText("");
    }
}

