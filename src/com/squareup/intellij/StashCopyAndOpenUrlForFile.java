package com.squareup.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.squareup.intellij.helper.ActionPerformer;
import com.squareup.intellij.helper.StashRepo;

public class StashCopyAndOpenUrlForFile extends AnAction {
  @Override
  public void actionPerformed(AnActionEvent event) {
    Project project = event.getData(PlatformDataKeys.PROJECT);
    new ActionPerformer(new StashRepo(project.getBasePath())).actionPerformed(event);
  }
}