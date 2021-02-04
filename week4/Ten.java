import java.io.File;
import java.util.*;
import java.io.IOException;

public class Ten{
    interface IFunction{
        Object call(Object arg);
    }

    public static void main (String[] args){
        try{
            if(args.length > 0){
                TFTheOne tf_theOne = new TFTheOne(args[0]);
                tf_theOne.bind(new LoadDataWords()).bind(new RemoveStopWords()).bind(new CountingWords()).bind(new SortWords()).bind(new GetTop25());
                tf_theOne.printMe();
               
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

    private static class TFTheOne{
        private Object value;
        public TFTheOne(Object v){
            value = v;
        }
        public TFTheOne bind(IFunction func){
            value = func.call(value);
            return this;
        } 
        public void printMe(){
            System.out.println(value);
        }
    }


    private static class LoadDataWords implements IFunction{
        public Object call(Object arg){
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
            return words;
        }
    }
    
    private static class RemoveStopWords implements IFunction{
        public Object call(Object arg){
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
            return words;
        }
    }

    private static class CountingWords implements IFunction{
        public Object call(Object arg){
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
            return temp;
        } 
    }

    //Sorting HashMap
    //citation: https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
    private static class SortWords implements IFunction{
        public Object call(Object arg){
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
            return sortedMap;
        }
    }

    private static class GetTop25 implements IFunction{
        public Object call(Object arg){
            LinkedHashMap<String, Integer> freq_sorted = (LinkedHashMap<String, Integer>)arg;
            int count = 0;
            String to_return = "";
            for(Map.Entry<String, Integer> e : freq_sorted.entrySet()){
                if (count < 25){
                    to_return += e.getKey()+" - "+e.getValue();
                    if(count != 24){
                        to_return += "\n";
                    }
                    count += 1;
                }
                else{
                    break;
                }
            }
            return to_return;
        }
    }
}

