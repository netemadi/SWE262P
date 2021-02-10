import java.io.*;
import java.util.*;
import java.lang.reflect.*;



public class Seventeen {
    /*
     * The main function
     */
    public static void main(String[] args) throws Exception {
        new WordFrequencyController(args[0]).run();

        String userInput = "";
        Scanner in = new Scanner(System.in);
        System.out.print("Please enter a class name to get its Info: ");
        userInput = in.nextLine();
        in.close();
        if(!userInput.equals("")){
            printInfoClass(userInput);
        }
    }

    private static void printInfoClass(String classInput){
        Class cls = null;
        try{
            cls = Class.forName(classInput);
        }
        catch(Exception e){
            System.out.println("No class " + classInput + " was found");
        }

        if(cls != null){
            boolean found = false;
            System.out.println("---Getting the class fields and their types:");
            Field[] fields = cls.getDeclaredFields();
            for (Field f:fields){
                found = true;
                System.out.println("Field Name is " + f.getName() +" and has type " + f.getType());
            }
            if (!found){
                System.out.println("No field was found for class " + classInput);
            }else{found = false;}

            System.out.println("\n---Getting the class methods:");
            Method[] methods = cls.getDeclaredMethods();
            for(Method m: methods){
                found = true;
                System.out.println("Method name is " + m.getName());
            }
            if (!found){
                System.out.println("No method was found for class " + classInput);
            }else{found = false;}

            System.out.println("\n---Getting all of the class superClasses:");
            Class super_class = cls.getSuperclass();
            while(super_class != null){
                found = true;
                System.out.println("Super class name is " + super_class.getName());
                super_class = super_class.getSuperclass();
            }
            if (!found){
                System.out.println("No Super class was found for class " + classInput);
            }else{found = false;}

            System.out.println("\n---Getting the the class interfaces:");
            Class[] class_intefaces = cls.getInterfaces();
            for(Class class_inteface: class_intefaces){
                found = true;
                System.out.println("Interface name is " + class_inteface.getName());
            }
            if (!found){
                System.out.println("No Interface was found for class " + classInput);
            }else{found = false;}
        }

    }
}

/*
 * The classes
 */

abstract class TFExercise {
    public String getInfo() {
        return this.getClass().getName();
    }
}

//Using invoke in the run method of the controller class
class WordFrequencyController extends TFExercise {
    private Class storageManagerClass;
    private DataStorageManager storageManager;
    private Class stopWordManagerClass;
    private StopWordManager stopWordManager;
    private Class wordFreqManagerClass;
    private WordFrequencyManager wordFreqManager;
    
    public WordFrequencyController(String pathToFile) throws IOException {
        try{
            this.storageManagerClass = Class.forName("DataStorageManager");
            this.storageManager = ((Class<DataStorageManager>)this.storageManagerClass).getDeclaredConstructor(String.class).newInstance(pathToFile);
            
            this.stopWordManagerClass = Class.forName("StopWordManager");
            this.stopWordManager = ((Class<StopWordManager>)this.stopWordManagerClass).getDeclaredConstructor().newInstance();

            this.wordFreqManagerClass = Class.forName("WordFrequencyManager");
            this.wordFreqManager = ((Class<WordFrequencyManager>)this.wordFreqManagerClass).getDeclaredConstructor().newInstance();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void run() {
        try{
        Method get_words = this.storageManagerClass.getDeclaredMethod("getWords");
        List<String> words = (List<String>)get_words.invoke(this.storageManager);

        Method is_stop_word = this.stopWordManagerClass.getDeclaredMethod("isStopWord", String.class);
        Method increment_count = this.wordFreqManagerClass.getDeclaredMethod("incrementCount", String.class);

        for (String word : words) {
            if (!((Boolean)is_stop_word.invoke(this.stopWordManager, word))) {
                increment_count.invoke(this.wordFreqManager, word);
            }
        }
        
        Method sorted_method = this.wordFreqManagerClass.getDeclaredMethod("sorted");
        List<WordFrequencyPair> sortedList = (List<WordFrequencyPair>)sorted_method.invoke(this.wordFreqManager);
        int numWordsPrinted = 0;
        Class<WordFrequencyPair> wfp_class = (Class<WordFrequencyPair>)Class.forName("WordFrequencyPair");
        WordFrequencyPair pair;
        for (int i=0; i< sortedList.size();i++) {
            pair = sortedList.get(i);
            String s = (String)wfp_class.getDeclaredMethod("getWord").invoke(pair);
            s += " - ";
            s += ((Integer)wfp_class.getDeclaredMethod("getFrequency").invoke(pair)).toString();
            System.out.println(s);
            
            numWordsPrinted++;
            if (numWordsPrinted >= 25) {
                break;
            }
        }
    }
    catch(Exception e){
        System.out.println(e);
    }
    }
}

/** Models the contents of the file. */
class DataStorageManager extends TFExercise {
    private List<String> words;
    
    public DataStorageManager(String pathToFile) throws IOException {
        this.words = new ArrayList<String>();
        
        Scanner f = new Scanner(new File(pathToFile), "UTF-8");
        try {
            f.useDelimiter("[\\W_]+");
            while (f.hasNext()) {
                this.words.add(f.next().toLowerCase());
            }
        } finally {
            f.close();
        }
    }
    
    public List<String> getWords() {
        return this.words;
    }
    
    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.words.getClass().getName();
    }
}

/** Models the stop word filter. */
class StopWordManager extends TFExercise {
    private Set<String> stopWords;
    
    public StopWordManager() throws IOException {
        this.stopWords = new HashSet<String>();
        
        Scanner f = new Scanner(new File("../stop_words.txt"), "UTF-8");
        try {
            f.useDelimiter(",");
            while (f.hasNext()) {
                this.stopWords.add(f.next().trim());
            }
        } finally {
            f.close();
        }

        // Add single-letter words
        for (char c = 'a'; c <= 'z'; c++) {
            this.stopWords.add("" + c);
        }
    }
    
    public boolean isStopWord(String word) {
        return this.stopWords.contains(word);
    }
    
    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.stopWords.getClass().getName();
    }
}

/** Keeps the word frequency data. */
class WordFrequencyManager extends TFExercise {
    private Map<String, MutableInteger> wordFreqs;
    
    public WordFrequencyManager() {
        this.wordFreqs = new HashMap<String, MutableInteger>();
    }
    
    public void incrementCount(String word) {
        MutableInteger count = this.wordFreqs.get(word);
        if (count == null) {
            this.wordFreqs.put(word, new MutableInteger(1));
        } else {
            count.setValue(count.getValue() + 1);
        }
    }
    
    public List<WordFrequencyPair> sorted() {
        List<WordFrequencyPair> pairs = new ArrayList<WordFrequencyPair>();
        for (Map.Entry<String, MutableInteger> entry : wordFreqs.entrySet()) {
            pairs.add(new WordFrequencyPair(entry.getKey(), entry.getValue().getValue()));
        }
        Collections.sort(pairs);
        Collections.reverse(pairs);
        return pairs;
    }
    
    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.wordFreqs.getClass().getName();
    }
}

class MutableInteger {
    private int value;
    
    public MutableInteger(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
}

class WordFrequencyPair implements Comparable<WordFrequencyPair> {
    private String word;
    private int frequency;
    
    public WordFrequencyPair(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }
    
    public String getWord() {
        return word;
    }
    
    public int getFrequency() {
        return frequency;
    }
    
    public int compareTo(WordFrequencyPair other) {
        return this.frequency - other.frequency;
    }
}