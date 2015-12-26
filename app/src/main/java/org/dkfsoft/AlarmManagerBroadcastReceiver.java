package org.dkfsoft;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        // actually no need to update these every time
        this.context = context;
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
        int c = context.getResources().getColor(R.color.colorText);
        if (res[0] != null && res[1] != null && res[0].length() > 0) {
            Log.d("MyWidget", "Update views to " + res[0] + ":" + res[1]);
            if (res[0] != null && res[1] != null && res[1].length() > 0) {
                if (!res[0].substring(0,1).equals("-")) res[0] = "+" + res[0];
            }
            c = (res[0].substring(0, 1).equals("+")) ? context.getResources().getColor(R.color.colorPositive) : context.getResources().getColor(R.color.colorNegative);
            rem_views.setTextColor(R.id.tv, c);
            rem_views.setTextColor(R.id.C, c);
            rem_views.setTextColor(R.id.rs, c);
            rem_views.setTextViewText(R.id.tv, res[0]);
            rem_views.setTextViewText(R.id.pressure, res[1]);
        } else {
            rem_views.setTextColor(R.id.tv, c);
            rem_views.setTextColor(R.id.C, c);
            rem_views.setTextColor(R.id.rs, c);
            rem_views.setTextViewText(R.id.tv, context.getResources().getString(R.string.widget_text));
            rem_views.setTextViewText(R.id.pressure, context.getResources().getString(R.string.pressure));
        }

        app_manager.updateAppWidget(comp_name, rem_views);
    }
}
