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

import java.io.File;
import java.util.List;

import group.pals.android.lib.ui.filechooser.FileChooserActivity;
import group.pals.android.lib.ui.filechooser.io.localfile.LocalFile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Distro_Editor extends BaseActivity {
	
	private static String				TAG						= "Complete Linux Installer";		// Used when logging as app name
	private static String				NAME 					= "Distro_Editor";						// Used as activity name when logging

	EditText 							txt_Name 				= null;
	EditText 							txt_Image				= null;
	Button 								btn_FileManager			= null;
	Button 								btn_SaveAndExit			= null;
	Button 								btn_DismissChanges		= null;

	private static final int 			_ReqChooseFile 			= 0;
	private String 						linuxName				= null;
	private String 						imageName				= null;


	public void onCreate(Bundle paramBundle) {
		mTitleRes = R.string.menu_EditDistro;
		super.onCreate(paramBundle);
		setContentView(R.layout.distro_editor_popup);
		setSlidingActionBarEnabled(false);
		
		getBundle();

		txt_Name = (EditText) this.findViewById(R.id.txt_LinuxName);
		txt_Image = (EditText) this.findViewById(R.id.txt_ImageName);

		btn_FileManager = (Button) this.findViewById(R.id.fileselector); 
		btn_SaveAndExit = (Button) this.findViewById(R.id.btn_SaveAndExit); 
		btn_DismissChanges = (Button) this.findViewById(R.id.btn_DismissChanges); 

		btn_FileManager.setOnClickListener(btn_FileManager_Onclick);
		btn_SaveAndExit.setOnClickListener(btn_SaveAndExit_Onclick);
		btn_DismissChanges.setOnClickListener(btn_DismissChanges_Onclick);

		txt_Name.setText(linuxName);
		txt_Image.setText(imageName);
	}

	private OnClickListener btn_FileManager_Onclick = new OnClickListener() {
		public void onClick(View v) {
 			Intent intent = new Intent(getApplicationContext(), FileChooserActivity.class);
 			intent.putExtra(FileChooserActivity._Rootpath, (Parcelable) new LocalFile("/"));
 			intent.putExtra(FileChooserActivity._RegexFilenameFilter, "(?si).*\\.(img)$");
 			startActivityForResult(intent, _ReqChooseFile);
		}
	};

	private OnClickListener btn_SaveAndExit_Onclick = new OnClickListener() {
		public void onClick(View v) {
			Bundle bundle = new Bundle();
			bundle.putString("linuxName", txt_Name.getText().toString());
			bundle.putString("imageName", txt_Image.getText().toString());

			Intent mIntent = new Intent();
			mIntent.putExtras(bundle);
			setResult(RESULT_OK, mIntent);
			finish();
		}
	};

	private OnClickListener btn_DismissChanges_Onclick = new OnClickListener() {
		public void onClick(View v) {
			// TODO: Here we probably should ask if the user really wish to dismiss the changes!
			finish();
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case _ReqChooseFile:
				if (resultCode == RESULT_OK) {
					@SuppressWarnings("unchecked")
					List<LocalFile> files = (List<LocalFile>) data.getSerializableExtra(FileChooserActivity._Results);
					for (File f : files){
						txt_Image.setText(f.getAbsolutePath());
					}
				}
		}
	}

	private void getBundle() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) { // Bundle was passed so we read it
			linuxName	= bundle.getString("Name");
			imageName	= bundle.getString("Image");

		} else { // Bundle is null
			Log.e (TAG, NAME + ": No bundle was passed to the intent!");
			finish();
		}

		if (imageName == null) {
			Log.e (TAG, NAME + ": No image name was passed to the intent or it was null!");
			finish();
		}
	}

}
