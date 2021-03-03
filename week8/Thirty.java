import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Thirty{
    private static LinkedList<String> stopWords;
    private static BlockingQueue<String> word_space = new LinkedBlockingQueue<>();
    private static BlockingQueue<LinkedHashMap<String, Integer>> freq_space = new LinkedBlockingQueue<>();

    private static void getStopWords(){
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
    }


    private static Runnable process_words = () -> {
        LinkedHashMap<String, Integer> word_freqs = new LinkedHashMap<>();

        while (!word_space.isEmpty()) {
            String word = "";
            try {
                word = word_space.poll(1, TimeUnit.SECONDS);
            } 
            catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (!stopWords.contains(word)) {
                if (word_freqs.containsKey(word)) {
                    word_freqs.put(word, word_freqs.get(word) + 1);
                } else {
                    word_freqs.put(word, 1);
                }
            }
        }

        try{
            freq_space.put(word_freqs);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    };

    public static void main(String[] args) throws Exception {
        if(!(args.length > 0)){
            System.out.println("Error: file name must be passed as argument");
            System.exit(0);
        }
        getStopWords();
        String path_to_file = args[0];
        LinkedList<String> words = new LinkedList<>();
        try{
            File file = new File(path_to_file);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().toLowerCase();
                line = line.replaceAll("[^a-z0-9]", " ");
                String[] result = line.split(" ");
                for(int i =0 ; i < result.length;i++){
                    if(result[i].length()>=2)
                        words.add(result[i]);
                }
            }
            sc.close();
        }
        catch(IOException e){
            System.out.println(e +  "Couldn't open file " + path_to_file);
            System.exit(0);
        }

        for(String word: words){
            word_space.add(word);
        }

        ArrayList<Thread> workers = new ArrayList<>();
        for(int i=0; i<5; i++){
            workers.add(new Thread(process_words));
        }

        for(Thread t: workers){
            t.start();
        }

        for(Thread t: workers){
            t.join();
        }

        LinkedHashMap<String, Integer> word_freqs = new LinkedHashMap<>();
        while (!freq_space.isEmpty()) {
            LinkedHashMap<String, Integer> freqs = freq_space.poll();
            for(Map.Entry<String, Integer> e : freqs.entrySet()){
                String word= e.getKey();
                if(word_freqs.containsKey(word)){
                    word_freqs.put(word, word_freqs.get(word)+e.getValue());
                }
                else{
                    word_freqs.put(word, e.getValue());
                }
            }
        }
        
        LinkedHashMap<String,Integer> sortedMap = word_freqs.entrySet().stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .limit(25)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        for(Map.Entry<String, Integer> e : sortedMap.entrySet()){
            System.out.println(e.getKey()+" - "+e.getValue());
        }
    }
    


}