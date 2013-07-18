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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

//TODO: Add a handler for the real back key on the device so the configs are saved too!

public class Mounts_Editor extends BaseActivity {

	private static String				TAG				= "Complete Linux Installer";		// Used when logging as app name
	private static String				NAME			= "Configure_Mounts";				// Used as activity name when logging

	private Hashtable<String, String>	mounts_android	= new Hashtable<String, String>();
	private Hashtable<String, String>	mounts_linux	= new Hashtable<String, String>();

	private final static int	MODE_ANDROID		= 1;
	private final static int	MODE_LINUX			= 2;
	private final static int	MODE_BOTH			= 3;

	private static final int	menu_ViewMode		= 0;
	private static final int	menu_AddMount		= 1;

	private static final int	context_menu_edit	= 0;
	private static final int	context_menu_delete	= 1;

	private static int			viewMode			= 1;

	private static Button		btn_SaveAndExit		= null;
	private static Button		btn_AbortChanges	= null;
	private static ListView		lst_Mounts			= null;

	private String				mnountFileExt		= ".mounts";

	@SuppressWarnings("unused")
	private String				linuxName			= null;
	private String				imageName			= null;

	private String				mountListFileName	= null;

	private List<String>		items				= null;

	private boolean				hasChanged			= false;


	@Override public void onCreate(Bundle savedInstanceState) {
		mTitleRes = R.string.title_ConfigureMounts;
		super.onCreate(savedInstanceState);

		getBundle();

		setContentView(R.layout.mounts_editor);
//		setTitleFromActivityLabel (R.id.title_text);
		setSlidingActionBarEnabled(false);
		
		btn_SaveAndExit = (Button) findViewById(R.id.btn_SaveAndExit);
		btn_SaveAndExit.setOnClickListener(btn_SaveAndExit_onClick);

		btn_AbortChanges = (Button) findViewById(R.id.btn_AbortChanges);
		btn_AbortChanges.setOnClickListener(btn_AbortChanges_onClick);

		lst_Mounts = (ListView) findViewById(R.id.lst_Mounts);
		lst_Mounts.setOnItemClickListener(lst_Mounts_onItemClick);

		registerForContextMenu(lst_Mounts);

		items = new ArrayList<String>();

		loadMounts();
		fillList();

//		Log.e (TAG, NAME + ": Name   = " + mountListFileName);
	}

	private OnClickListener btn_SaveAndExit_onClick = new OnClickListener() {
		public void onClick(View v) {
			if (hasChanged) {
				file_Delete(mountListFileName);
				try {
					FileWriter outFile = new FileWriter(mountListFileName);
					PrintWriter out = new PrintWriter(outFile);

					String key = null;
					Enumeration<String> mountList = mounts_android.keys();

					while(mountList.hasMoreElements()) {
						key = (String) mountList.nextElement();
						out.println(key + ";" + mounts_android.get(key));
					}

					out.close();

				} catch (IOException e){
					Log.e(TAG, NAME + ": btn_SaveAndExit_onClick: Save Error " + e.getMessage());
				}

				hasChanged = false;
				updateButtonStates();
			} else {
				finish();
			}
		}
	};

	private OnClickListener btn_AbortChanges_onClick = new OnClickListener() {
		public void onClick(View v) {
			loadMounts();
			hasChanged = false;
			fillList();
		}
	};

	private OnItemClickListener lst_Mounts_onItemClick = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String item = ((TextView)view).getText().toString();

			String androidPath = null;
			String linuxPath = null;

			switch (viewMode) {
			case MODE_ANDROID:
				androidPath = item;
				linuxPath = mounts_android.get(item);
				break;

			case MODE_LINUX:
				androidPath = mounts_linux.get(item);
				linuxPath = item;
				break;

			case MODE_BOTH:
				androidPath = item.substring(0, item.indexOf(" -> "));
				linuxPath = mounts_android.get(androidPath);
				break;
			}

