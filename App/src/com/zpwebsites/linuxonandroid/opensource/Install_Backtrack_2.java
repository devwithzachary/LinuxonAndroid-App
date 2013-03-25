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
import android.widget.ImageButton;

public class Install_Backtrack_2 extends DashboardActivity {

	private Button		btn_Back				= null;
	private Button		btn_Next				= null;

	private Button		btn_DownloadImage		= null;
	private Button		btn_DownloadVNC			= null;
	private Button		btn_DownloadTerminal	= null;
	private ImageButton	btn_donationad			= null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView (R.layout.install_backtrack_2);
		setTitleFromActivityLabel (R.id.title_text);

		btn_Back = (Button) findViewById(R.id.btn_Back);
		btn_Next = (Button) findViewById(R.id.btn_Next);

		btn_Back.setOnClickListener	(btn_Back_onClick);
		btn_Next.setOnClickListener	(btn_Next_onClick);

		btn_DownloadImage		= (Button) findViewById(R.id.btn_DownloadImage);
		btn_DownloadVNC			= (Button) findViewById(R.id.btn_DownloadVNC);
		btn_DownloadTerminal	= (Button) findViewById(R.id.btn_DownloadTerminal);
		btn_donationad			= (ImageButton) findViewById(R.id.btn_donationad);
		
		btn_DownloadImage		.setOnClickListener	(btn_DownloadImage_onClick);
		btn_DownloadVNC			.setOnClickListener	(btn_DownloadVNC_onClick);
		btn_DownloadTerminal	.setOnClickListener	(btn_DownloadTerminal_onClick);
		btn_donationad			.setOnClickListener (btn_donationad_onClick);	
		
	    
	}
	private OnClickListener btn_donationad_onClick = new OnClickListener() {
		public void onClick(View v) {
			gotoLink(CFG.playStoreURL_donation);
		}
	};
	private OnClickListener btn_Next_onClick = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), Install_Backtrack_3.class);
			v.getContext().startActivity(intent);
		}
	};

	private OnClickListener btn_Back_onClick = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	private OnClickListener btn_DownloadImage_onClick = new OnClickListener() {
		public void onClick(View v) {
			downloadImage(v.getContext(), CFG.torrentURL_Backtrack_Core, CFG.imageURL_Backtrack_Core);
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

	private void gotoLink(String linkAdr) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setData(Uri.parse(linkAdr));
		startActivity(intent);
	}

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
