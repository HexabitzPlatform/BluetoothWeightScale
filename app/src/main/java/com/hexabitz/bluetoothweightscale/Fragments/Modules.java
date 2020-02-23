package com.hexabitz.bluetoothweightscale.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hexabitz.bluetoothweightscale.Modules_Fragments.*;
import com.hexabitz.bluetoothweightscale.R;
import com.hexabitz.bluetoothweightscale.ViewPagerAdapter;


public class Modules extends Fragment {

  View rootView;

  Fragment Settings = new Settings();
  Fragment H26R0_LOAD_CELL = new H26R0_LOAD_CELL();

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.frag_modules, container, false);

    ViewPager viewPager = rootView.findViewById(R.id.pager);
    ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

    adapter.addFragment(Settings, "Settings");
    adapter.addFragment(H26R0_LOAD_CELL, "LOAD_CELL");

    viewPager.setAdapter(adapter);

    final TabLayout tabLayout = rootView.findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
//    int[] tabIcons = {
//        R.drawable.module_h01r00_led,
//        R.drawable.module_h09r00_relay,
//        R.drawable.module_h08r6_ir_sensor,
//    };
//    tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//    tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//    tabLayout.getTabAt(2).setIcon(tabIcons[2]);

    return rootView;
  }

}
