Here we have two programs:

Nine.java
Ten.java
 Both programs are doing the same task, which is counting the words in a file given by the user in the commandline. The difference between them is the style that we have used to develop the program.

### Letterbox:
- **Nine.java** program uses the "Kick your teammate forward" style. This style is a variation of the candy factory style, but it has more constraints. In this style, each function has an additional parameter which is another function. The current function being executed, needs to call the function parameter at the end with what would be its output. Larger problem is solved as a pipeline of functions, but where the next function to be applied is given as parameter to the current function
- To compile and run this program, on your commandline (shell/terminal), navigate to "week4" directory and run the following commands:
  - javac Nine.java
  - java Nine ../pride-and-prejudice.txt






### Closed Maps:
- **Ten.java** program uses a style that can be called “The One" or "Monadic Identity.” This style is based on having an abstraction where values can be converted. Larger problem is solved as a pipeline of functions bound together, with unwrapping happening at the end. Particularly for The One style, the bind operation simply calls the given function, giving it the value that it holds, and holds on to the returned value.
- To compile and run this program, on your commandline (shell/terminal), navigate to "week4" directory and run the following commands:
  - javac Ten.java
  - java Ten ../pride-and-prejudice.txt
