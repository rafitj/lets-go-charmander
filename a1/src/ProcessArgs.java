import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

enum ArgsStatus {
    INVALID,
    VALID,
    HELP,
}

public class ProcessArgs {

    private static Map<String, FindFileArg> FindFileArgs = new HashMap<>() {{
        put("help", new FindFileArg("help", "print out a help page and exit the program", 0 , 0));
        put("reg", new FindFileArg("reg", "find files using [filietofind] argument as a regular expression", 0, 0));
        put("r", new FindFileArg("r", "execute the command recursively in subfiles", 0, 0));
        put("dir", new FindFileArg("dir", "find the files the specified directory [directory] - the default directory is the calling directory", 1, 1));
        put("ext", new FindFileArg("ext", "find the files matching [filetofind] and with the given extensions [ext1, ext2,...]", 1, Integer.MAX_VALUE));
    }};

    private static String error;

    private static ArgsStatus getArgsStatus(Map<String,ArrayList<String>> args){
        if(args.containsKey("help")) {
            return ArgsStatus.HELP;
        }
        for (Map.Entry<String, ArrayList<String>> arg : args.entrySet()) {
            if (!FindFileArgs.containsKey(arg.getKey())) {
                error = "-" + arg.getKey() + " is an invalid option";
                return ArgsStatus.INVALID;
            }
            if (arg.getValue().size() > FindFileArgs.get(arg.getKey()).maxParams ||
                    arg.getValue().size() < FindFileArgs.get(arg.getKey()).minParams  ) {
                error = "-" + arg.getKey() + " option has an invalid number of arguments";
                return ArgsStatus.INVALID;
            }
        }
        return ArgsStatus.VALID;
    }
    // Print command-line syntax
    private static void printHelp() {
        System.out.println("Usage: java FindFiles filetofind [-option arg]");
        for(Map.Entry<String,FindFileArg> entry : FindFileArgs.entrySet()) {
            String arg = entry.getKey();
            String argDescription = entry.getValue().description;
            System.out.printf("-%-10s ::  %-10s%n", arg, argDescription);
        }
    }

    private static HashMap<String, String> parse(String[] args) {
        HashMap<String, String> arguments = new HashMap<>();
        String key = null;
        try {
            for(String entry : args) {
                if (entry.startsWith("-")) {
                    key = entry.substring(1);
                    if (!FindFileArgs.containsKey(key)) {
                        System.out.println(entry + " is an invalid option.");
                        throw new Exception();
                    }
                    arguments.put(key,"");
                } else {
                    if (key == null) {
                        System.out.println(entry + " is an invalid option/key.");
                        throw new Exception();
                    } else {
                        arguments.put(key, entry);
                        key = null;
                    }
                }
            }
        } catch(Exception err){
            printHelp();
            System.exit(0);
        }
        return arguments;
    }

    private static HashMap<String, ArrayList<String>> clean(HashMap<String, String> arguments) {
        HashMap<String, ArrayList<String>> parsedArgs = new HashMap<>();
        for (Map.Entry<String, String> arg: arguments.entrySet()) {
            ArrayList<String> optionParams = new ArrayList<>(Arrays.asList(arg.getValue().split(",")));
            optionParams.removeIf(String::isEmpty);
            parsedArgs.put(arg.getKey(), optionParams);
        }
        return parsedArgs;
    }


    public static ArgsData process(String[] args){
        if (args.length == 0) {
            System.out.println("Need folder to find");
            ProcessArgs.printHelp();
            return new ArgsData("", new HashMap<>(), ArgsStatus.INVALID);
        }
        if (args[0].startsWith("-")) {
            System.out.println("No filename to find provided (filename cannot start with '-')");
            ProcessArgs.printHelp();
            return new ArgsData("", new HashMap<>(), ArgsStatus.INVALID);
        }
        else {
            String fileToFind = args[0];
            String[] optionArgs = Arrays.copyOfRange(args, 1, args.length);
            HashMap<String, String> parsedArgs = parse(optionArgs);
            HashMap<String, ArrayList<String>> processedArgs = clean(parsedArgs);
            ArgsStatus status = ProcessArgs.getArgsStatus(processedArgs);
            ArgsData data = new ArgsData(fileToFind,processedArgs, status);
            if(status == ArgsStatus.HELP) {
                ProcessArgs.printHelp();
            } else if (status == ArgsStatus.INVALID){
                System.out.println(ProcessArgs.error);
                ProcessArgs.printHelp();
            }
            return data;
        }
    }
}
