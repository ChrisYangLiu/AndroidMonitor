package com.han.devtool;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.react.devsupport.DebugOverlayController;
import com.han.activitytracker.AccessibilityUtil;
import com.han.activitytracker.TrackerService;
import com.han.fps.FPSService;
import ezy.assist.compat.RomUtil;
import ezy.assist.compat.SettingsCompat;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private static final int REQUEST_CODE = 1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TextView tv = (TextView) findViewById(R.id.info);
    tv.setMovementMethod(ScrollingMovementMethod.getInstance());
    tv.setText(readString("/system/build.prop"));
  }



  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button1: {
        startService(new Intent(this, FPSService.class).putExtra(FPSService.FPS_COMMAND, FPSService.FPS_COMMAND_OPEN));
        break;
      }
      case R.id.button2: {
        if (AccessibilityUtil.checkAccessibility(this)) {
          startService(new Intent(this, TrackerService.class).putExtra(TrackerService.Tracker_COMMAND, TrackerService.Tracker_COMMAND_OPEN));
        }
        break;
      }

      case R.id.manage:
        SettingsCompat.manageDrawOverlays(this);
        break;
      case R.id.toggle:
        boolean granted1 = SettingsCompat.canDrawOverlays(this);
        SettingsCompat.setDrawOverlays(this, !granted1);
        boolean granted2 = SettingsCompat.canDrawOverlays(this);
        Toast.makeText(this,RomUtil.getVersion() + "\n" +RomUtil.getName() + "\ngranted: " + granted2,Toast.LENGTH_LONG).show();
        break;
      
    }
  }

  public static String readString(String file) {
    InputStream input = null;
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    try {
      input = new FileInputStream(new File(file));
      byte[] buffer = new byte[1024 * 4];
      int n;
      while (-1 != (n = input.read(buffer))) {
        output.write(buffer, 0, n);
      }
      output.flush();
      return output.toString("UTF-8");
    } catch (IOException e) {
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (output != null) {
        try {
          output.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return "";
  }
}
