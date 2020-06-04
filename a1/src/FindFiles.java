import java.util.ArrayList;
import java.util.Map;
import java.io.File;

public class FindFiles {
    private static Map<String, ArrayList<String>>  options;
    private static String fileToFind;
    private static ArrayList<String> matchedFilePaths = new ArrayList<>();
    private static ArrayList<String> matchables = new ArrayList<>();
    private static String regex;

    public static void main(String[] args) {
        ArgsData processedArgs = ProcessArgs.process(args);
        if(processedArgs.status == ArgsStatus.VALID) {
            options = processedArgs.options;
            fileToFind = processedArgs.fileToFind;

            String directory = getDirectory();
            File mainFile = new File(directory);
            File[] fileList = mainFile.listFiles();
            if (!mainFile.canRead()){
                System.out.println("No read access for directory: " + mainFile.getName());
                System.exit(0);
            }
            printStartText(directory);
            System.out.println("Searching directory: " + directory);
            if (options.containsKey("reg")){
                regex = formRegex();
                matchFiles(fileList, true);
            } else {
                matchables = formMatchables();
                matchFiles(fileList,false);
            }
            printMatchedFiles();
        }
    }

    private static void printStartText(String rootDir) {
        System.out.println("===============Find Files===============");
        System.out.println("ROOT-DIRECTORY: " + rootDir);
        if (options.containsKey("reg")) {
            regex = formRegex();
            System.out.println("REGEX: " + regex);
        } else {
            matchables = formMatchables();
            System.out.println("QUERY: " + String.join(" or ", matchables));
        }
        System.out.println("RECURSIVE: " + options.containsKey("r"));
        System.out.println();
        System.out.println("===============Search Log===============");
    }

    private static void matchFiles(File[] fileList, boolean isReg){
        if(options.containsKey("r")){
            for (File f: fileList) {
                recurseMatchFile(f,isReg);
            }
        } else {
            for (File f: fileList) {
                if (f.isFile() && compare(f.getName(),isReg)) {
                    matchedFilePaths.add(f.getAbsolutePath());
                }
            }
        }
    }

    private static void recurseMatchFile(File file, boolean isReg) {
        if (!file.isDirectory()){
            if (compare(file.getName(),isReg)) {
                System.out.println("Found match!");
                matchedFilePaths.add(file.getAbsolutePath());
            }
        } else {
            File fileHandler = new File(file.getAbsolutePath());
            if (!fileHandler.canRead()){
                System.out.println("Skipping directory: "+ fileHandler.getName() + " No read access." );
            } else {
                System.out.println("Searching directory: "+ fileHandler.getAbsolutePath() );
                File[] fileList = fileHandler.listFiles();
                for (File f: fileList) {
                    recurseMatchFile(f,isReg);
                }
            }

        }
    }


    private static void printMatchedFiles() {
        System.out.println();
        if(matchedFilePaths.size() > 0) {
            System.out.println("===============Matched Files===============");
            System.out.println("TOTAL: " + matchedFilePaths.size());
            System.out.println("FILE PATHS: ");
            for (String filePath: matchedFilePaths) {
                System.out.println(filePath);
            }
        } else {
            System.out.println("===============No Files Found===============");
            System.out.println("Please try again.");
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

    private static  ArrayList<String> formMatchables(){
        ArrayList<String> matchables = new ArrayList<>();
        if (options.containsKey("ext")){
            for (String e: options.get("ext")) {
                matchables.add(fileToFind+e);
            }
        } else {
            matchables.add(fileToFind);
        }
        return matchables;
    }

    private static boolean compare(String fileName,  boolean isReg) {
        if (isReg) {
            return fileName.matches(regex);
        }
        for (String match: matchables) {
            if (fileName.equals(match)) {
                return true;
            }
        }
        return false;
    }

    private  static String getExtRegex() {
        if (options.containsKey("ext")) {
            return "\\.(" + String.join("|",options.get("ext")) + ")";
        }
        return "";
    }
}
