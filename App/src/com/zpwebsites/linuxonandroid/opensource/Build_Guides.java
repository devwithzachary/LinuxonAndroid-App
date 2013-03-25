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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//You might have guessed by now what each class is far but anyway, this is used to display the build guides
public class Build_Guides extends DashboardActivity {


	private String		ubuntu_BaseName		= "UbuntuBuildGuide_";
	private int			ubuntu_PageCount	= 4;

	private Button		btn_Back		= null;
	private Button		btn_BuildUbuntu		= null;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView (R.layout.build_guides);
		setTitleFromActivityLabel (R.id.title_text);

		btn_Back = (Button) findViewById(R.id.btn_Back);
		btn_Back.setOnClickListener(btn_Back_onClick);

		btn_BuildUbuntu = (Button) findViewById(R.id.btn_BuildUbuntu);
		btn_BuildUbuntu.setOnClickListener(btn_BuildUbuntu_onClick);
	}

	private OnClickListener btn_Back_onClick = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	private OnClickListener btn_BuildUbuntu_onClick = new OnClickListener() {
		public void onClick(View v) {
			showGuide(v.getContext(), ubuntu_BaseName, ubuntu_PageCount);
		}
	};

	private void showGuide(Context context, String guideName, int guidePages) {
		Intent intent = new Intent(context, TextViewer.class);

		Bundle bundle = new Bundle();
		bundle.putString("FileName", guideName);
		bundle.putInt("Pages", guidePages);
		intent.putExtras(bundle);

		context.startActivity(intent);
	}
}
