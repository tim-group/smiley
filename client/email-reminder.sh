#!/bin/bash

for rec in `cat recipients.txt`
do 
    cat email.html | sed -e "s/\${email}/$rec/" > email.to.send
    mail -a "Content-type: text/html;" -s "Happy?"  $rec < email.to.send
    unlink email.to.send
done
