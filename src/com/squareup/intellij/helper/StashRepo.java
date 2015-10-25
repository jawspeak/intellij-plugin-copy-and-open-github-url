package com.squareup.intellij.helper;

import com.google.common.base.Joiner;

/**
 * Url builder for atlassian stash repos.
 */
public class StashRepo extends GitRepo {

  public StashRepo(String projectRoot) {
    super(projectRoot);
  }

  public StashRepo(String projectRoot, String gitconfig) {
    super(projectRoot, gitconfig);
  }

  @Override
  public String brand() {
    return "Stash";
  }

  @Override
  protected String buildUrlFor(String sanitizedUrlValue) {
    String[] chunks = sanitizedUrlValue.split("/");
    String domain = chunks[0];
    String project = chunks[1].toUpperCase();
    String repo = chunks[2];

    return "https://" + Joiner.on('/').join(domain, "projects", project, "repos", repo, "browse");
  }

  @Override
  protected String buildLineDomainPrefix() {
    return "#";
  }
}
