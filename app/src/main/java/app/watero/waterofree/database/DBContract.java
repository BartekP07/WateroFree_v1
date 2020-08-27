package app.watero.waterofree.database;

import android.provider.BaseColumns;

public class DBContract {

    private DBContract(){ }

    public static final class DBEntry implements BaseColumns {
        //TABLE DRINKS_ADDING
        public static final String TABLE_DRINKS = "my_drinks";
        public static final String DRINKS_ID = "drinks_id";
        public static final String COLUMN_DATE = "date";
        // Date when drinked
        // private static final String COLUMN_DRINKED_DAY = "day_drinked_ml";
        // Drinked in current day
        // private static final String COLUMN_TARGET_DAY = "day_target_ml";// Day target
        public static final String COLUMN_TIME = "time_h"; //Time when drinked
        public static final String COLUMN_DRINK_TYPE = "drink_name"; // Name of drink /  1-water / 2-milk / 3-juice / 4-coffe / 5-tea / 6-alcohol
        public static final String COLUMN_QUANTITY_DRINKED = "drink_ml"; //Drinks quantity
        public static final String COLUMN_DRINKHYDRATION = "drinks_hyd"; //Drinks hydration
        public static final String COLUMN_FINALY_DRINKED_ML = "final_drink_ml";

        //TABLE INFORMATION
        public static final String TABLE_INFORMATION = "my_information";
        public static final String INFORMATION_ID = "information_id";
        public static final String COLUMN_IS_ACTIVE_DAY = "active_day";
        public static final String COLUMN_ACTIVE_DAY_DATE = "active_day_date";
        public static final String COLUMN_VERSION = "active_day_date";

        //TABLE SETTINGS
        public static final String TABLE_SETTINGS = "my_settings";
        public static final String SETTINGS_ID = "settings_id";
        public static final String LANGUAGE_COLUMN = "language_set";
        public static final String SEX_COLUMN = "sex_set"; //   0 woman  --- 1 man
        public static final String WEIGHT_COLUMN = "weight_set";
        public static final String DAY_TARGET_COLUMN = "day_target_column";
        public static final String ACTIVE_DAY_TARGET_COLUMN = "active_day_set";
        public static final String NOTIFICATION_COLUMN = "notification_set";
        public static final String WAKEUP_COLUMN = "wakeup_set";
        public static final String GOTOSLEEP_COLUMN = "sleep_set";
        public static final String FREQUENCY_COLUMN = "frequency_set";
        public static final String ONBOARDING_COLUMN = "onboarding_active";
        public static final String QUESTIONNAIRE_COLUMN ="questionnaire_active";

        //TABLE HISTORY DAY INFORMATION
        public static final String TABLE_HISTORY_DAY_INFO = "my_history";
        public static final String HISTORY_ID = "history_id";
        public static final String HISTORY_COLUMN_DAY_NUMBER = "day_number";
        public static final String HISTORY_DATE = "day_date";
        public static final String HISTORY_QUANTITY = "day_quantity";
        public static final String HISTORY_PRECENTAGES = "day_precentages";
        public static final String HISTORY_ISACTIVE_DAY = "day_active";
        public static final String HISTORY_DAY_TARGET = "day_target";

    }

}
