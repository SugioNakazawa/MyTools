#!/bin/bash
# create schema 'usr1'.
# copy init.dmp file into container.
# And launch impdp in container.
cd `dirname $0`
sqlplus system/password@localhost:1521 @initDb.sql
docker cp init.dmp docker_oracle_11202:/u01/app/oracle/admin/XE/dpdump/init.dmp
docker exec -it docker_oracle_11202 impdp usr1/pass1@XE directory=DATA_PUMP_DIR dumpfile=init.dmp log=impdp.log
# for expdp & cp
# docker exec -it docker_oracle_11202 expdp usr1/pass1@XE directory=DATA_PUMP_DIR schemas=usr1 dumpfile=exp.dmp log=expdp.log
# docker cp docker_oracle_11202:/u01/app/oracle/admin/XE/dpdump/exp.dmp .
