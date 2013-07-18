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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Launcher extends BaseActivity {

		// This is the default distros set if no config is found. (First run or user deleted data!)
		// The format is "Name;ImageNameWithFullPath"
		// The first one is used as the default selected distro to run and template when adding new distros.
//		private static final int PICKFILE_RESULT_CODE = 1;
		private String[]					defaults					= { "Ubuntu;/sdcard/ubuntu/ubuntu.img",
																			"Backtrack;/sdcard/backtrack/backtrack.img",
																			"Debian;/sdcard/debian/debian.img",
																			"Archlinux;/sdcard/archlinux/archlinux.img",
																			"Fedora;/sdcard/fedora/fedora.img",
																			"Kali;/sdcard/kali/kali.img"
																		 };

		private static String				TAG							= "Complete Linux Installer";		// Used when logging as app name
		private static String				NAME 						= "Launcher";						// Used as activity name when logging

		public static final String			PREFS_NAME					= "LauncherConfig";

		private static final int			menu_AddDistro				= 7;
		private static final int			menu_DeleteDistro			= 1;
		private static final int			menu_EditDistro				= 2;
		private static final int			menu_OpenFolder				= 3;
		private static final int			menu_UpdateScripts			= 4;
		private static final int			menu_AutoRunConfig			= 5;
		private static final int			menu_ReinstallBusybox       = 6;

		private static Spinner				spn_Profiles;
		private List<String>				items						= null;
		private String						lastSelected				= null;

		private static	String	 			selected_Name				= null;
		private static	String	 			selected_Image				= null;

		private static	TextView 			lab_launcher_bottom_text	= null;


		private static	Button 				btn_StartLinux				= null;
		private static	Button 				btn_Config					= null;

		private SharedPreferences			prefs;
		private SharedPreferences.Editor	prefsEditor;

		private String						cmdString					= null;

		private Hashtable<String, String>	profiles					= new Hashtable<String, String>();
		
		final Context 						context 					= this;
		EditText 							txt_Name 					= null;
		EditText 							txt_Image					= null;
		EditText 							txt_Imageadd				= null;
		Button 								filemanger					= null;
		
		private static final int 			_ReqChooseFile 				= 0;
		private static final int 			_ReqChooseFileAdd 			= 2;

		private static String				oldName						= "";
		private static String				oldImage					= "";


	public void onCreate(Bundle paramBundle) {
		mTitleRes = R.string.frontpage_btn_Launch;
		super.onCreate(paramBundle);
		setContentView(R.layout.launch);
		setSlidingActionBarEnabled(false);        
		
		spn_Profiles = (Spinner) this.findViewById(R.id.spn_Profiles);

		btn_StartLinux = (Button) this.findViewById(R.id.btn_StartLinux);
		btn_StartLinux.setOnClickListener(btn_StartLinux_Onclick);

		lab_launcher_bottom_text = (TextView) findViewById(R.id.lab_launcher_bottom_text);


		prefs = getSharedPreferences(PREFS_NAME, 0);
		btn_Config = (Button) this.findViewById(R.id.btn_Config);
		btn_Config.setOnClickListener(btn_Config_Onclick);
		btn_Config.setVisibility(View.VISIBLE);

		
		items = new ArrayList<String>();

		spn_Profiles.setOnItemSelectedListener(spn_Profiles_OnItemSelectedListener);

//		// Here we check if the system is running by checking if the home folder exists in /data/local/mnt/
//		if (file_Exists(CFG.MNT + "/home/")) {
//			// TODO: Change "Start Linux" button text to "Goto Running" and change the button behavior accordingly
//			Log.i(TAG, NAME + ": System already running!");
//			btn_StartLinux.setEnabled(false);
//		} else {
//			Log.i(TAG, NAME + ": System NOT already running!");
//			btn_StartLinux.setEnabled(true);
//		}
		loadPrefs();
		fillSpinner();
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu1 = menu.addSubMenu("Settings");

        subMenu1.add(0, menu_AddDistro, 0, R.string.menu_AddDistro);
		subMenu1.add(0, menu_DeleteDistro, 0, R.string.menu_DeleteDistro);
		subMenu1.add(0, menu_EditDistro, 0, R.string.menu_EditDistro);
		subMenu1.add(0, menu_OpenFolder, 0, R.string.menu_OpenFolder);
		subMenu1.add(0, menu_UpdateScripts, 0, R.string.menu_UpdateScripts);
		subMenu1.add(0, menu_ReinstallBusybox, 0, R.string.menu_reinstallbusybox);
		subMenu1.add(0, menu_AutoRunConfig, 0, R.string.menu_AutoRunConfig);

        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
		case menu_AddDistro:
			addDistro();
			return true;

		case menu_DeleteDistro:
			deleteDistro();
			return true;

		case menu_EditDistro:
			editDistro();
			return true;

		case menu_OpenFolder:
			String selPath = selected_Image.substring(0, selected_Image.lastIndexOf("/"));

			cmdString  = "cd " + selPath + "\n";
			cmdString += "su \n";
			try{
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setComponent(new ComponentName("jackpal.androidterm", "jackpal.androidterm.RemoteInterface"));
				intent.setAction("jackpal.androidterm.RUN_SCRIPT");
				intent.putExtra("jackpal.androidterm.iInitialCommand", cmdString);
				startActivity(intent);
				break;
				}catch (ActivityNotFoundException e) {
			    LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
			    View popupView = layoutInflater.inflate(R.layout.popup, null);  
			             final PopupWindow popupWindow = new PopupWindow(
			               popupView, 
			               LayoutParams.WRAP_CONTENT,  
			                     LayoutParams.WRAP_CONTENT);  
			             
			     Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
			     btnDismiss.setOnClickListener(new Button.OnClickListener(){

			     public void onClick(View v) {
			      popupWindow.dismiss();
			     }});
			               
			             popupWindow.showAsDropDown(btn_StartLinux, 50, -30);
			         
			   
					
				}


			return true;

		case menu_AutoRunConfig:
			Bundle bundle = new Bundle();
			bundle.putString("Name", selected_Name);
			bundle.putString("Image", selected_Image);

			Intent arintent = new Intent(getApplicationContext(), AutoRun_ConfigEditor.class);
			arintent.putExtras(bundle);

			startActivity(arintent);
			return true;

		case menu_UpdateScripts:
			Intent suintent = new Intent(getApplicationContext(), Script_Updater.class);
			startActivity(suintent);
			return true;
			
		case menu_ReinstallBusybox:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			 
			// set title
			alertDialogBuilder.setTitle("Reinstall Busybox");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Use this to reinstall our busybox and make it executable.\n\nThis may need to be done if the boot script complains its not executable or there are other busybox errors.\n\nThis will ask for root access to make it executable.\n\n\nPress yes to reinstall and no to cancel.")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						String outPath = "/data/data/" + "com.zpwebsites.linuxonandroid" + "/files";
						File busyBox = new File(outPath + "/busybox");
						File dir = new File(outPath);
						if (!dir.exists()) dir.mkdir(); // Create folder if not found

						AssetManager assetManager = getAssets();
						try {
							InputStream in = assetManager.open("busybox");
							FileOutputStream out = new FileOutputStream(busyBox);

							byte[] buffer = new byte[1024];
							int read;
							while((read = in.read(buffer)) != -1){
								out.write(buffer, 0, read);
							}

							in.close();
							in = null;
							out.flush();
							out.close();
							out = null;

							Log.i(TAG, NAME + ": Busybox copied to " + outPath);

							// Now the file is copied we chmod it so it can be executed
							String script = "chmod 755 "+ outPath  + "/busybox\n";
							Context context = getApplicationContext();
							CharSequence text = "Busybox Reinstalled!";
							int duration = Toast.LENGTH_SHORT;

							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
							runAsRoot(script);

						} catch(Exception e) {
							Log.e(TAG, NAME + ": " + e.getMessage());
							Context context = getApplicationContext();
							CharSequence text = "Busybox Reinstall failed!";
							int duration = Toast.LENGTH_SHORT;

							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
						}
						dialog .dismiss();
					}

				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
				
				return true;
				
		default:
            return super.onOptionsItemSelected(item);
		}
		return false;
	}
	
	private OnClickListener btn_Config_Onclick = new OnClickListener() {
		public void onClick(View v) {
			Bundle bundle = new Bundle();
			bundle.putString("Name", selected_Name);
			bundle.putString("Image", selected_Image);

			Intent intent = new Intent(v.getContext(), Configure.class);
			intent.putExtras(bundle);
			v.getContext().startActivity(intent);
		}
	};

	private OnClickListener btn_StartLinux_Onclick = new OnClickListener() {
		public void onClick(View v) {
			if (file_Exists(CFG.MNT + "/home/")) { // A image is already mounted, offer user to chroot into it now
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which){
						case DialogInterface.BUTTON_POSITIVE:
							String mountedImage = ""; // I need to find a good way to get the mounted image name here, but it works without.
							cmdString = "su \n";
							cmdString += CFG.busyBoxPath + " chroot " + CFG.MNT + " /root/init.sh " + mountedImage + "\n";
							try{
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.setComponent(new ComponentName("jackpal.androidterm", "jackpal.androidterm.RemoteInterface"));
							intent.setAction("jackpal.androidterm.RUN_SCRIPT");
							intent.putExtra("jackpal.androidterm.iInitialCommand", cmdString);
							startActivity(intent);
							break;
							}catch (ActivityNotFoundException e) {
						    LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
						    View popupView = layoutInflater.inflate(R.layout.popup, null);  
						             final PopupWindow popupWindow = new PopupWindow(
						               popupView, 
						               LayoutParams.WRAP_CONTENT,  
						                     LayoutParams.WRAP_CONTENT);  
						             
						     Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
						     btnDismiss.setOnClickListener(new Button.OnClickListener(){

						     public void onClick(View v) {
						      popupWindow.dismiss();
						     }});
						               
						             popupWindow.showAsDropDown(btn_StartLinux, 50, -30);
						         
						   
								
							}

						case DialogInterface.BUTTON_NEGATIVE:
							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setMessage(R.string.launcer_ImageAlreadyMounted).setPositiveButton(R.string.dialog_button_yes, dialogClickListener).setNegativeButton(R.string.dialog_button_no, dialogClickListener).show();

			} else { // No image is mounted so we go on with the normal start
				String selPath = selected_Image.substring(0, selected_Image.lastIndexOf("/"));

				cmdString  = "cd " + selPath + "\n";
				cmdString += "su \n";
				cmdString += "sh " + CFG.scriptPath + " " + selected_Image;
				try{
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setComponent(new ComponentName("jackpal.androidterm", "jackpal.androidterm.RemoteInterface"));
				intent.setAction("jackpal.androidterm.RUN_SCRIPT");
				intent.putExtra("jackpal.androidterm.iInitialCommand", cmdString);
				startActivity(intent);
				}catch (ActivityNotFoundException e) {
				    LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
				    View popupView = layoutInflater.inflate(R.layout.popup, null);  
				             final PopupWindow popupWindow = new PopupWindow(
				               popupView, 
				               LayoutParams.WRAP_CONTENT,  
				                     LayoutParams.WRAP_CONTENT);  
				             
				     Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
				     btnDismiss.setOnClickListener(new Button.OnClickListener(){

				     public void onClick(View v) {
				      popupWindow.dismiss();
				     }});
				               
				             popupWindow.showAsDropDown(btn_StartLinux, 50, -30);
				         
				   
						
					}
			}
		}
	};

	private OnItemSelectedListener spn_Profiles_OnItemSelectedListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			selected_Name = spn_Profiles.getSelectedItem().toString();
			selected_Image = profiles.get(selected_Name);

			File file = new File(selected_Image);
			if (!file.exists()) {
				btn_Config.setVisibility(View.GONE);
				btn_StartLinux.setVisibility(View.GONE);
				lab_launcher_bottom_text.setText(Launcher.this.getString(R.string.Launcher_Intro_Warning));
			} else {
				btn_Config.setVisibility(View.VISIBLE);
				btn_StartLinux.setVisibility(View.VISIBLE);
				lab_launcher_bottom_text.setText(Launcher.this.getString(R.string.Launcher_Intro));
			}

			saveLastSelected();

			Log.i(TAG, NAME + ": Selected name = " + selected_Name + " Image = " + selected_Image);
		}

		public void onNothingSelected(AdapterView<?> parentView) {

		}
	};


	private void fillSpinner() {
		items.clear();

		String str = null;
		Enumeration<String> profil = profiles.keys();

		while(profil.hasMoreElements()) {
			str = (String) profil.nextElement();
			items.add(str);
		}

		Collections.sort(items);

		// Uncomment next line and comment the one below it to use the default Android spinner look, you can delete res/fils_list_row.xml if you do
	    //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.file_list_row, items);
		spn_Profiles.setAdapter(adapter);

		// If last selected exists we select it here
		int idx = items.indexOf(lastSelected);
		if (idx > -1) {
			spn_Profiles.setSelection(idx);
		}
	}

	public void addDistro() {
		Bundle bundle = new Bundle();
		bundle.putString("Name", "");
		bundle.putString("Image", "");

		oldName = "";
		oldImage = "";

		Intent arintent = new Intent(getApplicationContext(), Distro_Editor.class);
		arintent.putExtras(bundle);

		startActivityForResult(arintent, _ReqChooseFileAdd);


//		AlertDialog.Builder alert = new AlertDialog.Builder(this);
////			alert.setTitle("Add distro");
//
//		Context mContext = getApplicationContext();
//		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//		View layout = inflater.inflate(R.layout.distro_editor_popup, (ViewGroup) findViewById(R.id.layout_root));
//
//		final EditText txt_Name = (EditText) layout.findViewById(R.id.txt_LinuxName);
//		txt_Imageadd = (EditText) layout.findViewById(R.id.txt_ImageName);
//		filemanger = (Button) layout.findViewById(R.id.fileselector); 
//
//		txt_Name.setText("");
//		txt_Imageadd.setText(defaults[0].substring(defaults[0].indexOf(";") + 1));
//
//		alert.setView(layout);
//		filemanger.setOnClickListener(new View.OnClickListener() {
//	 		public void onClick(View v) {
//	 			Intent intent = new Intent(context, FileChooserActivity.class);
//	 			intent.putExtra(FileChooserActivity._Rootpath, (Parcelable) new LocalFile("/"));
//	 			intent.putExtra(FileChooserActivity._RegexFilenameFilter, "(?si).*\\.(img)$");
//	 			startActivityForResult(intent, _ReqChooseFileAdd);
//	 		}
//	 		
//	 	});
//		alert.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				String name = txt_Name.getText().toString();
//				String image = txt_Image.getText().toString();
//
//				// Make sure the user entered a name
//				if (name.equals("")) {
//					return;
//				}
//
//				// Make sure profile don't exist
//				if (profiles.containsKey(name)) {
//					return;
//				}
//
//				profiles.put(name, image);
//				lastSelected = name;
//
//				fillSpinner();
//				savePrefs();
//
//			}
//		});
//
//		alert.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//			}
//		});
//
//		alert.show();
	}

	public void editDistro() {
		Bundle bundle = new Bundle();
		bundle.putString("Name", selected_Name);
		bundle.putString("Image", selected_Image);

		oldName = selected_Name;
		oldImage = selected_Image;

		Intent arintent = new Intent(getApplicationContext(), Distro_Editor.class);
		arintent.putExtras(bundle);

		startActivityForResult(arintent, _ReqChooseFile);


//		AlertDialog.Builder alert = new AlertDialog.Builder(this);
////			alert.setTitle("Edit distro");
//
//		Context mContext = getApplicationContext();
//		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//		View layout = inflater.inflate(R.layout.distro_editor_popup, (ViewGroup) findViewById(R.id.layout_root));
//
//		txt_Name = (EditText) layout.findViewById(R.id.txt_LinuxName);
//		txt_Image = (EditText) layout.findViewById(R.id.txt_ImageName);
//		filemanger = (Button) layout.findViewById(R.id.fileselector); 
//		
//		txt_Name.setText(selected_Name);
//		txt_Image.setText(selected_Image);
//
//		final String oldName = selected_Name;
//		final String oldImage = selected_Image;
//		
//		alert.setView(layout);
//
//		filemanger.setOnClickListener(new View.OnClickListener() {
//	 		public void onClick(View v) {
//	 			Intent intent = new Intent(context, FileChooserActivity.class);
//	 			intent.putExtra(FileChooserActivity._Rootpath, (Parcelable) new LocalFile("/"));
//	 			intent.putExtra(FileChooserActivity._RegexFilenameFilter, "(?si).*\\.(img)$");
//	 			startActivityForResult(intent, _ReqChooseFile);
//	 		}
//	 		
//	 	});
//			  
//		alert.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				String name = txt_Name.getText().toString();
//				String image = txt_Image.getText().toString();
//
//				// Make sure the user entered a name
//				if (name.equals("")) {
//					return;
//				}
//
//				if (!oldName.equals(name)) {
//					// Name was changed so we have to delete the old one from the profiles first!
//					profiles.remove(oldName);
//				}
//
//				if (!oldImage.equals(image)) {
//					// Image name has changed so we rename the mounts and config files
//					file_Rename(oldImage + ".mounts", image + ".mounts");
//					file_Rename(oldImage + ".config", image + ".config");
//				}
//
//				profiles.put(name, image);
//				lastSelected = name;
//
//				fillSpinner();
//				savePrefs();
//			}
//		});
//		alert.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				dialog.dismiss();
//			}
//		});
//
//		alert.show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
//			case PICKFILE_RESULT_CODE:
//				if(resultCode==RESULT_OK){
//					String FilePath = data.getData().getPath();
//					txt_Image.setText(FilePath);
//				}

			case _ReqChooseFile:
				if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					String name = bundle.getString("linuxName");
					String image = bundle.getString("imageName");

					// Make sure the user entered a name
					if (name.equals("")) {
						return;
					}

					if (!oldName.equals(name)) {
						// Name was changed so we have to delete the old one from the profiles first!
						profiles.remove(oldName);
					}

					if (!oldImage.equals(image)) {
						// Image name has changed so we rename the mounts and config files
						file_Rename(oldImage + ".mounts", image + ".mounts");
						file_Rename(oldImage + ".config", image + ".config");
					}

					profiles.put(name, image);
					lastSelected = name;

					fillSpinner();
					savePrefs();
				}
				break;

			case _ReqChooseFileAdd:
				if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					String name = bundle.getString("linuxName");
					String image = bundle.getString("imageName");

					// Make sure the user entered a name
					if (name.equals("")) {
						// TODO: We probably should tell the user he has to enter a name here
						return;
					}

					// Make sure profile don't exist
					if (profiles.containsKey(name)) {
						// TODO: We probably should tell the user that the profile already exists here
						return;
					}

					profiles.put(name, image);
					lastSelected = name;

					fillSpinner();
					savePrefs();
				}
				break;

