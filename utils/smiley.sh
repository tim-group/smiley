#!/bin/bash
#
# command line interface for smiley
#
# symlink this script as happy, neutral and sad
# replace the email address with your own
# change the url to point to your instance of smiley

EMAIL=""
URL=""
MOOD=$(basename $0)

if [ -z ${EMAIL} ]; then
    echo "please edit $0 and fill-in your email address"
    exit 1
fi

if [ -z ${URL} ]; then
    echo "please edit $0 and fill-in the URL to smiley"
    exit 1
fi

if [[ ${MOOD} != "happy" && ${MOOD} != "neutral" && ${MOOD} != "sad" ]]; then
    echo "please use happy, neutral or sad to specify the mood"
    exit 2
fi

if [ $# > 1 ]; then
    DATESTR="$@"
else
    DATESTR="today"
fi

DATE=$(date +%Y-%m-%d -d "$DATESTR")

curl "${URL}?user=${EMAIL}&mood=${MOOD}&date=${DATE}"

echo
