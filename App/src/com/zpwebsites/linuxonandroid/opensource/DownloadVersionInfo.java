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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadVersionInfo extends AsyncTask<String, Integer, String> {

	private static String			TAG					= "Complete Linux Installer";		// Used when logging as app name
	private static String			NAME				= "DownloadFile";					// Used as activity name when logging

	@Override protected String doInBackground(String... sUrl) {
		try {
			URL url = new URL(sUrl[0]);
			URLConnection connection = url.openConnection();
			connection.connect();
			int fileLength = connection.getContentLength();

			InputStream input = new BufferedInputStream(url.openStream());

			byte data[] = new byte[128];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				total += count;
				publishProgress((int) (total * 100 / fileLength));

				String dataStr = new String(data).trim();

				String line1 = dataStr.substring(0, dataStr.indexOf("\n"));
				String line2 = dataStr.substring(dataStr.indexOf("\n") + 1);

				// TODO: We should probably verify the order of the lines, but if we make sure not to change the order in the file it will work!

				Script_Updater.online_Boot = Float.valueOf(line1.substring(line1.indexOf("=") + 1));
				Script_Updater.online_AutoBoot = Float.valueOf(line2.substring(line2.indexOf("=") + 1));

//				Log.e(TAG,  "Line1: " + line1 + "!");
//				Log.e(TAG,  "Line2: " + line2 + "!");
			}

			input.close();

		} catch (Exception e) {
			Log.e(TAG, NAME + ": Error downloading file! " + e.getMessage());
			return e.getMessage();
		}

		return null;
	}

	@Override protected void onPreExecute() {
		super.onPreExecute();
		Script_Updater.mProgressDialog.show();
	}

	@Override protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		Script_Updater.mProgressDialog.setProgress(progress[0]);
	}

	@Override protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Script_Updater.mProgressDialog.dismiss();

		Script_Updater.txt_OnlineBoot.setText(" V" + Script_Updater.online_Boot);
		Script_Updater.txt_OnlineAutoBoot.setText(" V" + Script_Updater.online_AutoBoot);

		Script_Updater.updateLayout();

		if (result != null) {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(Script_Updater.mProgressDialog.getContext());
			builder.setMessage(R.string.updater_ErrorGettingVersions).setPositiveButton(R.string.updater_Btn_Ok, dialogClickListener).show();

		}
	}

}