			if (androidPath != null && linuxPath != null) {
				editMount(androidPath, linuxPath);
			} else {
				Log.e(TAG, NAME + ": onItemClick - Error editing mount: " + androidPath + " -> " + linuxPath);
			}
		}
	};
	public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu1 = menu.addSubMenu("Options");
       
		subMenu1.add(0, menu_ViewMode, 0, R.string.menu_ToggleViewMode);
		subMenu1.add(0, menu_AddMount, 0, R.string.menu_AddMount);
        MenuItem subMenu1Item = (MenuItem) subMenu1.getItem();
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
	}
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case menu_ViewMode:
			toggle_ViewMode();
			return true;

		case menu_AddMount:
			addMount();
			return true;
		}

		return false; //should never happen
	}

	@Override public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId()==R.id.lst_Mounts) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
			menu.setHeaderTitle(((TextView)info.targetView).getText().toString());

			menu.add(Menu.NONE, context_menu_edit, 0,  R.string.context_menu_edit);
			menu.add(Menu.NONE, context_menu_delete, 0,  R.string.context_menu_delete);
		}
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();

		String name = ((TextView)info.targetView).getText().toString();

		String androidPath = null;
		String linuxPath = null;

		switch (viewMode) {
		case MODE_ANDROID:
			androidPath = name;
			linuxPath = mounts_android.get(name);
			break;

		case MODE_LINUX:
			androidPath = mounts_linux.get(name);
			linuxPath = name;
			break;

		case MODE_BOTH:
			androidPath = name.substring(0, name.indexOf(" -> "));
			linuxPath = mounts_android.get(androidPath);
			break;
		}


		if (androidPath == null || linuxPath == null) {
			Log.e(TAG, NAME + ": onContextItemSelected: " + androidPath + " -> " + linuxPath);
			return true;
		}


		switch (menuItemIndex) {
			case context_menu_edit:
				editMount(androidPath, linuxPath);
				break;

			case context_menu_delete:
				deleteMount(androidPath, linuxPath);
				break;
		}

		return true;
	}

	private void addMount() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.mounts_editor_popup, (ViewGroup) findViewById(R.id.layout_root));

		final EditText txt_AndroidPath = (EditText) layout.findViewById(R.id.txt_AndroidPath);
		final EditText txt_LinuxPath = (EditText) layout.findViewById(R.id.txt_LinuxPath);

