import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.BiFunction;
import java.io.IOException;

public class Fifteen {
    public static void main (String[] args){
        if(args.length > 0){
            WordFrequencyFramework wff =  new WordFrequencyFramework();
            StopWordPorcessor swp = new StopWordPorcessor(wff);
            FileWordProcessor fwp = new FileWordProcessor(wff, swp);
            WordFreqProcessor wfp = new WordFreqProcessor(wff, fwp);
            //for 15.2
            countWordsWithZ cwwz = new countWordsWithZ(wff, fwp);


            wff.run(args[0]);
        }
        else{
            System.out.println("Error: file name must be passed as argument");
            System.exit(0);
        }
    }

    private static class WordFrequencyFramework{
        private LinkedList<Object> loadEventHandlers = new LinkedList<>();
        private LinkedList<Object> doWorkHandlers = new LinkedList<>();
        private LinkedList<Object> endEventHandlers = new LinkedList<>();

        public void loadEvent(Consumer<String> handle){
            this.loadEventHandlers.add(handle);
        }

        public void endEvent(Runnable handle){
            this.endEventHandlers.add(handle);
        }

        public void doWorkEvent(Runnable handle){
            this.doWorkHandlers.add(handle);
        }

        public void run(String path){
            for(Object o: this.loadEventHandlers){
                ((Consumer<String>)o).accept(path);
            }
            for(Object o: this.doWorkHandlers){
                ((Runnable)o).run();
            }
            for(Object o: this.endEventHandlers){
                ((Runnable)o).run();
            }

        }

    }

    private static class StopWordPorcessor{
        LinkedList<String> stopWords = new LinkedList<>();

        public StopWordPorcessor(WordFrequencyFramework wff){
            wff.loadEvent(this::load);

        }
        private void load(String notNeeded){
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
        }
        
        public boolean is_stopWord(String word){
            return this.stopWords.contains(word);
        }
    }

    private static class FileWordProcessor{
        private ArrayList<String> words = new ArrayList<>();
        private StopWordPorcessor stop_word_porcessor = null;
        private LinkedList<Object> word_event_handlers = new LinkedList<>();
        public FileWordProcessor(WordFrequencyFramework wff, StopWordPorcessor swp){
            this.stop_word_porcessor = swp;
            wff.loadEvent(this::load);
            wff.doWorkEvent(this::countingWords);

        }

        private void load(String path){
            try{
                File file = new File(path);
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine().toLowerCase();
                    line = line.replaceAll("[^a-z0-9]", " ");
                    String[] result = line.split(" ");
                    for(int i =0 ; i < result.length;i++){
                        this.words.add(result[i]);
                    }
                }
                sc.close();
            }
            catch(IOException e){
                System.out.println(e +  "Couldn't open file " + path);
                System.exit(0);
            }
        }

        private void countingWords(){
            for(int i =0; i<words.size(); i++){
                String word = words.get(i);
                if(!this.stop_word_porcessor.is_stopWord(word) && word.length() >=2){
                    for(Object o: this.word_event_handlers){
                        ((Consumer<String>)o).accept(word);
                    }
                }
            }

        }

        public void loadEvent(Consumer<String> handler){
            word_event_handlers.add(handler);
        }

    }

    private static class WordFreqProcessor{
        private LinkedHashMap<String, Integer> wordCount = new LinkedHashMap<>();
        public WordFreqProcessor(WordFrequencyFramework wff, FileWordProcessor fwp ){
            fwp.loadEvent(this::counting_words);
            wff.endEvent(this::printTop25);
        }

        private void counting_words(String word){
            if(this.wordCount.containsKey(word)){
                this.wordCount.put(word, this.wordCount.get(word)+1);
            }
            else{
                this.wordCount.put(word, 1);
            } 
        }

        private void printTop25(){
            int count = 0;
            LinkedHashMap<String, Integer> freq_sorted = this.sorted();
            for(Map.Entry<String, Integer> e : freq_sorted.entrySet()){
                if (count < 25){
                System.out.println(e.getKey()+" -> "+e.getValue());
                count += 1;
                }
                else{
                    break;
                }
            }
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

    //for 15.2
    private static class countWordsWithZ{
        private int counter;
        public countWordsWithZ(WordFrequencyFramework wff, FileWordProcessor fwp){
            this.counter=0;
            fwp.loadEvent(this::count_z);
            wff.endEvent(this::printCount_z);
        }
        private void count_z(String word){
            if(word.contains("z"))
                this.counter++;
        }
        private void printCount_z(){
            System.out.println(this.counter + " words have letter 'z'");
        }


    }

}