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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
//Download menu for the Ubuntu 10.10 images
public class Download_Ubuntu10_Menu extends DashboardActivity {

	private Button		btn_Back			= null;
	private Button		btn_DownloadLarge	= null;
	private Button		btn_DownloadSmall	= null;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView (R.layout.download_ubuntu10_menu);
		setTitleFromActivityLabel (R.id.title_text);

		btn_Back = (Button) findViewById(R.id.btn_Back);
		btn_Back.setOnClickListener	(btn_Back_onClick);

		btn_DownloadLarge	= (Button) findViewById(R.id.btn_DownloadLarge);
		btn_DownloadSmall	= (Button) findViewById(R.id.btn_DownloadSmall);

		btn_DownloadLarge	.setOnClickListener	(btn_DownloadLarge_onClick);
		btn_DownloadSmall	.setOnClickListener	(btn_DownloadSmall_onClick);
	}

	private OnClickListener btn_Back_onClick = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	private OnClickListener btn_DownloadLarge_onClick = new OnClickListener() {
		public void onClick(View v) {
			downloadImage(v.getContext(), CFG.torrentURL_Ubuntu10_Large, CFG.imageURL_Ubuntu10_Large);
		}
	};

	private OnClickListener btn_DownloadSmall_onClick = new OnClickListener() {
		public void onClick(View v) {
			downloadImage(v.getContext(), CFG.torrentURL_Ubuntu10_Small, CFG.imageURL_Ubuntu10_Small);
		}
	};

	private void downloadImage(Context context, final String torrentName, final String sourceforgeName) {
		if (torrentName.equals("")) {
			Intent localIntent = new Intent("android.intent.action.VIEW");
    		localIntent.setData(Uri.parse(sourceforgeName));
    		startActivity(localIntent);
    		return;
		}

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_download_type_selector);
//		dialog.setTitle(R.string.dialog_select_donwload_type_title);
		dialog.setCancelable(true);

		Button btn_Torrent = (Button) dialog.findViewById(R.id.btn_Torrent);
		btn_Torrent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
	    		Intent localIntent = new Intent("android.intent.action.VIEW");
	    		localIntent.setData(Uri.parse(torrentName));
	    		startActivity(localIntent);
				dialog .dismiss();
			}
		});

		Button btn_Sourceforge = (Button) dialog.findViewById(R.id.btn_Sourceforge);
		btn_Sourceforge.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
	    		Intent localIntent = new Intent("android.intent.action.VIEW");
	    		localIntent.setData(Uri.parse(sourceforgeName));
	    		startActivity(localIntent);
				dialog .dismiss();
			}
		});

		Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
		btn_Cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog .dismiss();
			}
		});

		dialog.show();
	}

}
