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


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//Used to display the FAQ - could use being changed to something better im sure...
public class Faq extends DashboardActivity {

	private Button		btn_Back				= null;
	private Button		btn_WorkingDevices		= null;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView (R.layout.faq);
		setTitleFromActivityLabel (R.id.title_text);

		btn_Back = (Button) findViewById(R.id.btn_Back);
		btn_Back.setOnClickListener(btn_Back_onClick);

		btn_WorkingDevices = (Button) findViewById(R.id.btn_WorkingDevices);
		btn_WorkingDevices.setOnClickListener(btn_WorkingDevices_onClick);
	}

	private OnClickListener btn_Back_onClick = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	private OnClickListener btn_WorkingDevices_onClick = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.setData(Uri.parse("http://linuxonandroid.blogspot.co.uk/p/working-devices.html"));
			startActivity(intent);
		}
	};

}
