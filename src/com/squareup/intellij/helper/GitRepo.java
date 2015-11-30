// Copyright 2013 Square, Inc.
package com.squareup.intellij.helper;

import com.google.common.annotations.VisibleForTesting;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Basic interfacing with .git/config configurations.
 */
public abstract class GitRepo {
  private static Pattern INI_CATEGORY = Pattern.compile("\\[\\s*(\\w+)[\\s'\"]+(\\w+)[\\s'\"]+\\]");
  private static Pattern URL_VALUE = Pattern.compile("\\s*url\\s*=\\s*([^\\s]*)\\.git");
  private final File gitConfigFile;

  public GitRepo(String projectRoot) {
    this(projectRoot, ".git/config");
  }

  @VisibleForTesting
  public GitRepo(String projectRoot, String gitconfig) {
    String gitRoot = findDotGitFolder(new File(projectRoot));
    gitConfigFile = new File(gitRoot, gitconfig);
  }

  public abstract String brand();

  /* Implement for different repository systems. */
  abstract String buildUrlFor(String sanitizedUrlValue);

  abstract String buildLineDomainPrefix();

  public String repoUrlFor(String relativeFilePath) {
    return repoUrlFor(relativeFilePath, null);
  }

  public String repoUrlFor(String filePath, Integer line) {
    filePath = filePath.replaceFirst(gitConfigFile.getParentFile().getParent(), "");
    return gitBaseUrl() + filePath + (line != null ? buildLineDomainPrefix() + line : "");
  }

  String findDotGitFolder(File absolutePath) {
    if (absolutePath.getParent() == null) {
      throw new RuntimeException(
          "Could not find parent .git/ folder. Maybe path is not in a git repo? " + absolutePath);
    }
    FileFilter gitFolderFinder = new FileFilter() {
      @Override public boolean accept(File pathname) {
        return pathname.getName().equals(".git");
      }
    };
    if (absolutePath.listFiles(gitFolderFinder).length == 1) {
      return absolutePath.getAbsolutePath();
    }
    return findDotGitFolder(absolutePath.getParentFile());
  }

  private String gitBaseUrl() {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(gitConfigFile));
      String line;
      boolean inRemoteOriginSection = false;
      while ((line = reader.readLine()) != null) {
        if (line.matches("\\s*#")) continue;
        Matcher matcher = INI_CATEGORY.matcher(line);
        if (matcher.matches()) {
          inRemoteOriginSection = "remote".equals(matcher.group(1))
              && "origin".equals(matcher.group(2));
          continue;
        }
        matcher = URL_VALUE.matcher(line);
        if (inRemoteOriginSection && matcher.matches()) {
          return buildUrlFor(matcher.group(1)
              .replaceAll("ssh://|git://|git@|https://", "")
              .replaceAll(":", "/"));
        }
      }
      throw new RuntimeException("Did not find [remote \"origin\"] url set in " + gitConfigFile);
    } catch (IOException e) {
      throw new RuntimeException("File " + gitConfigFile + " does not exist.", e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException ignored) {
        }
      }
    }
  }
}
