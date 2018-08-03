package com.example.suelliton.horus;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetalhesActivity extends AppCompatActivity {

    TabLayout tabLayout;

    public static String nomeExperimento = "";
    public static Integer count = 0;
    ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        Bundle bundle = getIntent().getExtras();
        nomeExperimento = bundle.getString("nomeExp");
        count = bundle.getInt("count");


        setTabLayoutAndViewPager();
    }
    public void setTabLayoutAndViewPager(){
        vp = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pa = new FixedTabsPageAdapter(getSupportFragmentManager());
        vp.setAdapter(pa);

        tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(vp);

        //tabLayout.getTabAt(0).setIcon(R.mipmap.ic_barras);
        //tabLayout.getTabAt(1).setIcon(R.mipmap.ic_sol);
        //tabLayout.getTabAt(2).setIcon(R.mipmap.ic_paper);

    }







}
