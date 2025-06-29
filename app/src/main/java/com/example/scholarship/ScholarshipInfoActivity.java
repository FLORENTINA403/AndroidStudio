package com.example.scholarship;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ScholarshipInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_info);
        // Këtu vendos informatat që dëshiron të shfaqen
    }
    @Override
    //ketu eshte pjesa ku e bejme back ne faqen e kryesore
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_in_fade, R.anim.zoom_out_fade); // animacion rikthimi
    }
}