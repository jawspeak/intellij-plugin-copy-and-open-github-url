package com.squareup.intellij.helper;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
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

public class ActionPerformer {
  private static final Logger LOG = Logger.getInstance("#" + ActionPerformer.class.getName());
  private final GitRepo repo;

  public ActionPerformer(GitRepo repo) {
    this.repo = repo;
  }

  /**
   * MVP: copies current file name. then appropriate github prefix. If a line is selected, it
   * supports that in the URL, too.
   *
   * By design it does not support: branches, folders, or multiple files selected (it picks the
   * 1st).
   */
  public void actionPerformed(AnActionEvent event, boolean doOpen) {
    final Editor editor = event.getData(PlatformDataKeys.EDITOR);
    final VirtualFile file = event.getData(PlatformDataKeys.VIRTUAL_FILE);
    Integer line = (editor != null)
        // convert the VisualPosition to the LogicalPosition to have the correct line number.
        // http://grepcode.com/file/repository.grepcode.com/java/ext/com.jetbrains/intellij-idea/10.0/com/intellij/openapi/editor/LogicalPosition.java#LogicalPosition
        ? editor.visualToLogicalPosition(
        editor.getSelectionModel().getSelectionStartPosition()).line + 1 : null;
    String url = copyUrl(file, line);
    if (doOpen) {
      openBrowser(url);
    }
    showStatusBubble(event, file);
  }

  private String copyUrl(VirtualFile file, Integer line) {
    String url = repo.repoUrlFor(file.getCanonicalPath(), line);
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
            "<p>" + repo.brand() + " URL for '<tt>"
                + file.getPresentableName()
                + "</tt> (on master branch) copied to your clipboard.</p>",
            MessageType.INFO, null)
        .setFadeoutTime(5500)
        .createBalloon()
        .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
  }
}
