package com.hoge;

public class ColumnRec {
	private String columnName;
	private String dbType;
	private Integer dbScale;
	private String jdbcType;
	private String dmdlType;
	private String embType;

	public ColumnRec(String columnName, String dbType, Integer dbScale) {
		super();
		this.columnName = columnName;
		this.dbType = dbType;
		this.dbScale = dbScale;
	}

	/**
	 * @return the jdbcType
	 */
	public String getJdbcType() {
		return jdbcType;
	}

	/**
	 * @param jdbcType the jdbcType to set
	 */
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	/**
	 * @return the dmdlType
	 */
	public String getDmdlType() {
		return dmdlType;
	}

	/**
	 * @param dmdlType the dmdlType to set
	 */
	public void setDmdlType(String dmdlType) {
		this.dmdlType = dmdlType;
	}

	/**
	 * @return the embType
	 */
	public String getEmbType() {
		return embType;
	}

	/**
	 * @param embType the embType to set
	 */
	public void setEmbType(String embType) {
		this.embType = embType;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * @return the dbScale
	 */
	public Integer getDbScale() {
		return dbScale;
	}

	public String toString(String dlmt) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.columnName);
		sb.append(dlmt);
		sb.append(this.dbType);
		sb.append(dlmt);
		sb.append(this.dbScale);
		sb.append(dlmt);
		sb.append(this.jdbcType);
		sb.append(dlmt);
		sb.append(this.dmdlType);
		sb.append(dlmt);
		sb.append(this.embType);
		sb.append("\n");

		return sb.toString();
	}
}
