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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;


//Home Activity class used as the start activity
public class HomeActivity extends DashboardActivity {

	private static String	TAG		= "Complete Linux Installer";		// Used when logging as app name
	private static String	NAME 	= "HomeActivity";					// Used as activity name when logging

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		copyBusyBox();
		copyScript();
		copyAutoBootScript();
		SharedPreferences settings = this.getSharedPreferences("MyApp",0);
		setContentView(R.layout.home_activity);
        
		Log.i(TAG, "Language = " + Locale.getDefault().getISO3Language());
//		Log.i(TAG, "Language = " + java.util.Locale.getDefault().getDisplayName());
		
		
		 
	   };

	protected void onDestroy () {
		super.onDestroy ();
	}

	protected void onPause () {
		super.onPause ();
	}

	protected void onRestart () {
		super.onRestart ();
	}

	protected void onResume () {
		super.onResume ();
	}

	protected void onStart () {
		super.onStart ();
	}

	protected void onStop () {
		super.onStop ();
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

	private void copyScript() {
		String outPath = "/data/data/" + this.getPackageName() + "/files";
		File script = new File(outPath + "/bootscript.sh");
//		File dir = new File(outPath);

		if (script.exists()) return;	// return if file already is copied
//		if (!dir.exists()) dir.mkdir(); // Create folder if not found

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
	 

} // end class
