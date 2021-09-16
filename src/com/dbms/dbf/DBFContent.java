package com.dbms.dbf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.linuxense.javadbf.DBFField;

public class DBFContent {
	private String tableName;
	private int fieldCount, recordCount;
	private List<Map<String, Object>> contents;
	private List<DBFField> fields;
	
	public DBFContent() {
		contents = new ArrayList<Map<String,Object>>();
		fields = new ArrayList<DBFField>();
	}
	
	public DBFContent(int fieldCount, int recordCount,
			List<Map<String, Object>> contents, List<DBFField> fields) {
		this.fieldCount = fieldCount;
		this.recordCount = recordCount;
		this.contents = contents;
		this.fields = fields;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public int getFieldCount() {
		return fieldCount;
	}

	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public List<Map<String, Object>> getContents() {
		return contents;
	}

	public void setContents(List<Map<String, Object>> contents) {
		this.contents = contents;
	}

	public List<DBFField> getFields() {
		return fields;
	}

	public void setFields(List<DBFField> fields) {
		this.fields = fields;
	}
	
	@Override
	public String toString() {
		return "Data [fieldCount=" + fieldCount + ", recordCount="
				+ recordCount + ", contents=" + contents + ", fields=" + fields
				+ "]";
	}

	public boolean containField(String key) {
		for (DBFField field : fields) {
			if (field.getName().equals(key))
				return true;
		}
		return false;
	}
	

}
