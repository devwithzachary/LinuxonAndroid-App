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
//Class used to control the 'dashboard' or home page
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public abstract class DashboardActivity extends Activity {

	private String TAG = "Complete Linux Installer";	// Used when logging as app name
	private String NAME  = "DashboardActivity";			// Used as activity name when logging


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

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


	/** Click Methods **/

	/**
	 * Handle the click on the home button.
	 * 
	 * @param v View
	 * @return void
	 */
	public void onClickHome (View v) {
		goHome (this);
	}

	/**
	 * Handle the click on the search button.
	 * 
	 * @param v View
	 * @return void
	 */
	public void onClickSearch (View v) {
		startActivity (new Intent(getApplicationContext(), NewsActivity.class));
	}

	/**
	 * Handle the click on the About button.
	 * 
	 * @param v View
	 * @return void
	 */
	public void onClickAbout (View v) {
		startActivity (new Intent(getApplicationContext(), AboutActivity.class));
	}

	/**
	 * Handle the click of a Feature button.
	 * 
	 * @param v View
	 * @return void
	 */
	public void onClickFeature (View v) {
		int id = v.getId ();
		switch (id) {
			case R.id.home_btn_feature1 :
				startActivity (new Intent(getApplicationContext(), InstallGuides_Menu.class));
				break;

			case R.id.home_btn_feature2 :
				startActivity (new Intent(getApplicationContext(), Build_Guides.class));
				break;

			case R.id.home_btn_feature3 :
				startActivity (new Intent(getApplicationContext(), Tips.class));
				break;

			case R.id.home_btn_feature4 :
				startActivity (new Intent(getApplicationContext(), Faq.class));
				break;

			case R.id.home_btn_feature5 :
				startActivity (new Intent(getApplicationContext(), Changelog.class));
				break;

			case R.id.home_btn_feature6 :
				startActivity (new Intent(getApplicationContext(), Launch.class));
				break;
			default: 
				break;
		}
	}

	/** More Methods **/

	/**
	 * Go back to the home activity.
	 * 
	 * @param context Context
	 * @return void
	 */
	public void goHome(Context context) {
		final Intent intent = new Intent(context, HomeActivity.class);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity (intent);
		}

	/**
	 * Use the activity label to set the text in the activity's title text view.
	 * The argument gives the name of the view.
	 *
	 * <p> This method is needed because we have a custom title bar rather than the default Android title bar.
	 * See the theme definitons in styles.xml.
	 * 
	 * @param textViewId int
	 * @return void
	 */
	public void setTitleFromActivityLabel (int textViewId) {
		TextView tv = (TextView) findViewById (textViewId);
		if (tv != null) tv.setText (getTitle ());
	} // end setTitleText

	public void setTitle (int textViewId, String titleString) {
		TextView tv = (TextView) findViewById (textViewId);
		if (tv != null) tv.setText (titleString);
	}

	/**
	 * Show a string on the screen via Toast.
	 * 
	 * @param msg String
	 * @return void
	 */
	public void toast (String msg) {
		Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
	} // end toast

	/**
	 * Send a message to the debug log and display it using Toast.
	 */
	public void trace (String msg) {
		Log.d(TAG, NAME + ": " + msg);
		toast (msg);
	}

} // end class
