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

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Tips extends DashboardActivity {

	private Button		btn_Back			= null;


	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.tips);
		setTitleFromActivityLabel(R.id.title_text);

		btn_Back = (Button) findViewById(R.id.btn_Back);
		btn_Back.setOnClickListener	(btn_Back_onClick);
	}

	private OnClickListener btn_Back_onClick = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

}
