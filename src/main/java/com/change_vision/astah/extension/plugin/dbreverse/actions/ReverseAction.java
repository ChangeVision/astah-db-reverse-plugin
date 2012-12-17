package com.change_vision.astah.extension.plugin.dbreverse.actions;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;
import com.change_vision.astah.extension.plugin.dbreverse.util.AstahAPIWrapper;
import com.change_vision.astah.extension.plugin.dbreverse.view.ReverseDialog;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

public class ReverseAction implements IPluginActionDelegate {
	
	private AstahAPIWrapper apiWrapper  = new AstahAPIWrapper();

	public Object run(IWindow window) throws UnExpectedException {
	    
	    if(apiWrapper.isProjectOpened() == false){
	        apiWrapper.create();
	    }
	    if(apiWrapper.isProjectModified()){
	        int result = JOptionPane.showConfirmDialog(
	                window.getParent(), 
	                Messages.getMessage("warning_message.save_before_reverse_warning"), 
	                Messages.getMessage("dialog.title.confirm"),
	                JOptionPane.YES_NO_OPTION);
	        switch (result) {
	            case JOptionPane.YES_OPTION:
	            try {
	                apiWrapper.save();
	            } catch (LicenseNotFoundException e) {
	                JOptionPane.showMessageDialog(window.getParent(),
	                        Messages.getMessage("message.license_not_found"), Messages.getMessage("dialog.title.warning"),
	                        JOptionPane.ERROR_MESSAGE);
	                return null;
	            } catch (ProjectLockedException e) {
	                JOptionPane.showMessageDialog(window.getParent(),
	                        Messages.getMessage("message.project_lock"), Messages.getMessage("dialog.title.error"),
	                        JOptionPane.WARNING_MESSAGE);
	                return null;
	            } catch (IOException e) {
	                JOptionPane.showMessageDialog(window.getParent(),e.getMessage(), Messages.getMessage("dialog.title.error"),
	                        JOptionPane.WARNING_MESSAGE);
	            }
	                break;
	            case JOptionPane.NO_OPTION:
	                break;
	            case JOptionPane.CANCEL_OPTION:
	                return null;
	            default:
	                return null;
	        }
	    }

		JFrame frame = (JFrame) window.getParent();
		ReverseDialog.getInstance(frame).showView();
		return null;
	}
}