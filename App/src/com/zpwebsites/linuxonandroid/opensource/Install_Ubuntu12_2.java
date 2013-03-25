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
import android.widget.ImageButton;

public class Install_Ubuntu12_2 extends DashboardActivity {

	private Button		btn_Back				= null;
	private Button		btn_Next				= null;
	private Button		btn_DownloadImage		= null;
	private Button		btn_DownloadVNC			= null;
	private Button		btn_DownloadTerminal	= null;
	private ImageButton	btn_donationad		= null;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.install_ubuntu12_2);
		setTitleFromActivityLabel(R.id.title_text);

		btn_Back = (Button) findViewById(R.id.btn_Back);
		btn_Next = (Button) findViewById(R.id.btn_Next);
		btn_donationad		= (ImageButton) findViewById(R.id.btn_donationad);
		
		btn_Back.setOnClickListener	(btn_Back_onClick);
		btn_Next.setOnClickListener	(btn_Next_onClick);

		btn_DownloadImage		= (Button) findViewById(R.id.btn_DownloadImage);
		btn_DownloadVNC			= (Button) findViewById(R.id.btn_DownloadVNC);
		btn_DownloadTerminal	= (Button) findViewById(R.id.btn_DownloadTerminal);

		btn_DownloadImage		.setOnClickListener	(btn_DownloadImage_onClick);
		btn_DownloadVNC			.setOnClickListener	(btn_DownloadVNC_onClick);
		btn_DownloadTerminal	.setOnClickListener	(btn_DownloadTerminal_onClick);
		btn_donationad		.setOnClickListener (btn_donationad_onClick);	
		
	    
	}
	private OnClickListener btn_donationad_onClick = new OnClickListener() {
		public void onClick(View v) {
			gotoLink(CFG.playStoreURL_donation);
		}
	};

	private OnClickListener btn_DownloadImage_onClick = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), Download_Ubuntu12_menu.class);
			v.getContext().startActivity(intent);
		}
	};

	private OnClickListener btn_DownloadVNC_onClick = new OnClickListener() {
		public void onClick(View v) {
			gotoLink(CFG.playStoreURL_vnc);
		}
	};

	private OnClickListener btn_DownloadTerminal_onClick = new OnClickListener() {
		public void onClick(View v) {
			gotoLink(CFG.playStoreURL_term);
		}
	};

	private OnClickListener btn_Next_onClick = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), Install_Ubuntu12_3.class);
			v.getContext().startActivity(intent);
		}
	};

	private OnClickListener btn_Back_onClick = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	private void gotoLink(String linkAdr) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setData(Uri.parse(linkAdr));
		startActivity(intent);
	}


//		((Button)findViewById(R.id.downloadbutton)).setOnClickListener(new View.OnClickListener() {
//			public void onClick(View paramView) {
//				Intent localIntent = new Intent(paramView.getContext(), Download1.class);
//				Install_Ubuntu12_2.this.startActivityForResult(localIntent, 0);
//			}
//		});


}
