Here we have three programs: 
- Four.java 
- Five.java
- Six.java

All 3 of them are doing the same task, which is counting the words in a file given by the user in the commandline. The difference between them is the style that we have used to develop the program.


### Monolith:
- **Four.java** program uses the “Monolith” style. With this style, no abstractions or library functions will be used.
- To compile and run this program, on your commandline (shell/terminal), navigate to "week2" directory and run the following commands:
  - javac Four.java
  - java Four ../pride-and-prejudice.txt






### CookBook:
- **Five.java** program uses the “cookbook” style. In this style, larger problems are decomposed in procedural abstractions and can be solve as a sequence of commands. Each procedure has access to some shared data, and each can edit or add to that data to be used in next procedure. 
- To compile and run this program, on your commandline (shell/terminal), navigate to "week2" directory and run the following commands:
  - javac Five.java
  - java Five ../pride-and-prejudice.txt


### Pipeline:
- **Six.java** program uses the “pipeline” or “candy factory” style. In this style, larger problems are decomposed in functional abstractions. Functions, according to Mathematics, are relations from inputs to outputs. Large problems in this style can be solved by dividing the task to a smaller tasks. Each function will do a task by receiving some inputs and can output the result to be used by next function. Larger problems are solved as a pipeline of function applications
- To compile and run this program, on your commandline (shell/terminal), navigate to "week2" directory and run the following commands:
  - javac Six.java
  - java Six ../pride-and-prejudice.txt
