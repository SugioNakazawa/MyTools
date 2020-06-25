package com.hoge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyJdbcConnection {
	static Logger logger = LoggerFactory.getLogger(MyJdbcConnection.class);
	// connection database
	static String JDBC_DRIVER = System.getProperty("JDBC_DRIVER", "oracle.jdbc.driver.OracleDriver");
	static String JDBC_URL = System.getProperty("JDBC_URL", "jdbc:oracle:thin:@");
	static String DB_HOST = System.getProperty("DB_HOST", "localhost");
	static String DB_PORT = System.getProperty("DB_PORT", "1521");
	static String DB_SID = System.getProperty("DB_SID", "xe");
	static String DB_USR = System.getProperty("DB_USR", "usr1");
	static String DB_PASS = System.getProperty("DB_PASS", "pass1");

	static private MyJdbcConnection instance;

	/**
	 * 型マッピング：ORACLE<=>EMBULK
	 */
	private Map<String, String> typeMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("CHAR", "string"); //
			put("NCHAR", "string"); //
			put("VARCHAR2", "string"); //
			put("NVARCHAR2", "string"); //
			put("CLOB", "string"); //
			put("DATE", "timestamp, format: '%Y-%m-%d'"); //
			put("TIMESTAMP(6)", "timestamp, format: '%Y-%m-%d %k:%M:%S'"); //
			put("NUMBER", "double"); //

		}
	};
	/**
	 * 型マッピング：ORACLE<=>DMDL
	 */
	private Map<String, String> dmdlMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("CHAR", "TEXT"); //
			put("NCHAR", "TEXT"); //
			put("VARCHAR2", "TEXT"); //
			put("NVARCHAR2", "TEXT"); //
			put("CLOB", "TEXT"); //
			put("DATE", "DATE"); //
			put("TIMESTAMP(6)", "DATETIME"); //
			put("TIMESTAMP", "DATETIME"); //
			put("NUMBER", "DECIMAL"); //
		}
	};

	private Connection con;
	/** columns list from dbms **/
	private List<ColumnRec> colList2;

	static public MyJdbcConnection getInstance() {
		if (instance == null) {
			instance = new MyJdbcConnection();
		}
		return instance;
	}

	private MyJdbcConnection() {
		try {
			Class.forName(MyJdbcConnection.JDBC_DRIVER);
			con = DriverManager.getConnection(MyJdbcConnection.JDBC_URL + MyJdbcConnection.DB_HOST + ":"
					+ MyJdbcConnection.DB_PORT + ":" + MyJdbcConnection.DB_SID, MyJdbcConnection.DB_USR,
					MyJdbcConnection.DB_PASS);
			colList2 = new ArrayList<ColumnRec>();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() {
		return con;
	}

	public void getMeta(String tableName) throws Exception {
		if (colList2.size() > 0) {
			colList2.clear();
		}
		Statement statement = (Statement) con.createStatement();
		ResultSet result = null;
		result = statement.executeQuery(
				"select t.TABLE_NAME,t.COLUMN_NAME,DATA_TYPE,DATA_SCALE,COMMENTS " + "from USER_TAB_COLUMNS t "
						+ "join USER_COL_COMMENTS c on t.TABLE_NAME=c.TABLE_NAME and t.COLUMN_NAME=c.COLUMN_NAME "
						+ "where t.TABLE_NAME='" + tableName + "' " + "order by COLUMN_ID ");
		while (result.next()) {
			ColumnRec cRec = new ColumnRec(result.getString("COLUMN_NAME"), result.getString("DATA_TYPE"),
					result.getInt("DATA_SCALE"));
			cRec.setEmbType(convType(result.getString("DATA_TYPE"), result.getString("DATA_SCALE")));
			cRec.setDmdlType(convDmdl(result.getString("DATA_TYPE")));
			colList2.add(cRec);
		}
		if (colList2.size() < 1) {
			String errMsg = "no data table=" + tableName;
			throw new RuntimeException(errMsg);
		}
	}
	/**
	 * postgres用カラム取得 table=sample
	 * db/user/password test01
	 * select column_name,data_type,numeric_precision,numeric_scale from information_schema.columns where table_name='users' order by ordinal_position;
	 */
	/**
	 * @return the colList
	 */
	public List<ColumnRec> getColList2() {
		return colList2;
	}

	/**
	 * @param colList the colList to set
	 * @deprecated
	 */
	public void setColList(List<String[]> colList) {
//		this.colList = colList;
	}

	private String convType(String org, String scale) {
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
		// depend on column name
//		if ("DATE_COLUMN".equalsIgnoreCase(colName)) {
//			return "timestamp, format: '%Y-%m-%d %k:%M:%S'";
//		}
		return typeMap.get(org);
	}

	private String convDmdl(String org) {
		if (!dmdlMap.containsKey(org)) {
			return null;
		}
		return dmdlMap.get(org);
	}

}
