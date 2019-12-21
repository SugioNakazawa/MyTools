-- ディレクトリ作成
create or replace directory DATA_PUMP_DIR as '/u01/app/oracle/admin/XE/dpdump';
--ユーザー作成
create USER usr1 IDENTIFIED BY "pass1";
-- alter USER usr1 IDENTIFIED BY "pass1";
grant DBA to usr1;
GRANT UNLIMITED TABLESPACE TO usr1;
grant read, write on directory DATA_PUMP_DIR to usr1;

exit
