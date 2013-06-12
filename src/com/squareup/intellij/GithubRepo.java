// Copyright 2013 Square, Inc.
package com.squareup.intellij;

import com.google.common.annotations.VisibleForTesting;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class GithubRepo {
  private static Pattern INI_CATEGORY = Pattern.compile("\\[\\s*(\\w+)[\\s'\"]+(\\w+)[\\s'\"]+\\]");
  private static Pattern URL_VALUE = Pattern.compile("\\s*url\\s*=\\s*([^\\s]*)\\.git");
  private final File gitConfigFile;

  GithubRepo(String projectRoot) {
    this(projectRoot, ".git/config");
  }

  @VisibleForTesting GithubRepo(String projectRoot, String gitconfig) {
    gitConfigFile = new File(projectRoot, gitconfig);
  }

  String repoUrlFor(String relativeFilePath) {
    return repoUrlFor(relativeFilePath, null);
  }

  String repoUrlFor(String relativeFilePath, Integer line) {
    return githubBaseUrl() + relativeFilePath + (line != null ? "#L" + line : "");
  }

  private String githubBaseUrl() {
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
          return "https://" + matcher.group(1)
              .replaceAll("git://|git@|https://", "")
              .replaceAll(":", "/") + "/blob/master";
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
