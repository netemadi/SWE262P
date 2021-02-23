import java.io.File;
import java.util.*;
import java.io.IOException;

public class TwentyFive{
    
    interface IFunction{
        Object call(Object arg);
    }
    interface IFunction2{
        Object call2();
    }
    
    private static String[] savedArgs;
    
    public static void main (String[] args){
        savedArgs = args;
        try{
            if(args.length > 0){
                (new TFQuarantine(new GetInput()))
                .bind(new ExtractWords())
                .bind(new RemoveStopWords())
                .bind(new CountingWords())
                .bind(new SortWords())
                .bind(new GetTop25())
                .execute();
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

    private static class TFQuarantine{
        private LinkedList<Object> funcs = new LinkedList<>();
        public TFQuarantine(Object func){
            this.funcs.add(func);
        }
        public TFQuarantine bind(IFunction func){
            this.funcs.add(func);
            return this;
        } 

        public void execute(){
            Object value = "";
            for (int i=0; i < this.funcs.size(); i++ ){
                if(guard_callable(value)){
                    value = ((IFunction)this.funcs.get(i)).call(((IFunction2)value).call2());
                }
                else{
                    value = ((IFunction)this.funcs.get(i)).call(value);
                }
            }
            System.out.println(guard_callable(value)?((IFunction2)value).call2():value);
        }

        private boolean guard_callable(Object v){
            if (v instanceof IFunction2){
                return true;
            }
            else{
                return false;
            }
        }
    }

    private static class GetInput implements IFunction{
        private static class F implements IFunction2{
            public Object call2(){
                return savedArgs[0];
            }
        }
        public Object call(Object arg){
            return new F();
        }
    }

    private static class ExtractWords implements IFunction{
        private static String path;
        private static class F implements IFunction2{
            public Object call2(){
                LinkedList<String> words = new LinkedList<>();
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
        public Object call(Object arg){
            this.path = (String)arg;
            return new F();
        }
    }
    
    private static class RemoveStopWords implements IFunction{
        private static LinkedList<String> words;
        private static class F implements IFunction2{
            public Object call2(){
                
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
        public Object call(Object arg){
            this.words = (LinkedList<String>)arg;
            return new F();
        }
    }

    private static class CountingWords implements IFunction{
        private static LinkedList<String> words;
        private static class F implements IFunction2{
            public Object call2(){
                LinkedHashMap<String, Integer> temp = new LinkedHashMap<>();
                
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
        public Object call(Object arg){
            this.words = (LinkedList<String>)arg;
            return new F();
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

