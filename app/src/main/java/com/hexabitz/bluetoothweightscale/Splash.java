package com.hexabitz.bluetoothweightscale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.activity_splash);

    int SPLASH_DISPLAY_LENGTH = 1000;
    new Handler().postDelayed(new Runnable(){
      @Override
      public void run() {
        Intent mainIntent = new Intent(Splash.this, BluetoothDevices.class);
        startActivity(mainIntent);
        finish();
      }
    }, SPLASH_DISPLAY_LENGTH);
  }
}
