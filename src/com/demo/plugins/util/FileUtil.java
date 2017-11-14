package com.demo.plugins.util;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.demo.plugins.constants.StringConstants;
import com.demo.plugins.exceptions.UnexpectedException;

import java.io.File;

public class FileUtil {
    public static VirtualFile[] getAllOpenFiles(Project project)
            throws UnexpectedException {
        VirtualFile[] openFiles = null;
        if (project != null) {
            FileEditorManager fileEditMan = FileEditorManager.getInstance(project);
            if (fileEditMan != null) {
                openFiles = fileEditMan.getOpenFiles();
            }
        } else {
            throw new UnexpectedException(StringConstants.NO_PROJECT_OPENED);
        }
        return openFiles;
    }


    public static boolean isJavaFile(VirtualFile vFile) {
        boolean result = false;
        if (vFile != null) {
            String ext = vFile.getExtension();
            if ((ext != null) && (ext.equalsIgnoreCase("java"))) {
                result = true;
            }
        }
        return result;
    }


//    public static String getProjectClassPath(Module module) {
//        VirtualFile vFile = ModuleRootManager.getInstance(module).getCompilerOutputPath();
//        if (vFile != null) {
//            return vFile.getPath();
//        }
//        return "";
//    }


    public static String getProjectSrcPath(Module module) {
        VirtualFile vFile = ModuleRootManager.getInstance(module).getSourceRoots()[0];
        if (vFile != null) {
            return vFile.getPath();
        }
        return "";
    }


    public static String getProjectHomePath(Module module) {
        VirtualFile vFile = ModuleRootManager.getInstance(module).getContentRoots()[0];
        if (vFile != null) {
            return vFile.getPath();
        }
        return "";
    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}

