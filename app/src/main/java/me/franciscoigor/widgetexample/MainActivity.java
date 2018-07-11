package me.franciscoigor.widgetexample;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_widget);

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getApplicationContext()
                .getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        int items=0;
        ArrayList<String> filter=new ArrayList<String>();
        filter.add("android");
        filter.add("com.android.keychain");
        filter.add("com.android.deskclock");
        filter.add("com.android.gm");
        filter.add("com.android.defcontainer");
        filter.add("com.android.printspooler");
        filter.add("com.android.settings");
        filter.add("com.android.cellbroadcastreceiver");


        ArrayList<String> apps=new ArrayList<String>();
        for (int i = 0; i < queryUsageStats.size() ; i++) {
            UsageStats stat=queryUsageStats.get(i);
            String name = stat.getPackageName();
            if (filter.contains(name)
                    || name.startsWith("com.android.providers")
                    || name.startsWith("com.google.android.inputmethod")
                    || name.startsWith("com.google.android")
                    || apps.contains(name) ){
                continue;
            }
            System.out.println("Checking: "+name);
            try {

                Drawable iconDrawable = getApplicationContext().getPackageManager().getApplicationIcon(name);
                if (iconDrawable instanceof BitmapDrawable){
                    System.out.println(items+ " ICON for "+ name + " " + stat.getTotalTimeInForeground());
                    Bitmap bitmap = ((BitmapDrawable)iconDrawable).getBitmap();
                    if (items==0){
                        ImageButton b=findViewById(R.id.icon1);
                        b.setImageBitmap(bitmap);
                        TextView t=findViewById(R.id.text1);
                        t.setText(name);
                    }
                    if (items==1){
                        ImageButton b=findViewById(R.id.icon2);
                        b.setImageBitmap(bitmap);
                        TextView t=findViewById(R.id.text2);
                        t.setText(name);
                    }
                    apps.add(name);
                    items++;
                }


            }
            catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }


    }
}
