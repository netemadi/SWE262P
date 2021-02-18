import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwentyEight{
    
    public static void main(String[] args){
        check_arguments(args);

        for (var maps : count_and_sort(args[0])) {
            System.out.println("-----------------------------");
            for(Map.Entry<String, Integer> e : maps.entrySet()){
                System.out.println(e.getKey() + " - " + e.getValue());
            }
        }

    }

    private static Iterable<Map<String, Integer>> count_and_sort(String path){
        Map<String, Integer> freqWords = new HashMap<>();
        Stream.Builder<Map<String, Integer>> sortedList = Stream.builder();
        try{
            int i = 1;
            for (String word: non_stop_words(path)){
                if(freqWords.containsKey(word)){
                    freqWords.put(word, freqWords.get(word)+1);
                }
                else{
                    freqWords.put(word, 1);
                }
                if(i % 5000 == 0){
                  Stream<Map.Entry<String, Integer>> map_stream =(freqWords.entrySet()).stream();
                  
                  // citation: 
                  // https://javarevisited.blogspot.com/2017/09/java-8-sorting-hashmap-by-values-in.html#ixzz6maCMYeDp
                  // https://javarevisited.blogspot.com/2017/09/java-8-sorting-hashmap-by-values-in.html#axzz6ma1BEcin

                  Map<String, Integer> temp2 = freqWords.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .limit(25)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
                  sortedList.add(temp2);
                }
                i += 1;
            }
            // citation: 
            // https://javarevisited.blogspot.com/2017/09/java-8-sorting-hashmap-by-values-in.html#ixzz6maCMYeDp
            // https://javarevisited.blogspot.com/2017/09/java-8-sorting-hashmap-by-values-in.html#axzz6ma1BEcin
            Map<String, Integer> temp2 = freqWords.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(25)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));

            sortedList.add(temp2);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Iterable<Map<String, Integer>> to_return = () -> sortedList.build().iterator();
        return to_return;
    }

    private static Iterable<String> non_stop_words(String path){
        Stream.Builder<String> words = Stream.builder();
        try{
            List<String> stopWords = new LinkedList<String>();
            Path sp_path = Path.of("../stop_words.txt");
            Stream<String> stopWords_stream = Files.lines(sp_path);
            stopWords = stopWords_stream.flatMap(line -> Arrays.stream(line.split(","))).collect(Collectors.toList());
            
            //add each all letters to stop words
            String alaphbeticChar = "abcdefghijklmnopqrstuvwxyz";
            Stream<String> stream_as = alaphbeticChar.codePoints().mapToObj(c -> String.valueOf((char) c));

            Iterable<String> i = () -> stream_as.iterator();
            for (String s:  i){
                stopWords.add(s);
            }

            for (String word : all_words(path)){
                if(!(stopWords.contains(word))){
                    words.add(word);
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

        Stream<String> w_stream = words.build();
        Iterable<String> to_return = () -> w_stream.iterator(); 
        return to_return;

    }

    private static Iterable<String> all_words(String path){
      //https://www.tutorialspoint.com/the-build-method-in-java-stream-builder
        Stream.Builder<String> words = Stream.builder();
    // ----------------- 28.1 -----------------------------------
        // boolean start_char = true;
        // String word = "";
        // for (Character c : characterss(path)){
        //     if(start_char){
        //         word = "";
        //         if(Character.isLetterOrDigit(c)){
        //             word = c.toString();
        //             word = word.toLowerCase();
        //             start_char = false;
        //         }
        //         else{
        //             continue;
        //         }
        //     }
        //     else{
        //         if(Character.isLetterOrDigit(c)){
        //             String s = c.toString().toLowerCase();
        //             word = word + s;
        //         }
        //         else{ //end of the word
        //             start_char = true;
        //             words.add(word);
        //         }
        //     }
        // }

    // ----------------- 28.2 -------------------------------------
        //characterss returns an interator to the stream of lines
        for(String l: characterss(path)){
          //citation: https://www.baeldung.com/java-stream-operations-on-strings
          List<String> t = Stream.of(l.toLowerCase().replaceAll("[^a-z0-9]", " ").split(" "))
                              .map(elem -> new String(elem))
                              .collect(Collectors.toList());
              for(String word : t){
                if(!word.equals("")){
                    words.add(word);
                }
            }
        }
    // ---------------------------------------------    

        Stream<String> w_stream = words.build();
        Iterable<String> to_return = () -> w_stream.iterator();
        return to_return;
    }

    // --------- 28.1
    // private static Iterable<Character> characterss(String path){

    // --------- 28.2
    private static Iterable<String> characterss(String path){
        try{
            Stream<String> w_stream = Files.lines(Path.of(path));

        //------- 28.1
                                                                //Adding \n   type IntStream
            // Stream<Character> c_stream = w_stream.flatMap(l -> l.concat("\n").chars().mapToObj(c -> (char)c));
            // Iterable<Character> to_return = () -> c_stream.iterator();
        //----------

        //------- 28.2
            Iterable<String> to_return = () -> w_stream.iterator();
        //-------------
            return to_return;
                                        

        }
        catch(Exception e){
            e.printStackTrace();
            // ------ 28.1 -----
            // return () -> Stream.of((Character) null).iterator();

            // ------- 28.2
            return () -> Stream.of((String) null).iterator();
        }
    }

    private static void check_arguments(String[] args){
        if(!(args.length > 0)){
            System.out.println("Error: file name must be passed as argument");
            System.exit(0);
        }
    } 
}