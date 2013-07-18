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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class HomeActivity extends BaseActivity implements Runnable {
		private static String	TAG		= "Complete Linux Installer";		// Used when logging as app name
		private static String	NAME 	= "Splash";	

	    final Context context = this;
		private ProgressDialog progressDialog;
		//A thread, that will be used to execute code in parallel with the UI thread
		private Thread thread;
		//Create a Thread handler to queue code execution on a thread
		private Handler handler;

		//public static SharedPreferences sharedpreferences;
		SharedPreferences.Editor editor;

		public void onCreate(Bundle savedInstanceState) {
			mTitleRes = R.string.app_name;
			
	    	super.onCreate(savedInstanceState);
	        setContentView(R.layout.splash);
	        
	        setSlidingActionBarEnabled(false);
	        
			progressDialog = new ProgressDialog(HomeActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle("Running First Start");
			progressDialog.setMessage("Installing Busybox");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.show();
			//Initialize the handler
	        handler = new Handler();
	        //Initialize the thread
	        thread = new Thread(this, "ProgressDialogThread");
	        //start the thread
	        thread.start();
	    }

		//Initialize a counter integer to zero
		int counter = 0;

		@Override
		public void run()
		{
			try
			{
				synchronized (thread)
				{
					String outPath = "/data/data/" + this.getPackageName() + "/files/busybox";
					File test = new File(outPath);
					if (!test.exists()){
						copyBusyBox();
						handler.post(new Runnable()
						{
							@Override
							public void run()
							{
								progressDialog.setMessage("Copying BootScript....");
								progressDialog.setProgress(25);
							}
						});
						thread.wait(1000);
						copyScript();
						handler.post(new Runnable()
						{
							@Override
							public void run()
							{
								progressDialog.setMessage("Copying Auto BootScript....");
								progressDialog.setProgress(50);
							}
						});
						thread.wait(1000);
						copyAutoBootScript();
						handler.post(new Runnable()
						{
							@Override
							public void run()
							{
								progressDialog.setMessage("Checking for Donaton key....");
								progressDialog.setProgress(75);
							}
						});
						thread.wait(1000);
						@SuppressWarnings("unused")
						SharedPreferences settings = this.getSharedPreferences("MyApp",0);
						handler.post(new Runnable()
						{
							@Override
							public void run()
							{
								progressDialog.setMessage("Complete....");
								progressDialog.setProgress(100);
							}
						});
						thread.wait(1000);

					}

			        Log.i(TAG, "Language = " + Locale.getDefault().getISO3Language());
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}


			//This works just like the onPostExecute method from the AsyncTask class
			handler.post(new Runnable()
			{
				@Override
				public void run()
				{
					progressDialog.dismiss();
					toggle();
			    	
				}
			});

			//Try to "kill" the thread, by interrupting its execution
			synchronized (thread)
			{
				thread.interrupt();
			}
		}

		private void copyBusyBox() {
			String outPath = "/data/data/" + this.getPackageName() + "/files";
			File busyBox = new File(outPath + "/busybox");
			File dir = new File(outPath);

			if (busyBox.exists()) return;	// return if file already is copied
			if (!dir.exists()) dir.mkdir(); // Create folder if not found

			AssetManager assetManager = getAssets();
			try {
				InputStream in = assetManager.open("busybox");
				FileOutputStream out = new FileOutputStream(busyBox);

				byte[] buffer = new byte[1024];
				int read;
				while((read = in.read(buffer)) != -1){
					out.write(buffer, 0, read);
				}

				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;

				Log.i(TAG, NAME + ": Busybox copied to " + outPath);

				// Now the file is copied we chmod it so it can be executed
				String script = "chmod 755 "+ outPath  + "/busybox\n";
				runAsRoot(script);

			} catch(Exception e) {
				Log.e(TAG, NAME + ": " + e.getMessage());
			}
		}

		public static boolean runAsRoot(String script) {
			Process p;
			try {
				p = Runtime.getRuntime().exec("su"); 
				DataOutputStream os = new DataOutputStream(p.getOutputStream());

				os.writeBytes(script);
				os.writeBytes("exit\n");
				os.flush();

				try {
					p.waitFor();
					if (p.exitValue() != 255) {
						return true;
					}
					else {
						return false;
					}
				} catch (InterruptedException e) {
					return false;
				}

			} catch (IOException e) {
				Log.e (TAG, NAME + ": runAsRoot error: " + e.getMessage());
				return false;
			}
		}

		private void copyAutoBootScript() {
			String outPath = "/data/data/" + this.getPackageName() + "/files";
			File script = new File(outPath + "/autobootscript.sh");

			if (script.exists()) return;	// return if file already is copied

			AssetManager assetManager = getAssets();
			try {
				InputStream in = assetManager.open("autobootscript.sh");
				FileOutputStream out = new FileOutputStream(script);

				byte[] buffer = new byte[1024];
				int read;
				while((read = in.read(buffer)) != -1){
					out.write(buffer, 0, read);
				}

				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;

				Log.i(TAG, NAME + ": autobootscript.sh copied to " + outPath);

			} catch(Exception e) {
				Log.e(TAG, NAME + ": Error copying auto boot script! " + e.getMessage());
			}
		}

		private void copyScript() {
			String outPath = "/data/data/" + this.getPackageName() + "/files";
			File script = new File(outPath + "/bootscript.sh");
//			File dir = new File(outPath);

			if (script.exists()) return;	// return if file already is copied
//			if (!dir.exists()) dir.mkdir(); // Create folder if not found

			AssetManager assetManager = getAssets();
			try {
				InputStream in = assetManager.open("bootscript.sh");
				FileOutputStream out = new FileOutputStream(script);

				byte[] buffer = new byte[1024];
				int read;
				while((read = in.read(buffer)) != -1){
					out.write(buffer, 0, read);
				}

				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;

				Log.i(TAG, NAME + ": bootscript.sh copied to " + outPath);

			} catch(Exception e) {
				Log.e(TAG, NAME + ": Error copying boot script! " + e.getMessage());
			}
		}

	}