In this week, we are applying and using two differe styles. One of the styles that is called "Reflective" is used in _Seventeen.java_ file, and the other style, which is called "Plugins" is applied to the programs inside the folder _Twenty_.

 Both styles were used to develop two different implimentations which each will do the same task. The task is to count the words in a file given by the user in the commandline.

### **Refelctive:**
#### **Seventeen.java** 
  - This program uses the "Reflective" style. With this style, the program has access to information about itself. For example, it can see what methods have been declared and invoke them as needed.
  Also, The program can modify itself. For example, it can add more abstractions, variables, etc. at run-time
- To compile and run this program, on your commandline (shell/terminal), navigate to `week5` directory and run the following commands: 
```
- javac Seventeen.java
- java Seventeen ../pride-and-prejudice.txt 
```
When running this program, after the top 25 words with the highest frequency were printed, the program will prompt the user to `enter a class name to get its info:`. Then, program will load the class and print all its information if the class exists. So when promted, the user can enter a class name like `java.util.LinkedList` or `WordFrequencyManager`.




### **Plugins:**
#### **Twenty** 

###### **Description and Details:**
-  In this folder we have 3 different folders:  
    - app:
    In this folder, we have our applications:
      - App1.java
      - App2.java

    They have different implementations, but each of them has same goal of reading and counting the words from the file that passed by the user. These need to be used along side framwork when compiling since they are implementing and defining the methods from the interfaces used by _Framework.java_.
    - framework
    In this folder we have:
      - Framework.java
      - I_TFFreqs.java
      - I_TFWords.java
      - manifest,mf
    
    The Interfaces used by the _Framework.java_ are implemented in the app programs. So _Framework.java_, which uses "plugins" style, needs to load the codes and classes from application program. Therefore one program, can use multiple implementations of those interfaces based on what app it loads.
    Also, there is a manifest file here which is used when genearting the jar package from the _Framework.java_ to specify that this package has the Main class and program starts from here.

    - deploy
      - config.properties

    This file is needed to be used with the _framework.jar_ when running the program so that it knows which app it needs to load with plugins.
    After java files were compiled and *.jar packages were generated, these jar files can be copied to this folder to be used and run with config.properties 

###### **Running Twenty:**
- To compile this program, in commandline (shell/terminal), navigate to `week5` directory and run the following commands:
```
  - cd Twenty/framework
  - javac *.java
  - jar cmf manifest.mf framework.jar *.class
  - cd ../app
  - cp ../framework/framework.jar .
  - javac -cp framework.jar *.java
  - jar cf App1.jar App1.class
  - jar cf App2.jar App2.class
  - cd ../deploy/
  - cp ../framework/framework.jar .
  - cp ../app/*.jar .
  - java -jar framework.jar ../../../pride-and-prejudice.txt
  ```

- Right now _config.properties_ is set to use `App1` with framwork. If you want to run _App2.java_ please change the settings in this file. You can do so by just changing the value of `app` attribute like below:
`app=App2`

###### **Style Description:**
- In this style, the problem is decomposed using some form of abstraction (procedures, functions, objects, etc.)
- All or some of those abstractions are physically encapsulated into their own, usually pre-compiled, packages. Main program and each of the packages are compiled independently. These packages are loaded dynamically by the main program, usually in the beginning (but not necessarily).
- Main program uses functions/objects from the dynamically-loaded packages, without knowing which exact implementations will be used. New implementations can be used without having to adapt or recompile the main program.
- External specification of which packages to load. This can be done by a configuration file, path conventions, user input or other mechanisms for external specification of code to be linked at run time.
