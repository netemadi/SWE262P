import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface I_TFFreqs {
    // LinkedList<Map.Entry<String, Integer>> top25(List<String> words);
    LinkedHashMap<String,Integer> top25(ArrayList<String> words);
}