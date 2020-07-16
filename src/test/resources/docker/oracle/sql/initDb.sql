-- ディレクトリ作成
create or replace directory DATA_PUMP_DIR as '/u01/app/oracle/admin/XE/dpdump';
--ユーザー作成
create USER usr1 IDENTIFIED BY "pass1";
-- alter USER usr1 IDENTIFIED BY "pass1";
grant DBA to usr1;
GRANT UNLIMITED TABLESPACE TO usr1;
grant read, write on directory DATA_PUMP_DIR to usr1;

CREATE TABLE USR1.HOGE_TBL
(
  CHAR_COLUMN        CHAR,
  NCHAR_COLUMN       NCHAR(1),
  LONG_COLUMN        LONG,
  VARCHAR2_COLUMN    VARCHAR2(10),
  NVARCHAR2_COLUMN   NVARCHAR2(12),
  CLOB_COLUMN        CLOB,
  NUMBER_10_3_COLUMN NUMBER(10, 3),
  NUMBER_8_COLUMN    NUMBER(8),
  DATE_COLUMN        DATE,
  TIMESTAMP_COLUMN   TIMESTAMP(6),
  ROWID_COLUMN       ROWID,
  BLOB_COLUMN        BLOB
);
commit;

exit
