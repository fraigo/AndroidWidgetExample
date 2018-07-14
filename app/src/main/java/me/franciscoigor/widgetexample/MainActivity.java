package me.franciscoigor.widgetexample;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.franciscoigor.widgetexample.helpers.AppIcons;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_widget);


        HashMap<String, Bitmap> icons = AppIcons.getIcons(this);
        int[] iconIds = {
                R.id.icon1,
                R.id.icon2,
                R.id.icon3,
                R.id.icon4
        };

        for (int i = 0; i < iconIds.length; i++) {
            ImageButton button=findViewById(iconIds[i]);
            button.setVisibility(View.GONE);
        }

        Iterator it = icons.entrySet().iterator();
        int index=0;
        while (it.hasNext()) {
            Map.Entry<String, Bitmap> pair = (Map.Entry)it.next();
            if (index < iconIds.length){
                ImageButton button=findViewById(iconIds[index]);
                button.setImageBitmap(pair.getValue());
                button.setVisibility(View.VISIBLE);
                index++;
            }

        }



    }
}
