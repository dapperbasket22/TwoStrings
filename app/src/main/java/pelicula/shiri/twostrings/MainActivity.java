package pelicula.shiri.twostrings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import pelicula.shiri.twostrings.fragment.ExploreFragment;
import pelicula.shiri.twostrings.fragment.HomeFragment;
import pelicula.shiri.twostrings.fragment.PopularFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;
    NavigationView mNavView;
    FragmentManager mFragmentManager;

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        mNavView = (NavigationView) findViewById(R.id.nav_view_main);
        mNavView.setNavigationItemSelectedListener(this);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null){
            mFragmentManager.beginTransaction().add(R.id.fragmentContainerMain, new HomeFragment())
                    .commit();
            mNavView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment currentFrag = mFragmentManager.findFragmentById(R.id.fragmentContainerMain);
        FragmentTransaction fragTransact = mFragmentManager.beginTransaction();

        if (id == R.id.nav_home) {
            if (!(currentFrag instanceof HomeFragment)){
                fragTransact.replace(R.id.fragmentContainerMain, new HomeFragment()).commit();
                mNavView.setCheckedItem(R.id.nav_home);
            }
        } else if (id == R.id.nav_popular) {
            if (!(currentFrag instanceof PopularFragment)) {
                fragTransact.replace(R.id.fragmentContainerMain, new PopularFragment()).commit();
                mNavView.setCheckedItem(R.id.nav_popular);
            }
        } else if (id == R.id.nav_explore) {
            if (!(currentFrag instanceof ExploreFragment)) {
                fragTransact.replace(R.id.fragmentContainerMain, new ExploreFragment()).commit();
                mNavView.setCheckedItem(R.id.nav_explore);
            }
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
