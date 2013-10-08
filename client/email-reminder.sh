#!/bin/bash

DATE=`date +%Y-%m-%d`

for REC in `cat recipients.txt`
do 
    cat email-template.txt | sed -e "s/{{email}}/$REC/" -e "s/{{date}}/$DATE/" | sendmail -t 
done
