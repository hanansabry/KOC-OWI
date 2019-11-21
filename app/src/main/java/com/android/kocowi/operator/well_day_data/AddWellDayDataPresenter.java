package com.android.kocowi.operator.well_day_data;

import android.text.format.Time;

import com.android.kocowi.backend.wellsdata.WellsDailyDataRepository;
import com.android.kocowi.model.WellDailyData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddWellDayDataPresenter {

    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    private static final String TIME_FORMAT = "hh:mm a";

    private final AddWellDataBottomFragment addWellDayDataFragment;
    private final WellsDailyDataRepository wellsDailyDataRepository;

    public AddWellDayDataPresenter(AddWellDataBottomFragment addWellDayDataFragment, WellsDailyDataRepository wellsDailyDataRepository) {
        this.addWellDayDataFragment = addWellDayDataFragment;
        this.wellsDailyDataRepository = wellsDailyDataRepository;
    }


    public boolean validateWellData(WellDailyData wellDailyData) {
        boolean validate = true;
        if (wellDailyData.getWhp() == 0) {
            validate = false;
            addWellDayDataFragment.setWhpInputTextErrorMessage();
        }

        if (wellDailyData.getFlp() == 0) {
            validate = false;
            addWellDayDataFragment.setFlpInputTextErrorMessage();
        }

        if (wellDailyData.getTemp() == 0) {
            validate = false;
            addWellDayDataFragment.setTempInputTextErrorMessage();
        }

        return validate;
    }

    public void addTodayWellData(WellDailyData wellDailyData, WellsDailyDataRepository.WellDailyDataInsertionCallback callback) {
        wellsDailyDataRepository.addNewWellDailyData(wellDailyData, callback);
    }

    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        TimeZone ast = TimeZone.getTimeZone("AST");
        TimeZone currentZone = TimeZone.getTimeZone(Time.getCurrentTimezone());
        dateFormat.setTimeZone(currentZone);
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    public String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT, Locale.US);
        TimeZone ast = TimeZone.getTimeZone("AST");
        TimeZone currentZone = TimeZone.getTimeZone(Time.getCurrentTimezone());
        dateFormat.setTimeZone(currentZone);
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }
}
