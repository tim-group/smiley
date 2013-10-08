#!/bin/bash

for rec in `cat recipients.txt`
do 
    mail -a "Content-type: text/html;" -s "Happy?"  $rec < email.html
done
