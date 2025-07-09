package com.example.scholarship;
import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;

public class BottomNavHandler {

    public static void setup(final Activity activity) {
        LinearLayout home = activity.findViewById(R.id.nav_home);
        LinearLayout search = activity.findViewById(R.id.nav_search);
        LinearLayout about = activity.findViewById(R.id.nav_about);
        LinearLayout profile = activity.findViewById(R.id.nav_profile);

        if (home != null) {
            home.setOnClickListener(v -> {
                if (!(activity instanceof MainActivity)) {
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                }
            });
        }

        if (search != null) {
            search.setOnClickListener(v -> {
                if (!(activity instanceof MainActivity)) {
                    activity.startActivity(new Intent(activity, MainActivity.class)); // ose SearchActivity.class
                    activity.finish();
                }
            });
        }

        if (about != null) {
            about.setOnClickListener(v -> {
                if (!(activity instanceof ScholarshipInfoActivity)) {
                    activity.startActivity(new Intent(activity, ScholarshipInfoActivity.class)); // ose AboutActivity.class
                    activity.finish();
                }
            });
        }

        if (profile != null) {
            profile.setOnClickListener(v -> {
                if (!(activity instanceof ProfileActivity)) {
                    activity.startActivity(new Intent(activity, ProfileActivity.class));
                    activity.finish();
                }
            });
        }
    }
}
