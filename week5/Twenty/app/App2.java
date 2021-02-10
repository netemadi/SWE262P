import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// import java.util.Scanner;
// import java.util.LinkedList;
// import java.util.HashMap;
import java.util.*;

public class App2 implements I_TFFreqs, I_TFWords{
  @Override
  public ArrayList<String> extractWords(String pathString){
    ArrayList<String> al = new ArrayList<>();
    try{            
      File file = new File("../../../stop_words.txt"); 
      Scanner sc = new Scanner(file); 
      LinkedList<String> stopWords = readStopWords(sc);
      sc.close();

      file = new File(pathString);
      sc = new Scanner(file);
      al = readInputFile(sc, stopWords);
    }
    catch (Exception e){
      System.out.println("Couldn't read file. " + e.getMessage());
      return new ArrayList<String>();
    }
    return al;
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
  private static ArrayList<String> readInputFile(Scanner sc, LinkedList<String> sp){
    ArrayList<String> to_return = new ArrayList<>();
    while (sc.hasNextLine()) {
      String line = sc.nextLine().toLowerCase();
      line = line.replaceAll("[^a-z0-9]", " ");
      String[] result = line.split(" ");
      for(int i =0 ; i < result.length;i++){
        String word = result[i];
        if(!word.contains(" ")){
          if (!(word.length() < 2) && !word.matches("\\d+") && !sp.contains(word)){
            to_return.add(word);
          }
        }
      }
    }
    // System.out.println(to_return);
    return to_return;
  }

  @Override
  public LinkedHashMap<String,Integer> top25(ArrayList<String> words){
    LinkedHashMap<String, Integer> cw = countWords(words);
    cw = sortHashMapByValues(cw);
    return getTop25(cw);

  }

  private LinkedHashMap<String,Integer> countWords(ArrayList<String> word_param){
        LinkedHashMap<String, Integer> wordCount = new LinkedHashMap<>();
        for(int i=0; i<word_param.size(); i++){
            String word = word_param.get(i);
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
  private LinkedHashMap<String, Integer> sortHashMapByValues(
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
  private LinkedHashMap<String,Integer> getTop25(HashMap<String, Integer> m){
    LinkedHashMap<String,Integer>to_return = new LinkedHashMap<>();
    int count = 0;
    for(Map.Entry<String, Integer> e : m.entrySet()){
      if (count < 25){
        to_return.put(e.getKey(), e.getValue());
        count += 1;
      }
      else{
        break;
      }
    }
    return to_return;
  }


}