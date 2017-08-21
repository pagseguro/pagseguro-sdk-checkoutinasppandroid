package br.com.uol.pslibs.lojinhapagseguro.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import br.com.uol.pslibs.lojinhapagseguro.R;
import br.com.uol.pslibs.lojinhapagseguro.MainActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
