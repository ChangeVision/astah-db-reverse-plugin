package com.change_vision.astah.extension.plugin.dbreverse.reverser.model;

public class ConnectionInfo {
	/** Driver name */
	private String name;
	/** Example of a JDBC URL */
	private String jdbcurl;
	/** Complete class name : package + class */
	private String classname;
	/** Path where the library is located */
	private String pathfile;

	private String login;

	private String password;

	public String getJdbcurl() {
		return jdbcurl;
	}

	public void setJdbcurl(String url) {
		this.jdbcurl = url;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getPathfile() {
		return pathfile;
	}

	public void setPathfile(String pathfile) {
		this.pathfile = pathfile;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}