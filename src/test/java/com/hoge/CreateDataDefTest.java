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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nakazawasugio
 *
 */
public class CreateDataDefTest {
	static Logger logger = LoggerFactory.getLogger(CreateDataDefTest.class);

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
        String[] testParam = { "HOGE_TBL", "dmdl" };
        try {
            CreateDataDef.main(testParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * タイプがnullー＞エラー
     */
    @Test
    public final void testMainTypeNull() {
        String[] testParam = { "HOGE_TBL", null };
        try {
            CreateDataDef.main(testParam);
            fail();
        } catch (Exception e) {
        	Assert.assertEquals("Illegual type.DMDL, TOCSV, TODB.", e.getMessage());
        }
    }
    /**
     * カラムなしテーブルー＞エラー
     */
    @Test
    public final void testMainNoColumnsTable() {
        String[] testParam = { "NO_COL_TBL", "DMDL" };
        try {
            CreateDataDef.main(testParam);
            fail();
        } catch (Exception e) {
        	Assert.assertEquals("no data table=NO_COL_TBL", e.getMessage());
        }
    }

    /**
     * Test method for {@link com.hoge.CreateDataDef#exec()}.
     */
    @Test
    public final void testDmdl() {
        CreateDataDef target = new CreateDataDef();
        try {
            target.exec("HOGE_TBL", CreateDataDef.Type.DMDL);
//            Assert.assertEquals(12, target.getColList2().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
//        System.out.println(target.getOutput());
        Assert.assertEquals(expDmdl, target.getOutput());
    }

    @Test
    public final void testToCsv() {
        CreateDataDef target = new CreateDataDef();
        try {
            target.exec("HOGE_TBL", CreateDataDef.Type.TOCSV);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        Assert.assertEquals(expToCsv, target.getOutput());
    }

    @Test
    public final void testToDb() {
        CreateDataDef target = new CreateDataDef();
        try {
            target.exec("HOGE_TBL", CreateDataDef.Type.TODB);
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
        CreateDataDef target = new CreateDataDef();
        try {
			target.exec("HOGE_TBL", CreateDataDef.Type.DMDL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        target.setColList(colList);

        Assert.assertEquals(expDmdl,target.outputDmdl());
    }

    static private String expDmdl = "\"HOGE_TBL\"\n" + 
    		"@namespace(value = db)\n" + 
    		"@windgate.jdbc.table(name = \"HOGE_TBL\")\n" + 
    		"@directio.csv\n" + 
    		"hoge_tbl = {\n" + 
            "\t\t\"CHAR_COLUMN\"\n" + 
            "\t\t@windgate.jdbc.column(name = \"CHAR_COLUMN\")\n" + 
            "\t\tchar_column : TEXT;\n" + 
            "\n" + 
            "\t\t\"NCHAR_COLUMN\"\n" + 
            "\t\t@windgate.jdbc.column(name = \"NCHAR_COLUMN\")\n" + 
            "\t\tnchar_column : TEXT;\n" + 
            "\n" + 
            "\t\t\"VARCHAR2_COLUMN\"\n" + 
            "\t\t@windgate.jdbc.column(name = \"VARCHAR2_COLUMN\")\n" + 
            "\t\tvarchar2_column : TEXT;\n" + 
            "\n" + 
            "\t\t\"NVARCHAR2_COLUMN\"\n" + 
            "\t\t@windgate.jdbc.column(name = \"NVARCHAR2_COLUMN\")\n" + 
            "\t\tnvarchar2_column : TEXT;\n" + 
            "\n" + 
            "\t\t\"CLOB_COLUMN\"\n" + 
            "\t\t@windgate.jdbc.column(name = \"CLOB_COLUMN\")\n" + 
            "\t\tclob_column : TEXT;\n" + 
    		"\n" + 
    		"\t\t\"NUMBER_10_3_COLUMN\"\n" + 
    		"\t\t@windgate.jdbc.column(name = \"NUMBER_10_3_COLUMN\")\n" + 
    		"\t\tnumber_10_3_column : DECIMAL;\n" + 
    		"\n" + 
    		"\t\t\"NUMBER_8_COLUMN\"\n" + 
    		"\t\t@windgate.jdbc.column(name = \"NUMBER_8_COLUMN\")\n" + 
    		"\t\tnumber_8_column : DECIMAL;\n" + 
    		"\n" + 
    		"\t\t\"DATE_COLUMN\"\n" + 
    		"\t\t@windgate.jdbc.column(name = \"DATE_COLUMN\")\n" + 
    		"\t\tdate_column : DATE;\n" + 
    		"\n" + 
            "\t\t\"TIMESTAMP_COLUMN\"\n" + 
            "\t\t@windgate.jdbc.column(name = \"TIMESTAMP_COLUMN\")\n" + 
            "\t\ttimestamp_column : DATETIME;\n" +
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
    		"  select: \"CHAR_COLUMN,NCHAR_COLUMN,VARCHAR2_COLUMN,NVARCHAR2_COLUMN,CLOB_COLUMN,NUMBER_10_3_COLUMN,NUMBER_8_COLUMN,DATE_COLUMN,TIMESTAMP_COLUMN\"\n" + 
    		"  columns:\n" + 
    		"  - {name: CHAR_COLUMN, type: string}\n" + 
    		"  - {name: NCHAR_COLUMN, type: string}\n" + 
    		"  - {name: VARCHAR2_COLUMN, type: string}\n" + 
    		"  - {name: NVARCHAR2_COLUMN, type: string}\n" + 
    		"  - {name: CLOB_COLUMN, type: string}\n" + 
    		"  - {name: NUMBER_10_3_COLUMN, type: double}\n" + 
    		"  - {name: NUMBER_8_COLUMN, type: double}\n" + 
    		"  - {name: DATE_COLUMN, type: timestamp, format: '%Y-%m-%d'}\n" +
    		"  - {name: TIMESTAMP_COLUMN, type: timestamp, format: '%Y-%m-%d %k:%M:%S'}\n" +
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
    		"    - {name: TIMESTAMP_COLUMN, type: timestamp, format: '%Y-%m-%d %k:%M:%S'}\n" + 
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
