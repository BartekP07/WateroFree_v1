package app.watero.waterofree.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Methods {

    public String toDayDate() {
        Date date = Calendar.getInstance().getTime();
        // Display a date in day, month, year format
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(date);
        String summary = today.replace("/", "");
        int output = Integer.parseInt(summary);
        return summary;
    }
}
