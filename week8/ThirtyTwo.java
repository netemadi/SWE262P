import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.stream.Stream;


public class ThirtyTwo{
    private static Scanner read_file(String path) throws Exception{
        Scanner sc;
        File file = new File(path);
        sc = new Scanner(file);
        return sc;
    }

    private static Iterable<String> partition(Scanner sc, int nlines){
        Stream.Builder<String> lines = Stream.builder();
        int count = 1; 
        String join = "";
        while(sc.hasNextLine()){
            if(count == nlines){
                join = join+ " "+sc.nextLine();
                lines.add(join);
                join ="";
                count=1;
            }
            else{
                join = join+" "+sc.nextLine();
                count++;
            }
        }
        Iterable<String> to_return = () -> lines.build().iterator();
        return to_return;
    }

    private static LinkedList<String> scan(String s){
        LinkedList<String> words = new LinkedList<>();
        String line = s.toLowerCase();
        line = line.replaceAll("[^a-z0-9]", " ");
        String[] result = line.split(" ");
        for(int i =0 ; i < result.length;i++){
            if(result[i].length()>=2)
                words.add(result[i]);

        }
        return words;
    }

    private static LinkedList<String> remove_stop_words (LinkedList<String> words){
        // LinkedList<String> words = (LinkedList<String>)arg;
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
            //https://stackoverflow.com/questions/41379405/how-to-iterate-through-linkedlist-and-remove-certain-words-from-it-in-java
            for(Iterator<String> iter = words.iterator(); iter.hasNext();) {
                String e = iter.next();
                if(stopWords.contains(e) || e.length()<2){
                    iter.remove();
                }
            }
            
        }
        catch(IOException e){
            System.out.println(e + "couldn't open stop words file");
            System.exit(0);
        }
        return words;
    }

    private static LinkedList<LinkedList<Object>> split_words(String data_str){
        LinkedList<String> words = remove_stop_words(scan(data_str));
        LinkedList<LinkedList<Object>> result = new LinkedList<>();
        for (String w: words){
            LinkedList<Object> temp = new LinkedList<>();
            temp.add(w);
            temp.add(1);
            result.add(temp);
        }
        return result;

    }

    private static LinkedHashMap<String, LinkedList<LinkedList<Object>>> regroup(LinkedList<LinkedList<LinkedList<Object>>>  pairs_list){
        LinkedHashMap<String, LinkedList<LinkedList<Object>>> mapping = new LinkedHashMap<>();
        for(LinkedList<LinkedList<Object>> pairs: pairs_list){
            for(LinkedList<Object> p: pairs){
                if(mapping.containsKey((String)p.get(0))){
                    // mapping.put((String)p.get(0), mapping.get((String)p.get(0)).add(p));
                    mapping.get((String)p.get(0)).add(p);
                }
                else{
                    LinkedList<LinkedList<Object>> temp = new LinkedList<>();
                    temp.add(p);
                    mapping.put((String)p.get(0),temp);
                }
            }

        }
        return mapping;
    }

    //Sorting HashMap
        //citation: https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
        private static LinkedHashMap<String, Integer> sorted(LinkedHashMap<String, Integer> wordCount){
            List<String> mapKeys = new ArrayList<>(wordCount.keySet());
            List<Integer> mapValues = new ArrayList<>(wordCount.values());
            Collections.sort(mapValues, Collections.reverseOrder());
            Collections.sort(mapKeys);
        
            LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        
            Iterator<Integer> valueIt = mapValues.iterator();
            while (valueIt.hasNext()) {
              Integer val = valueIt.next();
              Iterator<String> keyIt = mapKeys.iterator();
        
              while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = wordCount.get(key);
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

    private static LinkedHashMap<String, Integer> counting_words(Map.Entry<String, LinkedList<LinkedList<Object>>> e){
        //reduce
        int total = 0;
        for(LinkedList<Object> pair: e.getValue()){
            total += (Integer)pair.get(1);
        }
        
        LinkedHashMap to_return = new LinkedHashMap<>();
        to_return.put(e.getKey(), total);
        return to_return;
    }

    public static void main(String[] args) throws Exception {
        if(!(args.length > 0)){
            System.out.println("Error: file name must be passed as argument");
            System.exit(0);
        }
        LinkedList<LinkedList<LinkedList<Object>>> splits = new LinkedList<>();
        for(String s: partition(read_file(args[0]), 200)){
            splits.add(split_words(s));
        }
        LinkedHashMap<String, LinkedList<LinkedList<Object>>> splits_per_word = regroup(splits);
        LinkedHashMap<String, Integer> word_freqsss= new LinkedHashMap<>();
        for(Map.Entry<String, LinkedList<LinkedList<Object>>> e: splits_per_word.entrySet()){
           LinkedHashMap<String, Integer> word_freq = counting_words(e);
           for(Map.Entry<String, Integer> e2: word_freq.entrySet()){
               word_freqsss.put(e2.getKey(), e2.getValue());
           }
        }
        LinkedHashMap<String, Integer> sortedMap = sorted(word_freqsss);


        int count = 0;
        for(Map.Entry<String, Integer> e : sortedMap.entrySet()){
            if (count < 25){
            System.out.println(e.getKey()+" - "+e.getValue());
            count += 1;
            }
            else{
                break;
            }
        }
    }
}