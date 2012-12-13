package com.change_vision.astah.extension.plugin.dbreverse.internal.progress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LogProggressMonitor implements ProgressMonitor {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(LogProggressMonitor.class);
    
    @Override
    public void showMessage(String message) {
        logger.debug(message);
    }
    
    @Override
    public void showErrorMessage(Throwable e) {
        logger.error("Exception is occurred.",e);
    }
}
