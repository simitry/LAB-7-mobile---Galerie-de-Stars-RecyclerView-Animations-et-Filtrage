package com.example.starsgallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.starsgallery.R;

/*
 * SPLASH SCREEN
 * -------------
 * The goal is visual: show a star, animate it, then open the list.
 * Handler is used instead of Thread.sleep() so the delayed navigation stays on the main thread.
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION_MS = 5000L;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);

        /*
         * The TP shows several animation ideas.
         * They are chained into one readable animation block here.
         */
        logo.animate()
                .rotation(360f)
                .scaleX(0.45f)
                .scaleY(0.45f)
                .translationYBy(900f)
                .alpha(0f)
                .setDuration(4200L)
                .start();

        mainHandler.postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, ListActivity.class));
            finish();
        }, SPLASH_DURATION_MS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // If the activity is destroyed early, avoid launching ListActivity from an old callback.
        mainHandler.removeCallbacksAndMessages(null);
    }
}
