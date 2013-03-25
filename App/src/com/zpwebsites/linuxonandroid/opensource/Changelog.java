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
//These comments are getting a little boring but I guess I shall keep going them ;)
//this is the class clicked when changlog button on the home page is pressed, it then uses the change log viewer to display the change log selected
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Changelog extends DashboardActivity {

	private Button		btn_Back				= null;
	private Button		btn_Changelog_App		= null;
	private Button		btn_Changelog_Image		= null;
	private Button		btn_Changelog_Script	= null;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView (R.layout.changelog_menu);
		setTitleFromActivityLabel (R.id.title_text);

		btn_Back = (Button) findViewById(R.id.btn_Back);
		btn_Back.setOnClickListener	(btn_Back_onClick);

		btn_Changelog_App	= (Button) findViewById(R.id.btn_ShowChangelog_App);
		btn_Changelog_Image	= (Button) findViewById(R.id.btn_ShowChangelog_Image);
		btn_Changelog_Script= (Button) findViewById(R.id.btn_ShowChangelog_Script);

		btn_Changelog_App.setOnClickListener(btn_Changelog_App_onClick);
		btn_Changelog_Image.setOnClickListener(btn_Changelog_Image_onClick);
		btn_Changelog_Script.setOnClickListener(btn_Changelog_Script_onClick);
	}

	private OnClickListener btn_Back_onClick = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	private OnClickListener btn_Changelog_App_onClick = new OnClickListener() {
		public void onClick(View v) {
			show_ChangeLog("changelog-app.txt");
		}
	};

	private OnClickListener btn_Changelog_Image_onClick = new OnClickListener() {
		public void onClick(View v) {
			show_ChangeLog("changelog-image.txt");
		}
	};

	private OnClickListener btn_Changelog_Script_onClick = new OnClickListener() {
		public void onClick(View v) {
			show_ChangeLog("changelog-script.txt");
		}
	};

	private void show_ChangeLog(String logName) {
		Intent intent = new Intent(Changelog.this, Changelog_Viewer.class);

		Bundle bundle = new Bundle();
		bundle.putString("logName", logName);
		intent.putExtras(bundle);

		Changelog.this.startActivity(intent);
	}
}
