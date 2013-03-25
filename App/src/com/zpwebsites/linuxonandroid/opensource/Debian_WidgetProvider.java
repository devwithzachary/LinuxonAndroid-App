/*
 * Copyright (C) 2012 linuxonandroid.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zpwebsites.linuxonandroid.opensource;

import java.util.Arrays;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
//The widget provider for the debian widget
public class Debian_WidgetProvider extends AppWidgetProvider {

	@Override public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.i("TFB Debug",	"UBUNTU onUpdate: " + Arrays.asList(appWidgetIds));

		for (int i = 0; i < appWidgetIds.length; i++) {
			Intent intent = new Intent(context, Debian_Activity.class);

			// IMPORTANT: The second parameter below (2) HAVE to be different if adding more distros!
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 2,	intent, 0);

			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_debian);
			views.setOnClickPendingIntent(R.id.button, pendingIntent);

			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
		}
	}

	@Override public void onDisabled(Context context) {
		Log.i("TFB Debug",	"DEBIAN onDisabled, deleting config");

		SharedPreferences prefs = context.getSharedPreferences(Debian_Activity.PREFS_NAME, 0);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.remove(String.format(Debian_Activity.cfg_Image));
        prefsEditor.commit();
	}

}
