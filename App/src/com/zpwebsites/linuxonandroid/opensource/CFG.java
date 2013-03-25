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
//Big old config class including all links for downloads and such to be used by other class files where needed
public class CFG {

	public static final String	scriptPath					= "/data/data/com.zpwebsites.linuxonandroid.opensource/files/bootscript.sh";
	public static final String	scriptPath_AutoBoot			= "/data/data/com.zpwebsites.linuxonandroid.opensource/files/autobootscript.sh";
	public static final String	busyBoxPath					= "/data/data/com.zpwebsites.linuxonandroid.opensource/files/busybox";

	public static final String	MNT							= "/data/local/mnt"; // This HAS to be the same as $mnt in the boot scripts!

	// Script updater
	public static final String	updater_VersionsFile		= "http://sourceforge.net/projects/linuxonandroid/files/latest-scripts.txt"; // Latest versions, is in script svn
	public static final String	updater_LatestBoot			= "http://sourceforge.net/projects/linuxonandroid/files/bootscript.sh"; // url to the boot script
	public static final String	updater_LatestAutoboot		= "http://sourceforge.net/projects/linuxonandroid/files/autobootscript.sh"; // url to the auto boot script

	// Ubuntu 10 sourceforge & script links
	public static final String	ScriptURL_Ubuntu10			= "http://zpwebsitefiles.com/apps/Ubuntu/10.10/ubuntuV6-1-script.zip";
	public static final String	imageURL_Ubuntu10_Core		= ""; // Don't exists right now, but it might come so put it in anyway!
	public static final String	imageURL_Ubuntu10_Small		= "http://zpwebsitefiles.com/apps/Ubuntu/10.10/ubuntuV5-sm-image.zip";
	public static final String	imageURL_Ubuntu10_Large		= "http://zpwebsitefiles.com/apps/Ubuntu/10.10/ubuntuV5-image.zip";

	// Ubuntu 10 torrent links (If torrents don't exist just put "" and the popup won't show!)
	public static final String	torrentURL_Ubuntu10_Core	= "";
	public static final String	torrentURL_Ubuntu10_Small	= "";
	public static final String	torrentURL_Ubuntu10_Large	= "";


	// Ubuntu 12 sourceforge & script links
	public static final String	ScriptURL_Ubuntu12			= "http://sourceforge.net/projects/linuxonandroid/files/Ubuntu/12.04/bootscripts/ubuntu-script-v7-complete.zip/download";
	public static final String	imageURL_Ubuntu12_Core		= "http://sourceforge.net/projects/linuxonandroid/files/Ubuntu/12.04/core/ubuntu1204-v4-core.zip/download";
	public static final String	imageURL_Ubuntu12_Small		= "http://sourceforge.net/projects/linuxonandroid/files/Ubuntu/12.04/small/ubuntu1204-v4-small.zip/download";
	public static final String	imageURL_Ubuntu12_Large		= "http://sourceforge.net/projects/linuxonandroid/files/Ubuntu/12.04/full/ubuntu1204-v4-full.zip/download";

	// Ubuntu 12 torrent links (If torrents don't exist just put "" and the popup won't show!)
	public static final String	torrentURL_Ubuntu12_Core	= "http://zpwebsitefiles.com/torrents/app/ubuntu1204-v4-core.torrent";
	public static final String	torrentURL_Ubuntu12_Small	= "http://zpwebsitefiles.com/torrents/app/ubuntu1204-v4-small.torrent";
	public static final String	torrentURL_Ubuntu12_Large	= "http://zpwebsitefiles.com/torrents/app/ubuntu1204-v4-full.torrent";


	// Backtrack sourceforge & script links
	public static final String	ScriptURL_Backtrack			= "http://zpwebsitefiles.com/apps/backtrack/backtrackV6-script.zip";
	public static final String	imageURL_Backtrack_Core		= "http://sourceforge.net/projects/linuxonandroid/files/Backtrack/Image/backtrack-v10-image.zip/download";
	public static final String	imageURL_Backtrack_Small	= ""; // Don't exists right now, but it might come so put it in anyway!
	public static final String	imageURL_Backtrack_Large	= ""; // Don't exists right now, but it might come so put it in anyway!

	// Backtrack torrent links (If torrents don't exist just put "" and the popup won't show!)
	public static final String	torrentURL_Backtrack_Core	= "http://zpwebsitefiles.com/torrents/app/backtrack-v10-image.zip.torrent";
	public static final String	torrentURL_Backtrack_Small	= "";
	public static final String	torrentURL_Backtrack_Large	= "";


	// Debian sourceforge & script links
	public static final String	ScriptURL_Debian			= "http://zpwebsitefiles.com/apps/debian/debian-script.zip";
	public static final String	imageURL_Debian_Core		= "http://sourceforge.net/projects/linuxonandroid/files/Debian/Image/Core/debian-v4-core.zip/download";
	public static final String	imageURL_Debian_Small		= "http://sourceforge.net/projects/linuxonandroid/files/Debian/Image/Small/debian-v4-small.zip/download";
	public static final String	imageURL_Debian_Large		= "http://sourceforge.net/projects/linuxonandroid/files/Debian/Image/Large/debian-v4-large.zip/download";

	// Debian torrent links (If torrents don't exist just put "" and the popup won't show!)
	public static final String	torrentURL_Debian_Core		= "http://zpwebsitefiles.com/torrents/app/debian-v4-core.torrent";
	public static final String	torrentURL_Debian_Small		= "http://zpwebsitefiles.com/torrents/app/debian-v4-small.torrent";
	public static final String	torrentURL_Debian_Large		= "http://zpwebsitefiles.com/torrents/app/debian-v4-large.torrent";

	// Archlinux sourceforge links
	public static final String	imageURL_Arch_Core		= "http://sourceforge.net/projects/linuxonandroid/files/ArchLinux/Core/archlinux-BETA-core.zip/download";
	public static final String	imageURL_Arch_Small		= "http://sourceforge.net/projects/linuxonandroid/files/ArchLinux/Small/archlinux-BETA-small.zip/download";
	public static final String	imageURL_Arch_Large		= "http://sourceforge.net/projects/linuxonandroid/files/ArchLinux/Large/archlinux-BETA-large.zip/download";

	// Archlinux torrent links (If torrents don't exist just put "" and the popup won't show!)
	public static final String	torrentURL_Arch_Core		= "http://zpwebsitefiles.com/torrents/app/archlinux-BETA-core.torrent";
	public static final String	torrentURL_Arch_Small		= "http://zpwebsitefiles.com/torrents/app/archlinux-BETA-small.torrent";
	public static final String	torrentURL_Arch_Large		= "http://zpwebsitefiles.com/torrents/app/archlinux-BETA-large.torrent";

	// Play Store links to the needed apps
	public static final String	playStoreURL_term			= "market://details?id=jackpal.androidterm";
	public static final String	playStoreURL_vnc			= "market://details?id=android.androidVNC";
	public static final String	playStoreURL_donation		= "https://play.google.com/store/apps/developer?id=ZPwebsites";
}
