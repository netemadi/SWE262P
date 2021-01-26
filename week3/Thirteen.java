import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.BiFunction;
import java.io.IOException;

public class Thirteen {
    public static void main (String[] args){
        try{
            if(args.length > 0){
                ((Runnable)stopWord_obj.get("init")).run();

                // https://www.geeksforgeeks.org/java-8-biconsumer-interface-in-java-with-examples/
                ((Consumer)dataWords_obj.get("init")).accept( args[0]);
            
                //https://mkyong.com/java8/java-8-supplier-examples/
                // System.out.println(((Supplier)dataWords_obj.get("words")).get());
                ArrayList<String> temp = (ArrayList<String>)((Supplier)dataWords_obj.get("words")).get();
                for(int i = 0; i < temp.size(); i++){
                    String word = temp.get(i);
                    if(!(Boolean)(((Function)stopWord_obj.get("is_stopWord")).apply(word)) && (word.length() >= 2)){
                        ((Consumer)wordFreq_obj.get("increment_count")).accept(word);
                    }
                }

                //for 13.2
                wordFreq_obj.put("top25", (Runnable) () -> printTop25(wordFreq_obj));
                ((Runnable)wordFreq_obj.get("top25")).run();
            }
            else{
                System.out.println("Error: file name must be passed as argument");
                System.exit(0);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    //read and put the stop words into a list
    private static void load_stop_words(LinkedHashMap<String, Object> obj){
        LinkedList<String> stopWords = new LinkedList<>();
        try{
            File file = new File("../stop_words.txt"); 
            Scanner sc = new Scanner(file); 
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] splitted = line.split(",");
                for (int i = 0; i<splitted.length; i++){
                    stopWords.add(splitted[i]);
                }
            }
            sc.close();
        }
        catch(IOException e){
            System.out.println(e + "couldn't open stop words file");
            System.exit(0);
        }
        obj.put("stop_words", stopWords);
    }

    //load the words from the input file from the user into a list
    private static void load_data_words(LinkedHashMap<String, Object> obj, String path){
        ArrayList<String> words = new ArrayList<>();
        try{
            File file = new File(path);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().toLowerCase();
                line = line.replaceAll("[^a-z0-9]", " ");
                String[] result = line.split(" ");
                for(int i =0 ; i < result.length;i++){
                    words.add(result[i]);
                }
            }
            sc.close();
        }
        catch(IOException e){
            System.out.println(e +  "Couldn't open file " + path);
            System.exit(0);
        }
        obj.put("data",words);
        // System.out.println(obj.get("data"));
    }

    //get the frequency of each word that is not a stop word
    private static void counting_words(LinkedHashMap<String, Object> obj, String word){
        LinkedHashMap<String, Integer> temp = (LinkedHashMap<String, Integer>)obj.get("freq");
        if(temp.containsKey(word)){
            temp.put(word, temp.get(word)+1);
        }
        else{
            temp.put(word, 1);
        } 
        obj.put("freq", temp);
    }
    //Sorting HashMap
    //citation: https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
    private static LinkedHashMap<String, Integer> sorted(LinkedHashMap<String, Object> obj){
        LinkedHashMap<String, Integer> temp = (LinkedHashMap<String, Integer>)obj.get("freq");
        List<String> mapKeys = new ArrayList<>(temp.keySet());
        List<Integer> mapValues = new ArrayList<>(temp.values());
        Collections.sort(mapValues, Collections.reverseOrder());
        Collections.sort(mapKeys);
    
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
    
        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
          Integer val = valueIt.next();
          Iterator<String> keyIt = mapKeys.iterator();
    
          while (keyIt.hasNext()) {
            String key = keyIt.next();
            Integer comp1 = temp.get(key);
            Integer comp2 = val;
    
            if (comp1 == comp2) {
              keyIt.remove();
              sortedMap.put(key, val);
              break;
            }
          }
        }
        return sortedMap;
    }

    //printing the the first 25 elements from the HashMap
    private static void printTop25(LinkedHashMap<String, Object> obj){
        LinkedHashMap<String, Integer> freq_sorted = (LinkedHashMap<String, Integer>)((Supplier)wordFreq_obj.get("sort")).get();
        int count = 0;
        for(Map.Entry<String, Integer> e : freq_sorted.entrySet()){
            if (count < 25){
            System.out.println(e.getKey()+" -> "+e.getValue());
            count += 1;
            }
            else{
                break;
            }
        }
    }

    private static LinkedHashMap<String, Object> stopWord_obj = new LinkedHashMap<>();
    static{
        stopWord_obj.put("stop_words", new LinkedList<String>());
        stopWord_obj.put("init", (Runnable)() -> load_stop_words(stopWord_obj));
        stopWord_obj.put("is_stopWord", (Function<String, Boolean>) w -> (((LinkedList<String>)(stopWord_obj.get("stop_words"))).contains(w)));
    }

    private static LinkedHashMap<String, Object> dataWords_obj = new LinkedHashMap<>();
    static{
        dataWords_obj.put("data", new ArrayList<>());
        dataWords_obj.put("init", (Consumer <String>)  p -> load_data_words(dataWords_obj,p));
        dataWords_obj.put("words", (Supplier<ArrayList<String>>) () -> (ArrayList<String>)dataWords_obj.get("data"));
    }

    private static LinkedHashMap<String, Object> wordFreq_obj = new LinkedHashMap<>();
    static{
        wordFreq_obj.put("freq", new LinkedHashMap<String, Integer>());
        wordFreq_obj.put("increment_count", (Consumer <String>)  word -> counting_words(wordFreq_obj,word));
        wordFreq_obj.put("sort",(Supplier<LinkedHashMap<String, Integer>>)() -> sorted(wordFreq_obj));
    }
}
