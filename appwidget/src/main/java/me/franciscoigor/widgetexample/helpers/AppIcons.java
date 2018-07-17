package me.franciscoigor.widgetexample.helpers;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AppIcons {

    static ArrayList<String> filters;

    static {
        filters =new ArrayList<String>();
        filters.add("android");
        filters.add("com.android.keychain");
        filters.add("com.android.phone");
        filters.add("com.android.systemui");
        filters.add("com.android.documentsui");
        filters.add("com.android.externalstorage");
        filters.add("com.google.android.apps.nexuslauncher");
        filters.add("com.android.mtp");
        filters.add("com.android.systemui");
        filters.add("com.android.deskclock");
        filters.add("com.android.gm");
        filters.add("com.android.defcontainer");
        filters.add("com.android.printspooler");
        filters.add("com.android.settings");
        filters.add("com.android.cellbroadcastreceiver");

    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @NonNull
    public static Bitmap getBitmapFromDrawable(@NonNull Drawable drawable, int count) {
        int realWidth = drawable.getIntrinsicWidth();
        int realHeight = drawable.getIntrinsicHeight();

        final Bitmap origBmp = Bitmap.createBitmap(realWidth, realHeight, Bitmap.Config.ARGB_8888);

        Canvas original = new Canvas(origBmp);

        drawable.setBounds(0, 0, original.getWidth(), original.getHeight());
        drawable.draw(original);

        /*
        Bitmap bmp = getResizedBitmap(origBmp, 72, 72);
        bmp = bmp.copy(android.graphics.Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bmp);


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#C0DDDDFF"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(drawable.getIntrinsicWidth() * 3.0f / 4.0f, drawable.getIntrinsicWidth() * 3.0f / 4.0f, drawable.getIntrinsicWidth() / 4, paint);
        paint.setColor(Color.parseColor("#404080"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawCircle(drawable.getIntrinsicWidth() * 3.0f / 4.0f, drawable.getIntrinsicWidth() * 3.0f / 4.0f, drawable.getIntrinsicWidth() / 4, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#000040"));
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(  Integer.toString(count),drawable.getIntrinsicWidth() * 3.0f / 4.0f, 5+ drawable.getIntrinsicWidth() * 3.0f / 4.0f, paint);
    */

        return origBmp;
    }


    public static ArrayList<UsageEvents.Event> getLastRunningApps(Context context) {
        ArrayList<UsageEvents.Event> result= new ArrayList<UsageEvents.Event>();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            final long INTERVAL = 3600000;
            final long end = System.currentTimeMillis();
            final long begin = end - INTERVAL;
            final UsageEvents usageEvents = mUsageStatsManager.queryEvents(begin, end);
            while (usageEvents.hasNextEvent()) {
                UsageEvents.Event event = new UsageEvents.Event();
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result.add(event);
                }
            }
        }
        return result;
    }




    public static HashMap<String, Bitmap> getIcons(Context context){
        HashMap<String, Bitmap> list=new HashMap<String, Bitmap>();
        ArrayList<String> appNames= new ArrayList<String>();

        ArrayList<UsageEvents.Event> stats = getLastRunningApps(context);


        if (stats.size()==0){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }



        Collections.sort(stats, new Comparator<UsageEvents.Event>() {
            @Override
            public int compare(UsageEvents.Event o1, UsageEvents.Event o2) {
                if (o1.getTimeStamp()>o2.getTimeStamp()){
                    return -1;
                }else if (o1.getTimeStamp()<o2.getTimeStamp()){
                    return 1;
                }
                return 0;
            }
        });
        System.out.println("SIZE: "+ stats.size());
        for (int i = 0; i < stats.size() ; i++) {
            UsageEvents.Event stat=stats.get(i);
            String name = stat.getPackageName();
            System.out.println("Package "+ i + ":" +name);
            if (filters.contains(name)
                    || name.startsWith("com.android.providers")
                    || name.startsWith("com.google.android.inputmethod")
                    || appNames.contains(name)){
                continue;
            }
            System.out.println("Package "+ i + " OK ");
            try {
                long time = (System.currentTimeMillis() - stat.getTimeStamp()) / (1000 * 60 );

                Drawable iconDrawable = context.getPackageManager().getApplicationIcon(name);
                if (iconDrawable.getIntrinsicWidth()<50){
                    continue;
                }
                System.out.println(stat.getTimeStamp()+" ICON for "+ name + " width:" + iconDrawable.getIntrinsicWidth()+" "+(new Date(stat.getTimeStamp())));
                Bitmap bitmap=getBitmapFromDrawable(iconDrawable, (int)time);

                list.put(name,bitmap);
                appNames.add(name);
                
            }
            catch (PackageManager.NameNotFoundException e) {
                System.out.println("Error converting image "+name);
                //e.printStackTrace();
            }

        }
        System.out.println("APPNAMES: "+appNames);
        return list;

    }

}
