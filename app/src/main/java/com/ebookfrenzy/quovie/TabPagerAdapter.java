package com.ebookfrenzy.quovie;

/**
 * Created by Rambo on 7/8/17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ebookfrenzy.quovie.Fragments.FinanceFragment;
import com.ebookfrenzy.quovie.Fragments.LifeStyleFragment;
import com.ebookfrenzy.quovie.Fragments.SportsFragment;
import com.ebookfrenzy.quovie.Fragments.TechFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {
    //to count the amount od tabs within the program
    int tabCount;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs){
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override public Fragment getItem(int position){
        //use a switch case to get the different types of tabs
        switch (position){
            case 0:
                SportsFragment sports = new SportsFragment();
                return sports;
            case 1:
                TechFragment tech = new TechFragment();
                return tech;
            case 2:
                FinanceFragment finance = new FinanceFragment();
                return finance;
            case 3:
                LifeStyleFragment lifestyle = new LifeStyleFragment();
                return lifestyle;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
