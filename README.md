# Zest
## Java 3033 Group Project
#### Contributors:
* Joshua LaFever
* Gary London
* Josh Ortner
* Zernab Saeed

### Setting up JavaFX in Eclipse

In order to get this project running in Eclipse, it is necessary to set up your environment with the required JavaFX libraries.

You will need the correct JavaFX SDK for your operating system. JavaFX allows applications to run cross-platform, but you do need the correct SDK in order for this to work.

1. Download the correct SDK for your operating system. This can be found in the Download section here: https://openjfx.io/

2. Extract the zip file to a new directory on your machine.

3. Open the Eclipse project, click the ```Project``` option on the top pane, and then select ```Properties``` from the drop-down menu.

4. On the left, you will see various options. Select ```Java Build Path```. In the window on the right select ```Libraries```.

5. Select ```Modulepath``` and then ```Add Library...```.

6. In the pop-up window, select ```User Library``` and ```Next>```.

7. Select ```User Libraries...``` then ```New...``` and then enter a name for your library.

8. On your new library, click ```Add External Jars...```.

9. Browse to find the directory you created in step 2, navigate to the ```lib``` subdirectory, and select all of the ```.jar``` files and add them to your User Library.

10. Click ```Apply and Close``` and save all options. The process is complete and you can now run JavaFX.

## SQLite Java Driver Setup

1. Download SQLite connector jar file: https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.32.3.2/

2. Open Eclipse and create folder within project called "libs."

3. Copy the jar file into the libs folder

4. Right click the project, select "Build Path" then "Configure Build Path"

5. In the Libraries section select "Classpath" and "Add Jars"

6. Select the jar file from the libs folder

7. If project has a module-info.java file, add "requires java.sql"
