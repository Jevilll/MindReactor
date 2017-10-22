package com.example.jevil.mindreactor.Statistic;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jevil.mindreactor.Other.HelpClass;
import com.example.jevil.mindreactor.R;
import com.example.jevil.mindreactor.Statistic.ExpandedStatistic.ExpandedStatisticFragment;

import java.util.Calendar;

public class StatisticTabs extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 5 ;
    HelpClass helpClass;
    Calendar calendar;
    String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.tab_layout_statistic,null);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        calendar = Calendar.getInstance();
        helpClass = new HelpClass();

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            if (type.equals("total")) {
                switch (position){
                    case 0 : return new TotalStatisticFragment(0); // все время
                    case 1 : return new TotalStatisticFragment(1); // сегодня
                    case 2 : return new TotalStatisticFragment(2); // вчера
                    case 3 : return new TotalStatisticFragment(3); // неделя
                    case 4 : return new TotalStatisticFragment(4); // месяц
                }
            } else {
                switch (position){
                    case 0 : return new ExpandedStatisticFragment(0); // все время
                    case 1 : return new ExpandedStatisticFragment(1); // сегодня
                    case 2 : return new ExpandedStatisticFragment(2); // вчера
                    case 3 : return new ExpandedStatisticFragment(3); // неделя
                    case 4 : return new ExpandedStatisticFragment(4); // месяц
                }
            }

            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "за все время";
                case 1 :
                    return "за сегодня";
                case 2 :
                    return "за вчера";
                case 3 :
                    return "за неделю";
                case 4 :
                    return "за месяц";
            }
            return null;
        }
    }

}
