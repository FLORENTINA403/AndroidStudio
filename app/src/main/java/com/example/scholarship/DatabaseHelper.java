package com.example.scholarship;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Users.db";

    // Tabela users
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_FULLNAME = "fullname";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    // Tabela e aplikimeve për bursa
    public static final String TABLE_APPLICATIONS = "scholarship_applications";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FULLNAME + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT)");}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
    // Shto user të ri
    public boolean insertUser(String fullname, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FULLNAME, fullname);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Kontrollo nëse user ekziston për login
    public boolean checkUser(String email, String passwordHash) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email=? AND password=?", new String[]{email, passwordHash});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    // Ndrysho fjalëkalimin
    public boolean updatePassword(String email, String newPasswordHash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PASSWORD, newPasswordHash);
        int rows = db.update(TABLE_USERS, values, "email=?", new String[]{email});
        db.close();
        return rows > 0;
    }
    // Shto aplikim për bursë
    public boolean insertScholarshipApplication(String fullname, String surname, String email, String personalId, String phone, String level, String field, String pdfPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fullname", fullname);
        cv.put("surname", surname);
        cv.put("email", email);
        cv.put("personal_id", personalId);
        cv.put("phone", phone);
        cv.put("level", level);
        cv.put("field", field);
        cv.put("pdf_path", pdfPath); // ruaj path-in

        long result = db.insert(TABLE_APPLICATIONS, null, cv);
        db.close();
        return result != -1;
    }
    // Kontrollo nëse email ekziston për reset password
    public boolean checkIfEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }


}
