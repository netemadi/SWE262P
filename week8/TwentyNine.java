import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwentyNine {
    public static void main(String[] args) throws Exception {
        if(!(args.length > 0)){
            System.out.println("Error: file name must be passed as argument");
            System.exit(0);
        }

        WordFrequencyManager word_freq_manager = new WordFrequencyManager();
        
        StopWordsManager stop_word_manager = new StopWordsManager();
        send(stop_word_manager, new Object[]{"init", word_freq_manager});

        DataStorageManager storage_manager = new DataStorageManager();
        send(storage_manager, new Object[]{"init", args[0], stop_word_manager});

        WordFrequencyController controller = new WordFrequencyController();
        send(controller, new Object[]{"run", storage_manager});

        controller.join();
        storage_manager.join();
        stop_word_manager.join();
        word_freq_manager.join();
    }

    private static abstract class ActiveManager extends Thread {
        private String name = "";
        private BlockingQueue<Object[]> queue;
        private boolean stopMe;

        ActiveManager(){
            super();
            this.name = this.getClass().getSimpleName();
            this.queue = new LinkedBlockingQueue<>();
            this.stopMe = false;
            this.start();
        }

        @Override
        public void run() {
            while (!this.stopMe) {
                Object[] message = queue.poll();
                if (message != null) {
                    this.dispatch(message);
                    if (message[0].equals("die")) {
                        this.stopMe = true;
                    }
                }
            }
        }

        abstract void dispatch(Object[] message);
        
        public void addMessage(Object[] message) {
            queue.add(message);
        }
        
        public void end() {
            this.stopMe = true;
        }
    }

    private static void send(ActiveManager receiver, Object[] message) {
        receiver.addMessage(message);
    }

    private static class DataStorageManager extends ActiveManager {
        private ArrayList<String> lines;
        private String path_to_file; 
        private StopWordsManager stop_words_manager;

        private void init(Object[] message){
            this.path_to_file = (String)message[0];
            this.stop_words_manager = (StopWordsManager)message[1];
            try{
                File file = new File(path_to_file);
                Scanner sc = new Scanner(file);
                this.lines = new ArrayList<>();
                while (sc.hasNextLine()) {
                    String line = sc.nextLine().toLowerCase();
                    lines.add(line);
                    // line = line.replaceAll("[^a-z0-9]", " ");
                    // String[] result = line.split(" ");
                    // for(int i =0 ; i < result.length;i++){
                    //     words.add(result[i]);
                    // }
                }
                sc.close();
            }
            catch(IOException e){
                System.out.println(e +  "Couldn't open file " + path_to_file);
                System.exit(0);
            }
        }

        @Override
        public void dispatch(Object[] message){

            if(message[0].equals("init")){
                this.init(new Object[]{message[1], message[2]});
            }
            else if(message[0].equals("send_word_freqs")){
                this.processWords(new Object[]{message[1]});
            }
            else{
                //forward
                send(this.stop_words_manager, message);
            }
        }


        private void processWords(Object[] message){
            WordFrequencyController recepient = (WordFrequencyController) message[0];
            LinkedList<String> words = new LinkedList<>();
            for (String line: this.lines){
                String temp = line.replaceAll("[^a-z0-9]", " ");
                String[] result = temp.split(" ");
                for(int i =0 ; i < result.length;i++){
                    words.add(result[i]);
                }
            }
            for(String word: words){
                send(this.stop_words_manager, new Object[]{"filter", word});
            }
            send(this.stop_words_manager, new Object[]{"top25", recepient});
        }
    }

    private static class StopWordsManager extends ActiveManager {
        LinkedList<String> stopWords;
        private WordFrequencyManager word_freqs_manager;

        private void init(Object[] message){
            this.word_freqs_manager = (WordFrequencyManager)message[0];
            this.stopWords = new LinkedList<>();
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

        @Override
        public void dispatch(Object[] msgs){
            if(msgs[0].equals("init")){
                this.init(new Object[]{msgs[1]});
            }
            else if(msgs[0].equals("filter")){
                this.filter(new Object[]{msgs[1]});
            }
            else{
                //forward
                send(this.word_freqs_manager, msgs);
            }
        }

        private void filter(Object[] message){
            String word = (String)message[0];
            if(!stopWords.contains(word) && word.length() > 1){
                send(this.word_freqs_manager, new Object[]{"word", word});
            }
        }
    }

    private static class WordFrequencyManager extends ActiveManager {
        LinkedHashMap<String, Integer> wordCount = new LinkedHashMap<>();
        
        @Override
        public void dispatch(Object[] msgs){
            if(msgs[0].equals("word")){
                 this.counting_words(new Object[]{msgs[1]});
            }
            else if(msgs[0].equals("top25")){
                 this.top25(new Object[]{msgs[1]});
            }

        }
        
        private void counting_words(Object[] message){
            String word = (String)message[0];
            if(this.wordCount.containsKey(word)){
                this.wordCount.put(word, wordCount.get(word)+1);
            }
            else{
                this.wordCount.put(word, 1);
            }
        }

        private void top25(Object[] message){
            LinkedHashMap<String, Integer> sortedMap = sorted();
            WordFrequencyController recepient = (WordFrequencyController)message[0];
            send(recepient, new Object[]{"top25",sortedMap});
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

    private static class WordFrequencyController extends ActiveManager{
        private DataStorageManager storage_manager;

        @Override
        public void dispatch(Object[] msgs){
            if(msgs[0].equals("run")){
                this.run(new Object[]{msgs[1]});
            }
            else if(msgs[0].equals("top25")){
                this.display(new Object[]{msgs[1]});
            }
            else{
                System.out.println("wrong key word in WordFrequencyController.dispatch(): " + (String)msgs[0]);
                System.exit(0);
            }
        }

        private void run(Object[] message){
            this.storage_manager = (DataStorageManager) message[0];
            send(this.storage_manager, new Object[]{"send_word_freqs", this});
        }

        private void display(Object[] message){
            LinkedHashMap<String,Integer> word_freq = (LinkedHashMap<String,Integer>)message[0];
            int count = 0;
            for(Map.Entry<String, Integer> e : word_freq.entrySet()){
                if (count < 25){
                  System.out.println(e.getKey()+" - "+e.getValue());
                  count += 1;
                }
                else{
                    break;
                }
            }
            end();
            send(this.storage_manager, new Object[]{"die"});
        }
    }

}