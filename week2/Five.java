import java.io.File;
import java.io.IOException;
import java.util.*;

public class Five{
    private static LinkedList<String> stopWords = new LinkedList<>();
    private static String input_file = "";
    private static ArrayList<String> words = new ArrayList<>();
    private static LinkedHashMap<String, Integer> word_count = new LinkedHashMap<>();

    public static void main (String[] args){
        try{             
            //expect an argument as a file path to read from, 
            //if not enough argument, exit the program
            check_arguments(args);
            
            readStopWords();
            read_and_extract_words();
            remove_stopWords();
            counting_words();
            sortHashMapByValues();
            
            //printing the the first 25 elements from the HashMap
            int count = 0;
            for(Map.Entry<String, Integer> e : word_count.entrySet()){
              if (count < 25){
                System.out.println(e.getKey()+" -> "+e.getValue());
                count += 1;
              }
              else{
                break;
              }
            }
  
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void check_arguments(String[] args){
        if(args.length > 0){
            input_file = args[0];
        }
        else{
            System.out.println("Error: file name must be passed as argument");
            System.exit(0);
        }
    } 

    private static void readStopWords() throws Exception{
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

    private static void read_and_extract_words() throws Exception{
        File file = new File(input_file);
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

    private static void remove_stopWords(){
        for(int i=0; i<words.size(); i++){
            String word = words.get(i);
            if((word.length()<2) || stopWords.contains(word)){
                words.remove(i);
                i--;
            }
        }
    }

    private static void counting_words(){
        for(int i=0; i<words.size(); i++){
            String word = words.get(i);
            if(word_count.containsKey(word)){
                word_count.put(word, word_count.get(word)+1);
            }
            else{
                word_count.put(word, 1);
            } 
        }
    }

  //Sorting HashMap
  //citation: https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
  private static void sortHashMapByValues() {
    List<String> mapKeys = new ArrayList<>(word_count.keySet());
    List<Integer> mapValues = new ArrayList<>(word_count.values());
    Collections.sort(mapValues, Collections.reverseOrder());
    Collections.sort(mapKeys);

    LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

    Iterator<Integer> valueIt = mapValues.iterator();
    while (valueIt.hasNext()) {
      Integer val = valueIt.next();
      Iterator<String> keyIt = mapKeys.iterator();

      while (keyIt.hasNext()) {
        String key = keyIt.next();
        Integer comp1 = word_count.get(key);
        Integer comp2 = val;

        if (comp1 == comp2) {
          keyIt.remove();
          sortedMap.put(key, val);
          break;
        }
      }
    }
    word_count = sortedMap;
  }
}