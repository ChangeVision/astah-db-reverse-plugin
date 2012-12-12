package com.change_vision.astah.extension.plugin.dbreverse.actions;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;
import com.change_vision.astah.extension.plugin.dbreverse.util.AstahAPIUtil;
import com.change_vision.astah.extension.plugin.dbreverse.view.ReverseDialog;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

public class ReverseAction implements IPluginActionDelegate {
	
	private AstahAPIUtil apiUtil  = new AstahAPIUtil();

	public Object run(IWindow window) throws UnExpectedException {
	    if (apiUtil.isNewModel()) {
			int result = JOptionPane.showConfirmDialog(
			        window.getParent(), 
			        Messages.getMessage("warning_message.save_before_reverse_warning"), 
			        Messages.getMessage("dialog.title.confirm"),
			        JOptionPane.YES_NO_CANCEL_OPTION);
			switch (result) {
				case JOptionPane.YES_OPTION:
				    boolean saved = apiUtil.saveProject(window);
				    if(saved == false){
				        return null;
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