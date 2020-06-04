import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class FindFiles {
    private static Map<String, ArrayList<String>>  options;
    private static String fileToFind;
    private static ArrayList<String> matchedFilePaths = new ArrayList<>();

    public static void main(String[] args) {
        ArgsData processedArgs = ProcessArgs.process(args);
        if(processedArgs.status == ArgsStatus.VALID) {
            options = processedArgs.options;
             fileToFind = processedArgs.fileToFind;

            String directory = getDirectory();
            String regex = formRegex();
            System.out.println(regex);
            System.out.println("Searching for files in directory: " + directory);
            File fileHandler = new File(directory);
            File[] fileList = fileHandler.listFiles();

            matchFiles(fileList, regex);
            printMatchedFiles();
        }
    }

    private static void matchFiles(File[] fileList, String regex){
        if(options.containsKey("r")){
            for (File f: fileList) {
                recurseMatchFile(f,regex);
            }
        } else {
            for (File f: fileList) {
                if (compare(f.getName(),regex)) {
                    matchedFilePaths.add(f.getAbsolutePath());
                }
            }
        }
    }

    private static void recurseMatchFile(File file, String regex) {
        System.out.println(file.getName());
        if (!file.isDirectory()){
            if (compare(file.getName(),regex)) {
                matchedFilePaths.add(file.getAbsolutePath());
            }
        } else {
            File fileHandler = new File(file.getAbsolutePath());
            File[] fileList = fileHandler.listFiles();
            for (File f: fileList) {
                recurseMatchFile(f,regex);
            }
        }
    }


    private static void printMatchedFiles() {
        for (String filePath: matchedFilePaths) {
            System.out.println(filePath);
        }
    }

    private static String getDirectory(){
        if (options.containsKey("dir")) {
            return options.get("dir").get(0);
        } else {
           return System.getProperty("user.dir");
        }
    }

    private static String formRegex(){
        return fileToFind+getExtRegex();
    }

    private static boolean compare(String fileName, String match) {
        if (options.containsKey("reg")) {
            return fileName.matches(match);
        }
        return fileName.equals(match);
    }

    private  static String getExtRegex() {
        if (options.containsKey("ext")) {
            return "\\.(" + String.join("|",options.get("ext")) + ")";
        }
        return "";
    }
}
