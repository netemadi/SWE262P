import java.io.File;
import java.io.IOException;
import java.util.*;

public class TwentyTwo{
    public static void main (String[] args){
        try{
            assert args.length > 0 : "I need an input file!";        
            LinkedHashMap<String, Integer> word_freqs = sortHashMapByValues(counting_words(remove_stopWords(read_and_extract_words(args[0]))));

            assert (word_freqs instanceof Map) : "OMG! This is not a Map!";
            assert (word_freqs.size() >= 25) : "SRSLY? Less than 25 words!";
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
        catch (Exception e){
           System.out.println("Something wrong: " + e.getMessage());
           e.printStackTrace(); 
        }

    }


    //Read the file from user, extract words, and add to a list.
    //Return the list of words
    private static ArrayList<String> read_and_extract_words(Object path) throws Exception{
        assert (path instanceof String) : "I need a string! (extract words)";
        assert (path != null) : "I need a non-empty string! (extract words)";

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
            throw e;
        }
        return words;
    }

    //remove the stop words from the list of words which is passed as the argument
    private static ArrayList<String> remove_stopWords(Object words_alParam) throws Exception{
        assert (words_alParam instanceof List) : "I need a list! (remove_stopWords)";

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
            throw e;
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
    private static LinkedHashMap<String,Integer> counting_words(Object alParam) throws Exception{
        assert (alParam instanceof List) : "I need a list! (counting_words/frequencies)";
        assert !((List)alParam).isEmpty() : "I need a non-empty list! (counting_words/frequencies)";

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
  private static LinkedHashMap<String, Integer> sortHashMapByValues(Object lhmParam) throws Exception{
    assert (lhmParam instanceof Map) : "I need a map (sort)";
    assert !((Map)lhmParam).isEmpty() : "I need a non-emoty map (sort)";

    LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> lhm = (LinkedHashMap<String, Integer>)lhmParam;
    try{
        List<String> mapKeys = new ArrayList<>(lhm.keySet());
        List<Integer> mapValues = new ArrayList<>(lhm.values());
        Collections.sort(mapValues, Collections.reverseOrder());
        Collections.sort(mapKeys);

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
    } catch(Exception e){
        System.out.println("Sorted threw " + e.getMessage());
        throw e;
    }
    return sortedMap;
  }

}