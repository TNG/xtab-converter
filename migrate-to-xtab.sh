#!/bin/bash

usage () { echo "usage: $0 -j <xtabJar> -d <directory> [-r removes old xls files]"; }

migrateTemplate=" [ MIGRATED ]"
deleteTemplate=" [ %16s ]"

migrateTemplateSize=`printf "$migrateTemplate" | wc -m`
maxFilenameWidth=$((`tput cols` - $migrateTemplateSize))

directory=
xtabJar=
delete=false
options=':hrj:d:'
while getopts $options option
do
    case $option in
      r  ) delete=true;;
      j  ) xtabJar="$OPTARG";;
      d  ) directory="$OPTARG";;
      h  ) usage; exit;;
	  \? ) echo "Unknown option: -$OPTARG" >&2; exit 1;;
	  :  ) echo "Missing option argument for -$OPTARG" >&2; exit 1;;
	  *  ) echo "Unimplemented option: -$OPTARG" >&2; exit 1;;
    esac
done
shift $(($OPTIND - 1))


if [[ -z $xtabJar ]]; then
	echo "Missing required option: -j" >&2; 
    usage >&2
    exit 1
fi

if [ "$delete" = true ]; then
	deleteTemplateSize=`printf "$deleteTemplate" | wc -m`
	maxFilenameWidth=$((maxFilenameWidth - deleteTemplateSize))
fi

function useCurrentFolderWhenNoDirectoryIsGiven {
	if [[ -z $directory ]]; then
	  directory=$(pwd)
	fi
}

function assertJava8OrHigherIsPresent {
	javaVersion=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}');

	if [[ $javaVersion < "1.8" ]]; then
		echo "java >= 1.8 is not available Got only '$javaVersion'"
		echo "used java at '`which java`'"
		exit 1
	fi
}

function assertXtabConverterJarIsPresent {
	if [[ (! -f $xtabJar) || (-z $xtabJar) ]]; then
		echo "xtab-converter could not be found. You gave me: '$1'"
		exit 1
	fi
}

function showStartBanner {
	echo ;
	echo "Start recursive .xls to .xtab migration in directory:"
	echo "  ${directory}"
	echo ;
}

function migrateXlsToXtab {
	find $directory -iname *.xls | while read xlsFile; do
		message=
		size=${#xlsFile}

		if [[ $size -gt $maxFilenameWidth ]]; then
			printf "%s" "${xlsFile:size-maxFilenameWidth:size}"
		else
			printf "%-${maxFilenameWidth}s" "$xlsFile"
		fi
		
		java -jar "$xtabJar" "$xlsFile"
		printf "$migrateTemplate"
		
		if [ "$delete" = true ] ; then
			rm "$xlsFile" 2> /dev/null
			successfullyDeleted=$?
			if [ $successfullyDeleted -eq 0 ]; then
				message="DELETED"
			else
				message="FAILED-TO-DELETE"
			fi
			printf "$deleteTemplate" "$message";
		fi
		printf "\n"
	done
}

function showFinishedMessage {
	echo ;
	echo "done"
}

useCurrentFolderWhenNoDirectoryIsGiven
assertJava8OrHigherIsPresent
assertXtabConverterJarIsPresent
showStartBanner
migrateXlsToXtab
showFinishedMessage

