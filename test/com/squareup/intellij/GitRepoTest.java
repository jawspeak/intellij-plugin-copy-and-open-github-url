// Copyright 2013 Square, Inc.
package com.squareup.intellij;

import com.squareup.intellij.helper.GithubRepo;
import com.squareup.intellij.helper.StashRepo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class GitRepoTest {
  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void github_parsesSshGitConfig_git_ro__with_line() throws Exception {
    final String projectRoot = givenProjectRoot();

    final GithubRepo repo = new GithubRepo(projectRoot, "test/test_github_gitconfig_git_ro.txt");

    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java#L30",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java", 30)
    );
  }

  @Test
  public void github_parsesSshGitConfig_git_ro() throws Exception {
    final String projectRoot = givenProjectRoot();

    final GithubRepo repo = new GithubRepo(projectRoot, "test/test_github_gitconfig_git_ro.txt");

    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void github_parsesSshGitConfig_https() throws Exception {
    final String projectRoot = givenProjectRoot();

    final GithubRepo repo = new GithubRepo(projectRoot, "test/test_github_gitconfig_https.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void invalidGitConfig() throws Exception {
    final String projectRoot = givenProjectRoot();

    final GithubRepo repo = new GithubRepo(projectRoot, "test/test_github_gitconfig_invalid.txt");

    expectedException.expect(RuntimeException.class);
    expectedException.expectMessage("Did not find");
    repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java");
  }

  @Test
  public void github_parsesSshGitConfig() throws Exception {
    final String projectRoot = givenProjectRoot();

    final GithubRepo repo = new GithubRepo(projectRoot, "test/test_github_gitconfig_ssh.txt");

    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void github_parsesSshGitConfig_comments() throws Exception {
    final String projectRoot = givenProjectRoot();

    final GithubRepo repo = new GithubRepo(projectRoot, "test/test_github_gitconfig_ssh_comments.txt");

    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void github_parsesSshGitConfig_variation() throws Exception {
    final String projectRoot = givenProjectRoot();

    final GithubRepo repo = new GithubRepo(projectRoot, "test/test_github_gitconfig_ssh_variation.txt");

    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void stash_parsesSshGitConfig() throws Exception {
    final String projectRoot = givenProjectRoot();

    final StashRepo repo = new StashRepo(projectRoot, "test/test_stash_gitconfig_ssh.txt");

    assertEquals(
        "https://git.corp.squareup.com:7770/projects/SQ/repos/docs/browse/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void stash_parsesSshWithContextPathGitConfig() throws Exception {
    final String projectRoot = givenProjectRoot();

    final StashRepo repo = new StashRepo(projectRoot, "test/test_stash_gitconfig_ssh_with_context_path.txt");

    assertEquals(
        "https://git.corp.squareup.com:7770/SCM/projects/SQ/repos/docs/browse/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  private String givenProjectRoot() throws URISyntaxException, MalformedURLException {
    return getClass().getResource(".").toURI().getSchemeSpecificPart();
  }
}
