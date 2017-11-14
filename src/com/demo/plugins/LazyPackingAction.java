package com.demo.plugins;

import com.demo.plugins.util.ActionUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileFilter;
import com.intellij.openapi.vfs.newvfs.VirtualFileFilteringListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class LazyPackingAction
        extends AnAction {
    private static final Logger logger = Logger.getInstance(LazyPackingAction.class.getName());

    public void actionPerformed(AnActionEvent e) {
        itemClicked(e);
    }

    public void itemClicked(AnActionEvent event) {
        try {
            final Project project = ActionUtil.getProjectFromAction(event);
            final ProjectRootManager prm = ProjectRootManager.getInstance(project);


            final Pattern pattern = Pattern.compile("$.+?//.(idea|git|metadata)");   ;
            final VirtualFileFilter filter =  null;
//                    new VirtualFileFilter() {
//                @Override
//                public boolean accept(@NotNull VirtualFile file) {
//                    return !pattern.matcher(file.getName()).matches();
//                }
//            };


            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                public void run() {

                    final List<VirtualFile> contents = new ArrayList<VirtualFile>();
                    for (VirtualFile file : prm.getContentRoots()) {
                        VfsUtil.iterateChildrenRecursively(file, filter, new ContentIterator() {
                            @Override
                            public boolean processFile(VirtualFile file) {
//                        if (".idea".equals(file.getName()) || ".git".equals(file.getName()) || ".metadata".equals(file.getName()))
//                            return true;
                                if (!file.isDirectory()) {
                                    contents.add(file);
                                    logger.info(file.getPath());
                                }
                                return true;
                            }
                        });
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String zipPath = project.getBasePath() + sdf.format(new Date())+ ".zip";
                    File basePath=new File(project.getBasePath());
                    String baseUrl=".";
                    try{
                        baseUrl=basePath.toURI().toURL().toString();
                    }catch (Exception e){
                        baseUrl= "file://"+project.getBasePath();
                    }

                    writeZip(zipPath, baseUrl, contents);
                    contents.clear();
                    logger.info("contents="+contents.size());
                }
            });

        } catch (Exception exception) {
            Messages.showErrorDialog(ActionUtil.getExceptionMessage(exception), "ERROR !");
        }
    }

    private void writeZip(String zipPath, String prefixUrl, List<VirtualFile> contents) {
        OutputStream fos = null;

        try {
            fos = new FileOutputStream(new File(zipPath));
            ZipOutputStream zip = new ZipOutputStream(fos);
            for (VirtualFile file : contents) {
                byte[] data = file.contentsToByteArray();
                ZipEntry entry = new ZipEntry(file.getUrl().substring(prefixUrl.length() + 1));
                entry.setSize(data.length);
                zip.putNextEntry(entry);
                zip.write(data);
//                    zip.closeEntry();
            }
            zip.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                } catch (IOException ioex) {
                }
                try {
                    fos.close();
                } catch (IOException ioex) {
                }
            }
        }
    }

}
