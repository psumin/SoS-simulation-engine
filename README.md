# MCI Response Simulator
## Notes
This branch (test/metamorphic_testing) is an attempt to preserve a stable version of the MCI SoS simulator
for it to be easily used by others to test out concepts,
methodology, etc.

Some quality of life changes include:
- Restructuring of folders and correcting gradle build script.
- Using a packaging build script (shadowJar)
  - Packages the entire code + dependencies into a single executable jar file.
  - Gradle > Build 
    - This will build the entire project including the executable jar file
    which can be found in build > libs > SoS-simulation-engine-1.0-all.jar
- Added code to accept runtime arguments. See misc.Settings.java for code.
  - For example: `java -jar SoS-simulation-engine-1.0-all.jar -a 5 -p 10 -ff 10 -bh 4 -h 4`
    (see main.java for a list of hints about the arguments)
- Fixed cs agents image loading problems.

## Download simulator
- found in: `build/libs/SoS-simulation-engine-1.0-all.jar`
- to execute:
  - `java -jar SoS-simulation-engine-1.0-all.jar -a 5 -p 10 -ff 10 -bh 4 -h 4`
  
  - `java -jar SoS-simulation-engine-1.0-all.jar`