//		txt_AndroidPath.setText(androidPath);
//		txt_LinuxPath.setText(linuxPath);

		alert.setView(layout);

		alert.setPositiveButton(Mounts_Editor.this.getString(R.string.dialog_button_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String androidPath = fix_AndroidPath(txt_AndroidPath.getText().toString());
				String linuxPath = fix_LinuxPath(txt_LinuxPath.getText().toString());

				// TODO: Handle duplicate android paths or Linux paths

				hasChanged = true;
				mounts_android.put(androidPath, linuxPath);
				mounts_linux.put(linuxPath, androidPath);

				fillList();
			}
		});

		alert.setNegativeButton(Mounts_Editor.this.getString(R.string.dialog_button_cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert.show();
	}

	private void editMount(String androidPath, String linuxPath) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.mounts_editor_popup, (ViewGroup) findViewById(R.id.layout_root));

		final String oldAndroidPath	= androidPath;
		final String oldLinuxPath	= linuxPath;

		final EditText txt_AndroidPath = (EditText) layout.findViewById(R.id.txt_AndroidPath);
		final EditText txt_LinuxPath = (EditText) layout.findViewById(R.id.txt_LinuxPath);

		txt_AndroidPath.setText(androidPath);
		txt_LinuxPath.setText(linuxPath);

		alert.setView(layout);

		alert.setPositiveButton(Mounts_Editor.this.getString(R.string.dialog_button_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
//				String androidPath = txt_AndroidPath.getText().toString();
//				String linuxPath = txt_LinuxPath.getText().toString();
				String androidPath = fix_AndroidPath(txt_AndroidPath.getText().toString());
				String linuxPath = fix_LinuxPath(txt_LinuxPath.getText().toString());

				if (oldAndroidPath.equals(androidPath) && oldLinuxPath.equals(linuxPath)) {
					return;
				}

				// TODO: Handle duplicate paths here.

				hasChanged = true;
				mounts_android.remove(oldAndroidPath);
				mounts_linux.remove(oldLinuxPath);

				mounts_android.put(androidPath, linuxPath);
				mounts_linux.put(linuxPath, androidPath);

				fillList();
			}
		});

		alert.setNegativeButton(Mounts_Editor.this.getString(R.string.dialog_button_cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		alert.show();
	}

	private void deleteMount(final String androidPath, final String linuxPath) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					mounts_android.remove(androidPath);
					mounts_linux.remove(linuxPath);
					hasChanged = true;

					// TODO: Delete the mounts file if the last mount point is deleted!

					fillList();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.dialog_delete_mount).setPositiveButton(R.string.dialog_button_yes, dialogClickListener).setNegativeButton(R.string.dialog_button_no, dialogClickListener).show();
	}

	private String fix_AndroidPath (String pathToFix) {

		// TODO: This is a great place to check if the android folder actually exists!
		//		 And if it don't exits it can tell the user and ask if he want to cancel or use it anyway! (So he can make the folder afterwards)

		// I path is / just return
		if (pathToFix.equals("/")) {
			return pathToFix;
		}

		// Make sure the path start with /
		if (!pathToFix.startsWith("/")) {
			pathToFix = "/" + pathToFix;
		}

		// Make sure the path don't end with /
		while (pathToFix.endsWith("/")) {
			pathToFix = pathToFix.substring(0, pathToFix.length() - 1);
		}

		return pathToFix;
	}

	private String fix_LinuxPath (String pathToFix) {
		// Make sure the path don't start with /
		while (pathToFix.startsWith("/")) {
			pathToFix = pathToFix.substring(1);
		}

		// Make sure the path don't end with /
		while (pathToFix.endsWith("/")) {
			pathToFix = pathToFix.substring(0, pathToFix.length() - 1);
		}

		return pathToFix;
	}

	private void toggle_ViewMode() {
		viewMode++;
		if (viewMode == 4) {
			viewMode = 1;
		}

		fillList();

		switch (viewMode) {
		case MODE_ANDROID:
			break;

		case MODE_LINUX:
			break;

		case MODE_BOTH:
			break;
		}
	}

	private void fillList() {
		items.clear();

		String key = null;
		Enumeration<String> mountList = mounts_android.keys();

		while(mountList.hasMoreElements()) {
			key = (String) mountList.nextElement();

			switch (viewMode) {
			case MODE_ANDROID:
				items.add(key);
				break;

			case MODE_LINUX:
				items.add(mounts_android.get(key));
				break;

			case MODE_BOTH:
				items.add(key + " -> " + mounts_android.get(key));
				break;
			}

		}

		Collections.sort(items);
		updateButtonStates();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.mounts_list_row, items);
		lst_Mounts.setAdapter(adapter);
	}

	private void updateButtonStates() {
		if (hasChanged) {
			btn_SaveAndExit.setText(R.string.button_SaveAndExit);
			btn_AbortChanges.setVisibility(View.VISIBLE);
		} else {
			btn_SaveAndExit.setText(R.string.button_mounts_back);
			btn_AbortChanges.setVisibility(View.GONE);
		}
	}

	private void loadMounts() {
		mounts_android.clear();
		mounts_linux.clear();

		File file = new File(mountListFileName);

		if (!file.exists()) {
			return;
		}

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
 
			// repeat until all lines is read
			while ((line = reader.readLine()) != null) {
//				Log.e(TAG, NAME + ": " + line);
				mounts_android.put(line.substring(0, line.indexOf(";")), line.substring(line.indexOf(";") + 1));
				mounts_linux.put(line.substring(line.indexOf(";") + 1), line.substring(0, line.indexOf(";")));
			}

		} catch (IOException e) {
			Log.e(TAG, NAME + ": " + e.getMessage());

		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				Log.e(TAG, NAME + ": " + e.getMessage());
			}
		}
	}

	private void getBundle() {
		// Read the log file name from the extras
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

		mountListFileName = imageName + mnountFileExt;
	}

	private void file_Delete(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			if (!file.delete()) {
				Log.e(TAG, NAME + ": Error deleting " + fileName);
			}
		}
	}
	
}
