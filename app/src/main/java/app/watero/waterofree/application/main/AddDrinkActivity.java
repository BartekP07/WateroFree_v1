package app.watero.waterofree.application.main;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.watero.waterofree.R;
import app.watero.waterofree.database.MyDBHelper;
import app.watero.waterofree.helpers.App;
import app.watero.waterofree.helpers.UserStorage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDrinkActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    LinearLayout backBtn;
    @BindView(R.id.drinkName_text)
    TextView drinkNameText;
    @BindView(R.id.drinkpercentages_text)
    TextView drinkpercentagesText;
    @BindView(R.id.show_image_r)
    ImageView showImageR;
    @BindView(R.id.show_image_l)
    ImageView showImageL;
    @BindView(R.id.show_image)
    ImageView showImage;
    @BindView(R.id.swipeleft_btn)
    ImageView swipeleftBtn;
    @BindView(R.id.container_type)
    TextView containerType;
    @BindView(R.id.swiperight_btn)
    ImageView swiperightBtn;
    @BindView(R.id.drinks_quantity)
    TextView drinksQuantity;
    @BindView(R.id.minus_btn)
    Button minusBtn;
    @BindView(R.id.plus_btn)
    Button plusBtn;
    @BindView(R.id.add_button)
    Button addButton;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.intresting_info)
    TextView intrestingInfo;

    private static final long GAME_LENGTH_MILLISECONDS = 3000;
   // private static final String AD_UNIT_ID = "ca-app-pub-9369047073176706/2875205151";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"; //test
    private InterstitialAd interstitialAd;

    private boolean gameIsInProgress;
    private boolean adIsLoading;
    private long timerMilliseconds;
    private CountDownTimer countDownTimer;
    private UserStorage userStorage;

    private int position = 0;
    private int next = 1;
    private int central = 0;
    private int last = 5;

    private int lightImg[] = {R.drawable.water_light_img, R.drawable.milk_light_img, R.drawable.juice_light_img,
            R.drawable.coffe_light_img, R.drawable.tea_light_img, R.drawable.alcohol_light_img};
    private int darkImg[] = {R.drawable.water_dark_img, R.drawable.milk_dark_img, R.drawable.juice_dark_img,
            R.drawable.coffe_dark_img, R.drawable.tea_dark_img, R.drawable.alcohol_dark_img};

    private int drinksName[] = {R.string.g_water, R.string.g_milk, R.string.g_juice, R.string.g_coffe, R.string.g_tea, R.string.g_alcohol};
    private int percentages[] = {100, 80, 80, -50, 80, -100};
    private int intresting_info[] = {R.string.intresting_info_watter, R.string.intresting_info_milk,
            R.string.intresting_info_juice, R.string.intresting_info_coffe, R.string.intresting_info_tea, R.string.intresting_info_alco};

    private int waterCups[] = {R.string.g_glass, R.string.g_bootle};
    private int milkCups[] = {R.string.g_glass, R.string.g_bootle};
    private int juiceCups[] = {R.string.g_glass, R.string.g_bootle};
    private int coffeAndTeaCups[] = {R.string.g_cup, R.string.cup_coffe, R.string.thermos};
    private int alcoholCups[] = {R.string.g_glass, R.string.g_bootle};

    private int usingTab[] = waterCups;
    private int myCup = 0;
    private int setMl = 200;

    private JobScheduler mSchedular;
    private static final int jobId = 0;
    private int selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;

    MyDBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddrinks);
        ButterKnife.bind(this);
        userStorage = ((App) getApplication()).getUserStorage();
        tryLoadAdMob();

        dbHelper = new MyDBHelper(this);
    }

    private void tryLoadAdMob(){
        // Create the InterstitialAd and set the adUnitId.
        interstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        interstitialAd.setAdUnitId(AD_UNIT_ID);
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        new Locale(userStorage.getLangugae(), userStorage.getLangugae());

    }

    @Override
    public void onPause() {
        // Cancel the timer if the game is paused.
        super.onPause();
    }

    @Override
    public void onResume() {
        // Start or resume the game.
        super.onResume();
    }

    //Zmiana napoju
    @OnClick({R.id.show_image_r, R.id.show_image_l, R.id.show_image})
    public void switchImage(View view) {
        int lenght = lightImg.length - 1;

        switch (view.getId()) {
            case R.id.show_image_r:
                switchRight(lenght);
                break;
            case R.id.show_image:
                break;
            case R.id.show_image_l:
                switchLeft(lenght);
                break;
        }
    }

    private void switchRight(int lenght) {

        if (central == lenght) {
            central = 0;
        } else {
            central++;
        }
        if (next == lenght) {
            next = 0;
        } else {
            next++;
        }
        if (last == lenght) {
            last = 0;
        } else {
            last++;
        }

        switch (central) {
            case 0:
                usingTab = waterCups;
                break;
            case 1:
                usingTab = milkCups;
                break;
            case 2:
                usingTab = juiceCups;
                break;
            case 3:
                usingTab = coffeAndTeaCups;
                break;
            case 4:
                usingTab = coffeAndTeaCups;
                break;
            case 5:
                usingTab = alcoholCups;
                break;
        }

        myCup = 0;
        drinksQuantity.setText(switchMl(usingTab[myCup]) + "");
        containerType.setText(usingTab[0]);
        drinkpercentagesText.setText(percentages[central] + "");
        drinkNameText.setText(drinksName[central]);
        intrestingInfo.setText(intresting_info[central]);
        showImage.setBackground(getResources().getDrawable(lightImg[central]));
        showImageR.setBackground(getResources().getDrawable(darkImg[next]));
        showImageL.setBackground(getResources().getDrawable(darkImg[last]));
        position = central;

    }

    private void switchLeft(int lenght) {

        if (central == 0) {
            central = lenght;
        } else {
            central--;
        }
        if (next == 0) {
            next = lenght;
        } else {
            next--;
        }
        if (last == 0) {
            last = lenght;
        } else {
            last--;
        }

        switch (central) {
            case 0:
                usingTab = waterCups;
                break;
            case 1:
                usingTab = milkCups;
                break;
            case 2:
                usingTab = juiceCups;
                break;
            case 3:
                usingTab = coffeAndTeaCups;
                break;
            case 4:
                usingTab = coffeAndTeaCups;
                break;
            case 5:
                usingTab = alcoholCups;
                break;
        }

        myCup = 0;

        drinksQuantity.setText(switchMl(usingTab[myCup]) + "");
        containerType.setText(usingTab[0]);
        drinkpercentagesText.setText(percentages[central] + "");
        drinkNameText.setText(drinksName[central]);
        intrestingInfo.setText(intresting_info[central]);
        showImage.setBackground(getResources().getDrawable(lightImg[central]));
        showImageR.setBackground(getResources().getDrawable(darkImg[next]));
        showImageL.setBackground(getResources().getDrawable(darkImg[last]));

        position = central - 1;
    }

    //zmiana ilosci wypitego napoju
    @OnClick({R.id.minus_btn, R.id.plus_btn})
    public void addMilimeters(View view) {

        int quantity = Integer.parseInt(drinksQuantity.getText().toString());

        switch (view.getId()) {
            case R.id.minus_btn:
                if (quantity == 100) {
                    minusBtn.setEnabled(false);
                    minusBtn.setBackground(getResources().getDrawable(R.drawable.minus_btn));
                } else {
                    minusBtn.setEnabled(true);
                    drinksQuantity.setText((quantity - 50) + "");
                }

                break;
            case R.id.plus_btn:
                minusBtn.setEnabled(true);
                minusBtn.setBackground(getResources().getDrawable(R.drawable.plus_btn));
                drinksQuantity.setText((quantity + 50) + "");
                break;
        }
    }

    //wybranie pojemnika do picia
    @OnClick({R.id.swipeleft_btn, R.id.swiperight_btn})
    public void chooseYourContainer(View view) {
        int lenght = usingTab.length - 1;

        switch (view.getId()) {
            case R.id.swipeleft_btn:
                if (myCup == 0) {
                    myCup = lenght;
                } else {
                    myCup--;
                }
                containerType.setText(usingTab[myCup]);
                drinksQuantity.setText(switchMl(usingTab[myCup]) + "");
                break;
            case R.id.swiperight_btn:
                if (myCup == lenght) {
                    myCup = 0;
                } else {
                    myCup++;
                }
                containerType.setText(usingTab[myCup]);
                drinksQuantity.setText(switchMl(usingTab[myCup]) + "");
                break;
        }
    }
    private int switchMl(int e) {

        switch (e) {
            case R.string.g_glass:
                setMl = 200;
                break;
            case R.string.g_bootle:
                setMl = 500;
                break;
            case R.string.g_bidon:
                setMl = 1000;
                break;
            case R.string.g_cup:
                setMl = 150;
                break;
            case R.string.cup_coffe:
                setMl = 300;
                break;
            case R.string.thermos:
                setMl = 700;
                break;
            default:
                setMl = 200;
                break;
        }
        return setMl;
    }

    //powrot do okna mian
    @OnClick(R.id.back_btn)
    public void backBtn() {
        finish();
    }

    //dodanie wypitego napoju
    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick(R.id.add_button)
    public void addDrinks() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(date) + "";

        String drink = drinkNameText.getText().toString();
        int drinkQuantity = Integer.valueOf(drinksQuantity.getText().toString().trim());
        int hydration = Integer.valueOf(drinkpercentagesText.getText().toString().trim());

        MyDBHelper dbHelper = new MyDBHelper(this);
        dbHelper.addDrink(chooseDrink(drink), time, drinkQuantity, hydration, toDayDate(), drinkedCounting());
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
        }
        finish();
    }

    private int chooseDrink(String drink){
        int drinkName = 1;
        if(drink.equals(getString(R.string.g_water))){
            return drinkName = 1;
        }else if(drink.equals(getString(R.string.g_milk))){
            return drinkName = 2;
        }else if(drink.equals(getString(R.string.g_juice))){
            return drinkName = 3;
        }else if(drink.equals(getString(R.string.g_coffe))){
            return drinkName = 4;
        }else if(drink.equals(getString(R.string.g_tea))){
            return drinkName = 5;
        }else if(drink.equals(getString(R.string.g_alcohol))){
            return drinkName = 6;
        }

        return drinkName;
    }

    private String toDayDate() {
        Date date = Calendar.getInstance().getTime();
        // Display a date in day, month, year format
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(date);
        return today;
    }

    private int drinkedCounting() {
        int result = 0;
        int precentages = Integer.parseInt(drinkpercentagesText.getText().toString());
        int drinks_ml = Integer.parseInt(drinksQuantity.getText().toString());
        if (precentages < 0) {
            switch (precentages) {
                case -50:
                    result = -(drinks_ml / 2);
                    break;
                case -100:
                    result = -(drinks_ml);
                    break;
            }
        } else if (precentages > 0) {
            switch (precentages) {
                case 100:
                    result = drinks_ml;
                    break;
                case 80:
                    result = (int) (0.8 * drinks_ml);
                    break;
            }
        } else {
            result = 0;
        }
        return result;
    }
}
