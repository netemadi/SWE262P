import java.io.File;
import java.io.IOException;
import java.util.*;

public class TwentyOne{
    public static void main (String[] args){
        String fileName = "";
        if(args.length > 0){
            fileName = args[0];
        }
        else{
            fileName = "input.txt";
        }           
        LinkedHashMap<String, Integer> word_freqs = sortHashMapByValues(counting_words(remove_stopWords(read_and_extract_words(fileName))));

        int count = 0;
        for(Map.Entry<String, Integer> e : word_freqs.entrySet()){
          if (count < 25){
            System.out.println(e.getKey()+" - "+e.getValue());
            count += 1;
          }
          else{
            break;
          }
        }

    }


    //Read the file from user, extract words, and add to a list.
    //Return the list of words
    private static ArrayList<String> read_and_extract_words(Object path){
        if (!(path instanceof String) || path == null){
            return new ArrayList<>();
        }

        ArrayList<String> words = new ArrayList<>();
        try{
            File file = new File((String)path);
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
        } catch (IOException e){
            System.out.println("I/O error(" + e.getMessage() + ") when opening " + path + ":");
            e.printStackTrace();
            return new ArrayList<>();
        }
        return words;
    }

    //remove the stop words from the list of words which is passed as the argument
    private static ArrayList<String> remove_stopWords(Object words_alParam){
        if (!(words_alParam instanceof List)){
            return new ArrayList<>();
        }
        LinkedList<String> stopWords = new LinkedList<>();
        ArrayList<String> words_al = (ArrayList)words_alParam;
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
        } catch (IOException e){
            System.out.println("I/O error(" + e.getMessage() + ") when opening ../stop_words.txt:");
            e.printStackTrace();
            return words_al;
        }

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
    private static LinkedHashMap<String,Integer> counting_words(Object alParam){
        if(!(alParam instanceof List) || ((List)alParam).isEmpty()){
            return new LinkedHashMap<>();
        }
        LinkedHashMap<String, Integer> wordCount = new LinkedHashMap<>();
        ArrayList<String> al = (ArrayList<String>)alParam;
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
  private static LinkedHashMap<String, Integer> sortHashMapByValues(Object lhmParam) {
    if(!(lhmParam instanceof Map) || ((Map)lhmParam).isEmpty()){
        return new LinkedHashMap<>();
    }
    LinkedHashMap<String, Integer> lhm = (LinkedHashMap<String, Integer>)lhmParam;
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