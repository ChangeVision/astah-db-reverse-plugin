package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;

public class DBToProjectTest {
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public final void testImportToProject_空ファイルを指定しても落ちないこと() throws LicenseNotFoundException, ProjectLockedException, InvalidEditingException, Throwable {
		DBToProject dbToProject = new DBToProject();
		File projectFile = folder.newFile("test.asta");
		String projectFilePath = projectFile.getAbsolutePath();
		List<TableInfo> tables = new ArrayList<TableInfo>();
		List<ERRelationshipInfo> relationships = new ArrayList<ERRelationshipInfo>();

		dbToProject.importToProject(projectFilePath, tables, relationships);
	}
	
	@Test
	public final void testImportToProject_空のastaファイルを指定しても落ちないこと() throws LicenseNotFoundException, ProjectLockedException, InvalidEditingException, Throwable {
		DBToProject dbToProject = new DBToProject();
		List<TableInfo> tables = new ArrayList<TableInfo>();
		List<ERRelationshipInfo> relationships = new ArrayList<ERRelationshipInfo>();
		
		URL resource = getClass().getResource("empty.asta");

		String projectFilePath = resource.getFile();
		dbToProject.importToProject(projectFilePath , tables, relationships);
	}

}