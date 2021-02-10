import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.io.File;
import java.util.Properties;
import java.io.FileInputStream;

public class Framework {
    private static I_TFWords tfWords;
    private static I_TFFreqs tfFreqs;

    private static String path_to_preperties = "config.properties";
    private static Properties prop = new Properties();

    //Expect an argument as a file path to read from. 
    //Return the path to the file if it was given.
    //If not enough argument, exit the program
    private static String check_arguments(String[] args){
        String input_file_path = "";
        if(args.length > 0){
            input_file_path = args[0];
        }
        else{
            System.out.println("Error: file name must be passed as argument");
            System.exit(0);
        }
        return input_file_path;
    } 

    private static void load_plugins() throws Exception{
        prop.load(new FileInputStream(path_to_preperties));
        String class_name = prop.getProperty("app");
        

        Class cls = null;
        URL appJarURL = null; 
        try{
            String appJarName = class_name + ".jar";
            appJarURL = (new File(appJarName)).toURI().toURL();

        } catch(Exception e){
            e.printStackTrace();
        }

        URL[] classUrls = {appJarURL};
        URLClassLoader cLoader = new URLClassLoader(classUrls);
        try{
            cls = cLoader.loadClass(class_name);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(cls != null){
            tfWords = (I_TFWords)cls.getDeclaredConstructor().newInstance();
            tfFreqs = (I_TFFreqs)cls.getDeclaredConstructor().newInstance();
        }
    }

    public static void main(String[] args) throws Exception {
        String path = check_arguments(args);
        load_plugins();

        ArrayList<String> words =  tfWords.extractWords(path);
        LinkedHashMap<String, Integer> top_25 = tfFreqs.top25(words);
        //print words
        for(Map.Entry<String, Integer> e : top_25.entrySet()){
            System.out.println(e.getKey() + " - " + e.getValue());
        }
    }
}