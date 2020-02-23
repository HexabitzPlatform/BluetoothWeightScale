package com.hexabitz.bluetoothweightscale.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.hexabitz.bluetoothweightscale.R;


public class Settings extends Fragment {

  public static int Destination = 2, Source = 1;

  View rootView;

  NumberPicker destinationNP;
  NumberPicker sourceNP;


  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.frag_settings, container, false);


    destinationNP = rootView.findViewById(R.id.destinationNP);
    sourceNP = rootView.findViewById(R.id.sourceNP);

    String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    destinationNP.setMinValue(0);
    destinationNP.setMaxValue(numbers.length-1);
    destinationNP.setDisplayedValues(numbers);
    destinationNP.setWrapSelectorWheel(true);
    destinationNP.setValue(1);

    sourceNP.setMinValue(0);
    sourceNP.setMaxValue(numbers.length - 1);
    sourceNP.setDisplayedValues(numbers);
    sourceNP.setWrapSelectorWheel(true);
    sourceNP.setValue(0);

    destinationNP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
      @Override
      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Destination = newVal + 1;
      }
    });


    sourceNP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
      @Override
      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Source = newVal + 1;
      }
    });

    return rootView;
  }

}
