package com.rachitmishra.gcmdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.iid.InstanceID;
import com.onesignal.OneSignal;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String SCOPE = "GCM";
    public static final String GCM_SENDER_ID = "GcmSenderId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GcmRegistrationTask().execute();
    }

    private String getGcmRegistrationToken() {
        String gcmRegistrationId = "";
        try {
            gcmRegistrationId = InstanceID.getInstance(this).getToken(GCM_SENDER_ID, SCOPE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gcmRegistrationId;
    }

    private class GcmRegistrationTask extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return getGcmRegistrationToken();
        }

        @Override
        protected void onPostExecute(String gcmRegistrationId) {
            TextView.class.cast(findViewById(R.id.textView)).setText(gcmRegistrationId);

            //One-Signal
            OneSignal.startInit(MainActivity.this)
                    .setAutoPromptLocation(false)
                    .init();
            //One-Signal logs, it logs all msg received and all actions related to One-Signal
            //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);

            OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {

                @Override
                public void idsAvailable(String playerId, String gcmRegistrationId) {
                    TextView.class.cast(findViewById(R.id.textView2)).setText(playerId);
                    TextView.class.cast(findViewById(R.id.textView3)).setText(gcmRegistrationId);
                }
            });
        }
    }
}
