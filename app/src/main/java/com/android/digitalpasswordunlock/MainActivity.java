package com.android.digitalpasswordunlock;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DigitalPasswordUnlockPanel digitalPasswordUnlockPanel= findViewById(R.id.password_panel);
        digitalPasswordUnlockPanel.setPassword(MD5Utils.stringToMD5("1234"));
        digitalPasswordUnlockPanel.setOnVerifyListener(new DigitalPasswordUnlockPanel.OnVerifyListener() {
            @Override
            public void onSucceed() {

            }

            @Override
            public void onFailed() {

            }
        });
    }
}
