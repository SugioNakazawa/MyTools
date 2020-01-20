#!/bin/bash -xeu

JSTACK=jstack
# for specify jstack
#JSTACK=/usr/java/jdk1.8.0_74/bin/jstack
OUTFILE=thread_dump_`date "+%Y%m%d_%H%M%S"`.log
while true
do
for pid in `ps -f -C java | awk '$1=="'$USER'" {print $2}'`
# for mac
#for pid in `ps -f -C | grep -v grep | grep java | awk '{print $2}'`
do
   $JSTACK $pid >> $OUTFILE
done
sleep 10
done
