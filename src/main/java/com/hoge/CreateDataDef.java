/**
 *
 */
package com.hoge;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OracleDBスキーマ情報からDMDL、embulkスクリプトを生成。 ３種類のスクリプトを出力。 引数１ dmdl:AsakusaFW用DMDL
 * tocsv:embulk用db -> csv todb:embulk用csv -> db 引数２ テーブル名
 * 
 * @author nakazawasugio
 *
 */
public class CreateDataDef {
	static Logger logger = LoggerFactory.getLogger(CreateDataDef.class);
	/** DMDL出力用 namespace */
	static final String DMDL_NAME_SPACE = System.getProperty("DMDL_NAME_SPACE", "db");

	private Map<String, String> typeMap;
	private String[][] typeData = { //
			{ "CHAR", "string" }, //
			{ "NCHAR", "string" }, //
			{ "VARCHAR2", "string" }, //
			{ "NVARCHAR2", "string" }, //
			{ "CLOB", "string" }, //
			{ "DATE", "timestamp, format: '%Y-%m-%d'" }, //
			{ "TIMESTAMP(6)", "timestamp, format: '%Y-%m-%d %k:%M:%S'" }, //
			{ "NUMBER", "double" }, //
	};
	private Map<String, String> dmdlMap;
	private String[][] dmdlData = { //
			{ "CHAR", "TEXT" }, //
			{ "NCHAR", "TEXT" }, //
			{ "VARCHAR2", "TEXT" }, //
			{ "NVARCHAR2", "TEXT" }, //
			{ "CLOB", "TEXT" }, //
			{ "DATE", "DATE" }, //
			{ "TIMESTAMP(6)", "DATETIME" }, //
			{ "NUMBER", "DECIMAL" }, //
	};

	private String tableName;
	private Type type;

	enum Type {
		DMDL, TOCSV, TODB
	};

	private List<String[]> colList;
	private String output;

