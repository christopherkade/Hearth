package com.kade_c.hearth.fragments.statistics_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kade_c.hearth.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the Slide View for our General Statistics page.
 */
public class GeneralStatisticsVP extends Fragment {

    View view;

    ViewPager mViewPager;
    StatisticsPagerAdapter mStatisticsPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.statistics_general_vp, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        createViewPager(mViewPager);

        return view;
    }

    /**
     * Creates and sets our slides in our ViewPager.
     */
    private void createViewPager(ViewPager viewPager) {
        Log.d("activity", "viewpager");
        mStatisticsPagerAdapter = new StatisticsPagerAdapter(getActivity().getSupportFragmentManager());
        mStatisticsPagerAdapter.addFrag(new GeneralStatistics(), "GeneralStatistics");
        mStatisticsPagerAdapter.addFrag(new GeneralStatisticsGraph(), "GeneralStatisticsGraph");

        viewPager.setAdapter(mStatisticsPagerAdapter);
    }

    /**
     * Our ViewPager adapter, handles the change of pages.
     */
    public class StatisticsPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public StatisticsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}
