package com.squareup.intellij.helper;

/**
 * Url builder for github repos.
 */
public class GithubRepo extends GitRepo {
  public GithubRepo(String projectRoot) {
    super(projectRoot);
  }

  public GithubRepo(String projectRoot, String gitconfig) {
    super(projectRoot, gitconfig);
  }

  @Override
  public String brand() {
    return "Github";
  }

  @Override
  protected String buildUrlFor(String originUrl) {
    final String domainAndContextpath = originUrl
            .replaceAll("ssh://|git://|git@|https://", "")
            .replaceAll(":", "/");
    return "https://" + domainAndContextpath + "/blob/master";

  }

  @Override
  protected String buildLineDomainPrefix() {
    return "#L";
  }
}
