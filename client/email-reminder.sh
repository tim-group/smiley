#!/bin/bash

for rec in `cat recipients.txt`
do 
    cat email-template.txt | sed -e "s/\${email}/$rec/" > email.to.send
    sendmail -t < email.to.send
    unlink email.to.send
done
