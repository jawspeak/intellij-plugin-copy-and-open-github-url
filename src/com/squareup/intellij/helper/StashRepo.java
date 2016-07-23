package com.squareup.intellij.helper;

import com.google.common.base.Joiner;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.Arrays;

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
  protected String buildUrlFor(String originUrl) {
    final String domainAndPath = originUrl.replaceAll("ssh://|git://|git@|https://", "");
    final String[] domainAndPathParts = domainAndPath.split("/");
    final ArrayDeque<String> partDeque = new ArrayDeque<String>(Arrays.asList(domainAndPathParts));
    final String repo = partDeque.pollLast();
    final String project = partDeque.pollLast().toUpperCase();
    final String domainAndContextPath = Joiner.on("/").join(partDeque);
    final String browsePath = Joiner.on('/').join(domainAndContextPath, "projects", project, "repos", repo, "browse");
    return URI.create("https://" + browsePath).toASCIIString();
  }

  @Override
  protected String buildLineDomainPrefix() {
    return "#";
  }
}
