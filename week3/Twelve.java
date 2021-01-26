import java.io.File;
import java.util.*;
import java.io.IOException;

public class Twelve{
    public static void main (String[] args){
        try{
            if(args.length > 0){
                String[] temp = {"init", args[0]};
                Controller c = new Controller();
                c.dispatch(temp);
                c.dispatch(new String[]{"run"});
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

    private static class Controller{
        private StorageManager sm;
        private StopWords sw;
        private WordFreq wf;
        public void dispatch(String[] msgs) throws Exception{
            if(msgs[0].equals("init")){
                this.init(msgs[1]);
            }
            else if(msgs[0].equals("run")){
                this.run();
            }
            else{
                throw new Exception("MessageNotFound");
            }
        }
        private void init(String path) throws Exception{
            this.sm = new StorageManager();
            String[] temp = {"init", path};
            this.sm.dispatch(temp);

            this.sw = new StopWords();
            String[] temp2 = {"init"};
            this.sw.dispatch(temp2);

            this.wf = new WordFreq();
            this.wf.dispatch(temp2);
        }

        private void run() throws Exception{
            String[] temp = {"words"};
            ArrayList<String> al = (ArrayList<String>)sm.dispatch(temp);
            for(int i=0; i<al.size(); i++){
                String w = al.get(i);
                String[] temp2={"is_stop_word", w};
                boolean is_stopWord = (boolean)sw.dispatch(temp2);
                if(!is_stopWord && w.length() >=2){
                    String[] temp3={"counting_words", w};
                    wf.dispatch(temp3);
                }
            }
            LinkedHashMap<String, Integer> word_freq = (LinkedHashMap<String, Integer>)wf.dispatch(new String[]{"sorted"});
            int count = 0;
            for(Map.Entry<String, Integer> e : word_freq.entrySet()){
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

    private static class StorageManager{
        private ArrayList<String> words;
        public Object dispatch(String[] msgs) throws Exception{
            if(msgs[0].equals("init")){
                return this.init(msgs[1]);
            }
            else if(msgs[0].equals("words")){
                return this.words();
            }
            else{
                throw new Exception("MessageNotFound");
            }
        }
        private Object init(String path){
            try{
                File file = new File(path);
                Scanner sc = new Scanner(file);
                this.words = new ArrayList<>();
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
            return new Object();
        }
        private ArrayList<String> words(){
            return this.words;
        }
    }

    private static class StopWords{
        LinkedList<String> stopWords;
        public Object dispatch(String[] msgs) throws Exception{
            if(msgs[0].equals("init")){
                return this.init();
            }
            else if(msgs[0].equals("is_stop_word")){
                return this.is_stop_word(msgs[1]);
            }
            else{
                throw new Exception("MessageNotFound");
            }
        }

        private Object init(){
            stopWords = new LinkedList<>();
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
        }
        catch(IOException e){
            System.out.println(e + "couldn't open stop words file");
            System.exit(0);
        }
            return new Object();
        }

        private boolean is_stop_word(String word){
            return stopWords.contains(word);
        }
    }

    private static class WordFreq{
        LinkedHashMap<String, Integer> wordCount;
        public Object dispatch(String[] msgs) throws Exception{
            if(msgs[0].equals("init")){
              return this.init();
            }
            else if(msgs[0].equals("counting_words")){
                return this.counting_words(msgs[1]);
            }
            else if(msgs[0].equals("sorted")){
                return this.sorted();
            }
            else{
                throw new Exception("MessageNotFound");
            }
        }

        private Object init(){
          wordCount = new LinkedHashMap<>();
          return new Object();
        }
        
        private Object counting_words(String word){
            if(this.wordCount.containsKey(word)){
                this.wordCount.put(word, wordCount.get(word)+1);
            }
            else{
                this.wordCount.put(word, 1);
            } 
            return new Object();
        }

        //Sorting HashMap
        //citation: https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
        private LinkedHashMap<String, Integer> sorted(){
            List<String> mapKeys = new ArrayList<>(this.wordCount.keySet());
            List<Integer> mapValues = new ArrayList<>(this.wordCount.values());
            Collections.sort(mapValues, Collections.reverseOrder());
            Collections.sort(mapKeys);
        
            LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        
            Iterator<Integer> valueIt = mapValues.iterator();
            while (valueIt.hasNext()) {
              Integer val = valueIt.next();
              Iterator<String> keyIt = mapKeys.iterator();
        
              while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = this.wordCount.get(key);
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

}