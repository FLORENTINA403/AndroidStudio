package com.example.scholarship;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //animaciomi tek about
        Button btnmore=findViewById(R.id.btn_apply);
        btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScholarshipInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in_fade, R.anim.zoom_out_fade); // opsionale, për animacion
            }
        });

        //lidhja me butonin signup
        Button signupButton = findViewById(R.id.btn_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        //lidhja me login
        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //meritaward
        LinearLayout meritAwardLayout = findViewById(R.id.layout_merit_award);
        meritAwardLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScholarshipType.class);
            startActivity(intent);
        });
        //APLY BUTON
        LinearLayout Aplyeasily = findViewById(R.id.aply_easily);
        Aplyeasily.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScholarshipInfoActivity.class);
            startActivity(intent);
        });
// Bottom Navigation
        BottomNavHandler.setup(this);
    }
}