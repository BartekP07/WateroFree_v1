package app.watero.waterofree.helpers;

import android.content.SharedPreferences;

public class UserStorage  {

    public static String LANGUAGE = "language";

    private final SharedPreferences sharedPreferences;


    public UserStorage(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public void setLANGUAGE(String language){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE,language);
        editor.apply();
    }

    public String getLangugae(){return  sharedPreferences.getString(LANGUAGE,"pl");
    }

    public void deleteLanguage(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