	/**
	 * @param args[0] type
	 * @param args[1] table name
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Type type = null;
		for (Type t : Type.values()) {
			if (t.name().equalsIgnoreCase(args[0])) {
				type = t;
			}
		}
		if (type == null) {
			throw new RuntimeException("Illegual type.DMDL, TOCSV, TODB.");
		}
		CreateDataDef target = new CreateDataDef(type, args[1]);

		target.exec();
	}

	public CreateDataDef(Type type, String tableName) {
		this.type = type;
		this.tableName = tableName;
		typeMap = new HashMap<String, String>();
		for (String[] dat : typeData) {
			typeMap.put(dat[0], dat[1]);
		}
		dmdlMap = new HashMap<String, String>();
		for (String[] dat : dmdlData) {
			dmdlMap.put(dat[0], dat[1]);
		}
		colList = new ArrayList<String[]>();
	}

	void exec() throws Exception {
		getMeta(tableName.toUpperCase());
		if (colList.size() < 1) {
			logger.info("no data table=" + tableName);
			System.exit(0);
		}
		// output
		switch (type) {
		case DMDL:
			output = outputDmdl();
			break;
		case TOCSV:
			output = outputToCsv();
			break;
		case TODB:
			output = outputToDb();
			break;
		}
		System.out.println(output);
	}

	public String getOutput() {
		return output;
	}

	private String outputToCsv() {
		StringBuffer sb = new StringBuffer();

		sb.append("{% include 'myenv' %}\n");
		sb.append("\n");
		sb.append("in:\n");
		sb.append("  type: oracle\n");
		sb.append("  driver_path: {{my_driver_path}}\n");
		sb.append("  url: {{my_url}}\n");
		sb.append("  user: {{my_usr}}\n");
		sb.append("  password: {{my_pass}}\n");
		sb.append("  table: " + tableName + "\n");
		//	select
		sb.append("  select: \"");
		StringBuffer cols = new StringBuffer();
		for (String[] dat : colList) {
			if (convType(dat[1], dat[2], dat[3]) != null) {
				if(cols.length()>0) {
					cols.append(",");
				}
				cols.append(dat[1]);
			}
		}
		sb.append(cols.toString());
		sb.append("\"\n");
		//
		sb.append(convWhere());
		sb.append("  columns:\n");
		//
		for (String[] dat : colList) {
			if (convType(dat[1], dat[2], dat[3]) != null) {
				sb.append("  - {name: " + dat[1].toUpperCase() + ", type: " + convType(dat[1], dat[2], dat[3]) + "}\n");
			}
		}

		sb.append("out:\n");
		sb.append("  type: file\n");
		sb.append(
				"  path_prefix: {{my_to_csv_path}}/" + tableName.toLowerCase() + "/" + tableName.toLowerCase() + "\n");
		sb.append("  file_ext: csv\n");
		sb.append("  formatter:\n");
		sb.append("    type: csv\n");
		sb.append("    charset: UTF-8\n");
		sb.append("    newline: LF\n");
		sb.append("    delimiter: ','\n");
		sb.append("    quote: '\"'\n");
		sb.append("    escape: '\"'\n");
		sb.append("    header_line: false\n");
		sb.append("    trim_if_not_quoted: false\n");
		sb.append("    allow_extra_columns: false\n");
		sb.append("    allow_optional_columns: false\n");
		sb.append("    default_timezone: 'Asia/Tokyo'\n");

		return sb.toString();
	}

	private String outputToDb() {
		StringBuffer sb = new StringBuffer();

		sb.append("{% include 'myenv' %}\n");
		sb.append("\n");
		sb.append("in:\n");
		sb.append("  type: file\n");
		sb.append("  path_prefix: {{my_to_db_path}}/" + tableName.toLowerCase() + "/" + tableName.toLowerCase() + "\n");
		sb.append("  parser:\n");
		sb.append("    charset: UTF-8\n");
		sb.append("    newline: LF\n");
		sb.append("    type: csv\n");
		sb.append("    delimiter: ','\n");
		sb.append("    quote: '\"'\n");
		sb.append("    escape: '\"'\n");
		sb.append("    trim_if_not_quoted: false\n");
		sb.append("    skip_header_lines: 0\n");
		sb.append("    allow_extra_columns: false\n");
		sb.append("    allow_optional_columns: false\n");

		sb.append("    columns:\n");
		for (String[] dat : colList) {
			if (convType(dat[1], dat[2], dat[3]) != null) {
				sb.append(
						"    - {name: " + dat[1].toUpperCase() + ", type: " + convType(dat[1], dat[2], dat[3]) + "}\n");
			}
		}
		sb.append("out:\n");
		sb.append("  type: oracle\n");
		sb.append("  driver_path: {{my_driver_path}}\n");
		sb.append("  url: {{my_url}}\n");
		sb.append("  user: {{my_usr}}\n");
		sb.append("  password: {{my_pass}}\n");
		sb.append("  table: " + this.tableName + "\n");
		sb.append("  options: {LoginTimeout: 20000}\n");
		sb.append("  mode: truncate_insert\n");
		sb.append("  insert_method: direct\n");

		return sb.toString();
	}

	// for bts project
	@SuppressWarnings("unused")
	private String convDpTableName() {
		if (tableName.startsWith("BTS_")) {
			return "dpdb_" + tableName.substring(4, tableName.length());
		}
		return "dpdc_" + tableName.substring(5, tableName.length());
	}

	// for bts project
	@SuppressWarnings("unused")
	private String getDirectory() {
		if (this.tableName.startsWith("BTS_")) {
			return "blenderts";
		}
		return "btscustom";
	}

	private String convWhere() {
		if (colList.size() > 0) {
			if ("LAST_MODIFIED".equalsIgnoreCase(this.colList.get(colList.size() - 1)[1])) {
				return "  where: to_char(LAST_MODIFIED,'YYYYMMDDHH24MISS')>'{{ env.LAST_UPD }}'\n";
			}
		}
		return "";
	}

	String outputDmdl() {
		StringBuffer sb = new StringBuffer();
		sb.append("\"" + tableName + "\"\n");
		sb.append("@namespace(value = " + DMDL_NAME_SPACE + ")\n");
		sb.append("@windgate.jdbc.table(name = \"" + tableName + "\")\n");
		sb.append("@directio.csv\n");
		sb.append(tableName.toLowerCase() + " = {\n");
		for (String[] dat : colList) {
			if (convDmdl(dat[2]) != null) {
				sb.append( //
						"\t\t\"" + dat[1] + "\"\n" + //
								"\t\t@windgate.jdbc.column(name = \"" + dat[1] + "\")\n" + //
								"\t\t" + dat[1].toLowerCase() + " : " + convDmdl(dat[2]) + ";\n\n");
			}
		}
		sb.append("};");
		return sb.toString();
	}

	public List<String[]> getColList() {
		return colList;
	}

	public void setColList(List<String[]> colList) {
		this.colList = colList;
	}

	private String convDmdl(String org) {
		if (!dmdlMap.containsKey(org)) {
			logger.warn("Skip column. Illegal column type : " + org);
			return null;
		}
		return dmdlMap.get(org);
	}

	private void getMeta(String tableName) throws Exception {
		Connection connection = MyJdbcConnection.getInstance().getCon();

		String[] COLNAMES = { "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "DATA_SCALE" };

		Statement statement = (Statement) connection.createStatement();
		ResultSet result = null;
		result = statement.executeQuery(
				"select t.TABLE_NAME,t.COLUMN_NAME,DATA_TYPE,DATA_SCALE,COMMENTS " + "from USER_TAB_COLUMNS t "
						+ "join USER_COL_COMMENTS c on t.TABLE_NAME=c.TABLE_NAME and t.COLUMN_NAME=c.COLUMN_NAME "
						+ "where t.TABLE_NAME='" + tableName + "' " + "order by COLUMN_ID ");
		while (result.next()) {
			String[] dat = new String[COLNAMES.length];
			int i = 0;
			for (String col : COLNAMES) {
				dat[i++] = result.getString(col);
			}
			colList.add(dat);
		}
	}

	private String convType(String colName, String org, String scale) {
		if (!typeMap.containsKey(org)) {
			return null;
		}
		if (scale != null) {
			if (org.startsWith("TIMESTAMP")) {
				return "timestamp, format: '%Y-%m-%d %k:%M:%S'";
			} else if (Integer.parseInt(scale) > 0) {
				return "double";
			}
		}
		if ("WRITE_DATE".equalsIgnoreCase(colName)) {
			return "timestamp, format: '%Y-%m-%d %k:%M:%S'";
		}
		if ("UPD_DATE".equalsIgnoreCase(colName)) {
			return "timestamp, format: '%Y-%m-%d %k:%M:%S'";
		}
		if ("NEW_DATE".equalsIgnoreCase(colName)) {
			return "timestamp, format: '%Y-%m-%d %k:%M:%S'";
		}
		// NEW_DATE
		return typeMap.get(org);
	}
}
