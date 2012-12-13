package com.change_vision.astah.extension.plugin.dbreverse.util;

public enum DatabaseTypes {

	ORACLE("Oracle"),
	MYSQL("MySQL"),
	MSSQLSERVER("MS SQLServer"),
	POSTGRES("PostgreSQL"),
	HSQL("HSQLDB"),
    H2("H2 Database Engine"),
    HiRDB("HiRDB"),
    OTHERS_SCHEMA("Others[set schema]"),
    OTHERS_CATEGORY("Others[set category]");

    private String type;
	
	private DatabaseTypes(String type){
	    this.type = type;
	}
	
	public String getType() {
        return type;
    }

    public boolean selected(String dbType) {
        return getType().equalsIgnoreCase(dbType);
    }
}