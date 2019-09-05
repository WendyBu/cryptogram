package edu.gatech.seclass.crypto6300.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.gatech.seclass.crypto6300.models.Administrator;
import edu.gatech.seclass.crypto6300.models.Cryptogram;
import edu.gatech.seclass.crypto6300.models.GameProgress;
import edu.gatech.seclass.crypto6300.models.Player;
import edu.gatech.seclass.crypto6300.utils.Enums;
import edu.gatech.seclass.crypto6300.utils.Utils;

//To read from and write to database I watched and read the following video and articles:
//https://www.youtube.com/watch?v=aQAIMY-HzL8
//https://stackoverflow.com/questions/41783000/how-to-create-multiple-tables-in-android-studio-database
//http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
//https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#insert(java.lang.String,%20java.lang.String,%20android.content.ContentValues)
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "game";
    private static final int DATABASE_VERSION = 1;
    //Name of tables
    private static final String TABLE_NAME_PLAYER = "players";
    private static final String TABLE_NAME_ADMINISTRATOR = "administrators";
    private static final String TABLE_NAME_CRYPTOGRAM = "cryptograms";
    private static final String TABLE_NAME_PROGRESS = "progress";
    //Column names of Players table
    private static final String PRIMARY_KEY_ID = "id";
    private static final String PLAYER_FIRST_NAME = "first_name";
    private static final String PLAYER_LAST_NAME = "last_name";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String PLAYER_DIFFICULTY_CATEGORY = "difficulty_category";
    private static final String PLAYER_NUM_WINS = "num_wins"; //Is this part of game or player?
    private static final String PLAYER_NUM_LOSS = "num_losses"; //Is this part of game or player?
    //Column names of Cryptograms table
    private static final String CRYPTOGRAM_NAME = "name";
    private static final String CRYPTOGRAM_SOLUTION_PHRASE = "solution_phrase";
    private static final String CRYPTOGRAM_MAX_ALLOWED_ATTEMPTS = "maximum_allowed_attempts";
    //Column names of Progress table
    private static final String PROGRESS_PLAYER_USERNAME = "player_name";
    private static final String PROGRESS_PLAYER_CRYTPOGRAM = "cryptogram_name";
    private static final String PROGRESS_PLAY_COUNT = "play_count";
    private static final String PROGRESS_STATUS = "status";  // WIN, LOSS or INPROGRESS
    private static final String PROGRESS_POTENTIALSOLUTION_PHRASE = "potential_solution";
    private static final String PROGRESS_ENCRYPTED_PHRASE = "encrypted_phrase";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createPlayersTable(db);
        createAdministratorsTable(db);
        createCryptogramsTable(db);
        createGameProgressTable(db);
    }

    /**
     * This method creates the players table.
     * @param db database
     */
    private void createPlayersTable(SQLiteDatabase db){
        final String CREATE_TABLE_PLAYER = "CREATE TABLE " + TABLE_NAME_PLAYER + "("
                + USERNAME + " TEXT PRIMARY KEY,"
                + PASSWORD + " TEXT,"
                + PLAYER_FIRST_NAME + " TEXT,"
                + PLAYER_LAST_NAME + " TEXT,"
                + PLAYER_DIFFICULTY_CATEGORY + " TEXT,"
                + PLAYER_NUM_WINS + " INTEGER,"
                + PLAYER_NUM_LOSS + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_PLAYER);
    }
    /**
     * This method creates the Progress table.
     *
     * @param db database
     */
    private void createGameProgressTable(SQLiteDatabase db){
        final String CREATE_TABLE_PLAYER = "CREATE TABLE " + TABLE_NAME_PROGRESS + "("
                + PROGRESS_PLAYER_USERNAME + " TEXT NOT NULL,"
                + PROGRESS_PLAYER_CRYTPOGRAM + " TEXT NOT NULL,"
                + PROGRESS_POTENTIALSOLUTION_PHRASE + " TEXT,"
                + PROGRESS_PLAY_COUNT + " INTEGER,"
                + PROGRESS_STATUS + " TEXT,"
                + PROGRESS_ENCRYPTED_PHRASE + " TEXT,"
                + " PRIMARY KEY(" + PROGRESS_PLAYER_USERNAME + ", " + PROGRESS_PLAYER_CRYTPOGRAM + "))";
        db.execSQL(CREATE_TABLE_PLAYER);
    }

    /**
     * This method creates the administrators table.
     * @param db database
     */
    private void createAdministratorsTable(SQLiteDatabase db){
        final String CREATE_TABLE_ADMIN = "CREATE TABLE " + TABLE_NAME_ADMINISTRATOR + "("
                + USERNAME + " TEXT PRIMARY KEY,"
                + PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_ADMIN);
    }

    /**
     * This method creates the cryptograms table.
     * @param db database
     */
    private void createCryptogramsTable(SQLiteDatabase db){
        final String CREATE_TABLE_CRYPTOGRAM = "CREATE TABLE " + TABLE_NAME_CRYPTOGRAM + "("
                + CRYPTOGRAM_NAME + " TEXT PRIMARY KEY,"
                + CRYPTOGRAM_SOLUTION_PHRASE + " TEXT,"
                + CRYPTOGRAM_MAX_ALLOWED_ATTEMPTS + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_CRYPTOGRAM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PLAYER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ADMINISTRATOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CRYPTOGRAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PROGRESS);
        // Create new tables.
        onCreate(db);
    }

    public boolean createPlayer(Player player){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PLAYER_FIRST_NAME, player.getFirstName());
        values.put(PLAYER_LAST_NAME, player.getLastName());
        values.put(USERNAME, player.getUserName());
        values.put(PASSWORD, player.getPassword());
        values.put(PLAYER_DIFFICULTY_CATEGORY, player.getDifficultyCategory().toString());
        values.put(PLAYER_NUM_WINS, player.getNumWins());
        values.put(PLAYER_NUM_LOSS, player.getNumLoss());

        //Check if username is already present in database. If yes, update; if not, insert.
        long playerId;
        if(checkPlayerAccount(player.getUserName(), null)){
            playerId = db.update(TABLE_NAME_PLAYER, values, null,null);
            return playerId > 0;
        }
        else {
            playerId = db.insert(TABLE_NAME_PLAYER, null, values);
            return playerId != -1;
        }
    }

    public boolean createAdministrator(Administrator administrator){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERNAME, administrator.getUserName());
        values.put(PASSWORD, administrator.getPassword());

        //Check if username is already present in database. If yes, update; if not, insert.
        long adminId;
        if(checkAdminAccount(administrator.getUserName(), null)){
            adminId = 1;//db.update(TABLE_NAME_ADMINISTRATOR, values, null,null);
            return adminId > 0;
        }
        else {
            adminId = db.insert(TABLE_NAME_ADMINISTRATOR, null, values);
            return adminId != -1;
        }
    }

    public boolean checkPlayerAccount(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuffer query = new StringBuffer("SELECT  * FROM " + TABLE_NAME_PLAYER +
                " WHERE " + USERNAME + " = \"" + username + "\"");
        if (password != null) {
            query.append(" AND " + PASSWORD + " = \"" + password + "\"");
        }
        final Cursor cursor = db.rawQuery(query.toString(), null);
        return cursor.getCount() > 0;
    }

    public boolean checkAdminAccount(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuffer query = new StringBuffer("SELECT  * FROM " + TABLE_NAME_ADMINISTRATOR +
                " WHERE " + USERNAME + " = \"" + username + "\"");
        if (password != null) {
            query.append(" AND " + PASSWORD + " = \"" + password + "\"");
        }
        final Cursor cursor = db.rawQuery(query.toString(), null);
        return cursor.getCount() > 0;
    }

    public boolean doesCryptogramExist(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME_CRYPTOGRAM +
                " WHERE " + CRYPTOGRAM_NAME + " = \"" + name + "\"";
        final Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount() > 0;
    }

    public boolean doesPlayerExist(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME_PLAYER +
                " WHERE " + USERNAME + " = \"" + name + "\"";
        final Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount() > 0;
    }

    public boolean doesProgressExist(String username, String cryptogram){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME_PROGRESS +
                " WHERE " + PROGRESS_PLAYER_USERNAME + " = ?" +
                " AND " + PROGRESS_PLAYER_CRYTPOGRAM + " = ?";
        final Cursor cursor = db.rawQuery(query, new String[] {username, cryptogram});
        int a = cursor.getCount();
        return cursor.getCount() > 0;
    }
    public boolean updateProgress(GameProgress p, StringBuffer errorMsg){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROGRESS_PLAYER_USERNAME, p.getPlayerName());
        values.put(PROGRESS_PLAYER_CRYTPOGRAM, p.getCurrentCryptogram());
        values.put(PROGRESS_POTENTIALSOLUTION_PHRASE, p.getPotentialSolution());
        values.put(PROGRESS_PLAY_COUNT, p.getCurrentPlayCount());
        values.put(PROGRESS_STATUS, p.getCryptogramStatus().toString());
        values.put(PROGRESS_ENCRYPTED_PHRASE, p.getEncryptedPhrase());

        //Check if username+cryptogram is already present in database. If yes, update; if not, insert.
        long progressId;
        String whereClause = PROGRESS_PLAYER_USERNAME + " = \"" + p.getPlayerName() +
                "\" AND " + PROGRESS_PLAYER_CRYTPOGRAM + " = \"" + p.getCurrentCryptogram() + "\"";
        if(doesProgressExist(p.getPlayerName(), p.getCurrentCryptogram())) {
            progressId = db.update(TABLE_NAME_PROGRESS, values, whereClause,null);
            return progressId > 0;
        }
        else {
            progressId = db.insert(TABLE_NAME_PROGRESS, null, values);
            return progressId != -1;
        }
    }
    /**
     * This method returns the list of PROGRESS.
     * @return list of PROGRESS
     */
    public ArrayList<GameProgress> getProgressList(String playerName, String cryptogram) {


        StringBuffer query = new StringBuffer("SELECT  * FROM " + TABLE_NAME_PROGRESS);

        if (playerName != null || cryptogram != null) {
            query.append(" WHERE ");

        }
        if (playerName != null) {
            query.append(PROGRESS_PLAYER_USERNAME + " = \"" + playerName + "\"");
            if (cryptogram != null) {
                query.append(" AND ");
            }
        }

        if (cryptogram != null) {
            query.append(PROGRESS_PLAYER_CRYTPOGRAM + " = \"" + cryptogram + "\"");
        }

        return getProgressListFromQuery(query.toString());
    }
    private ArrayList<GameProgress> getProgressListFromQuery(String query) {
        ArrayList<GameProgress> progressList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(query, null);

        // Get all rows and add PROGRESS object to list.
        if (c.moveToFirst()) {
            do {
                GameProgress p = new GameProgress(
                        c.getString(c.getColumnIndex(PROGRESS_PLAYER_USERNAME)),
                        c.getString(c.getColumnIndex(PROGRESS_PLAYER_CRYTPOGRAM)),
                        c.getString(c.getColumnIndex(PROGRESS_POTENTIALSOLUTION_PHRASE)),
                        Enums.CryptogramStatus.valueOf(c.getString(c.getColumnIndex(PROGRESS_STATUS))),
                        c.getInt(c.getColumnIndex(PROGRESS_PLAY_COUNT)),
                        c.getString(c.getColumnIndex(PROGRESS_ENCRYPTED_PHRASE))
                        );

                // Add current PROGRESS to list of PROGRESS.
                progressList.add(p);
            } while (c.moveToNext());
        }

        return progressList;
    }
    public boolean createCryptogram(Cryptogram cryptogram, StringBuffer errorMsg){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CRYPTOGRAM_NAME, cryptogram.getCryptogramName());
        values.put(CRYPTOGRAM_SOLUTION_PHRASE, cryptogram.getSolutionPhrase());
        values.put(CRYPTOGRAM_MAX_ALLOWED_ATTEMPTS, Utils.toString(cryptogram.getMaxAllowedAttempts()));
        long rcode = -1;
        try {
            rcode = db.insertOrThrow(TABLE_NAME_CRYPTOGRAM, null, values);
        } catch (android.database.SQLException e) {
            errorMsg.append(e.getMessage());
            return false;
        }
        if (rcode == -1) {
            // error occur
            return false;
        }
        return true;
    }

    /**
     * This method returns the list of players.
     * @return list of players
     */
    public ArrayList<Player> getPlayersList(String player) {

        StringBuffer query = new StringBuffer("SELECT  * FROM " + TABLE_NAME_PLAYER);
        if (player != null) {
            query.append(" WHERE " + USERNAME + " = \"" + player + "\"");
        }

        return getPlayersListFromQuery(query.toString());
    }
    private ArrayList<Player> getPlayersListFromQuery(String query) {
        ArrayList<Player> playersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor c = db.rawQuery(query, null);

        // Get all rows and add Player object to list.
        if (c.moveToFirst()) {
            do {
                Player player = new Player();
                player.setFirstName(c.getString(c.getColumnIndex(PLAYER_FIRST_NAME)));
                player.setLastName(c.getString(c.getColumnIndex(PLAYER_LAST_NAME)));
                player.setUserName(c.getString(c.getColumnIndex(USERNAME)));
                player.setPassword(c.getString(c.getColumnIndex(PASSWORD)));
                player.setDifficultyCategory(Enums.DifficultyCategory.valueOf(c.getString(c.getColumnIndex(PLAYER_DIFFICULTY_CATEGORY))));
                player.setNumWins(c.getInt(c.getColumnIndex(PLAYER_NUM_WINS)));
                player.setNumLoss(c.getInt(c.getColumnIndex(PLAYER_NUM_LOSS)));
                //TODO: Make sure we don't need to list anything else.

                // Add current player to list of players.
                playersList.add(player);
            } while (c.moveToNext());
        }
        // sort player list in order of descending number of cryptograms won

        Collections.sort(playersList, new PlayerComparator());
        return playersList;
    }

    /**
     * This method returns the list of cryptograms.
     * @return list of cryptograms
     */
    public ArrayList<Cryptogram> getCryptogramList(String cryptogram) {

        StringBuffer query = new StringBuffer("SELECT  * FROM " + TABLE_NAME_CRYPTOGRAM);

        if (cryptogram != null) {
            query.append(" WHERE " + CRYPTOGRAM_NAME + " = \"" + cryptogram + "\"");
        }

        return getCryptogramsListFromQuery(query.toString());
    }
    private ArrayList<Cryptogram> getCryptogramsListFromQuery(String query) {
        ArrayList<Cryptogram> cryptogramsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor c = db.rawQuery(query, null);

        // Get all rows and add Cryptogram object to list.
        if (c.moveToFirst()) {
            do {
                Cryptogram cryptogram = new Cryptogram();
                cryptogram.setCryptogramName(c.getString(c.getColumnIndex(CRYPTOGRAM_NAME)));
                cryptogram.setSolutionPhrase(c.getString(c.getColumnIndex(CRYPTOGRAM_SOLUTION_PHRASE)));
                final int[] maxAllowedAttemptsArray = Utils.toArray(c.getString(c.getColumnIndex(CRYPTOGRAM_MAX_ALLOWED_ATTEMPTS)));
                cryptogram.setMaxAllowedAttempts(maxAllowedAttemptsArray);

                // Add current cryptogram to list of cryptograms.
                cryptogramsList.add(cryptogram);
            } while (c.moveToNext());
        }

        return cryptogramsList;
    }
}

class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player p1, Player p2) {
        return p2.getNumWins() - p1.getNumWins();
    }
}
