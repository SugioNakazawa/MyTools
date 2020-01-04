/**
 *
 */
package com.hoge;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author nakazawasugio
 *
 */
public class CreateDataDefTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.hoge.CreateDataDef#main(java.lang.String[])}.
     */
    @Test
    public final void testMain() {
        String[] testParam = { "dmdl", "HOGE_TBL" };
        try {
            CreateDataDef.main(testParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for {@link com.hoge.CreateDataDef#exec()}.
     */
    @Test
    public final void testDmdl() {
        CreateDataDef target = new CreateDataDef(CreateDataDef.Type.DMDL, "HOGE_TBL");
        try {
            target.exec();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        Assert.assertEquals(expDmdl, target.getOutput());
    }

    @Test
    public final void testToCsv() {
        CreateDataDef target = new CreateDataDef(CreateDataDef.Type.TOCSV, "HOGE_TBL");
        try {
            target.exec();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        Assert.assertEquals(expToCsv, target.getOutput());
    }

    @Test
    public final void testToDb() {
        CreateDataDef target = new CreateDataDef(CreateDataDef.Type.TODB, "HOGE_TBL");
        try {
            target.exec();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        Assert.assertEquals(expToDb, target.getOutput());
    }
    /**
     * DBがアクセスを行わないテストケース。
     */
    @Test
    public void test() {
    	List<String[]> colList = new ArrayList<String[]>();
    	{
    		String[][] dats = {
    				{ "0", "CHAR_COLUMN", "CHAR" },
    				{ "1", "NCHAR_COLUMN", "NCHAR" },
    				{ "2", "LONG_COLUMN", "LONG" },
    				{ "3", "VARCHAR2_COLUMN", "VARCHAR2" },
    				{ "4", "NVARCHAR2_COLUMN", "NVARCHAR2" },
    				{ "5", "CLOB_COLUMN", "CLOB" },
    				{ "6", "NUMBER_10_3_COLUMN", "NUMBER" },
    				{ "7", "NUMBER_8_COLUMN", "NUMBER" },
    				{ "8", "DATE_COLUMN", "DATE" },
    				{ "9", "TIMESTAMP_COLUMN", "TIMESTAMP" },
    				{ "10", "ROWID_COLUMN", "ROWID" },
    				{ "11", "BLOB_COLUMN", "BLOB" },
    		};
    		for(String[] dat:dats) {
    			colList.add(dat);
    		}
    	}
        CreateDataDef target = new CreateDataDef(CreateDataDef.Type.DMDL, "HOGE_TBL");
        target.setColList(colList);

        Assert.assertEquals(expDmdl,target.outputDmdl());
    }

    static private String expDmdl = "\"HOGE_TBL\"\n" + 
    		"@namespace(value = db)\n" + 
    		"@windgate.jdbc.table(name = \"HOGE_TBL\")\n" + 
    		"@directio.csv\n" + 
    		"hoge_tbl = {\n" + 
    		"		\"CHAR_COLUMN\"\n" + 
    		"		@windgate.jdbc.column(name = \"CHAR_COLUMN\")\n" + 
    		"		char_column : TEXT;\n" + 
    		"\n" + 
    		"		\"NCHAR_COLUMN\"\n" + 
    		"		@windgate.jdbc.column(name = \"NCHAR_COLUMN\")\n" + 
    		"		nchar_column : TEXT;\n" + 
    		"\n" + 
    		"		\"VARCHAR2_COLUMN\"\n" + 
    		"		@windgate.jdbc.column(name = \"VARCHAR2_COLUMN\")\n" + 
    		"		varchar2_column : TEXT;\n" + 
    		"\n" + 
    		"		\"NVARCHAR2_COLUMN\"\n" + 
    		"		@windgate.jdbc.column(name = \"NVARCHAR2_COLUMN\")\n" + 
    		"		nvarchar2_column : TEXT;\n" + 
    		"\n" + 
    		"		\"CLOB_COLUMN\"\n" + 
    		"		@windgate.jdbc.column(name = \"CLOB_COLUMN\")\n" + 
    		"		clob_column : TEXT;\n" + 
    		"\n" + 
    		"		\"NUMBER_10_3_COLUMN\"\n" + 
    		"		@windgate.jdbc.column(name = \"NUMBER_10_3_COLUMN\")\n" + 
    		"		number_10_3_column : DECIMAL;\n" + 
    		"\n" + 
    		"		\"NUMBER_8_COLUMN\"\n" + 
    		"		@windgate.jdbc.column(name = \"NUMBER_8_COLUMN\")\n" + 
    		"		number_8_column : DECIMAL;\n" + 
    		"\n" + 
    		"		\"DATE_COLUMN\"\n" + 
    		"		@windgate.jdbc.column(name = \"DATE_COLUMN\")\n" + 
    		"		date_column : DATE;\n" + 
    		"\n" + 
    		"};";

    static private String expToCsv = "{% include 'myenv' %}\n" + 
    		"\n" + 
    		"in:\n" + 
    		"  type: oracle\n" + 
    		"  driver_path: {{my_driver_path}}\n" + 
    		"  url: {{my_url}}\n" + 
    		"  user: {{my_usr}}\n" + 
    		"  password: {{my_pass}}\n" + 
    		"  table: HOGE_TBL\n" + 
    		"  select: \"CHAR_COLUMN,NCHAR_COLUMN,VARCHAR2_COLUMN,NVARCHAR2_COLUMN,CLOB_COLUMN,NUMBER_10_3_COLUMN,NUMBER_8_COLUMN,DATE_COLUMN\"\n" + 
    		"  columns:\n" + 
    		"  - {name: CHAR_COLUMN, type: string}\n" + 
    		"  - {name: NCHAR_COLUMN, type: string}\n" + 
    		"  - {name: VARCHAR2_COLUMN, type: string}\n" + 
    		"  - {name: NVARCHAR2_COLUMN, type: string}\n" + 
    		"  - {name: CLOB_COLUMN, type: string}\n" + 
    		"  - {name: NUMBER_10_3_COLUMN, type: double}\n" + 
    		"  - {name: NUMBER_8_COLUMN, type: double}\n" + 
    		"  - {name: DATE_COLUMN, type: timestamp, format: '%Y-%m-%d'}\n" + 
    		"out:\n" + 
    		"  type: file\n" + 
    		"  path_prefix: {{my_to_csv_path}}/hoge_tbl/hoge_tbl\n" + 
    		"  file_ext: csv\n" + 
    		"  formatter:\n" + 
    		"    type: csv\n" + 
    		"    charset: UTF-8\n" + 
    		"    newline: LF\n" + 
    		"    delimiter: ','\n" + 
    		"    quote: '\"'\n" + 
    		"    escape: '\"'\n" + 
    		"    header_line: false\n" + 
    		"    trim_if_not_quoted: false\n" + 
    		"    allow_extra_columns: false\n" + 
    		"    allow_optional_columns: false\n" + 
    		"    default_timezone: 'Asia/Tokyo'\n";

    static private String expToDb = "{% include 'myenv' %}\n" + 
    		"\n" + 
    		"in:\n" + 
    		"  type: file\n" + 
    		"  path_prefix: {{my_to_db_path}}/hoge_tbl/hoge_tbl\n" + 
    		"  parser:\n" + 
    		"    charset: UTF-8\n" + 
    		"    newline: LF\n" + 
    		"    type: csv\n" + 
    		"    delimiter: ','\n" + 
    		"    quote: '\"'\n" + 
    		"    escape: '\"'\n" + 
    		"    trim_if_not_quoted: false\n" + 
    		"    skip_header_lines: 0\n" + 
    		"    allow_extra_columns: false\n" + 
    		"    allow_optional_columns: false\n" + 
    		"    columns:\n" + 
    		"    - {name: CHAR_COLUMN, type: string}\n" + 
    		"    - {name: NCHAR_COLUMN, type: string}\n" + 
    		"    - {name: VARCHAR2_COLUMN, type: string}\n" + 
    		"    - {name: NVARCHAR2_COLUMN, type: string}\n" + 
    		"    - {name: CLOB_COLUMN, type: string}\n" + 
    		"    - {name: NUMBER_10_3_COLUMN, type: double}\n" + 
    		"    - {name: NUMBER_8_COLUMN, type: double}\n" + 
    		"    - {name: DATE_COLUMN, type: timestamp, format: '%Y-%m-%d'}\n" + 
    		"out:\n" + 
    		"  type: oracle\n" + 
    		"  driver_path: {{my_driver_path}}\n" + 
    		"  url: {{my_url}}\n" + 
    		"  user: {{my_usr}}\n" + 
    		"  password: {{my_pass}}\n" + 
    		"  table: HOGE_TBL\n" + 
    		"  options: {LoginTimeout: 20000}\n" + 
    		"  mode: truncate_insert\n" + 
    		"  insert_method: direct\n";

}
