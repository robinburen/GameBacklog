package com.example.gamebacklog;

import android.app.ActionBar;
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

import java.util.ArrayList;
import java.util.List;

public class UpdateGameActivity extends AppCompatActivity {

    private EditText updateTitle;
    private EditText updatePlatform;
    private Spinner updateStatus;
    private Game game;
    private Snackbar mSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GameActivity.class));
            }
        });

        updateTitle = findViewById(R.id.updateTitle);
        updatePlatform = findViewById(R.id.updatePlatform);
        updateStatus = findViewById(R.id.spinnerStatus);

        if (getIntent().getExtras() == null) {
            finishActivity(RESULT_CANCELED);
        } else {
            game = getIntent().getExtras().getParcelable(GameActivity.EXTRA_BACKLOG_ITEM);

            assert game != null;
            updateTitle.setText(game.getTitle());
            updatePlatform.setText(game.getPlatform());
            if (game.getStatus().equals("Want to play")) {
                updateStatus.setSelection(0);
            } else if (game.getStatus().equals("Playing")) {
                updateStatus.setSelection(1);
            } else if (game.getStatus().equals("Stalled")) {
                updateStatus.setSelection(2);
            } else if (game.getStatus().equals("Dropped")) {
                updateStatus.setSelection(3);
            }
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(updateTitle.getText()) || TextUtils.isEmpty(updatePlatform.getText()) || TextUtils.isEmpty(updateStatus.getSelectedItem().toString())) {
                    mSnackBar = Snackbar.make(view, R.string.enterinfo, Snackbar.LENGTH_SHORT);
                    mSnackBar.show();
                } else {
                    game.setTitle(updateTitle.getText().toString());
                    game.setPlatform(updatePlatform.getText().toString());
                    game.setStatus(String.valueOf(updateStatus.getSelectedItem()));

                    // Intent back
                    Intent intent = new Intent();
                    intent.putExtra(GameActivity.EXTRA_BACKLOG_ITEM, game);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
