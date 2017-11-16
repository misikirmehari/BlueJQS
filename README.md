#	BlueJQS

![bluej icon](/Users/hunter/Pictures/Bluej.png "BlueJQS icon")

**Developed By:** Maxwell Stark, Hunter Hobbs, Erin Gurnett, Misikir Mehari, Vicky Lym, Haley Ittner.  

**Most recent release:** Version 1.0.0  
**Release date:** November 12, 2017

## Project Vision

**Provides BlueJ users the ability to analyze source code using a suite of standard tools.**

Our product supports novice Java programmers in forming a foundation of high-quality code practices and industry standards. Students can use our product in conjunction with BlueJ to assess the quality of their code. Our product incorporates a collection of existing tools used to identify potential weaknesses in source code (for example, bugs, non-adherence to conventions, poor test coverage, etc.) via both static analysis, which evaluates the source code itself, and dynamic analysis, which evaluates the behavior of the code during run-time.

## Wiki

Currently, BlueJ Quality Suite provides a framework for multiple tools to perform static code analysis in BlueJ 4.1.1. BlueJQS v1.0.0 provides the user the ability to run PMD on their class files. More tools are being added and will be included in the next version release.

Refer to the [wiki](https://gouda.msudenver.edu/redmine/projects/dew/wiki) page for current news regarding this project. 


## Use

To use BlueJQS, while in BlueJ - Open the main class window. Right click on a class object and at the bottom of the menu - click on "Open Quality Suite Tools".  

![Open Quality Suite Tools screenshot](/Users/hunter/Desktop/blueJhelloWorld.png)
You will then see a dialog box prompting to choose which tool you would like to run (currently there is only one option, 1 - PMD).  A dialog box will then pop up to tell you that the class was or wasn't check successfully, followed by another dialog box that will list all the errors that PMD found for that class. To run a different class, follow the same steps.


