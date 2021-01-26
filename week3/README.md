Here we have three programs:

Twelve.java
Thirteen.java
Fifteen.java
All 3 of them are doing the same task, which is counting the words in a file given by the user in the commandline. The difference between them is the style that we have used to develop the program.

### Letterbox:
- **Twelve.java** program uses the “Letterbox” style. With this style, large problem can be decomposed to smaller tasks, and each task can be come a "thing" like an object. This style is based on messages that can be sent from one "thing" to another one. Each "thing" needs to dispatch the messages, and based on that, it needs to perform some actions or send a message to another "thing" or capsule.
- To compile and run this program, on your commandline (shell/terminal), navigate to "week3" directory and run the following commands:
  - javac Twelve.java
  - java Twelve ../pride-and-prejudice.txt






### Closed Maps:
- **Thirteen.java** program uses the “Closed Maps” style. In this style, larger problems are decomposed into "things" that make sense for the problem domain. This style is based on the maps that have some keys and value. Some of the values in this map can be procedures/functions.
- To compile and run this program, on your commandline (shell/terminal), navigate to "week3" directory and run the following commands:
  - javac Thirteen.java
  - java Thirteen ../pride-and-prejudice.txt


### Hollywood agent: "don't call us, we'll call you":
- **Fifteen.java** program uses the “Hollywood style. In this style, Larger problem is decomposed into entities using some form of abstraction (objects, modules or similar). The entities are never called on directly for actions. The entities provide interfaces for other entities to be able to register callbacks. At certain points of the computation, the entities call on the other entities that have registered for callbacks.
- This program added a new functionality to also count all the words that have letter z. After printing the top 25 words with the largest frequencies, the progrom will print the number of words with letter z
- To compile and run this program, on your commandline (shell/terminal), navigate to "week3" directory and run the following commands:
  - javac Fifteen.java
  - java Fifteen ../pride-and-prejudice.txt
