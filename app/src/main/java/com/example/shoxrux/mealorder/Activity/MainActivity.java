package com.example.shoxrux.mealorder.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shoxrux.mealorder.Adapter.FavoriteRecyclerViewAdapter;
import com.example.shoxrux.mealorder.R;
import com.example.shoxrux.mealorder.Fragment.Tab1Favorite;
import com.example.shoxrux.mealorder.Fragment.Tab2Menu;
import com.example.shoxrux.mealorder.Fragment.Tab3Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//This activity starts after log in as User
public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private static int LOGIN_SUCCEED = 111;
    private static int ADMIN_PAGE = 212;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check user logged or not, if not go to login page
        checkUserSigned();
        //Find a toolbar from id
        //and set to Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //Create a Tab Layout (buttons)
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //set up view pager to the layout
        tabLayout.setupWithViewPager(mViewPager);
        //set icons of the tab buttons
        tabLayout.getTabAt(0).setIcon(R.drawable.favorite);
        tabLayout.getTabAt(1).setIcon(R.drawable.menu);
        tabLayout.getTabAt(2).setIcon(R.drawable.user);
        //Add listener on swiping in the page
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //Add listener to buttons of tab
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }


    //Check user logged or not, if not go to login page
    private void checkUserSigned() {
        //Get Firebase auth
        mAuth = FirebaseAuth.getInstance();
        //Get current user
        currentUser = mAuth.getCurrentUser();
        // Check if user is signed in
        if (currentUser == null) {
            //start Login Activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_SUCCEED);
        }else{
            //If current user admin and signed successfully
            if(currentUser.getEmail().equals("admin@gmail.com")){
                //start Admin Activity
                Intent adminIntent = new Intent(this, AdminActivity.class);
                startActivityForResult(adminIntent, ADMIN_PAGE);
            }
        }

    }
    //Log Out function
    public void logout() {
        //sing out of Firebase
        mAuth.signOut();
        //finish the activity
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check If intent requested back from Login Page
        if (requestCode == LOGIN_SUCCEED) {
            //Check if it succeeded successfully
            if (resultCode == Activity.RESULT_OK) {
                //Finish this activity and reload it again
                finish();
                startActivity(getIntent());
            }
        }
        if(requestCode == FavoriteRecyclerViewAdapter.MENU_ORDER){
            if(resultCode == Activity.RESULT_OK){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Successfully ordered!");
                dialog.setMessage("Your order was received, it will be confirmed soon. Please be in touch");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        }
        //Check If intent requested back from Admin Activity
        if (requestCode == ADMIN_PAGE){
            //If the result RESULT_OK and log out admin
            if(resultCode == Activity.RESULT_OK){
                logout();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Log out when log out button clicked
        if (id == R.id.log_out) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        //Set title of tab buttons
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "FAVORITE";
                case 1:
                    return "MENU";
                case 2:
                    return "PROFILE";
            }
            return null;
        }
        // getItem is called to instantiate the fragment for the given page.
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Tab1Favorite tab1 = new Tab1Favorite();
                    return tab1;
                case 1:
                    Tab2Menu tab2 = new Tab2Menu();
                    return tab2;
                case 2:
                    Tab3Profile tab3 = new Tab3Profile();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
