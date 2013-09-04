// Copyright 2013 Square, Inc.
package com.squareup.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import java.awt.Desktop;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CopyAndOpenGithubUrlForFile extends AnAction {
  private static final Logger LOG = Logger.getInstance("#" + CopyAndOpenGithubUrlForFile.class.getName());

  /**
   * MVP: copies current file name. then appropriate github prefix. If a line is selected, it
   * supports that in the URL, too.
   *
   * By design it does not support: branches, folders, or multiple files selected (it picks the
   * 1st).
   */
  public void actionPerformed(AnActionEvent event) {
    Project project = event.getData(PlatformDataKeys.PROJECT);
    final Editor editor = event.getData(PlatformDataKeys.EDITOR);
    final VirtualFile file = event.getData(PlatformDataKeys.VIRTUAL_FILE);
    Integer line = (editor != null)
        // convert the VisualPosition to the LogicalPosition to have the correct line number.
        // http://grepcode.com/file/repository.grepcode.com/java/ext/com.jetbrains/intellij-idea/10.0/com/intellij/openapi/editor/LogicalPosition.java#LogicalPosition
        ? editor.visualToLogicalPosition(editor.getSelectionModel().getSelectionStartPosition()).line + 1 : null;
    String url = copyUrl(project, file, line);
    openBrowser(url);
    showStatusBubble(event, file);
  }

  private String copyUrl(Project project, VirtualFile file, Integer line) {
    String basePath = project.getBasePath();
    GithubRepo githubRepo = new GithubRepo(basePath);
    String relativeFilePath = file.getCanonicalPath().replaceFirst(basePath, "");
    String url = githubRepo.repoUrlFor(relativeFilePath, line);
    CopyPasteManager.getInstance().setContents(new StringSelection(url));
    return url;
  }

  private void openBrowser(String url) {
    try {
      Desktop.getDesktop().browse(new URI(url));
    } catch (IOException e) {
      LOG.error("Unable to open browser for url: " + url, e);
    } catch (URISyntaxException e) {
      LOG.error("Unable to open browser for url: " + url, e);
    }
  }

  private void showStatusBubble(AnActionEvent event, VirtualFile file) {
    StatusBar statusBar = WindowManager.getInstance()
        .getStatusBar(DataKeys.PROJECT.getData(event.getDataContext()));

    JBPopupFactory.getInstance()
        .createHtmlTextBalloonBuilder(
            "<p>Github URL for '<tt>"
                + file.getPresentableName()
                + "</tt> (on master branch) copied to your clipboard.</p>",
            MessageType.INFO, null)
        .setFadeoutTime(5500)
        .createBalloon()
        .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
  }
}
