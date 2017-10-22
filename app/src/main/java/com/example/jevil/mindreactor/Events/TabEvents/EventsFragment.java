package com.example.jevil.mindreactor.Events.TabEvents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jevil.mindreactor.Events.TabStory.StoryTabFragment;
import com.example.jevil.mindreactor.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventsFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    public static int int_items = 2 ;
    MyAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.tab_layout, container, false);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        myAdapter = new MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    StoryTabFragment page = (StoryTabFragment) getCurrentFragment(viewPager, myAdapter);
                    if (page != null) {
                        page.initList();
                        page.refreshList();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        return x;
    }

    private class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position) {
                case 0:
                    return new EventsTabFragment();
                case 1:
                    return new StoryTabFragment();
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
                    return "Список";
                case 1 :
                    return "История";
            }
            return null;
        }
    }
    public static Fragment getCurrentFragment(ViewPager pager, FragmentPagerAdapter adapter) {
        try {
            Method m = adapter.getClass().getSuperclass().getDeclaredMethod("makeFragmentName", int.class, long.class);
            Field f = adapter.getClass().getSuperclass().getDeclaredField("mFragmentManager");
            f.setAccessible(true);
            FragmentManager fm = (FragmentManager) f.get(adapter);
            m.setAccessible(true);
            String tag = (String) m.invoke(null, pager.getId(), (long) pager.getCurrentItem());
            return fm.findFragmentByTag(tag);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}

