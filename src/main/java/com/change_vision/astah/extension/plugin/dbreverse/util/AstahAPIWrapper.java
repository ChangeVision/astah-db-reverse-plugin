package com.change_vision.astah.extension.plugin.dbreverse.util;

import java.io.IOException;

import javax.swing.JFrame;

import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.NonCompatibleException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.view.IViewManager;

public class AstahAPIWrapper {
    
    private static final String DEFAULT_NEW_MODEL_NAME = "no_title";
    
    public ProjectAccessor getProjectAccessor(){
        try {
            return ProjectAccessorFactory.getProjectAccessor();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Maybe it does not contain astah-api.jar in the classpath.");
        }
    }
    
    public void createProject(String path) throws IOException{
        getProjectAccessor().create(path);
    }
    
    public IModel getProjectModel(){
        try {
            return getProjectAccessor().getProject();
        } catch (ProjectNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void easyMerge(String path){
        try {
            getProjectAccessor().easyMerge(path, true);
        } catch (ProjectNotFoundException e) {
            throw new IllegalStateException(e);
        } catch (InvalidEditingException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public IViewManager getViewManager(){
        ProjectAccessor projectAccessor = getProjectAccessor();
        if (projectAccessor == null) return null;
        try {
            return projectAccessor.getViewManager();
        } catch (InvalidUsingException e) {
            throw new IllegalStateException("Error has occured.",e);
        }
    }
    
    public JFrame getMainFrame(){
        IViewManager viewManager = getViewManager();
        if(viewManager == null) return null;
        return viewManager.getMainFrame();

    }
    
    public String getEdition() {
        return getProjectAccessor().getAstahEdition();
    }
    
    public String getProjectPath(){
        try {
            return getProjectAccessor().getProjectPath();
        } catch (ProjectNotFoundException e) {
            return DEFAULT_NEW_MODEL_NAME;
        }
    }
    
    public boolean isEditing(){
        return getMainFrame().getTitle().contains("*");
    }
    
    public boolean isNewModel(){
        return DEFAULT_NEW_MODEL_NAME.equals(getProjectPath());
    }

    public boolean isExistedModel(){
        return ! isNewModel();
    }

    public void addProjectEventListener(ProjectEventListener listener){
        getProjectAccessor().addProjectEventListener(listener);
    }
    
    public void removeProjectEventListener(ProjectEventListener listener) {
        getProjectAccessor().removeProjectEventListener(listener);
    }

    public ERModelEditor getERModelEditor() {
        try {
            return ModelEditorFactory.getERModelEditor();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        } catch (InvalidEditingException e) {
            throw new IllegalStateException(e);
        }
    }

    public void save() throws LicenseNotFoundException, IOException, ProjectLockedException {
        try {
            getProjectAccessor().save();
        } catch (ProjectNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public void open(String path) throws LicenseNotFoundException, IOException, ProjectLockedException {
        try {
            getProjectAccessor().open(path);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        } catch (NonCompatibleException e) {
            throw new IllegalStateException(e);
        } catch (ProjectNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public void closeProject() {
        getProjectAccessor().close();
    }

    public void saveAs(String currentProjectPath)  throws LicenseNotFoundException, IOException, ProjectLockedException {
        try {
            getProjectAccessor().saveAs(currentProjectPath);
        } catch (ProjectNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

}
