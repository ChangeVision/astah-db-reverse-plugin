package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.dbreverse.internal.progress.ProgressMonitor;
import com.change_vision.astah.extension.plugin.dbreverse.util.AstahAPIWrapper;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;

public final class ProjectOpenedEasyMergeLisetener implements ProjectEventListener {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(ProjectOpenedEasyMergeLisetener.class);

    private String temporaryProjectFilePath;

    private AstahAPIWrapper util = new AstahAPIWrapper();

    private ProgressMonitor monitor;

    private String currentProjectFilePath;
    
    public ProjectOpenedEasyMergeLisetener(ProgressMonitor monitor, String currentProjectFilePath, String temporaryProjectFilePath) {
        this.monitor = monitor;
        this.currentProjectFilePath = currentProjectFilePath;
        this.temporaryProjectFilePath = temporaryProjectFilePath;
    }

    @Override
    public void projectChanged(ProjectEvent paramProjectEvent) {
    }

    @Override
    public void projectClosed(ProjectEvent paramProjectEvent) {
    }

    @Override
    public void projectOpened(ProjectEvent paramProjectEvent) {
        try {
            if (temporaryProjectFilePath != null) {
                util.easyMerge(temporaryProjectFilePath);
            }
            util.saveAs(currentProjectFilePath);
        } catch (LicenseNotFoundException e) {
            monitor.showErrorMessage(e);
            logger.error("error has occurred.",e);
        } catch (IOException e) {
            monitor.showErrorMessage(e);
            logger.error("error has occurred.",e);
        } catch (ProjectLockedException e) {
            monitor.showErrorMessage(e);
            logger.error("error has occurred.",e);
        } finally {
            util.removeProjectEventListener(this);
        }
    }
}
