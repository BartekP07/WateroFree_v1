package app.watero.waterofree.helpers;

import android.app.Application;

public class App extends Application {

    private UserStorage userStorage;
    @Override
    public void onCreate() {
        super.onCreate();
        userStorage = new UserStorage(getSharedPreferences("watero_preferences", MODE_PRIVATE));

    }


    public UserStorage getUserStorage() {

        return userStorage;
    }
}