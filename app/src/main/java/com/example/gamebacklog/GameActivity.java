package com.example.gamebacklog;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GameActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    public static final String EXTRA_BACKLOG_ITEM = "updateGame";
    private static final int UPDATE_REQUESTCODE = 20;
    private RecyclerView gameView;
    private GameRoomDatabase db;
    private GameAdapter mGameAdapter;
    private MainViewModel mMainViewModel;
    private List<Game> gameList;
    private GestureDetector gestureDetector;
    private static final int ADD_REQUESTCODE = 30;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gameView = findViewById(R.id.recyclerView);
        db = GameRoomDatabase.getDatabase(this);
        gameList = new ArrayList<>();

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getGames().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                gameList = games;
                updateUI(gameList);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(GameActivity.this, AddGameActivity.class);
            startActivityForResult(intent, ADD_REQUESTCODE);
        });

        gameView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mGameAdapter = new GameAdapter(gameList);
        gameView.setAdapter(mGameAdapter);
        gameView.addOnItemTouchListener(this);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int adapterPosition = viewHolder.getAdapterPosition();

                final Game item = gameList.get(adapterPosition);
                Snackbar.make(GameActivity.this.findViewById(android.R.id.content),
                        getString(R.string.delete) + " " + item.getTitle(),
                        Snackbar.LENGTH_LONG).setAction(R.string.undo,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMainViewModel.insert(item);
                            }
                        }).show();

                mMainViewModel.delete(gameList.get(adapterPosition));
            }
        };

        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(gameView);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if (data != null) {
                Game item = data.getParcelableExtra(EXTRA_BACKLOG_ITEM);
                if (item == null) {
                    return;
                }
                if (requestCode == ADD_REQUESTCODE) {
                    mMainViewModel.insert(item);
                } else if (requestCode == UPDATE_REQUESTCODE) {
                    Date date = new Date();
                    String strDateFormat = "dd/MM/yyyy";
                    DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                    String formattedDate = dateFormat.format(date);
                    item.setDate(formattedDate);

                    mMainViewModel.update(item);
                }
            }
        }
    }

    private void updateUI(List<Game> newList) {
        mGameAdapter.swapList(gameList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_item) {
            List<Game> newList = new ArrayList<>();
            newList.addAll(gameList);

            Snackbar.make(GameActivity.this.findViewById(android.R.id.content), " " + item.getTitle(),
                    Snackbar.LENGTH_LONG).setAction(R.string.undo,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMainViewModel.insert(newList);
                        }
                    }).show();

            mMainViewModel.delete(gameList);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        int itemLocation = recyclerView.getChildAdapterPosition(child);

        if(child != null && gestureDetector.onTouchEvent(motionEvent)) {
            Game game = gameList.get(itemLocation);

            // Go to edit activity
            Intent intent = new Intent(GameActivity.this, UpdateGameActivity.class);
            intent.putExtra(EXTRA_BACKLOG_ITEM, game);
            startActivityForResult(intent, UPDATE_REQUESTCODE);
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
