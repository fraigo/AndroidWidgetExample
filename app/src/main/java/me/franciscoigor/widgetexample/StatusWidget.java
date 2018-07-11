package me.franciscoigor.widgetexample;

import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link StatusWidgetConfigureActivity StatusWidgetConfigureActivity}
 */
public class StatusWidget extends AppWidgetProvider {

    static HashMap<String, Integer> status;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        CharSequence configItems = StatusWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        int maxItems;
        try{
            maxItems = Integer.parseInt("0"+configItems);
        } catch(Exception e){
            maxItems = 5;
        }



        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.status_widget);

        System.out.println("*** ON UPDATE APP WIDGET ***");
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context
                .getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, cal.getTimeInMillis(),
                        System.currentTimeMillis());
        int items=0;
        for (int i = 0; i < queryUsageStats.size() && items< maxItems; i++) {
            UsageStats stat=queryUsageStats.get(i);
            String name = stat.getPackageName();
            if (name.startsWith("com.android.provider")){
                continue;
            }
            try {

                Drawable iconDrawable = context.getPackageManager().getApplicationIcon(name);
                if (iconDrawable instanceof BitmapDrawable){
                    System.out.println(items+ " ICON for "+ name);
                    Bitmap bitmap = ((BitmapDrawable)iconDrawable).getBitmap();
                    Intent appIntent = new Intent(Intent.ACTION_MAIN);
                    String packageName = appIntent.getStringExtra(name);
                    appIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                    PendingIntent appsPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    if (items==0){
                        views.setTextViewText(R.id.text1, name);
                        views.setImageViewBitmap(R.id.icon1, bitmap);
                        views.setOnClickPendingIntent(R.id.icon1, appsPendingIntent);
                    }
                    if (items==1){
                        views.setTextViewText(R.id.text2, name);
                        views.setImageViewBitmap(R.id.icon2, bitmap);
                        views.setOnClickPendingIntent(R.id.icon2, appsPendingIntent);
                    }
                    items++;
                }


            }
            catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }




    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            StatusWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

