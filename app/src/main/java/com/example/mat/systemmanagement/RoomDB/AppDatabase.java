package com.example.mat.systemmanagement.RoomDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.mat.systemmanagement.User.User;
import com.example.mat.systemmanagement.User.UserDao;


@Database(entities = {User.class}, version = 1) // change version when columns in db are changed
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
