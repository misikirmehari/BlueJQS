-README document for BlueJQS extension for BlueJ 4.1.1 -

# CONTENTS OF THIS FILE
-----------------------

 * About/Synopsis
 * Status/To-do
 * Installation
 * Usage
 * Build
 * Deploy
 * Contributors
 * License


# ABOUT / SYNOPSIS
------------------

 BlueJQS is an extension for BlueJ v4.1.1 that provides users the ability to analyze 
 source code using a suite of standard tools.

 This software is intended to support novice Java programmers to form a foundation of high-
 quality code practices and industry standards. Students can use our product in conjunction
 with BlueJ to assess the quality of their code. Our product incorporates a collection
 of existing tools used to identify potential weaknesses in source code (e.g., potential
 bugs, non-adherence to conventions, poor test coverage, etc.) via both static analysis, 
 which evaluates the source code itself, and dynamic analysis, which evaluates the
 behavior of the code during run-time.

  - Current version: BlueJQS v1.1.1


# STATUS / TO-DO
----------------
 
 * Project status: Working with some functionality. Limitations and development to-do
   list is as follows:

 ** Limitations: 
    - BlueJ projects MUST be located in a directory where the path to that directory
      DOES NOT contain spaces. 
      (e.g., '~/User/BlueJ Projects/HelloWorld.java' will crash the software)
    - The location of the BlueJQS folder (containing dependencies) MUST be placed in a
      location that DOES NOT contain spaces in the file path.
      (e.g., 'C:\Program Files\Blue J\BlueJQS' will crash the software)
    - Currently google-checks.xml is hard-coded as the Checkstyle configuration.

 *** To Do list:
     - Fix space issue with location of project and location of dependency folder.
     - Incorporate suite dependency files into extension jar.
     - Add functionality for SpotBugs.
     - Write unit tests for Java classes.
     - Refactor suite output.
     - Add Checkstyle configuration option.
     - Create an installer for software.

# INSTALLATION
--------------
 
 The current software release can be found here:

    https://github.com/JHunterHobbs/BlueJQS/releases/tag/svn%2Ftags%2Frelease-1.1.1
 
 1: Download BlueJQS.zip and unzip in a directory that contains NO SPACES in the file path.

  - Suggested location to unzip BlueJQS.zip is C:\User\[username]\Documents on Windows 
    (assuming the username has no spaces) and ~/ on macOS or Linux.
    You will need to remember this location to configure BlueJ preferences.

    In the unzip BlueJQS folder you will find the following files:
     * BlueJQualitySuite.jar
     * checkstyle-8.5-all.jar
     * google_checks.xml
     * pmd-bin-5.8.1/
     * README
     * license/

 2: Copy the BlueJQualitySuite.jar file and paste into the BlueJ extensions folder.

  - Windows users: the extension folder is typically found here:
    C:\Program Files (x86)\BlueJ\lib\extensions

  - macOS users: the extension folder is typically found here:
    /BlueJ 4.1.1/BlueJ.app/Contents/Resources/Java/extensions
    (if you have trouble finding this file location, navigate to where BlueJ is,
    control click and click "show package contents")

  - Linux users: the extension folder is typically found here:
    /usr/share/bluej/extensions

 3: Start BlueJ and navigate to the Preferences menu. (Tools->Preferences for Windows,
    BlueJ->Preferences for macOS, Tools->Preferences for Linux)

    In the Preferences menu, under Extensions you must set BlueJQS Library Path to the
    folder that you unzipped in step 1. Use the select button to find the folder.

 BlueJQS is now successfully installed and configured.


# USAGE
-------

 To use the software suite, open the main class window. Right click on a class object
 and at the bottom of the dialog menu click on 'Open Quality Suite Tools'. You will then
 see a drop down menu that prompts the user to choose one of the following three options
 ALL, PMD, or Checkstyle. This gives the user the options of running tools individually, or
 receiving an analysis report from all the tools. The results from the analysis test(s)
 are then displayed to the user in a dialog box.


# BUILD
-------

 This software uses Maven as a build environment. To build the project from sources, 
 Maven must be installed on your machine.

 To build the project from source code - from the command line, navigate to the pom.xml file 
 that can be found in release-1.1.1/BlueJQualitySuite/ and run:
   
    mvn clean install

 To generate Javadoc API, run the following command: 
  
    mvn javadoc:javadoc


# DEPLOYMENT
------------

 Copy build artifact BlueJQualitySuite.jar into BlueJ extensions folder. See
 installation instructions for further details.


# AUTHORS
---------
 
 Team DEW:
 Erin Gurnett
 Haley Ittner
 Hunter Hobbs
 Maxwell Stark
 Misikir Mehari
 Vicky Lym

 Under the consultation of Dr. Jody Paul, Metropolitan State University of Denver, 2017.


# LICENSE
---------

 Licenses for any BlueJQS dependencies can be found in release-X.X.X/license and in 
 BlueJQS.zip - in BlueJQS/license/
 Current dependency licenses: Checkstyle, PMD.
 
END OF README FILE
