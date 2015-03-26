package com.example.dhruvil.parkmomo.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.dhruvil.parkmomo.R;


public class MyBroadcastReceiver extends BroadcastReceiver {

	private Context context;
	public NotificationManager myNotificationManager;
	public static final int NOTIFICATION_ID = 1;

	@Override
	public void onReceive(Context context, final Intent intent) {
		this.context=context;
		Bundle bundle = intent.getExtras();
		String minutes=bundle.getString("minutes","");
		myNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

		CharSequence NotificationTicket = "Park MoMo";
		CharSequence NotificationTitle = "Park MoMo";
		CharSequence NotificationContent = minutes;

//		Notification notification = new Notification(R.drawable.logo, NotificationTicket, 0);
//		Intent notificationIntent = new Intent(context, MapActivity.class);
//		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//		notification.setLatestEventInfo(context, NotificationTitle, NotificationContent, contentIntent);
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		notification.setStyle(new Notification.BigTextStyle()
//				.bigText(aVeryLongString));
		Notification notif = new Notification.Builder(context)
				.setContentTitle(NotificationTitle)
				.setContentText(NotificationContent)
				.setSmallIcon(R.drawable.logo)

				.setStyle(new Notification.BigTextStyle()
						.bigText(NotificationContent))
				.build();
		myNotificationManager.notify(NOTIFICATION_ID, notif);


	}
}
