package com.example.gamebacklog;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GameRepository {

    private GameRoomDatabase mAppDatabase;
    private GameDao mGameDao;
    private LiveData<List<Game>> mGames;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public GameRepository (Context context) {
        mAppDatabase = GameRoomDatabase.getDatabase(context);
        mGameDao = mAppDatabase.gameDao();
        mGames = mGameDao.getAllGames();
    }

    public LiveData<List<Game>> getAllGames() {
        return mGames;
    }

    public void insert(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.insert(game);
            }
        });
    }

    public void insert(final List<Game> list) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.insert(list);
            }
        });
    }

    public void update(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.update(game);
            }
        });
    }

    public void delete(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.delete(game);
            }
        });
    }

    public void delete(final List<Game> list) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.delete(list);
            }
        });
    }
}
