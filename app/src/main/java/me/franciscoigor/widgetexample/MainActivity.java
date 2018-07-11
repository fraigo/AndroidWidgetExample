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
        for (int i = 0; i < queryUsageStats.size() && i< 8; i++) {
            UsageStats stat=queryUsageStats.get(i);
            String name = stat.getPackageName();
            try {

                Drawable iconDrawable = getApplicationContext().getPackageManager().getApplicationIcon(name);
                if (iconDrawable instanceof BitmapDrawable){
                    System.out.println(items+ " ICON for "+ name);
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
                    items++;
                }


            }
            catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }


    }
}
