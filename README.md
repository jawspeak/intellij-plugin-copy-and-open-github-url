## Info
For projects with git repos in the base, it allows you to copy the current file's path on the master branch, and open that with the default browser.

Handy when you want to open a file in Intellij and then share the github link (including line number) with someone else.


## Developing

You'll need to set this up as any other plugin, see IntelliJ's tutorials.


## Deploying for the World to Use

1. right click on the plugin module and choose: "Prepare plugin module 'copy-as-github-path-plugin' for Deployment"
2. this creates a zip, which you upload to [Jetbrains](http://plugins.jetbrains.com/plugin/addPlugin?pr=).


## Installing locally

1. Build as before, but copy the extracted zip into `~/Library/Application Support/IntelliJIdeaXX`, where XX is your current version of Intellij.


## Bugs

1. Report them on [github's issue tracker](https://github.com/jawspeak/intellij-plugin-copy-and-open-github-url/issues).

