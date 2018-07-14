package me.franciscoigor.widgetexample;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.franciscoigor.widgetexample.helpers.AppIcons;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link StatusWidgetConfigureActivity StatusWidgetConfigureActivity}
 */
public class StatusWidget extends AppWidgetProvider {

    static HashMap<String, Integer> status;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        System.out.println("ONUPDATE");
        CharSequence configItems = StatusWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        int maxItems;
        try{
            maxItems = Integer.parseInt("0"+configItems);
        } catch(Exception e){
            maxItems = 5;
        }
        System.out.println("MAXITEMS "+ maxItems);

        int[] iconIds = {
                R.id.icon1,
                R.id.icon2,
                R.id.icon3,
                R.id.icon4
        };


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.status_widget);

        for (int i = 0; i < iconIds.length; i++) {
            views.setViewVisibility(iconIds[i], View.GONE);
        }

        HashMap<String, Bitmap> icons = AppIcons.getIcons(context);

        Intent appIntent = new Intent(Intent.ACTION_MAIN);
        String packageName = appIntent.getStringExtra(context.getPackageName());
        appIntent.setPackage(context.getPackageName());
        appIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent appsPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_title, appsPendingIntent);

        Iterator it = icons.entrySet().iterator();
        int index=0;
        while (it.hasNext()) {
            Map.Entry<String, Bitmap> pair = (Map.Entry)it.next();
            appIntent = new Intent(Intent.ACTION_MAIN);
            packageName = appIntent.getStringExtra(pair.getKey());
            appIntent.setPackage(packageName);
            appIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            appsPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (index < iconIds.length){
                views.setImageViewBitmap(iconIds[index], pair.getValue());
                views.setOnClickPendingIntent(iconIds[index], appsPendingIntent);
                views.setViewVisibility(iconIds[index], View.VISIBLE);
            }
            index++;
        }


        appWidgetManager.updateAppWidget(appWidgetId, views);




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

