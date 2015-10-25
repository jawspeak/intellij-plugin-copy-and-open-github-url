// Copyright 2013 Square, Inc.
package com.squareup.intellij;

import com.squareup.intellij.helper.GithubRepo;
import com.squareup.intellij.helper.StashRepo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class GitRepoTest {
  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void github_parsesSshGitConfig_git_ro__with_line() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_github_gitconfig_git_ro.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java#L30",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java", 30)
    );
  }

  @Test
  public void github_parsesSshGitConfig_git_ro() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_github_gitconfig_git_ro.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void github_parsesSshGitConfig_https() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_github_gitconfig_https.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void invalidGitConfig() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_github_gitconfig_invalid.txt");
    expectedException.expect(RuntimeException.class);
    expectedException.expectMessage("Did not find");
    repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java");
  }

  @Test
  public void github_parsesSshGitConfig() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_github_gitconfig_ssh.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void github_parsesSshGitConfig_comments() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_github_gitconfig_ssh_comments.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void github_parsesSshGitConfig_variation() throws Exception {
    GithubRepo repo = new GithubRepo(".", "test/test_github_gitconfig_ssh_variation.txt");
    assertEquals(
        "https://git.squareup.com/square/java/blob/master/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }

  @Test
  public void stash_parsesSshGitConfig() throws Exception {
    StashRepo repo = new StashRepo(".", "test/test_stash_gitconfig_ssh.txt");
    assertEquals(
        "https://git.corp.squareup.com/projects/SQ/repos/docs/browse/projectX/src/main/java/com/ex/Ex.java",
        repo.repoUrlFor("/projectX/src/main/java/com/ex/Ex.java")
    );
  }
}
