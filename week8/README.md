Here we have three programs: 
- TwentyNine.java 
- Thirty.java
- ThirtyTwo.java

All three programs are doing the same task, which is counting the words in a file given by the user in the commandline. The difference between them is the style that we have used to develop the program.


### Free agents:
- **TwentyNine.java** program uses the “Free agents” or "Actor" style. This style is similar to the letterbox style, but where the 'things' have independent threads of execution. In this style, the larger problem is decomposed into 'things' that make sense for the problem domain. Each 'thing' has a queue meant for other things to place messages in it. Each 'thing' is a capsule of data that exposes only its ability to receive messages via the queue. Each 'thing' has its own thread of execution independent of the others.

- To compile and run this program, on your commandline (shell/terminal/console), navigate to "week7" directory and run the following commands:
  - `javac TwentyNine.java`
  - `java TwentyNine ../pride-and-prejudice.txt`

##

### Tantrum:
- **TwentyTwo.java** program uses the “Tantrum” style. In this style, every single procedure and function checks the sanity of its arguments and refuses to continue when the arguments are unreasonable. Also, all code blocks check for all possible errors, possibly print out context-specific messages when errors occur, and pass the errors up the function call chain

- To compile and run this program, on your commandline (shell/terminal/console), navigate to "week7" directory and run the following commands:
  - `javac TwentyTwo.java`
  - `java -ea TwentyTwo ../pride-and-prejudice.txt`
  To enable the assert in java, we need to pass the `-ea` argument when running the code. If something is wrong, assert will hit and stop the program.

  ##
### Quarantine:
- **TwentyFive.java** program uses the “Quarantine” style. This style is a variation of style "The One", with the following additional constraints: 
  - Core program functions have no side effects of any kind, including IO
  - All IO actions must be contained in computation sequences that are clearly separated from the pure functions
  - All sequences that have IO must be called from the main program

- To compile and run this program, on your commandline (shell/terminal/console), navigate to "week7" directory and run the following commands:
  - `javac TwentyFive.java`
  - `java TwentyFive ../pride-and-prejudice.txt`

