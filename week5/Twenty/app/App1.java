import java.io.*;
import java.util.*;



public class App1 implements I_TFFreqs, I_TFWords{
    
    //Read the file from user, extract words, and add to a list.
    //Return the list of words
    @Override
    public ArrayList<String> extractWords(String pathString){
        Scanner sc = null;
        ArrayList<String> words = new ArrayList<>();
        try{
            File file = new File(pathString);
            sc = new Scanner(file);
        } catch (Exception e){
            System.out.println("Couldn't extract words from file. " + e.getMessage());
            return new ArrayList<>();
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine().toLowerCase();
            line = line.replaceAll("[^a-z0-9]", " ");
            String[] result = line.split(" ");
            for(int i =0 ; i < result.length;i++){
                words.add(result[i]);
            }
        }
        sc.close();
        return remove_stopWords(words);
    }

    private ArrayList<String> remove_stopWords(ArrayList<String> words){
        LinkedList<String> stopWords = new LinkedList<>();
        Scanner sc = null;
        try{
        File file = new File("../../../stop_words.txt"); 
        sc = new Scanner(file);
        } catch(Exception e){
            System.out.println("Couldn't remove stop words. " + e.getMessage());
            return words;
        }
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String[] splitted = line.split(",");
            for (int i = 0; i<splitted.length; i++){
                stopWords.add(splitted[i]);
            }
        }
        sc.close();
        for(int i=0; i < words.size(); i++){
            String word = words.get(i);
            if((word.length()<2) || stopWords.contains(word)){
                words.remove(i);
                i--;
            }
        }
        return words;
    }

    @Override
    public LinkedHashMap<String,Integer> top25(ArrayList<String> words){
        LinkedHashMap<String,Integer> to_return = new LinkedHashMap<>();
        LinkedHashMap<String,Integer> counted = counting_words(words);
        counted = sortHashMapByValues(counted);
        int count = 0;
        for(Map.Entry<String, Integer> e : counted.entrySet()){
          if (count < 25){
            to_return.put(e.getKey(),e.getValue());
            count += 1;
          }
          else{
            break;
          }
        }
        return to_return;
    }

    private LinkedHashMap<String,Integer> counting_words(ArrayList<String> word_param){
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
  private LinkedHashMap<String, Integer> sortHashMapByValues(LinkedHashMap<String,Integer> lhm) {
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

}