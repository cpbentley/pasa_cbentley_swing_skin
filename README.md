

project id | pasa_cbentley_swing_skin
------------ | -------------
author | Charles-Philip Bentley
license | ![license MIT](https://img.shields.io/badge/license-MIT-yellowgreen.svg?style=plastic)
tags | Java
created | July 2019
modified | ![GitHub last commit](https://img.shields.io/github/last-commit/cpbentley/pasa_cbentley_swing_skin.svg?style=plastic)

Quote by Mahatma Gandhi
> When I admire the wonders of a sunset or the beauty of the moon, my soul expands in the worship of the creator.

# Summary

Libary for generating a Swing menu for currently available skin (look and feels)


## Crypto: Don't Trust, Verify -> No Magic.

Workflow only uses basic Eclipse projects with libaries included with sources.

You don't have to look anywhere. 

Its all here sourced on github repositories. 

## Github Repository Dependencies

It uses the pasa_cbentley_swing dependency graph

num | id | Right Click -> Copy Link Address
----| -- | -------------
1 | java_src4_compat_j2se | [Import Link](https://github.com/cpbentley/java_src4_compat_j2se)
2 | pasa_cbentley_core_src4 | [Import Link](https://github.com/cpbentley/pasa_cbentley_core_src4)
3 | pasa_cbentley_core_src5 | [Import Link](https://github.com/cpbentley/pasa_cbentley_core_src5)
4 | pasa_cbentley_core_src8 | [Import Link](https://github.com/cpbentley/pasa_cbentley_core_src8)
5 | pasa_cbentley_swing | [Import Link](https://github.com/cpbentley/pasa_cbentley_swing)

Current dependencies for the Eclipse project are 

num | id | Right Click -> Copy Link Address
----| -- | -------------
1 | pasa_jtattoo | [Import Link](https://github.com/cpbentley/pasa_jtattoo)
2 | JFormDesigner_FlatLaf | [Import Link](https://github.com/cpbentley/JFormDesigner_FlatLaf)

## How to import in Eclipse

**Basics**

* Install Java 8 **JDK**. You need the Java _Development_ Kit, which includes the Runtime (JRE). [Oracle JDK 8 Download Page](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html "Lastest JDK 8 from Oracle")
* Install Eclipse (tested with 2019-06). [Eclipse Download Page](https://www.eclipse.org/downloads/ "https://www.eclipse.org/downloads/")

* When launching Eclipse, create a workspace or use an existing workspace where you will check out all the github repos.

* An Eclipse workspace compiles continuously all its projects and their files. Errors are reported in the problems view.


**Specifics**

* Import the **github repositories** listed above. Following the given order, you should not see any compilation errors.

  1. Right click on link and copy link to clipboard
  2. In Eclipse, use the menu File -> Import
  ![eclipse_run_as.jpg](https://github.com/cpbentley/pasa_cbentley_app_hello_ctx/blob/master/res/tutorial/eclipse_import_git.jpg)
  3. In the dialog find Git -> Projects from Git -> Clone URI
  4. If you have copied the github repository url in the clipboard, you should see
    ![explorer_git.jpg](https://github.com/cpbentley/pasa_cbentley_app_hello_ctx/blob/master/res/tutorial/explorer_git.jpg)
  5. Select master branch
   ![eclipse_git_branch.jpg](https://github.com/cpbentley/pasa_cbentley_app_hello_ctx/blob/master/res/tutorial/eclipse_git_branch.jpg)
  6. Next you decide where the cloned repository will be created.
  ![eclipse_import_git_path.jpg](https://github.com/cpbentley/pasa_cbentley_app_hello_ctx/blob/master/res/tutorial/eclipse_import_git_path.jpg)
  7. Select import Eclipse Project.
   ![eclipse_import_git_choose.jpg](https://github.com/cpbentley/pasa_cbentley_app_hello_ctx/blob/master/res/tutorial/eclipse_import_git_choose.jpg)
  8. Eclipse project is selected. Press Finish
  ![eclipse_import_git_finish.jpg](https://github.com/cpbentley/pasa_cbentley_app_hello_ctx/blob/master/res/tutorial/eclipse_import_git_finish.jpg)
