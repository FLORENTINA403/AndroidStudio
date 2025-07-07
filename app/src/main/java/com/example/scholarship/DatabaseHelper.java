package com.example.scholarship;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
                COL_PASSWORD + " TEXT)");

    db.execSQL("CREATE TABLE "+TABLE_APPLICATIONS +" ("+
            "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "fullname TEXT, "+
            "surname TEXT, "+
            "email TEXT, "+
            "personal_id TEXT, "+
            "phone TEXT, "+
            "field TEXT, "+
            "level TEXT, "+
            "pdf_path TEXT, "+
            "scholarship_type TEXT)");
}


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

    public int getUserIdByCredentials(String email, String hashedPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email=? AND password=?",
                new String[]{email, hashedPassword});
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            cursor.close();
            return userId;
        }
        return -1; // Nëse nuk u gjet përdoruesi
    }
    //pjesa per profile merr te dhenat
    public Cursor getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userId)});
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
    // Metoda per update te profilit
    public boolean updateUserProfile(int userId, String fullName, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullname", fullName);
        values.put("email", email);
        values.put("phoneNumber", phone);
        int rows = db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }
    //marrja e listave_aplikantit
    public Cursor getAllApplications() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_APPLICATIONS, null);
    }

    //getAllAplication
    public List<AplicationModel> getAllApplicationsAsList() {
        List<AplicationModel> applicationList = new ArrayList<>();
        Cursor cursor = getAllApplications();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String fullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
                String surname = cursor.getString(cursor.getColumnIndexOrThrow("surname"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String personalId = cursor.getString(cursor.getColumnIndexOrThrow("personal_id"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String field = cursor.getString(cursor.getColumnIndexOrThrow("field"));
                String level = cursor.getString(cursor.getColumnIndexOrThrow("level"));
                String pdfPath = cursor.getString(cursor.getColumnIndexOrThrow("pdf_path"));
                String scholarshipType = cursor.getString(cursor.getColumnIndexOrThrow("scholarship_type"));


                AplicationModel model = new AplicationModel(id,
                        fullname, surname, email, personalId, phone, field, level, pdfPath,scholarshipType
                );

                applicationList.add(model);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return applicationList;
    }
    //update apliacant
    public boolean updateApplicant(int id, String fullname, String email,String personalId, String phone, String level, String field) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullname", fullname);
        values.put("email", email);
        values.put("personal_Id",personalId);
        values.put("phone", phone);
        values.put("level", level);
        values.put("field", field);

        int rows = db.update(TABLE_APPLICATIONS, values, "id = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }
    //delete app
    public boolean deleteApplicant(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_APPLICATIONS, "id = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }




}
