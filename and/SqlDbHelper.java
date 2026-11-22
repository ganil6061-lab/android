package com.wocare.ssql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.io.File;

public class SqlDbHelper {

    private static SQLiteDatabase db;

    public static SQLiteDatabase createDb(Context context, String query) {
        String dbName = parseDbName(query);
        return openDatabase(context, dbName);
    }

    public static void createTable(String sql) {
        if (db != null && db.isOpen() && sql != null && !sql.trim().isEmpty()) {
            db.execSQL(sql);
        }
    }

    public static SQLiteDatabase createTable(Context context, String dbName, String createTableQuery) {
        SQLiteDatabase targetDb = openDatabase(context, normalizeDbName(dbName));
        if (targetDb != null && targetDb.isOpen() && createTableQuery != null && !createTableQuery.trim().isEmpty()) {
            targetDb.execSQL(createTableQuery);
        }
        return targetDb;
    }

    public static long insertDb(Context context, String dbName, String sql) {
        SQLiteDatabase targetDb = openDatabase(context, normalizeDbName(dbName));
        if (targetDb == null || !targetDb.isOpen() || sql == null || sql.trim().isEmpty()) {
            return -1L;
        }
        SQLiteStatement stmt = targetDb.compileStatement(sql.trim());
        try {
            return stmt.executeInsert();
        } finally {
            stmt.close();
        }
    }

    public static SQLiteDatabase get() {
        return db;
    }

    public static void exec(String sql) {
        if (db != null && db.isOpen() && sql != null && !sql.trim().isEmpty()) {
            db.execSQL(sql);
        }
    }

    public static void close() {
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
    }

    private static SQLiteDatabase openDatabase(Context context, String dbName) {
        File file = context.getDatabasePath(dbName);

        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (db == null || !db.isOpen() || !file.getAbsolutePath().equals(db.getPath())) {
            if (db != null && db.isOpen()) {
                db.close();
            }
            db = SQLiteDatabase.openOrCreateDatabase(file.getAbsolutePath(), null);
        }

        return db;
    }

    private static String normalizeDbName(String dbName) {
        if (dbName == null || dbName.trim().isEmpty()) {
            return "app.db";
        }
        String name = dbName.trim();
        return name.endsWith(".db") ? name : name + ".db";
    }

    private static String parseDbName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "app.db";
        }
        String s = query.replace(";", "").trim();
        String up = s.toUpperCase();

        if (up.startsWith("CREATE DATABASE IF NOT EXISTS")) {
            s = s.substring("CREATE DATABASE IF NOT EXISTS".length()).trim();
        } else if (up.startsWith("CREATE DATABASE")) {
            s = s.substring("CREATE DATABASE".length()).trim();
        }

        return normalizeDbName(s);
    }
}