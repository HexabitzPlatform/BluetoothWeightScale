package com.hexabitz.bluetoothweightscale.Modules_Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.bendaschel.sevensegmentview.SevenSegmentView;
import com.hexabitz.bluetoothweightscale.Fragments.Settings;
import com.hexabitz.bluetoothweightscale.JAVA_COMS_LIB.HexaInterface;
import com.hexabitz.bluetoothweightscale.MainActivity;
import com.hexabitz.bluetoothweightscale.R;

import java.nio.ByteBuffer;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class H26R0_LOAD_CELL extends Fragment {

  boolean isLocked = false, isOn = false;
  int Code;
  byte[] Payload;
  int time;
  Timer t = new Timer();

  View rootView;
  Spinner unitSpinner;



  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.frag_h26r0_load_cell, container, false);

    unitSpinner = rootView.findViewById(R.id.unitSpinner);
    List<String> spinnerArray = new ArrayList<>();
    spinnerArray.add("Gram");
    spinnerArray.add("KGram");
    spinnerArray.add("Pound");
    spinnerArray.add("Ounces");

    ArrayAdapter<String> adapter = new ArrayAdapter<>(
        getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    Code = HexaInterface.Message_Codes.CODE_H26R0_STREAM_PORT_GRAM;
    unitSpinner.setAdapter(adapter);

    unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
          case 0: Code = HexaInterface.Message_Codes.CODE_H26R0_STREAM_PORT_GRAM; break;
          case 1: Code = HexaInterface.Message_Codes.CODE_H26R0_STREAM_PORT_KGRAM; break;
          case 2: Code = HexaInterface.Message_Codes.CODE_H26R0_STREAM_PORT_POUND; break;
          case 3: Code = HexaInterface.Message_Codes.CODE_H26R0_STREAM_PORT_OUNCE; break;
        }

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    final TextView PeriodTV = rootView.findViewById(R.id.PeriodTV);
    final TextView TimeOutTV = rootView.findViewById(R.id.TimeOutTV);
    final Switch LoadCellSwitch = rootView.findViewById(R.id.LoadCellSwitch);
    final TextView weightLBL = rootView.findViewById(R.id.weightLBL);


    final Switch infiniteTimeSwitch = rootView.findViewById(R.id.infiniteTimeSwitch);


    LoadCellSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
          LoadCellSwitch.setText("On");
          Code = HexaInterface.Message_Codes.CODE_H26R0_STREAM_PORT_GRAM;

          int period = Integer.parseInt(PeriodTV.getText().toString());
          if(infiniteTimeSwitch.isChecked())
            time = 0xFFFFFFFF;
          else
            time = Integer.parseInt(TimeOutTV.getText().toString());

          byte[] periodBytes = ByteBuffer.allocate(4).putInt(period).array();
          byte[] timeBytes = ByteBuffer.allocate(4).putInt(time).array();


          Payload = new byte[]{
              1,
              periodBytes[0], // we reverse them here because in Java the allocate reversed their order
              periodBytes[1],
              periodBytes[2],
              periodBytes[3],

              timeBytes[0],
              timeBytes[1],
              timeBytes[2],
              timeBytes[3],

              6,
              1};
          SendMessage();
          if(Code != HexaInterface.Message_Codes.CODE_H26R0_STOP)
            ReceiveMessage();
        }
        else
        {
          LoadCellSwitch.setText("Off");
          weightLBL.setText("Stopped");

          Code = HexaInterface.Message_Codes.CODE_H26R0_STOP;
          Payload = new byte[0];
          SendMessage();
          StopReceiving();
        }
      }
    });

    infiniteTimeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked) {
          TimeOutTV.setEnabled(false);
          infiniteTimeSwitch.setText("On");

        }
        else {
          TimeOutTV.setEnabled(true);
          infiniteTimeSwitch.setText("Off");
        }
      }
    });


    return rootView;
  }

  private void SendMessage() {
    if (!isLocked) {
      ((MainActivity) Objects.requireNonNull(getActivity())).SendMessage((byte) Settings.Destination, (byte) Settings.Source, Code, Payload);
      isLocked = true;
      t.schedule(new TimerTask() {
        @Override
        public void run() {
          isLocked = false;
        }
      }, 100);
    }
  }

  private void ReceiveMessage() {
    ((MainActivity) Objects.requireNonNull(getActivity())).ReceiveMessage();
  }
  private void StopReceiving() {
    ((MainActivity) Objects.requireNonNull(getActivity())).StopReceiving();
  }
}
