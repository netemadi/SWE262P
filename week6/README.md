Here we have three programs: 
- TwentySix.java 
- TwentyEight.java

Both of these programs are doing the same task, which is counting the words in a file given by the user in the commandline. The difference between them is the style that we have used to develop the program.


### Tabular:
- **TwentySix.java** program uses the “Tabular” style. In this style, the input data of the problem is modeled as entities with relations between them. Based on how the problem is modeled, some tables can be created, and then, data is placed in tables . It is possible that columns of some tables have cross-referencing data in other tables. To retrives data from these table relational query engine will be used. The problem is solved by issuing queries over the tabular data

- To compile and run this program, you need to have the `sqlite-jdbc-3.32.3.2.jar` file also. Then, on your commandline (shell/terminal/console), navigate to "week6" directory and run the following commands:
  - `javac TwentySix.java`
  - `java -cp .:sqlite-jdbc-3.32.3.2.jar TwentySix ../pride-and-prejudice.txt`
  or
  `java -cp .:../target/dependency/sqlite-jdbc-3.34.0.jar TwentySix ../pride-and-prejudice.txt`






### Lazy Rivers:
- **TwentyEight.java** program uses the “Lazy Rivers” style. In this style, data comes to functions in streams, rather than as a complete whole all at at once. When implementing functions in this style, they will be methods for apllying filters or transforming from one kind of data stream to another.

- To compile and run this program, on your commandline (shell/terminal), navigate to "week2" directory and run the following commands:
  - `javac TwentyEight.java`
  - `java TwentyEight ../pride-and-prejudice.txt`
