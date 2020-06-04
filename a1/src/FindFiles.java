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

            File mainFile = new File(directory);
            File[] fileList = mainFile.listFiles();
            if (!mainFile.canRead()){
                System.out.println("No read access for directory: " + mainFile.getName());
            }
            else {
                System.out.println("Searching for files in directory: " + directory);
                matchFiles(fileList, regex);
                printMatchedFiles();
            }

        }
    }

    private static void matchFiles(File[] fileList, String regex){
        if(options.containsKey("r")){
            for (File f: fileList) {
                recurseMatchFile(f,regex);
            }
        } else {
            for (File f: fileList) {
                if (f.isFile() && compare(f.getName(),regex)) {
                    matchedFilePaths.add(f.getAbsolutePath());
                }
            }
        }
    }

    private static void recurseMatchFile(File file, String regex) {
        if (!file.isDirectory()){
            if (compare(file.getName(),regex)) {
                matchedFilePaths.add(file.getAbsolutePath());
            }
        } else {
            File fileHandler = new File(file.getAbsolutePath());
            if (!fileHandler.canRead()){
                System.out.println("Skipping directory: "+ fileHandler.getName() + " No read access." );
            } else {
                File[] fileList = fileHandler.listFiles();
                for (File f: fileList) {
                    recurseMatchFile(f,regex);
                }
            }

        }
    }


    private static void printMatchedFiles() {
        for (String filePath: matchedFilePaths) {
            System.out.println(filePath);
        }
        System.out.println(matchedFilePaths.size() + " files found.");
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
