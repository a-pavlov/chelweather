package org.dkfsoft;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    /*
    private static int temp = -50;
    private int min_temp = -50;
    private int temp_range = 100;
    private int begin_color = Color.parseColor("#29B6F6");
    private int end_color = Color.parseColor("#FFEA00");
    private int green_step = (Color.green(end_color) - Color.green(begin_color)) / (temp_range);
    private int red_step = (Color.red(end_color) - Color.red(begin_color)) / temp_range;
    private int blue_step = (Color.blue(end_color) - Color.blue(begin_color)) / temp_range;
*/
    RemoteViews rem_views;
    ComponentName comp_name;
    AppWidgetManager app_manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        // actually no need to update these every time
        rem_views = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        comp_name = new ComponentName(context, ChelWeather.class);
        app_manager = AppWidgetManager.getInstance(context);

        // run http request to weather.kikimor
        new KikimorWeather(this).execute();

        /*int absolute_temp = temp + temp_range/2;
        int current_color = Color.rgb(
                Color.red(begin_color) + red_step*absolute_temp,
                Color.green(begin_color) + green_step*absolute_temp,
                Color.blue(begin_color) + blue_step*absolute_temp);
                */
    }

    void refreshValues(String[] res) {
        Log.d("MyWidget", "Update views to " + res[0] + ":" + res[1]);
        rem_views.setTextViewText(R.id.tv, res[0]);
        rem_views.setTextViewText(R.id.pressure, res[1]);
        app_manager.updateAppWidget(comp_name, rem_views);
    }
}
