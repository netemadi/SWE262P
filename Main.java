import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// import java.util.Scanner;
// import java.util.LinkedList;
// import java.util.HashMap;
import java.util.*;

public class Main{
  public static void main (String[] args){
    try{            
      File file = new File("stop_words.txt"); 
      Scanner sc = new Scanner(file); 
      LinkedList<String> stopWords = readStopWords(sc);
      sc.close();

      String input_file =  "";
      if(args.length > 0){
        input_file = args[0];
      }
      else{
        System.out.println("Error: file name must be passed as argument");
        System.exit(0);
      }
      
      file = new File(input_file);
      sc = new Scanner(file);
      HashMap<String, Integer> hm = readInputFile(sc, stopWords);
      printTop25(hm);
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }

  private static LinkedList<String> readStopWords(Scanner sc){
    LinkedList<String> ll = new LinkedList<>();
    while(sc.hasNextLine()){
      String line = sc.nextLine();
      String[] splitted = line.split(",");
      for (int i = 0; i<splitted.length; i++){
        ll.add(splitted[i]);
      }
    }
    return ll;
  }

  //Read and tokenize file passed by user
  private static HashMap<String, Integer> readInputFile(Scanner sc, LinkedList sp){
    HashMap<String, Integer> to_return = new HashMap<>();
    while (sc.hasNextLine()) {
      String line = sc.nextLine().toLowerCase();
      line = line.replaceAll("[^a-z0-9]", " ");
      String[] result = line.split(" ");
      for(int i =0 ; i < result.length;i++){
        String word = result[i];
        if(!word.contains(" ")){
          if (!(word.length() < 2) && !word.matches("\\d+") && !sp.contains(word)){
            if(to_return.containsKey(word)){
              to_return.put(word, to_return.get(word)+1);
            }
            else{
              to_return.put(word, 1);
            }
          }
        }
      }
    }
    // System.out.println(to_return);
    return sortHashMapByValues(to_return);
  }

  //Sorting HashMap
  //citation: https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
  private static LinkedHashMap<String, Integer> sortHashMapByValues(
        HashMap<String, Integer> passedMap) {
    List<String> mapKeys = new ArrayList<>(passedMap.keySet());
    List<Integer> mapValues = new ArrayList<>(passedMap.values());
    Collections.sort(mapValues, Collections.reverseOrder());
    Collections.sort(mapKeys);

    LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

    Iterator<Integer> valueIt = mapValues.iterator();
    while (valueIt.hasNext()) {
      Integer val = valueIt.next();
      Iterator<String> keyIt = mapKeys.iterator();

      while (keyIt.hasNext()) {
        String key = keyIt.next();
        Integer comp1 = passedMap.get(key);
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
  private static void printTop25(HashMap<String, Integer> m){
    int count = 0;
    for(Map.Entry<String, Integer> e : m.entrySet()){
      if (count < 25){
        System.out.println(e.getKey()+"->"+e.getValue());
        count += 1;
      }
      else{
        break;
      }
    }
  }


}