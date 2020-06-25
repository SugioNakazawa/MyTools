/**
 *
 */
package com.hoge;

import java.util.List;

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

	private String tableName;

	enum Type {
		DMDL, TOCSV, TODB
	};

	MyJdbcConnection myJdbcConnection;

	private String output;

	/**
	 * @param args [0] tablename
	 * @param args [1] type
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String tableName = args[0];
		Type type = null;
		for (Type t : Type.values()) {
			if (t.name().equalsIgnoreCase(args[1])) {
				type = t;
			}
		}
		if (type == null) {
			throw new RuntimeException("Illegual type.DMDL, TOCSV, TODB.");
		}
		CreateDataDef target = new CreateDataDef();

		target.exec(tableName, type);
	}

	public CreateDataDef() {
		this.myJdbcConnection = MyJdbcConnection.getInstance();
	}

	void exec(String tableName,Type type) throws Exception {
		this.tableName = tableName;
		myJdbcConnection.getMeta(tableName.toUpperCase());
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
		// select
		sb.append("  select: \"");
		StringBuffer cols = new StringBuffer();
		for (ColumnRec dat : myJdbcConnection.getColList2()) {
			if (dat.getEmbType() != null) {
				if (cols.length() > 0) {
					cols.append(",");
				}
				cols.append(dat.getColumnName());
			}
		}
		sb.append(cols.toString());
		sb.append("\"\n");
		//
		sb.append(convWhere());
		sb.append("  columns:\n");
		//
		for (ColumnRec dat : myJdbcConnection.getColList2()) {
			if (dat.getEmbType() != null) {
				sb.append("  - {name: " + dat.getColumnName().toUpperCase() + ", type: " + dat.getEmbType() + "}\n");
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
		for (ColumnRec dat : myJdbcConnection.getColList2()) {
			if (dat.getEmbType() != null) {
				sb.append("    - {name: " + dat.getColumnName().toUpperCase() + ", type: " + dat.getEmbType() + "}\n");
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

	private String convWhere() {
//		if (colList.size() > 0) {
//			if ("LAST_MODIFIED".equalsIgnoreCase(this.colList.get(colList.size() - 1)[1])) {
//				return "  where: to_char(LAST_MODIFIED,'YYYYMMDDHH24MISS')>'{{ env.LAST_UPD }}'\n";
//			}
//		}
		return "";
	}

	String outputDmdl() {
		StringBuffer sb = new StringBuffer();
		sb.append("\"" + tableName + "\"\n");
		sb.append("@namespace(value = " + DMDL_NAME_SPACE + ")\n");
		sb.append("@windgate.jdbc.table(name = \"" + tableName + "\")\n");
		sb.append("@directio.csv\n");
		sb.append(tableName.toLowerCase() + " = {\n");
		for (ColumnRec dat : myJdbcConnection.getColList2()) {
			if (dat.getDmdlType() != null) {
				sb.append( //
						"\t\t\"" + dat.getColumnName() + "\"\n" + //
								"\t\t@windgate.jdbc.column(name = \"" + dat.getColumnName() + "\")\n" + //
								"\t\t" + dat.getColumnName().toLowerCase() + " : " + dat.getDmdlType() + ";\n\n");
			}
		}
		sb.append("};");
		return sb.toString();
	}

//	public List<ColumnRec> getColList2() {
//		return myJdbcConnection.getColList2();
//	}

//	public void setColList(List<String[]> colList) {
//		myJdbcConnection.setColList(colList);
//	}
}
