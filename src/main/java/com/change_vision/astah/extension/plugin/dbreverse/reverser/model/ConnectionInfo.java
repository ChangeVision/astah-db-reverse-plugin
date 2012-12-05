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

    @Override
    public String toString() {
        return "ConnectionInfo [name=" + name + ", jdbcurl=" + jdbcurl + ", classname=" + classname
                + ", pathfile=" + pathfile + ", login=" + login + ", password=" + password + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classname == null) ? 0 : classname.hashCode());
        result = prime * result + ((jdbcurl == null) ? 0 : jdbcurl.hashCode());
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((pathfile == null) ? 0 : pathfile.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ConnectionInfo other = (ConnectionInfo) obj;
        if (classname == null) {
            if (other.classname != null) return false;
        } else if (!classname.equals(other.classname)) return false;
        if (jdbcurl == null) {
            if (other.jdbcurl != null) return false;
        } else if (!jdbcurl.equals(other.jdbcurl)) return false;
        if (login == null) {
            if (other.login != null) return false;
        } else if (!login.equals(other.login)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (password == null) {
            if (other.password != null) return false;
        } else if (!password.equals(other.password)) return false;
        if (pathfile == null) {
            if (other.pathfile != null) return false;
        } else if (!pathfile.equals(other.pathfile)) return false;
        return true;
    }
    
}