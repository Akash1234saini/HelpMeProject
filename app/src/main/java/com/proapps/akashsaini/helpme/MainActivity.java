package com.proapps.akashsaini.helpme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // instance
    NavigationView navigationView;
    ImageView quickAccessIcon;
    TextView quickAccessText;
    ImageView recentViewIcon;
    TextView recentViewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager viewPager = findViewById(R.id.viewPager);
        NumbersFragmentPagerAdapter adapter = new NumbersFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // creating new Tab instance and assign a new layout Tab in it.
        final TabLayout.Tab quickAccess = tabLayout.newTab();
        final TabLayout.Tab recent = tabLayout.newTab();

        // set string in Tabs.
        quickAccess.setText("Quick Touch");
        recent.setText("Recent");

        // Creating View of custom tab layout by the help of getLayoutInflater.
        @SuppressLint("InflateParams")
        View QucikAccessView = getLayoutInflater().inflate(R.layout.custom_tab_layout, null);
        // assign Ids of Image and Text in Tab view.
        quickAccessIcon = QucikAccessView.findViewById(R.id.tabLayoutIcon);
        quickAccessText = QucikAccessView.findViewById(R.id.tabLayoutText);
        // get image from resource.
        quickAccessIcon.setImageResource(R.drawable.quick_touch_icon);
        quickAccessText.setText("Quick Touch");

        @SuppressLint("InflateParams")
        View RecentView = getLayoutInflater().inflate(R.layout.custom_tab_layout, null);
        recentViewIcon = RecentView.findViewById(R.id.tabLayoutIcon);
        recentViewIcon.setAlpha(0.5f);
        recentViewText = RecentView.findViewById(R.id.tabLayoutText);
        recentViewText.setAlpha(0.5f);
        recentViewIcon.setImageResource(R.drawable.recent_use);
        recentViewText.setText("Recent");

        // setting custom Tab view in Tab instance.
        quickAccess.setCustomView(QucikAccessView);
        recent.setCustomView(RecentView);

        // set activate and non-activate color for Tab layout.
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tabActivate));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabNonActivate));

        // set positions.
        tabLayout.addTab(quickAccess, 0);
        tabLayout.addTab(recent, 1);

        // By viewPager instance we're adding on Page Change Listener so whenever user will change tabs view pager change, too.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // get support of toolbar.
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // making drawable instance.
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // set and call navigation view(which will show on drawer).
        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // check the first item of navigation view because it is home.
        navigationView.getMenu().getItem(0).setChecked(true);

        // it will call when user will change the viewPager.
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1)
            {
                // this function is user to set activate and non-activate tab color and Alpha.
                onTabChanged(i, "");
            }

            @Override
            public void onPageSelected(int i) {
                onTabChanged(i, "");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    // this function change tab but according to name of tab.
                    onTabChanged(9, Objects.requireNonNull(tab.getText()).toString());
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void onTabChanged(int i, String s){
        if (i == 0 || s.equals("Quick Touch")) {
            quickAccessIcon.setAlpha(1f);
            recentViewIcon.setAlpha(0.5f);
            quickAccessText.setAlpha(1f);
            recentViewText.setAlpha(0.5f);
        } else {
                quickAccessIcon.setAlpha(0.5f);
                recentViewIcon.setAlpha(1f);
                quickAccessText.setAlpha(0.5f);
                recentViewText.setAlpha(1f);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        // this function close the drawer if it will open and push that to starting.
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.add_new_number:
                Intent intent = new Intent(this, AddSOS.class);
                startActivity(intent);
                break;
            case R.id.feedback:
                Intent feedbackActivity = new Intent(this, Feedback.class);
                startActivity(feedbackActivity);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.emergencyNum:
                Intent intent = new Intent(this, EmergencyNumbers.class);
                startActivity(intent);
                break;
            case R.id.saved:
                Intent savedNumberActivity = new Intent(this, SavedNumber.class);
                startActivity(savedNumberActivity);
                break;
            case R.id.history:
                Intent history = new Intent(this, History.class);
                startActivity(history);
                break;
            case R.id.setting:
                Intent settings = new Intent(this, setting.class);
                startActivity(settings);
                break;
            case R.id.about:
                Intent about = new Intent(this, About.class);
                startActivity(about);
                break;
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        navigationView.getMenu().getItem(0).setChecked(true);
    }
}
