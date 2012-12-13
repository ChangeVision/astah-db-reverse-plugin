package com.change_vision.astah.extension.plugin.dbreverse.internal.progress;

import com.change_vision.astah.extension.plugin.dbreverse.util.DBReverseUtil;

public final class MessageAreaNotifier implements ProgressMonitor {
    @Override
    public void showMessage(String message) {
        DBReverseUtil.showMessage(message);
        
    }
    
    @Override
    public void showErrorMessage(Throwable e) {
        DBReverseUtil.showErrorMessage(e);
    }
}
