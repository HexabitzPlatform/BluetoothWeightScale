package com.hexabitz.bluetoothweightscale;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressLint("SetTextI18n")
public class WiFiConnection extends AppCompatActivity {
  Thread Thread1 = null;
  EditText etIP, etPort;
  TextView tvMessages;
  EditText etMessage;
  Button btnSend;
  String SERVER_IP;
  int SERVER_PORT;

  private PrintWriter output;
  private BufferedReader input;
  private Socket socket;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wi_fi_connection);
    setTitle("Wifi Connection");
    etIP = findViewById(R.id.etIP);
    etPort = findViewById(R.id.etPort);
    tvMessages = findViewById(R.id.tvMessages);
    etMessage = findViewById(R.id.etMessage);
    btnSend = findViewById(R.id.btnSend);
    Button btnConnect = findViewById(R.id.btnConnect);
    btnConnect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        tvMessages.setText("");
        SERVER_IP = etIP.getText().toString().trim();
        SERVER_PORT = Integer.parseInt(etPort.getText().toString().trim());
//        try {
//          Socket socket = new Socket(SERVER_IP, SERVER_PORT);
//          output = new PrintWriter(socket.getOutputStream());
//          input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//          Toast.makeText(WiFiConnection.this, "Connected", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//          Toast.makeText(WiFiConnection.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
        Thread1 = new Thread(new Thread1());
        Thread1.start();
      }
    });

    Button btnOpen = findViewById(R.id.btnOpen);
    btnOpen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent Intent = new Intent(WiFiConnection.this, MainActivity.class);
        Intent.putExtra("wifi", true);
        Intent.putExtra("SERVER_IP",  SERVER_IP);
        Intent.putExtra("SERVER_PORT",  SERVER_PORT);
        Thread1.destroy();
        startActivity(Intent);
      }
    });


    btnSend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String message = etMessage.getText().toString().trim();
        if (!message.isEmpty()) {
          new Thread(new Thread3(message)).start();
        }
      }
    });
  }




  class Thread1 implements Runnable {
    public void run() {
      try {
        socket = new Socket(SERVER_IP, SERVER_PORT);
        output = new PrintWriter(socket.getOutputStream());
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Toast.makeText(WiFiConnection.this, "Connected", Toast.LENGTH_SHORT).show();
            tvMessages.setText("Connected\n");
          }
        });
        new Thread(new Thread2()).start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  class Thread2 implements Runnable {
    @Override
    public void run() {
      while (true) {
        try {
          final String message = input.readLine();
          if (message != null) {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                tvMessages.setText("Weight : " + message + "\n");
              }
            });
          } else {
            Thread1 = new Thread(new Thread1());
            Thread1.start();
            return;
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  class Thread3 implements Runnable {
    private String message;
    Thread3(String message) {
      this.message = message;
    }
    @Override
    public void run() {
      output.write(message);
      output.flush();
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          tvMessages.append("client: " + message + "\n");
          etMessage.setText("");
        }
      });
    }
  }
}