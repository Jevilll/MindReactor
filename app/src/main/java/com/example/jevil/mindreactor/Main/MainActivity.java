package com.example.jevil.mindreactor.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.jevil.mindreactor.Services.MyService;
import com.example.jevil.mindreactor.Events.TabEvents.EventsFragment;
import com.example.jevil.mindreactor.Other.HelpClass;
import com.example.jevil.mindreactor.Other.InDevelopingFragment;
import com.example.jevil.mindreactor.R;
import com.example.jevil.mindreactor.Statistic.StatisticTabs;
import com.example.jevil.mindreactor.Tools.ToolsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public Context context;
    int id;
    TextView tvMainMark;
    FragmentManager fragmentManager;
    HelpClass helpClass;
    final String LOG_TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Log.d(LOG_TAG, "onStartCommand");
        //запускаем сервис для добавления налога на жизнь каждый час
        startService(new Intent(this, MyService.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        context = this;
        helpClass = new HelpClass();

        helpClass.recalculateLifeTaxes(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                tvMainMark = (TextView) findViewById(R.id.tvMainMark);
                tvMainMark.setText(helpClass.setMainMark(context));
                invalidateOptionsMenu(); // делает вызов onPrepareOptionsMenu()
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) { // кнопкой "назад" закрываем Drawer
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            //finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Создадим новый фрагмент
        Fragment fragment = null;
        Class fragmentClass = null;
        Bundle bundle = new Bundle();
        id = item.getItemId();

        switch (id) {
            case R.id.nav_tasks:
                fragmentClass = EventsFragment.class;
                break;
            case R.id.nav_scheduler:
                fragmentClass = InDevelopingFragment.class;
                break;
            case R.id.nav_total:
                fragmentClass = StatisticTabs.class;
                bundle.putString("type", "total");
                break;
            case R.id.nav_expanded:
                fragmentClass = StatisticTabs.class;
                bundle.putString("type", "expanded");
                break;
            case R.id.nav_tools:
                fragmentClass = ToolsFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragment.setArguments(bundle);

        // Вставляем фрагмент, заменяя текущий фрагмент
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_navigation, fragment).commit();
        // Выделяем выбранный пункт меню в шторке
        item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
