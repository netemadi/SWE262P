import java.io.File;
import java.util.*;
import java.io.IOException;
import java.util.function.BiConsumer;

public class Nine{
    interface IFunction{
        void call(Object arg, IFunction func);
    }

    public static void main (String[] args){
        try{
            if(args.length > 0){
                new LoadDataWords().call(args[0], new RemoveStopWords());
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

    private static class LoadDataWords implements IFunction{
        public void call(Object arg, IFunction func){
            LinkedList<String> words = new LinkedList<>();
            String path = (String)arg;
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
            //tells RemoveStopWrods to call CountingWords
            func.call(words, new CountingWords());
        }
    }
    
    private static class RemoveStopWords implements IFunction{
        public void call(Object arg, IFunction func ){
            LinkedList<String> words = (LinkedList<String>)arg;
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
            //RemoveStopWords tells CountingWords to call SortWords next
            func.call(words, new SortWords());
        }
    }

    private static class CountingWords implements IFunction{
        public void call(Object arg, IFunction func){
            LinkedHashMap<String, Integer> temp = new LinkedHashMap<>();
            LinkedList<String> words = (LinkedList<String>)arg;
            for(String word : words){
                if(temp.containsKey(word)){
                    temp.put(word, temp.get(word)+1);
                }
                else{
                    temp.put(word, 1);
                } 
            }
            //CountingWords calls Sorts and tells it to call PrintTop25
            func.call(temp, new PrintTop25());
        } 
    }

    //Sorting HashMap
    //citation: https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
    private static class SortWords implements IFunction{
        public void call(Object arg, IFunction func){
            LinkedHashMap<String, Integer> temp = (LinkedHashMap<String, Integer>)arg;
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
            //SortWords class printTop25 and tells it to call no_op (which is doing nothing and program ends)
            func.call(sortedMap, new NoOp());
        }
    }

    private static class PrintTop25 implements IFunction{
        public void call(Object arg, IFunction func){
            LinkedHashMap<String, Integer> freq_sorted = (LinkedHashMap<String, Integer>)arg;
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
            func.call(null, null);
        }
    }

    private static class NoOp implements IFunction{
        public void call(Object arg, IFunction func){
            return;
        }
    }
    
}

