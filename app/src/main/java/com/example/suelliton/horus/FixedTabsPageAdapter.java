package com.example.suelliton.horus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.suelliton.horus.fragments.FragmentArea;
import com.example.suelliton.horus.fragments.FragmentInformacoes;
import com.example.suelliton.horus.fragments.FragmentPercentual;

public class FixedTabsPageAdapter extends FragmentPagerAdapter {
    public FixedTabsPageAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentArea();
            case 1:
                return new FragmentPercentual();
            case 2:
                return new FragmentInformacoes();
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
                return "Área";
            case 1:
                return "Percentual";
            case 2:
                return "Informações";
            default:
                return null;
        }
    }

}