package com.cosmas.dev.ug.apps.schoolfinder;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

    public class SchoolFinderApp extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

    }
