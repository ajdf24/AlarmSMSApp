package rieger.alarmsmsapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.BundleHandler;

/**
 * Clear activity as light
 */
public class LightActivity extends AppCompatActivity {

    private static final String LOG_TAG = LightActivity.class.getSimpleName();

    PowerManager.WakeLock wakeLock;

    Rule rule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        rule = BundleHandler.getRuleFromBundle(this);

        setScreenToFullBrigtnis();

        Thread thread = new Thread() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                while (currentTime + rule.getLightTime() > System.currentTimeMillis()) {

                }
                finish();

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                Thread.currentThread().interrupt();
            }
        };
        thread.start();

    }

    private void setScreenToFullBrigtnis(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        findViewById(R.id.activity_light).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        getSupportActionBar().hide();


        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = 1F;
        getWindow().setAttributes(layout);

        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
