package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;
import com.change_vision.astah.extension.plugin.dbreverse.internal.progress.LogProggressMonitor;
import com.change_vision.astah.extension.plugin.dbreverse.internal.progress.ProgressMonitor;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ERRelationshipInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.TableInfo;
import com.change_vision.astah.extension.plugin.dbreverse.util.AstahAPIWrapper;
import com.change_vision.astah.extension.plugin.dbreverse.util.DatabaseTypes;
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.model.IModel;

public class ImportToProject {

    private static final Logger logger = LoggerFactory.getLogger(ImportToProject.class);

    private DBReader reader;
    private ProgressMonitor monitor;
    private AstahAPIWrapper util = new AstahAPIWrapper();

    ImportToProject(DBReader reader) {
        this(reader,new LogProggressMonitor());
    }
    
    public ImportToProject(DBReader reader, ProgressMonitor monitor){
        this.reader = reader;
        this.monitor = monitor;
    }

    public boolean doImport(String currentDBType, String schema) {
        monitor.showMessage(Messages.getMessage("message.database.importing"));
        List<TableInfo> tables;
        try {
            tables = getTables(currentDBType,schema);
        } catch (SQLException e) {
            monitor.showErrorMessage(e);
            logger.error("Error has occured when scanning table",e);
            return false;
        }
        List<ERRelationshipInfo> relationships;
        try {
            relationships = getRelationships(currentDBType, tables);
        } catch (SQLException e) {
            monitor.showErrorMessage(e);
            logger.error("Error has occured when scanning relationship",e);
            return false;
        }
        String temporaryProjectFilePath = null;
        try {
            temporaryProjectFilePath = getTemporaryProjectFilePath();
        } catch (IOException e) {
            return errorHandling(e);
        }
        boolean needMerge = util.isExistedModel();
        String currentProjectPath = util.getProjectPath();
        try {
            util.createProject(temporaryProjectFilePath);
        } catch (IOException e) {
            return errorHandling(e);
        }
        IModel temporaryModel = util.getProjectModel();
        ERModelEditor editor = util.getERModelEditor();
        DBToProject dbtj = new DBToProject(editor, monitor);
        try {
            dbtj.importToProject(temporaryModel, tables, relationships);
        } catch (InvalidEditingException e) {
            return errorHandling(e);
        }
        if ( needMerge ) {
            try {
                mergeExistProject(currentProjectPath, temporaryProjectFilePath);
            } catch (LicenseNotFoundException e) {
                return errorHandling(e);
            } catch (ProjectLockedException e) {
                return errorHandling(e);
            } catch (IOException e) {
                return errorHandling(e);
            }
        }
        return true;
    }

    private boolean errorHandling(Exception e) {
        monitor.showErrorMessage(e);
        logger.error(e.getMessage(), e);
        return false;
    }

    private void mergeExistProject(String currentProjectPath, String temporaryProjectFilePath) throws LicenseNotFoundException, IOException, ProjectLockedException {
        util.save();
        util.closeProject();
        util.addProjectEventListener(new ProjectOpenedEasyMergeLisetener(monitor,currentProjectPath,temporaryProjectFilePath));
        util.open(currentProjectPath);
    }

    private List<TableInfo> getTables(String currentDBType, String schema) throws SQLException {
        if (isScanCategoryType(currentDBType)) {
            return reader.getTables(schema, null);
        } else {
            return reader.getTables(null, schema);
        }
    }

    private List<ERRelationshipInfo> getRelationships(String currentDBType, List<TableInfo> tables) throws SQLException {
        return reader.getRelationships(tables);
    }

    private boolean isScanCategoryType(String currentDBType) {
        return DatabaseTypes.MYSQL.selected(currentDBType)
                || DatabaseTypes.MSSQLSERVER.selected(currentDBType)
                || DatabaseTypes.OTHERS_CATEGORY.selected(currentDBType);
    }

    private String getTemporaryProjectFilePath() throws IOException {
        Calendar cal = Calendar.getInstance();
        StringBuilder tempFileName = new StringBuilder(Integer.toString(cal.get(Calendar.YEAR)))
                .append(Integer.toString(cal.get(Calendar.MONTH) + 1))
                .append(Integer.toString(cal.get(Calendar.DATE)))
                .append(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)))
                .append(Integer.toString(cal.get(Calendar.MINUTE)))
                .append(Integer.toString(cal.get(Calendar.SECOND)));
        File tempFile = File.createTempFile(tempFileName.toString(), ".asta");
        tempFile.deleteOnExit();
        return tempFile.getAbsolutePath();
    }
}