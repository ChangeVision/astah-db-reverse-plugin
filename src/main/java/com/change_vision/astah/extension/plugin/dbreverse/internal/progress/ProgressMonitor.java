package com.change_vision.astah.extension.plugin.dbreverse.internal.progress;

public interface ProgressMonitor {
    
    public void showMessage(String message);
    
    public void showErrorMessage(Throwable e);

}
