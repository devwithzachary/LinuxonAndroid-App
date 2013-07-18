###########################################
# Linux boot script V7.9 for Android      #
# Built by Zachary Powell (zacthespack)   #
# and Martin Møller (Tuxling)             #
# Thanks to:                              #
# Johan Vromans                           #
# Marshall Levin                          #
# and to everyone at XDA!                 #
# Feel free to edit/use this script as you#
# like but credit Linuxonandroid.org      #
###########################################
# $ver: V7.9                              #
###########################################


###########################################
# This is a function we use to stop the   #
# script in case of errors                #
###########################################
error_exit() {
    echo "Error: $1"
    exit 1
}


###########################################
# Set up variables                        #
###########################################
if [ -f /data/data/com.zpwebsites.linuxonandroid/files/busybox ]; then
      	export bbox=/data/data/com.zpwebsites.linuxonandroid/files/busybox
elif [ -f /data/data/com.zpwebsites.linuxonandroid.opensource/files/busybox ]; then
      	export bbox=/data/data/com.zpwebsites.linuxonandroid.opensource/files/busybox
else
	export bbox=/system/xbin/busybox
fi

export usermounts=android	# Base folder all user mounts are done in, should be moved to app later
export imgfile=$(dirname $0)/ubuntu.img    # Default image file, another can be set by using an argument
export bin=/system/bin
export mnt=/data/local/mnt
export USER=root
if [[ ! -d $mnt ]]; then mkdir $mnt; fi
export PATH=$bin:/usr/bin:/usr/local/bin:/usr/sbin:/bin:/usr/local/sbin:/usr/games:$PATH
export TERM=linux
export HOME=/root

