package satedshark.com.vk.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_CRIME_ID= "com.vk.satedshark.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mFirstButton;
    private Button mLastButton;
    private Crime crime;

    public static Intent newIntent(Context packageContext, UUID crimeID){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeID);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = findViewById(R.id.crime_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        mFirstButton = findViewById(R.id.button_first);
        mLastButton = findViewById(R.id.button_last);
        mFirstButton.setOnClickListener(this);
        mLastButton.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }

            @Override
            public void startUpdate(ViewGroup container) {
                super.startUpdate(container);
                if (mViewPager.getCurrentItem() == 0)mFirstButton.setClickable(false);
                else mFirstButton.setClickable(true);
                if (mViewPager.getCurrentItem() == mCrimes.size() - 1)mLastButton.setClickable(false);
                else mLastButton.setClickable(true);
                crime = mCrimes.get(mViewPager.getCurrentItem());
            }

        });
        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        for(int i = 0; i < mCrimes.size(); i++){
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == mFirstButton)mViewPager.setCurrentItem(0);
        if (v == mLastButton)mViewPager.setCurrentItem(mCrimes.size() - 1);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_crime, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.del_crime):
                CrimeLab.get(getApplicationContext()).delCrime(crime);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