//			case _ReqChooseFile:
//				if (resultCode == RESULT_OK) {
//					/*
//					 * you can use two flags included in data
//					 */
//					IFileProvider.FilterMode filterMode = (IFileProvider.FilterMode) data.getSerializableExtra(FileChooserActivity._FilterMode);
//					boolean saveDialog = data.getBooleanExtra(FileChooserActivity._SaveDialog, false);
//
//					/*
//					 * a list of files will always return,
//					 * if selection mode is single, the list contains one file
//					 */
//					@SuppressWarnings("unchecked")
//					List<LocalFile> files = (List<LocalFile>) data.getSerializableExtra(FileChooserActivity._Results);
//					for (File f : files){
//						txt_Image.setText(f.getAbsolutePath());
//					}
//				}   

//			case _ReqChooseFileAdd:
//				if (resultCode == RESULT_OK) {
//					/*
//		             * you can use two flags included in data
//		             */
//					IFileProvider.FilterMode filterMode = (IFileProvider.FilterMode) data.getSerializableExtra(FileChooserActivity._FilterMode);
//					boolean saveDialog = data.getBooleanExtra(FileChooserActivity._SaveDialog, false);
//
//		            /*
//		             * a list of files will always return,
//		             * if selection mode is single, the list contains one file
//		             */
//					@SuppressWarnings("unchecked")
//					List<LocalFile> files = (List<LocalFile>) data.getSerializableExtra(FileChooserActivity._Results);
//					for (File f : files) {
//						txt_Imageadd.setText(f.getAbsolutePath());
//					}   	
//					break;
//				}
		}
	}

	public void deleteDistro() {
		if (profiles.size() == 1) {
			return;
		}

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					String name = spn_Profiles.getSelectedItem().toString();

					file_Delete(profiles.get(name) + ".mounts");
					file_Delete(profiles.get(name) + ".config");
					profiles.remove(name);
					lastSelected = "";

					fillSpinner();
					savePrefs();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.dialog_delete_distro).setPositiveButton(R.string.dialog_button_yes, dialogClickListener).setNegativeButton(R.string.dialog_button_no, dialogClickListener).show();
	}

	private void file_Rename(String oldName, String newName) {
		File file = new File(oldName);
		if (file.exists()) {
			File file2 = new File(newName);
			if (!file.renameTo(file2)) {
				Log.e(TAG, NAME + ": Error renaming " + oldName + " to " + newName);
			}
		}
	}

	private void file_Delete(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			if (!file.delete()) {
				Log.e(TAG, NAME + ": Error deleting " + fileName);
			}
		}
	}

	private boolean file_Exists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	private void loadPrefs() {
		if (!prefs.contains("profiles")) {
			saveDefaultPrefs();
			Log.i(TAG, NAME + ": Default prefs saved");
			return;
		}

		lastSelected = prefs.getString("lastSelected", defaults[0].substring(0, defaults[0].indexOf(";")));
		int count = prefs.getInt("profiles", 0);

		profiles.clear();
		for (int i = 0; i < count; i++) {
			String str = prefs.getString("profile_" + i, "ERROR!");
			String name = str.substring(0, str.indexOf(";"));
			String img  = str.substring(str.indexOf(";") + 1);

			profiles.put(name, img);
//			Log.i("TFB Debug", "Profil " + i + " Name = " + name + " Cmd = " + img);
		}
	}

	private void saveLastSelected() {
		prefsEditor = prefs.edit();
		prefsEditor.putString("lastSelected", selected_Name);
		prefsEditor.commit();
	}

	private void savePrefs() {
		prefsEditor = prefs.edit();
		prefsEditor.clear();

//		lastSelected = spn_Profiles.getSelectedItem().toString();
		prefsEditor.putString("lastSelected", lastSelected);
		prefsEditor.putInt("profiles", profiles.size());

		String str = null;
		Enumeration<String> profil = profiles.keys();
		int cnt = 0;

		while(profil.hasMoreElements()) {
			str = (String) profil.nextElement();
			prefsEditor.putString("profile_" + cnt, str + ";" + profiles.get(str));
			cnt++;
		}

		prefsEditor.commit();
	}

	private void saveDefaultPrefs() {
		profiles.clear();
		for (int i = 0; i < defaults.length; i++) {
			String key = defaults[i].substring(0, defaults[i].indexOf(";"));
			String dat = defaults[i].substring(defaults[i].indexOf(";") + 1);

			profiles.put(key, dat);
		}

		lastSelected = defaults[0].substring(0, defaults[0].indexOf(";"));

		savePrefs();
	
	}

	public static boolean runAsRoot(String script) {
		Process p;
		try {
			p = Runtime.getRuntime().exec("su"); 
			DataOutputStream os = new DataOutputStream(p.getOutputStream());

			os.writeBytes(script);
			os.writeBytes("exit\n");
			os.flush();

			try {
				p.waitFor();
				if (p.exitValue() != 255) {
					return true;
				}
				else {
					return false;
				}
			} catch (InterruptedException e) {
				return false;
			}

		} catch (IOException e) {
			Log.e (TAG, NAME + ": runAsRoot error: " + e.getMessage());
			return false;
		}
	}
	
}
