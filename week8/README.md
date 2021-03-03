Here we have three programs: 
- TwentyNine.java 
- Thirty.java
- ThirtyTwo.java

All three programs are doing the same task, which is counting the words in a file given by the user in the commandline. The difference between them is the style that we have used to develop the program.


### Free agents:
- **TwentyNine.java** program uses the “Free agents” or "Actor" style. This style is similar to the letterbox style, but where the 'things' have independent threads of execution. In this style, the larger problem is decomposed into 'things' that make sense for the problem domain. Each 'thing' has a queue meant for other things to place messages in it. Each 'thing' is a capsule of data that exposes only its ability to receive messages via the queue. Each 'thing' has its own thread of execution independent of the others.

- To compile and run this program, on your commandline (shell/terminal/console), navigate to "week8" directory and run the following commands:
  - `javac TwentyNine.java`
  - `java TwentyNine ../pride-and-prejudice.txt`

##
### Dataspaces:
- **Thirty.java** program uses the “Dataspaces” style. In this style, one or more units exist that will execute concurrently. Also, one or more data spaces exist where concurrent units store and retrieve data from. There is no direct data exchanges between the concurrent units, other than via the data spaces.
- To compile and run this program, on your commandline (shell/terminal/console), navigate to "week8" directory and run the following commands:
  - `javac Thirty.java`
  - `java Thirty ../pride-and-prejudice.txt`

  ##
### Map-Reduce:
- **ThirtyTwo.java** program uses the “Map-reduce” style. In this style, input data is divided in chunks, similar to what an inverse multiplexer does to input signals. A map function applies a given worker function to each chunk of data, potentially in parallel. The results of the many worker functions are reshuffled in a way that allows for the reduce step to be also parallelized. The reshuffled chunks of data are given as input to a second map function that takes a reducible function as input

- To compile and run this program, on your commandline (shell/terminal/console), navigate to "week8" directory and run the following commands:
  - `javac ThirtyTwo.java`
  - `java ThirtyTwo ../pride-and-prejudice.txt`
