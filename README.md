# Gradle Template

A simple base for Slay the Spire mods that uses Gradle instead of Maven. This template assumes you are roughly familiar with the basics of setting up a mod already (e.g. needing to edit a ModTheSpire.json, knowing how to run Maven builds etc.) and is more aimed at making the transition from Maven to Gradle as painless as possible. *This repo is designed for mods written in Java, not Kotlin, although the switch over for Kotlin development should be extremely simple to accomplish.*

This repository just has a basic starting mod that will spit out a hello world into the console in game after running the included "buildAndCopyJAR" task. It doesn't quite work right out of the box (you need to set up some environment variables), but if you have built Maven mods before you should be able to get it working after cloning this repository relatively quickly.

## Why Gradle?
1. Easier to read and maintain (subjective: but in my opinion tasks in the Kotlin script are miles, miles, miles ahead of working with a nightmare pom.xml)
2. Easier dependency management (don't need to mess about with shading/etc. - dependencies are graph based instead of some weird linear stuff and much easier to reason about; most importantly: including external libraries are a breeze compared to Maven)
3. Faster compile times (YMMV and it might be placebo, but it seems to me that the Gradle builds are optimized much better than the Maven builds; likely due to Gradle being designed as a more modern successor to Maven)

## Requirements:

* You need to add two environment variables to your system. On Linux, I put these in my *~/.bashrc*:
  
    * **STS_INSTALL** - points to the Steam folder where Slay the Spire is installed. On my system, my env. var command is:
  
      ```
      export STS_INSTALL="/home/casey/.steam/steam/steamapps/common/SlayTheSpire/"
      ```
  * **STS_MODDING_LIB** - points to a folder which contains any JAR libraries that you would want to reference in your mod but not include in the final compiled JAR. I.e. in this folder is a *desktop-1.0.jar*, a *BaseMod.jar*, and a *ModTheSpire.jar* - all libraries that will exist on the client end.
      ```
      export STS_MODDING_LIB="/home/casey/documents/sts_modding/lib/"
      ```

* You need to launch IntelliJ IDEA (or other tools like Vim) in a manner that actually includes your environment variables. On my system, this doesn't work from DMENU and requires me to launch *idea* from a terminal (with a helper script/alias to make that as convenient as possible).

* **IMPORTANT:** once you clone this project and open it up in IntelliJ idea, make sure that you're using the proper Java 1.8 SDK. I noticed on my own machine that loading it up in IntelliJ after cloning into a tmp directory that it defaulted to my most recent Java SDK instead of the one necessary for Slay the Spire mods. You can tell if you're using the wrong SDK at a glance by seeing something other than Java 1.8 in the "External Libraries" section of your project pane (left side of the work area by default in IDEA, which you can open/close with the Alt+1 keyboard shortcut). You can select the proper SDK via the File -> Project Structure -> Project Settings -> Project -> Project SDK and choose the proper 1.8 one from the drop down menu. I'm not quite sure if there's something I can do to alter the repo so that this doesn't happen when cloning, but it's an easy enough fix that it might not matter.

## Usage
Each project has a **build.gradle.kts** (written in Kotlin) and a **settings.gradle** (containing one line, written in Groovy). The build file sets up the gradle build tasks (one that builds a fat JAR and one that builds the JAR and copies it to your **STS_INSTALL/mods** directory for testing). This **build.gradle.kts** file will use your environment variables and won't need to be touched in most cases. The **settings.gradle** file however should be edited for each mod, as the line inside will determine the name of the final artifact that gets created. If you don't edit the **settings.gradle** project name you'll be building *GradleTemplate.jar* instead of *YourMod.jar*.

If you require additional libraries (and this is one example where the Gradle system shines over Maven), you can tweak the *build.gradle.kts* file. There are some minor explanatory comments in there to hopefully guide you on what is required. Basically, if you pull from an online database repository like Maven Central or Jitpack, you need to include the source in a repositories section. Then, to get access to the actual libraries, you include it as an "implementation(...)" inside the dependencies section. Marking it as implementation (not compileOnly) means it will be included in the final fat JAR, and usable in game without a runtime error. You can also use locally downloaded JAR libraries as well (this only needs to be placed inside the dependencies section, as no repository is needed).

### Compiling in IntelliJ IDEA
Much like Maven, there are particular build tasks that we're interested in running. The primary one is "buildAndCopyJAR" under the "slay the spire" group. You can find these in the following UI menu:

    View->Tool Windows->Gradle

Like Maven mods, you can make one of these build tasks your primary run target. On my system, I've set the **buildAndCopyJAR** task to be the thing that happens when I run code (which on my system I've keybound to Ctrl+R). This **buildAndCopyJAR** task will copy any built JARs over to your *SlayTheSpire/mods* folder automatically, letting you test changes in game very easily.

