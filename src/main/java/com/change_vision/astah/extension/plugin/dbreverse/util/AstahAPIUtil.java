package com.change_vision.astah.extension.plugin.dbreverse.util;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.ui.IWindow;

public class AstahAPIUtil {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(AstahAPIUtil.class);
    
    private IWindow window;
    
    public AstahAPIUtil(IWindow window){
        this.window = window;
    }
    
    public ProjectAccessor getProjectAccessor(){
        try {
            return ProjectAccessorFactory.getProjectAccessor();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Maybe it does not contain astah-api.jar in the classpath.");
        }
    }
    
    public String getEdition() {
        return getProjectAccessor().getAstahEdition();
    }

    public boolean saveProject(){
        try {
            getProjectAccessor().save();
        } catch (LicenseNotFoundException e) {
            JOptionPane.showMessageDialog(window.getParent(),
                    Messages.getMessage("message.license_not_found"), Messages.getMessage("dialog.title.warning"),
                    JOptionPane.ERROR_MESSAGE);
            logger.error(e.getMessage(), e);
            return false;
        } catch (ProjectLockedException e) {
            JOptionPane.showMessageDialog(window.getParent(),
                    Messages.getMessage("message.project_lock"), Messages.getMessage("dialog.title.error"),
                    JOptionPane.WARNING_MESSAGE);
        } catch (ProjectNotFoundException e) {
            JOptionPane.showMessageDialog(window.getParent(),
                    Messages.getMessage("message.project_not_open"), Messages.getMessage("dialog.title.warning"),
                    JOptionPane.WARNING_MESSAGE);
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            // 保存の取り消し
        }
        return true;
    }

}