###########################################
# Handle arguments if present             #
###########################################
if [ $# -ne 0 ]; then
    if [ -f $1 ]; then # Is full path present?
        imgfile=$1

	elif [ -f $(dirname $0)/$1 ]; then # Is only a filename present?
        imgfile=$(dirname $0)/$1

	else
        error_exit "Image file not found!($1)"
    fi
fi

###########################################
# If a md5 file is found we check it here #
###########################################
if [ -f $imgfile.md5 ]; then
        echo "MD5 file found, use to check .img file? (y/n)"
	read answer
	if [ $answer == y ]; then
	     echo -n "Validating image checksum... "
    	     $bbox md5sum -c -s $imgfile.md5
    	     if [ $? -ne 0 ];then
                  echo "FAILED!"
                  error_exit "Checksum failed! The image is corrupted!"
             else
                  echo "OK"
                  rm $imgfile.md5
             fi
	fi
   
fi
################################
# Find and read config file    #
# or use defaults if not found #
################################
use_swap=no

cfgfile=$imgfile.config # Default config file if not specified

if [ -f $imgfile.config ]; then
	source $imgfile.config
fi

###########################################
# Set Swap up if wanted                   #
#                                         #
###########################################
if [ $use_swap == yes ]; then
	if [ -f $imgfile.swap ]; then
		echo "Swap file found, using file"
		echo "Turning on swap (if it errors here you do not have swap support"
		swapon $imgfile.swap
		
	else
		echo "Creating Swap file"
		dd if=/dev/zero of=$imgfile.swap bs=1048576 count=1024
		mkswap $imgfile.swap
		echo "Turning on swap (if it errors here you do not have swap support"
		swapon $imgfile.swap
			
	fi
fi
###########################################
# Set up loop device and mount image      #
###########################################
echo -n "Checking loop device... "
if [ -b /dev/block/loop255 ]; then
	echo "FOUND"
else
	echo "MISSING"
	# Loop device not found so we create it and verify it was actually created
	echo -n "Creating loop device... "
	$bbox mknod /dev/block/loop255 b 7 255
	if [ -b /dev/block/loop255 ]; then
		echo "OK"
	else
		echo "FAILED"
		error_exit "Unable to create loop device!"
	fi
fi

$bbox losetup /dev/block/loop255 $imgfile
if [ $? -ne 0 ];then error_exit "Unable to attach image to loop device! (Image = $imgfile)"; fi

$bbox mount -t ext2 /dev/block/loop255 $mnt
if [ $? -ne 0 ];then error_exit "Unable to mount the loop device!"; fi

###########################################
# Mount all required partitions           #
###########################################
$bbox mount -t devpts devpts $mnt/dev/pts
if [ $? -ne 0 ];then error_exit "Unable to mount $mnt/dev/pts!"; fi
$bbox mount -t proc proc $mnt/proc
if [ $? -ne 0 ];then error_exit "Unable to mount $mnt/proc!"; fi
$bbox mount -t sysfs sysfs $mnt/sys
if [ $? -ne 0 ];then error_exit "Unable to mount $mnt/sys!"; fi
$bbox mount -o bind /sdcard $mnt/sdcard
if [ $? -ne 0 ];then error_exit "Unable to bind $mnt/sdcard!"; fi

if [[ ! -d $mnt/root/cfg ]]; then mkdir $mnt/root/cfg; fi
$bbox mount -o bind $(dirname $imgfile) $mnt/root/cfg


###########################################
# Checks if you have a external sdcard    #
# and mounts it if you do                 #
###########################################
if [ -d /sdcard/external_sd ]; then
	$bbox mount -o bind /sdcard/external_sd  $mnt/external_sd
fi
if [ -d /Removable/MicroSD ]; then
	$bbox mount -o bind /Removable/MicroSD  $mnt/external_sd
fi
# This is for the HD version of the Archos 70 internet tablet, may be the same for the SD card edition but i dont know.
if [ -d /storage ]; then
	$bbox mount -o bind /storage  $mnt/external_sd
fi

###########################################
# Mount all user defined mounts if any    #
###########################################
if [ -f $imgfile.mounts ]; then
	olddir=$(pwd)
	echo "Mounting user mounts"

	cd $mnt
	if [[ ! -d $mnt/$usermounts ]]; then $bbox mkdir -p $usermounts; fi

	echo "# Script to unmount user defined mounts, do not delete or edit!" > $imgfile.shutdown
	echo "cd $mnt/$usermounts" > $imgfile.shutdown

	cd $mnt/$usermounts
	for entry in $(cat "$imgfile.mounts"); do
		ANDROID=${entry%;*}
		LINUX=${entry#*;}

		if [[ -d $ANDROID ]]; then
			echo -n "Mounting $ANDROID to $usermounts/$LINUX... "
			if [[ ! -d $mnt/$usermounts/$LINUX ]]; then $bbox mkdir -p $LINUX; fi
			$bbox mount -o bind $ANDROID $mnt/$usermounts/$LINUX &> /dev/null
			if [ $? -ne 0 ];then
				echo FAIL
				if [[ -d $mnt/$usermounts/$LINUX ]]; then $bbox rmdir -p $LINUX; fi
			else
				echo OK
				echo "$bbox umount $mnt/$usermounts/$LINUX" >> $imgfile.shutdown
				echo "$bbox rmdir -p $LINUX" >> $imgfile.shutdown
			fi
		else
			echo "Android folder not found: $ANDROID"
		fi
	done
	echo "cd $mnt" >> $imgfile.shutdown
	echo "$bbox rmdir -p $usermounts" >> $imgfile.shutdown
	cd $olddir

else
	echo "No user defined mount points"
fi

###########################################
# Sets up network forwarding              #
###########################################
$bbox sysctl -w net.ipv4.ip_forward=1
if [ $? -ne 0 ];then error_exit "Unable to forward network!"; fi

# If NOT $mnt/root/DONOTDELETE.txt exists we setup hosts and resolv.conf now
if [ ! -f $mnt/root/DONOTDELETE.txt ]; then
	echo "nameserver 8.8.8.8" > $mnt/etc/resolv.conf
	if [ $? -ne 0 ];then error_exit "Unable to write resolv.conf file!"; fi
	echo "nameserver 8.8.4.4" >> $mnt/etc/resolv.conf
	echo "127.0.0.1 localhost" > $mnt/etc/hosts
	if [ $? -ne 0 ];then error_exit "Unable to write hosts file!"; fi
fi


###########################################
# Chroot into ubuntu                      #
###########################################
$bbox chroot $mnt /root/init.sh $(basename $imgfile)

###########################################
# Shut down ubuntu                        #
###########################################
echo "Shutting down Linux ARM"
#for pid in `lsof | grep $mnt | sed -e's/  / /g' | cut -d' ' -f2`; do kill -9 $pid >/dev/null 2>&1; done
for pid in `$bbox lsof | $bbox grep $mnt | $bbox sed -e's/  / /g' | $bbox cut -d' ' -f2`; do $bbox kill -9 $pid >/dev/null 2>&1; done
sleep 5

###########################################
# Unmount all user defined mounts if any  #
###########################################
if [ -f $imgfile.shutdown ]; then
	echo "Unmounting user defined mounts"
	sh $imgfile.shutdown
	rm $imgfile.shutdown
fi

$bbox umount $mnt/root/cfg
$bbox umount $mnt/sdcard
$bbox umount $mnt/external_sd
$bbox umount $mnt/dev/pts
$bbox umount $mnt/dev
$bbox umount $mnt/proc
$bbox umount $mnt/sys
$bbox umount $mnt
$bbox losetup -d /dev/block/loop255 &> /dev/null