## Troubleshooting / Additional Tweaks

If you have trouble setting environment variables and get complaints that the values are null (e.g. you're not running IntelliJ in a manner which gives it access to your env. vars), you can always hardcode the directories directly into the strings inside the **build.gradle.kts** file. This is less flexible for collaborative purposes and can require a lot of changes across multiple projects if you change directories later on, but will work fine in the short term if you can't get the env. vars to work.

You can tweak the dependencies section slightly to refer to the **desktop-1.0.jar** that lives inside the *STS_INSTALL* directory instead of one that lives inside the *STS_MODDING_LIB* directory (similarly you can try and point to the actual location of the library mods based on where the Steam workshop downloads them). On my system, I copied over the ~300mb main game JAR into my *STS_MODDING_LIB* folder, along with copies of BaseMod and ModTheSpire. This particular method is storage expensive and has the caveat of potentially going out of date, but I prefer the stability of limiting changes to when I fully expect them by manual updates. I imagine a symlink might be an effective solution to get the live versions of the libraries, but I haven't tested that personally.


You may wish to add an additional task inside your **build.gradle.kts** which *dependsOn(buildAndCopyJAR)* and also launches Slay the Spire with mods. While I haven't set this up for myself, I imagine it wouldn't be too difficult, and you can use this as a little exercise to learn a bit more about Gradle. Additional exercises that may be useful for learning purposes would be to make the buildAndCopyJAR also perform a clean build step, or to make a task that copies to a folder where you manage uploads to the Steam workshop.

If you use IntelliJ IDEA, it's probably worth making this repo (or one with your own tweaks) as a project template or collection of file templates, to make starting new mods as painless as possible. A dream goal would be to make a Gradle SlayTheSpire IntelliJ new project wizard (using the IntelliJ plugin SDK), but I don't have the time to make that personally. Imagine a world where you can do File -> New Project -> New Slay the Spire Gradle Project where you can set the author name, mod name, etc. in a simple wizard and have it automatically make everything including the ModTheSpire.json etc. in a simple one-step process. Somebody get on that!

---

## Quick reference steps for starting a new mod

#### Precursors (only need to do the first time)
1. Read through the entirety of this README at least once just so you have a general idea of what this repository is supposed to do / why it exists.
2. Ensure you have the proper folders set up with the JAR files in the right places and your environment variables working. (See instructions in previous section).
   
#### Each time you start a new gradle mod (at least until you make IntelliJ templates like a reasonable person would do)
1. Clone this repository into an empty folder.

```
git clone https://github.com/casey-c/stsgradletemplate
```

2. Open in IntelliJ-IDEA and wait for Gradle / indexing / etc. to finish initializing.
3. Expand the "External Libraries" option of the project pane (on the left) to verify that Java 1.8 is included, as well as the game, BaseMod, and ModTheSpire. If you see the wrong SDK version, be sure to change it using the ```File -> Project Structure``` menu.
4. Open **settings.gradle** and rename the ```rootProject.name``` to be your desired mod name. You can also refactor->rename the main classes/folders to be whatever you want them to be and fill out the ModTheSpire.json now if you wish.
5. ```View -> Tool Windows -> Gradle``` to open up the Gradle panel. Expand the "Slay the Spire" group and double click the "buildAndCopyJAR" task to run it. After running it once, your run/debug configuration on the top bar should automatically update to have this task for future builds (in the latest versions of IntelliJ-IDEA at least). A "YourMod.jar" will have been copied to the Slay the Spire install location, where "YourMod" is whatever you put inside the rootProject.name in step 4.
6. Launch Slay the Spire with mods (and with the debug console output logs), enable your mod, and verify that you see your mod's post initialize hello world.
7. Draw the rest of the owl and make your mod