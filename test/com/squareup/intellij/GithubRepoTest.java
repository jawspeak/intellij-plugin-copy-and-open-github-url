// Copyright 2013 Square, Inc.
package com.squareup.intellij;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class GithubRepoTest {
  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void parsesSshGitConfig_git_ro__with_line() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_gitconfig_git_ro.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java#L30",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java", 30)
    );
  }

  @Test
  public void parsesSshGitConfig_git_ro() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_gitconfig_git_ro.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void parsesSshGitConfig_https() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_gitconfig_https.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void invalidGitConfig() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_gitconfig_invalid.txt");
    expectedException.expect(RuntimeException.class);
    expectedException.expectMessage("Did not find");
    repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java");
  }

  @Test
  public void parsesSshGitConfig() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_gitconfig_ssh.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void parsesSshGitConfig_comments() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_gitconfig_ssh_comments.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void parsesSshGitConfig_variation() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_gitconfig_ssh_variation.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }
}
