/**
 * 
 */
package com.sogou.bizdev.compass.core.router;

/**
 * @Description: 路由结果的唯一标示，标识路由之后的库信息和表信息,支持分库之后再分表
 * @author zjc
 * @since 1.0.0
 */
public class TableContext 
{
	private String masterSlaveDataSourceId;
	/**
	 * 如果不分表可以为null
	 */
	private String dbIndex;
	/**
	 * 如果不分表可以为null
	 */
	private String tableIndex;
	
	

	public TableContext(String masterSlaveDataSourceId, String dbIndex,String tableIndex)
	{
		this.masterSlaveDataSourceId = masterSlaveDataSourceId;
		this.dbIndex = dbIndex;
		this.tableIndex = tableIndex;
	}

	public TableContext(String dbId, String tableId) 
	{
		this.dbIndex = dbId;
		this.tableIndex = tableId;
	}

	public String getDbIndex() {
		return dbIndex;
	}

	public void setDbIndex(String dbId) {
		this.dbIndex = dbId;
	}

	public String getTableIndex() {
		return tableIndex;
	}

	public void setTableIndex(String tableId) {
		this.tableIndex = tableId;
	}

	public String getMasterSlaveDataSourceId() {
		return masterSlaveDataSourceId;
	}

	public void setMasterSlaveDataSourceId(String masterSlaveDataSourceId) {
		this.masterSlaveDataSourceId = masterSlaveDataSourceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbIndex == null) ? 0 : dbIndex.hashCode());
		result = prime
				* result
				+ ((masterSlaveDataSourceId == null) ? 0
						: masterSlaveDataSourceId.hashCode());
		result = prime * result
				+ ((tableIndex == null) ? 0 : tableIndex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableContext other = (TableContext) obj;
		if (dbIndex == null) {
			if (other.dbIndex != null)
				return false;
		} else if (!dbIndex.equals(other.dbIndex))
			return false;
		if (masterSlaveDataSourceId == null) {
			if (other.masterSlaveDataSourceId != null)
				return false;
		} else if (!masterSlaveDataSourceId
				.equals(other.masterSlaveDataSourceId))
			return false;
		if (tableIndex == null) {
			if (other.tableIndex != null)
				return false;
		} else if (!tableIndex.equals(other.tableIndex))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TableContext [masterSlaveDataSourceId="
				+ masterSlaveDataSourceId + ", dbIndex=" + dbIndex
				+ ", tableIndex=" + tableIndex + "]";
	}

	

}
