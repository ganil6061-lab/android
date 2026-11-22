package com.wocare.ssql;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String DB_Name = "master";
        String TB_Name = "stock";


        SqlDbHelper.createDb(getApplicationContext(), "CREATE DATABASE IF NOT EXISTS "+DB_Name);

        String tableSql = "CREATE TABLE IF NOT EXISTS "+TB_Name+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "shop_name TEXT, " +
                "item_name TEXT NOT NULL, " +
                "size TEXT, " +
                "price REAL DEFAULT 0.0, " +
                "opening_stock INTEGER DEFAULT 0, " +
                "purchase_qty INTEGER DEFAULT 0, " +
                "sold_qty INTEGER DEFAULT 0, " +
                "last_updated_date TEXT)";

        SqlDbHelper.createTable(this, DB_Name, tableSql);


        String sql = "INSERT INTO "+TB_Name+" (shop_name, item_name, size, price, opening_stock, purchase_qty, sold_qty, last_updated_date) " +
                "VALUES ('Babu Tyre Works', 'Apollo EnduRace RA', '295/90 R20', 24500.0, 10, 4, 2, '2026-11-22')";

        SqlDbHelper.insertDb(this, DB_Name, sql);


    }
}