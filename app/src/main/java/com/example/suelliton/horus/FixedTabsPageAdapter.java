package com.example.suelliton.horus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.suelliton.horus.fragments.FragmentCrescimento;
import com.example.suelliton.horus.fragments.FragmentDetalhes;
import com.example.suelliton.horus.fragments.FragmentSensoriamento;

public class FixedTabsPageAdapter extends FragmentPagerAdapter {
    public FixedTabsPageAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentCrescimento();
            case 1:
                return new FragmentSensoriamento();
            case 2:
                return new FragmentDetalhes();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Crescimento";
            case 1:
                return "Sensoriamento";
            case 2:
                return "Detalhes";
            default:
                return null;
        }
    }

}