# Gradle Template

A simple base for Slay the Spire mods that uses Gradle instead of Maven. This template assumes you are roughly familiar with the basics of setting up a mod already (e.g. needing to edit a ModTheSpire.json, knowing how to run Maven builds etc.) and is more aimed at making the transition from Maven to Gradle as painless as possible.

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

## Usage
Each project has a **build.gradle.kts** (written in Kotlin) and a **settings.gradle** (containing one line, written in Groovy). The build file sets up the gradle build tasks (one that builds a fat JAR and one that builds the JAR and copies it to your **STS_INSTALL/mods** directory for testing). This **build.gradle.kts** file will use your environment variables and won't need to be touched in most cases. The **settings.gradle** file however should be edited for each mod, as the line inside will determine the name of the final artifact that gets created. If you don't edit the **settings.gradle** project name you'll be building *GradleTemplate.jar* instead of *YourMod.jar*.

If you require additional libraries (and this is one example where the Gradle system shines over Maven), you can tweak the *build.gradle.kts* file. There are some minor explanatory comments in there to hopefully guide you on what is required. Basically, if you pull from an online database repository like Maven Central or Jitpack, you need to include the source in a repositories section. Then, to get access to the actual libraries, you include it as an "implementation(...)" inside the dependencies section. Marking it as implementation (not compileOnly) means it will be included in the final fat JAR, and usable in game without a runtime error. You can also use locally downloaded JAR libraries as well (this only needs to be placed inside the dependencies section, as no repository is needed).

### Compiling in IntelliJ IDEA
Much like Maven, there are particular build tasks that we're interested in running. The primary one is "buildAndCopyJAR" under the "slay the spire" group. You can find these in the following UI menu:

    View->Tool Windows->Gradle

Like Maven mods, you can make one of these build tasks your primary run target. On my system, I've set the **buildAndCopyJAR** task to be the thing that happens when I run code (which on my system I've keybound to Ctrl+R). This **buildAndCopyJAR** task will copy any built JARs over to your *SlayTheSpire/mods* folder automatically, letting you test changes in game very easily.

## Troubleshooting / Additional Tweaks

If you have trouble setting environment variables and get complaints that the values are null (e.g. you're not running IntelliJ in a manner which gives it access to your env. vars), you can always hardcode the directories directly into the strings inside the **build.gradle.kts** file. This is less flexible for collaborative purposes and can require a lot of changes across multiple projects if you change directories later on, but will work fine in the short term if you can't get the env. vars to work.

You can tweak the dependencies section slightly to refer to the **desktop-1.0.jar** that lives inside the *STS_INSTALL* directory instead of one that lives inside the *STS_MODDING_LIB* directory. On my system, I copied over the ~300mb main game JAR into my *STS_MODDING_LIB* folder, along with copies of BaseMod etc. Similarly, you can tweak it so that it points to the most up-to-date Steam workshop versions of BaseMod or ModTheSpire to avoid the need to manually update the library directory. I prefer the stability of having it fixed and only changed when I'm aware of it and choose to update by copying things over, but I can imagine several convincing arguments for pointing to the most up to date versions of the library mods needed. I imagine a symlink might also be an effective solution, but I haven't tested that personally.

You may wish to add an additional task inside your **build.gradle.kts** which *dependsOn(buildAndCopyJAR)* and also launches Slay the Spire with mods. While I haven't set this up for myself, I imagine it wouldn't be too difficult, and you can use this as a little exercise to learn a bit more about Gradle. Additional exercises that may be useful for learning purposes would be to make the buildAndCopyJAR also perform a clean build step, or to make a task that copies to a folder where you manage uploads to the Steam workshop.

If you use IntelliJ IDEA, it's probably worth making this repo (or one with your own tweaks) as a project template or collection of file templates, to make starting new mods as painless as possible. A dream goal would be to make a Gradle SlayTheSpire IntelliJ new project wizard (using the IntelliJ plugin SDK), but I don't have the time to make that personally. Imagine a world where you can do File -> New Project -> New Slay the Spire Gradle Project where you can set the author name, mod name, etc. in a simple wizard and have it automatically make everything including the ModTheSpire.json etc. in a simple one step process. A man can dream...