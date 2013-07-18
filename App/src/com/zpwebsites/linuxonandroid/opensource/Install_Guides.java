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


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ImageButton;

public class Install_Guides extends BaseActivity {

	private Button		btn_Ubuntu10Guide	= null;
	private Button		btn_Ubuntu12Guide	= null;
	private Button		btn_BacktrackGuide	= null;
	private Button		btn_DebianGuide		= null;
	private Button		btn_ArchLinuxGuide  = null;
	private Button		btn_FedoraGuide  	= null;
	private Button		btn_openSUSEGuide  	= null;
	private Button		btn_KaliGuide  	= null;

	 
	public void onCreate(Bundle paramBundle) {
		mTitleRes = R.string.frontpage_btn_InstallGuides;
		super.onCreate(paramBundle);
		setContentView(R.layout.installguides_menu);
		setSlidingActionBarEnabled(false);
		
		btn_Ubuntu10Guide	= (Button) findViewById(R.id.btn_Ubuntu10Guide);
		btn_Ubuntu12Guide	= (Button) findViewById(R.id.btn_Ubuntu12Guide);
		btn_BacktrackGuide	= (Button) findViewById(R.id.btn_BacktrackGuide);
		btn_DebianGuide		= (Button) findViewById(R.id.btn_DebianGuide);
		btn_ArchLinuxGuide  = (Button) findViewById(R.id.btn_ArchLinuxGuide);
		btn_FedoraGuide  	= (Button) findViewById(R.id.btn_FedoraGuide);
		btn_openSUSEGuide	= (Button) findViewById(R.id.btn_openSUSEGuide);
		btn_KaliGuide		= (Button) findViewById(R.id.btn_KaliGuide);	

		btn_Ubuntu10Guide	.setOnClickListener	(btn_Ubuntu10Guide_onClick);
		btn_Ubuntu12Guide	.setOnClickListener	(btn_Ubuntu12Guide_onClick);
		btn_BacktrackGuide	.setOnClickListener	(btn_BacktrackGuide_onClick);
		btn_DebianGuide		.setOnClickListener	(btn_DebianGuide_onClick);
	    btn_ArchLinuxGuide  .setOnClickListener	(btn_ArchLinuxGuide_onClick);
		btn_FedoraGuide  	.setOnClickListener	(btn_FedoraGuide_onClick);
		btn_openSUSEGuide	.setOnClickListener	(btn_openSUSEGuide_onClick);
		btn_KaliGuide		.setOnClickListener	(btn_KaliGuide_onClick);

		
	}

	private OnClickListener btn_Ubuntu10Guide_onClick = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), Install_Ubuntu10.class);
			v.getContext().startActivity(intent);
		}
	};

	private OnClickListener btn_Ubuntu12Guide_onClick = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), Install_Ubuntu12.class);
			v.getContext().startActivity(intent);
		}
	};

	private OnClickListener btn_BacktrackGuide_onClick = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), Install_Backtrack.class);
			v.getContext().startActivity(intent);
		}
	};

	private OnClickListener btn_DebianGuide_onClick = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), Install_Debian.class);
			v.getContext().startActivity(intent);
		}
	};
	private OnClickListener btn_ArchLinuxGuide_onClick = new OnClickListener() {
		public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Install_Arch.class);
				v.getContext().startActivity(intent);
		}
	};
	private OnClickListener btn_FedoraGuide_onClick = new OnClickListener() {
		public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Install_Fedora.class);
				v.getContext().startActivity(intent);
		}
	};
	private OnClickListener btn_KaliGuide_onClick = new OnClickListener() {
		public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Install_Kali.class);
				v.getContext().startActivity(intent);
		}
	};
	private OnClickListener btn_openSUSEGuide_onClick = new OnClickListener() {
		public void onClick(View v) {
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.comingsoon_popup, null);  
		             final PopupWindow popupWindow = new PopupWindow(
		               popupView, 
		               LayoutParams.WRAP_CONTENT,  
		                     LayoutParams.WRAP_CONTENT);  
		             
		     Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
		     btnDismiss.setOnClickListener(new Button.OnClickListener(){

		     public void onClick(View v) {
		      popupWindow.dismiss();
		     }});
		               
		             popupWindow.showAsDropDown(btn_Ubuntu10Guide, 50, -30);
		         
		   }
		}
	;
}
