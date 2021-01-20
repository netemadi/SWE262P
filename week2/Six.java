import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Six{
    public static void main (String[] args){
        try{             
            printTop25(sortHashMapByValues(counting_words(remove_stopWords(read_and_extract_words(check_arguments(args))))));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Expect an argument as a file path to read from. 
    //Return the path to the file if it was given.
    //If not enough argument, exit the program
    private static String check_arguments(String[] args){
        String input_file_path = "";
        if(args.length > 0){
            input_file_path = args[0];
        }
        else{
            System.out.println("Error: file name must be passed as argument");
            System.exit(0);
        }
        return input_file_path;
    } 

    //Read the file from user, extract words, and add to a list.
    //Return the list of words
    private static ArrayList<String> read_and_extract_words(String path) throws Exception{
        File file = new File(path);
        Scanner sc = new Scanner(file);
        ArrayList<String> words = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().toLowerCase();
            line = line.replaceAll("[^a-z0-9]", " ");
            String[] result = line.split(" ");
            for(int i =0 ; i < result.length;i++){
                words.add(result[i]);
            }
        }
        sc.close();
        return words;
    }

    //remove the stop words from the list of words which is passed as the argument
    private static ArrayList<String> remove_stopWords(ArrayList<String> words_al) throws Exception{
        LinkedList<String> stopWords = new LinkedList<>();
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
        for(int i=0; i<words_al.size(); i++){
            String word = words_al.get(i);
            if((word.length()<2) || stopWords.contains(word)){
                words_al.remove(i);
                i--;
            }
        }
        return words_al;
    }

    //Find the frequency of each word from the given list of words
    private static LinkedHashMap<String,Integer> counting_words(ArrayList<String> al){
        LinkedHashMap<String, Integer> wordCount = new LinkedHashMap<>();
        for(int i=0; i<al.size(); i++){
            String word = al.get(i);
            if(wordCount.containsKey(word)){
                wordCount.put(word, wordCount.get(word)+1);
            }
            else{
                wordCount.put(word, 1);
            } 
        }
        return wordCount;
    }

  //Sorting HashMap
  //citation: https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
  private static LinkedHashMap<String, Integer> sortHashMapByValues(LinkedHashMap<String,Integer> lhm) {
    List<String> mapKeys = new ArrayList<>(lhm.keySet());
    List<Integer> mapValues = new ArrayList<>(lhm.values());
    Collections.sort(mapValues, Collections.reverseOrder());
    Collections.sort(mapKeys);

    LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

    Iterator<Integer> valueIt = mapValues.iterator();
    while (valueIt.hasNext()) {
      Integer val = valueIt.next();
      Iterator<String> keyIt = mapKeys.iterator();

      while (keyIt.hasNext()) {
        String key = keyIt.next();
        Integer comp1 = lhm.get(key);
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
  private static void printTop25(LinkedHashMap<String, Integer> lhm){
    int count = 0;
    for(Map.Entry<String, Integer> e : lhm.entrySet()){
      if (count < 25){
        System.out.println(e.getKey()+" -> "+e.getValue());
        count += 1;
      }
      else{
        break;
      }
    }
  }
}