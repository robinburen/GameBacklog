package com.example.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class AddGameActivity extends AppCompatActivity {

    private EditText inputTitle;
    private EditText inputPlatform;
    private Spinner inputStatus;
    private Snackbar mSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GameActivity.class));
            }
        });

        inputTitle = findViewById(R.id.inputTitle);
        inputPlatform = findViewById(R.id.inputPlatform);
        inputStatus = findViewById(R.id.spinnerStatus);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(inputTitle.getText()) || TextUtils.isEmpty(inputPlatform.getText()) || TextUtils.isEmpty(inputStatus.getSelectedItem().toString())) {
                    mSnackBar = Snackbar.make(view, R.string.enterinfo, Snackbar.LENGTH_SHORT);
                    mSnackBar.show();
                } else {
                    Game game = new Game(inputTitle.getText().toString(),
                            inputPlatform.getText().toString(), String.valueOf(inputStatus.getSelectedItem()));

                    Intent data = new Intent();
                    data.putExtra(GameActivity.EXTRA_BACKLOG_ITEM, game);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            }
        });
    }
}
