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

import com.example.shoxrux.mealorder.R;
import com.example.shoxrux.mealorder.Fragment.Tab1AdminUsers;
import com.example.shoxrux.mealorder.Fragment.Tab2AdminMenus;
import com.example.shoxrux.mealorder.Fragment.Tab3AdminOrders;


//This activity starts after log in as Admin
public class AdminActivity extends AppCompatActivity {

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
    //Request Codes
    public static int MENU_CHANGED = 231;
    public static int MENU_CREATE = 109;
    public static int ORDER_REQUEST_CODE= 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Find a toolbar from id
        //and set to Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //Create a Tab Layout (buttons)
        TabLayout tabLayout = findViewById(R.id.tabs);
        //set up view pager to the layout
        tabLayout.setupWithViewPager(mViewPager);
        //set icons of the tab buttons
        tabLayout.getTabAt(0).setIcon(R.drawable.user);
        tabLayout.getTabAt(1).setIcon(R.drawable.menu);
        tabLayout.getTabAt(2).setIcon(R.drawable.list_white);
        //Add listener on swiping in the page
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //Add listener to buttons of tab
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //If request code equal to the code when created from AdminMenuRecyclerViewAdapter to AdminMenuInfoActivity
        if(requestCode == MENU_CHANGED){
            //if result code is equal to MENU_UPDATED after success finish of AdminMenuInfoActivity, after editing menu
            if(resultCode == AdminMenuInfoActivity.MENU_UPDATED){
                //Create Dialog with Messages and button
                createDialogWithOk("Menu edited", "Menu information has been edited successfully!");
            }
            //if result code is equal to MENU_DELETED after success finish of AdminMenuInfoActivity, after deleting menu
            if(resultCode == AdminMenuInfoActivity.MENU_DELETED){
                //Create Dialog with Messages and OK button
                createDialogWithOk("Menu deleted","Menu has been deleted successfully!");
            }
        }
        //If request code equal to MENU_CREATE when created from Tab2AdminMenus to AdminMenuCreateActivity
        if(requestCode == MENU_CREATE){
            //if result code is equal to MENU_CREATED after success finish of AdminMenuCreateActivity
            if(resultCode == AdminMenuCreateActivity.MENU_CREATED) {
                //Create Dialog with Messages and OK button
                createDialogWithOk("Menu added", "Menu information has been created successfully!");
            }
        }

        //If ORDER_REQUEST_CODE when we intended from AdminOrderRecyclerViewAdapter (AdminActivity) to AdminOrderInfoActivity
        if(requestCode == ORDER_REQUEST_CODE){
            //if result was ORDER_STATUS_CHANGED after success change of the status from AdminOrderInfoActivity
            if(resultCode == AdminOrderInfoActivity.ORDER_STATUS_CHANGED){
                //Create Dialog with Messages and OK button
                createDialogWithOk("Order stats changed", "Order status has been changed successfully!");
            }
        }

    }
    //Create Dialog with Messages and OK button
    public void createDialogWithOk(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //Create Dialog with Messages and OK button
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
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
                    return "USERS";
                case 1:
                    return "MENUS";
                case 2:
                    return "ORDERS";
            }
            return null;
        }
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    Tab1AdminUsers tab1 = new Tab1AdminUsers();
                    return tab1;
                case 1:
                    Tab2AdminMenus tab2 = new Tab2AdminMenus();
                    return tab2;
                case 2:
                    Tab3AdminOrders tab3 = new Tab3AdminOrders();
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
