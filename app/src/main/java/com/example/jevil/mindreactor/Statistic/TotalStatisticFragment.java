package com.example.jevil.mindreactor.Statistic;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jevil.mindreactor.BDHelpers.DBHelperC;
import com.example.jevil.mindreactor.Other.HelpClass;
import com.example.jevil.mindreactor.R;

import java.util.ArrayList;
import java.util.Calendar;

import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;

public class TotalStatisticFragment extends Fragment {
    private DBHelperC dbHelperC;
    private SQLiteDatabase dbC;
//    HelpClass helpClass;
    TextView tvMainMark, tvBenUseful, tvBenHarmful, tvLifeTaxes, tvUsefulHours, tvHarmfulHours, tvLifeTaxesHours, tvUsefulOnce, tvHarmfulOnce;
    private int typeOfStatistic;
//    String time, timeEnd;
    public PieView pieView;
//    boolean isYesterday = false;
    String selection;
    String[] selectionArgs;
    String time, timeEnd;
    boolean isYesterday = false;
    public int currentSelectedItemPosition = -1;
    HelpClass helpClass;
    Calendar calendar;

    public TotalStatisticFragment(int typeOfStatistic) { //0 - сегодня, 1 - неделя, 2 - месяц, 3 - год, 4 - все время
        this.typeOfStatistic = typeOfStatistic;
//        this.timeEnd = timeEnd;
//        this.isYesterday = isYesterday;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_total_statistic, container, false);
        tvMainMark = (TextView) v.findViewById(R.id.tvMainMark);
        tvBenUseful = (TextView) v.findViewById(R.id.tvBenUseful);
        tvLifeTaxes = (TextView) v.findViewById(R.id.tvLifeTaxes);
        tvBenHarmful = (TextView) v.findViewById(R.id.tvBenHarmful);
        pieView = (PieView) v.findViewById(R.id.chart_pie);

        tvUsefulHours = (TextView) v.findViewById(R.id.tvUsefulHours);
        tvHarmfulHours = (TextView) v.findViewById(R.id.tvHarmfulHours);
        tvLifeTaxesHours = (TextView) v.findViewById(R.id.tvLifeTaxesHours);

        tvUsefulOnce = (TextView) v.findViewById(R.id.tvUsefulOnce);
        tvHarmfulOnce = (TextView) v.findViewById(R.id.tvHarmfulOnce);

        helpClass = new HelpClass();

        Calendar calendar = Calendar.getInstance();
        switch (typeOfStatistic) {
            case 0: // все время
                calendar.set(2017, 0, 1);
                time = String.valueOf(calendar.getTimeInMillis());
                break;
            case 1: // сегодня
                time = String.valueOf(helpClass.getStartOfTheDay(calendar).getTimeInMillis());
                Log.d("mLog", "Time " + calendar.getTime());
                break;
            case 2: // вчера
                time = String.valueOf(helpClass.getYesterday(helpClass.getStartOfTheDay(calendar)).getTimeInMillis());
                calendar.add(calendar.DAY_OF_WEEK, 1);
                timeEnd = String.valueOf(helpClass.getStartOfTheDay(calendar).getTimeInMillis());
                isYesterday = true;
                break;
            case 3: // неделя
                time = String.valueOf(helpClass.getWeek(helpClass.getStartOfTheDay(calendar)).getTimeInMillis());
                break;
            case 4: // месяц
                time = String.valueOf(helpClass.getMonth(helpClass.getStartOfTheDay(calendar)).getTimeInMillis());
                break;
        }
        setMainMark();
        setStatistic();

        // calendar.set(2017, 06, 26);
        //time = String.valueOf(calendar.getTimeInMillis());
//        long date = System.currentTimeMillis();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
//        String dateString = sdf.format(date);
//
//
//        Calendar calendar = Calendar.getInstance();
//
//        //int year=calendar.get(calendar.YEAR);
//        int hours=calendar.get(calendar.HOUR);
//        int minutes=calendar.get(calendar.MINUTE);
//        tvBenUseful.setText(calendar.getTime().toString());
//        tvBenHarmful.setText(helpClass.getHalfOfTheYear(calendar).getTime().toString());

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.HOUR, -1);
//        calendar.add(Calendar.MINUTE, -48);
//        calendar.add(Calendar.DATE, -21);
        return v;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        inflater.inflate(R.menu.menu_activity_statistic_toolbar, menu);
//
//        MenuItem item = menu.findItem(R.id.spinner);
//        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(adapter);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent,
//                                       View itemSelected, int selectedItemPosition, long selectedId) {
//                if (currentSelectedItemPosition != selectedItemPosition) {
//                    currentSelectedItemPosition = selectedItemPosition;
//
//                    String[] choose = getResources().getStringArray(R.array.spinner_list_item_array);
//                    Toast toast = Toast.makeText(getContext(),
//                            "Ваш выбор: " + choose[selectedItemPosition], Toast.LENGTH_SHORT);
//                    toast.show();
//                    Calendar calendar = Calendar.getInstance();
//                    switch (selectedItemPosition) {
//                        case 0: // все время
//                            calendar.set(2017, 0, 1);
//                            time = String.valueOf(calendar.getTimeInMillis());
//                            break;
//                        case 1: // сегодня
//                            time = String.valueOf(helpClass.getStartOfTheDay(calendar).getTimeInMillis());
//                            Log.d("mLog", "Time " + calendar.getTime());
//                            break;
//                        case 2: // вчера
//                            time = String.valueOf(helpClass.getYesterday(helpClass.getStartOfTheDay(calendar)).getTimeInMillis());
//                            calendar.add(calendar.DAY_OF_WEEK, 1);
//                            timeEnd = String.valueOf(helpClass.getStartOfTheDay(calendar).getTimeInMillis());
//                            isYesterday = true;
//                            break;
//                        case 3: // неделя
//                            time = String.valueOf(helpClass.getWeek(helpClass.getStartOfTheDay(calendar)).getTimeInMillis());
//                            break;
//                        case 4: // месяц
//                            time = String.valueOf(helpClass.getMonth(helpClass.getStartOfTheDay(calendar)).getTimeInMillis());
//                            break;
//                    }
//                    setMainMark();
//                    setStatistic();
//                }
//
//            }
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }





    public void setMainMark() {

        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();

        if (!isYesterday) {
            selection = "time > ?";
            selectionArgs = new String[]{time};
        } else {
            selection = "time > ? AND time < ?";
            selectionArgs = new String[]{time, timeEnd};
            isYesterday = false;
        }

        dbHelperC = new DBHelperC(getActivity());
        dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД
        String[] columns = null;
        String groupBy;
        groupBy = "benefit";
        float benUseful = 0, benHarmful = 0, lifeTaxes = 0;
        columns = new String[]{"benefit", "sum(marks) as marks"};
        Cursor cursor = dbC.query(dbHelperC.TABLE_C_TASKS, columns, selection, selectionArgs, groupBy, null, null); //чтение данных из БД
        //cursor - набор данных из БД
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : cursor.getColumnNames()) {

                        str = str.concat(cn + " = "
                                + cursor.getString(cursor.getColumnIndex(cn)) + "; ");
                    }

                    switch (cursor.getString(cursor.getColumnIndex("benefit"))) {
                        case "useful":
                            benUseful = cursor.getFloat(cursor.getColumnIndex("marks"));
                            Log.d("mLog", "harmful " + benUseful);
                            break;
                        case "harmful":
                            benHarmful = cursor.getFloat(cursor.getColumnIndex("marks"));
                            Log.d("mLog", "useful " + benHarmful);
                            break;
                        case "lifeTaxes":
                            lifeTaxes = cursor.getFloat(cursor.getColumnIndex("marks"));
                            Log.d("mLog", "useful " + benHarmful);
                            break;
                    }

                } while (cursor.moveToNext());
            }
            double mainMark = benUseful - benHarmful - lifeTaxes;
            String mark = "Баланс: " + helpClass.getMarkFormatString(mainMark);
            tvMainMark.setText(mark);
            String benUsefulString = "Польза: " + helpClass.getMarkFormatString(benUseful);
            String benHarmfulString = "Вред: " + helpClass.getMarkFormatString(benHarmful);
            String lifeTaxesString = "Налог на жизнь: " + helpClass.getMarkFormatString(lifeTaxes);
            tvBenUseful.setText(benUsefulString);
            tvBenHarmful.setText(benHarmfulString);
            tvLifeTaxes.setText(lifeTaxesString);
            cursor.close();

            // график пирог
            // float lifeTaxes = (float) (24 * 0.75);
            float sum = benUseful + benHarmful + lifeTaxes;

            float percentageUseful = benUseful * 100 / (sum);
            float percentageHarmful = benHarmful * 100 / (sum);
            float percentageLifeTaxes = lifeTaxes * 100 / (sum);

            pieHelperArrayList.add(new PieHelper(percentageUseful, Color.GREEN));
            pieHelperArrayList.add(new PieHelper(percentageHarmful, Color.RED));
            pieHelperArrayList.add(new PieHelper(percentageLifeTaxes, Color.GRAY));
            pieView.setDate(pieHelperArrayList);
        } else
            Log.d("mLog", "Cursor is null");

        cursor.close(); //закрываем для освобождения памяти
    }

    public void setStatistic() {

        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();

        if (!isYesterday) {
            selection = "time > ?";
            selectionArgs = new String[]{time};
        } else {
            selection = "time > ? AND time < ?";
            selectionArgs = new String[]{time, timeEnd};
            isYesterday = false;
        }

        dbHelperC = new DBHelperC(getActivity());
        dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД
        String[] columns = null;
        String groupBy;
        groupBy = "benefit";
        int usefulHours = 0, harmfulHours = 0, lifeTaxesHours = 0, usefulOnce = 0, harmfulOnce = 0;
        columns = new String[]{"benefit, type", "sum(minutes) as minutes"};
        Cursor cursor = dbC.query(dbHelperC.TABLE_C_TASKS, null, selection, selectionArgs, null, null, null); //чтение данных из БД
        //cursor - набор данных из БД
//        Log.d("myLogs", "-------------------------------");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
//                String str;
                do {


                    switch (cursor.getString(cursor.getColumnIndex("benefit"))) {
                        case "useful":
                            switch (cursor.getString(cursor.getColumnIndex("type"))) {
                                case "cont":
                                    usefulHours = usefulHours + cursor.getInt(cursor.getColumnIndex("minutes"));
                                    break;
                                case "once":
                                    usefulOnce = usefulOnce + cursor.getInt(cursor.getColumnIndex("minutes"));
                                    break;
                            }
                            break;
                        case "harmful":
                            switch (cursor.getString(cursor.getColumnIndex("type"))) {
                                case "cont":
                                    harmfulHours = harmfulHours + cursor.getInt(cursor.getColumnIndex("minutes"));
                                    break;
                                case "once":
                                    harmfulOnce = harmfulOnce + cursor.getInt(cursor.getColumnIndex("minutes"));
                                    break;
                            }
                        case "lifeTaxes":
                            lifeTaxesHours = lifeTaxesHours + cursor.getInt(cursor.getColumnIndex("minutes"));
                            break;
                    }

                    Log.d("myLogs", "benefit: " + cursor.getString(cursor.getColumnIndex("benefit")) + " type: " + cursor.getString(cursor.getColumnIndex("type")) + " minutes: " + cursor.getFloat(cursor.getColumnIndex("minutes")));

                }
                while (cursor.moveToNext());
                String addStr = "Количество полезных часов: " + String.valueOf(helpClass.getRightTime(usefulHours));
                tvUsefulHours.setText(addStr);
                addStr = "Количество бесполезных часов: " + String.valueOf(helpClass.getRightTime(harmfulHours));
                tvHarmfulHours.setText(addStr);
                addStr = "Количество часов налога на жизнь: " + String.valueOf(helpClass.getRightTime(lifeTaxesHours));
                tvLifeTaxesHours.setText(addStr);
                addStr = "Количество разовых полезных действий: " + String.valueOf(helpClass.getRightOnce(usefulOnce));
                tvUsefulOnce.setText(addStr);
                addStr = "Количество разовых бесполезных действий: " + String.valueOf(helpClass.getRightOnce(usefulOnce));
                tvHarmfulOnce.setText(addStr);
            }

        } else
            Log.d("mLog", "Cursor is null");

        cursor.close(); //закрываем для освобождения памяти
    }


    public void setOnceStatistic() {


        if (!isYesterday) {
            selection = "time > ?";
            selectionArgs = new String[]{time};
        } else {
            selection = "time > ? AND time < ?";
            selectionArgs = new String[]{time, timeEnd};
            isYesterday = false;
        }

        dbHelperC = new DBHelperC(getActivity());
        dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД
        String[] columns = null;
        String groupBy;
        groupBy = "benefit";
        float usefulHours = 0, harmfulHours = 0, lifeTaxesHours = 0;
        columns = new String[]{"benefit, type", "sum(minutes) as minutes"};
        Cursor cursor = dbC.query(dbHelperC.TABLE_C_TASKS, null, selection, selectionArgs, null, null, null); //чтение данных из БД
        //cursor - набор данных из БД
        Log.d("myLogs", "-------------------------------");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
//                String str;
                do {


                    switch (cursor.getString(cursor.getColumnIndex("benefit"))) {
                        case "useful":
                            usefulHours = usefulHours + cursor.getFloat(cursor.getColumnIndex("minutes"));
                            break;
                        case "harmful":
                            harmfulHours = harmfulHours + cursor.getFloat(cursor.getColumnIndex("minutes"));
                            break;
                        case "lifeTaxes":
                            lifeTaxesHours = lifeTaxesHours + cursor.getFloat(cursor.getColumnIndex("minutes"));
                            break;
                    }
                    Log.d("myLogs", "benefit: " + cursor.getString(cursor.getColumnIndex("benefit")) + " type: " + cursor.getString(cursor.getColumnIndex("type")) + " minutes: " + cursor.getFloat(cursor.getColumnIndex("minutes")));

                } while (cursor.moveToNext());
                tvUsefulHours.setText(String.valueOf(usefulHours));
                tvHarmfulHours.setText(String.valueOf(harmfulHours));
                tvLifeTaxesHours.setText(String.valueOf(lifeTaxesHours));
            }

        } else
            Log.d("mLog", "Cursor is null");

        cursor.close(); //закрываем для освобождения памяти
    }


}
