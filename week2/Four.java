import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Four {
    public static void main(String[] args) {
        try{  
            //adding the stop words to the list          
            File file = new File("../stop_words.txt"); 
            Scanner sc = new Scanner(file); 
            ArrayList<String> stopWords = new ArrayList<String>();
            while(sc.hasNextLine()){
                String templine = sc.nextLine();
                String word = "";
                int i = 0;
                for (; i<templine.length(); i++){
                    if((templine.charAt(i) == ',')){
                        stopWords.add(word.toLowerCase());
                        word = "";
                    }
                    else{
                        word += templine.charAt(i);
                    }
                }
                //adding the last word since there is no comma after that
                if(templine.charAt(i-1) != ','){
                    stopWords.add(word.toLowerCase());
                }
            }
            sc.close();
      
            //expecting an input file from the user 
            String input_file =  "";
            if(args.length > 0){
              input_file = args[0];
            }
            else{
              System.out.println("Error: file name must be passed as argument");
              System.exit(0);
            }
            
            //reading the and count the words that are not included in stop words
            file = new File(input_file);
            sc = new Scanner(file);
            ArrayList<ArrayList<Object>> frequencies = new ArrayList<>();
            while(sc.hasNextLine()){
              //adding "\n" to find the words at the end of the lines
                String templine = sc.nextLine().toLowerCase() + "\n";
                int startChar = Integer.MAX_VALUE;
                int i = 0;
                for (; i < templine.length(); i++){
                    char c = templine.charAt(i);
                    if (startChar == Integer.MAX_VALUE){
                        //if alphanumeric, we found the start of a word.
                        if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')){
                            startChar = i;
                        }
                    }
                    else{
                        if (!((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))){
                            //if not alphanumerc, we found the end of a word
                            boolean found = false;
                            // System.out.println("line:" + templine);
                            String word = templine.substring(startChar, i);
                            // System.out.println(word);
                            //Ignore stop words
                            if ((word.length() > 1) && !stopWords.contains(word)){
                                //j as pair_index
                                int j = 0;
                                for (; j<frequencies.size(); j++){
                                    if (word.equals(frequencies.get(j).get(0))){
                                        int count = (Integer)frequencies.get(j).get(1) + 1;
                                        frequencies.get(j).set(1, count);
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found){
                                    //add pair
                                    ArrayList<Object> pair = new ArrayList<>();
                                    pair.add(word);
                                    pair.add(1);
                                    frequencies.add(pair);
                                }
                                else if(frequencies.size() > 1){
                                    for(int k = j-1; k >= 0; k-- ){
                                        if((Integer)frequencies.get(j).get(1) > (Integer)frequencies.get(k).get(1)){
                                            //swap
                                            ArrayList<Object> temp = frequencies.get(j);
                                            frequencies.set(j, frequencies.get(k));
                                            frequencies.set(k, temp);
                                            j = k;
                                        }
                                    }
                                }
                            }
                            startChar = Integer.MAX_VALUE;
                        }
                    }
                }
            }
            sc.close();
            for (int i = 0; i<25; i++){
                System.out.println(frequencies.get(i).get(0)
                + " -> " + frequencies.get(i).get(1) );
            }
          }
          catch (Exception e){
            e.printStackTrace();
          }
    }
}