package org.dkfsoft;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import android.app.AlarmManager;

public class ChelWeather extends AppWidgetProvider {

	private static final String CLICK_ACTION = "CWClick";

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		String action = intent.getAction();

		if (CLICK_ACTION.equals(action)) {
			Log.d("MyWidget", "Click");
			// temporary do nothing
			// reserved for future usage
			//Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//Update every 10 minutes
		am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, 1000*10*60, pi);
    }

	@Override
	public void onDisabled(Context context) {
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
		super.onDisabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		for (int widgetId : appWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			Intent ic = new Intent(context, ChelWeather.class);

			ic.setAction(CLICK_ACTION);
			ic.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, ic, 0);

			remoteViews.setOnClickPendingIntent(R.id.WidgetLayout,
					pendingIntent);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
}
