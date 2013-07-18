/*
* Copyright (C) 2013 linuxonandroid.org
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.zpwebsites.linuxonandroid.opensource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadFile extends AsyncTask<String, Integer, String> {

	private static String			TAG					= "Complete Linux Installer";		// Used when logging as app name
	private static String			NAME				= "DownloadFile";					// Used as activity name when logging

	@Override protected String doInBackground(String... sUrl) {
		try {
			String destName = sUrl[1];
			file_Delete(destName); // Just to make sure!

			URL url = new URL(sUrl[0]);
			URLConnection connection = url.openConnection();
			connection.connect();
			int fileLength = connection.getContentLength();

			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(destName);

			byte data[] = new byte[1024];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				total += count;
				publishProgress((int) (total * 100 / fileLength));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
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
			builder.setMessage(R.string.updater_ErrorDownloadingFile).setPositiveButton(R.string.updater_Btn_Ok, dialogClickListener).show();
		}
    }


	private void file_Delete(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			if (!file.delete()) {
				Log.e(TAG, NAME + ": Error deleting " + fileName);
			}
		}
	}

}
