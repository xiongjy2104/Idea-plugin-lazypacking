package com.demo.plugins.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.demo.plugins.constants.StringConstants;
import com.demo.plugins.exceptions.UnexpectedException;

public class ActionUtil {
    public static Project getProjectFromAction(AnActionEvent event)
            throws UnexpectedException {
        Project projectToReturn = null;
        if (event != null) {
            projectToReturn = (Project) event.getDataContext().getData("project");
        } else {
            throw new UnexpectedException(StringConstants.NO_PROJECT_OPENED);
        }
        return projectToReturn;
    }

    public static Module getModuleFromAction(AnActionEvent event)
            throws UnexpectedException {
        Module moduleToReturn = null;
        if (event != null) {
            moduleToReturn = (Module) event.getDataContext().getData("module");
        } else {
            throw new UnexpectedException(StringConstants.NO_PROJECT_OPENED);
        }
        return moduleToReturn;
    }

    public static String getExceptionMessage(Exception ex) {
        String exceptionStr = StringConstants.UNEXPECTED_ERROR;
        if ((ex != null) && (
                (ex.getMessage() == null) || (ex.getMessage().equalsIgnoreCase("")))) {
            exceptionStr = ex.getMessage();
        }

        return exceptionStr;
    }
}
