import java.util.ArrayList;
import java.util.Map;

public class ArgsData {
    public String fileToFind ;
    public ArgsStatus status;
    public Map<String, ArrayList<String>> options;
    ArgsData(String fileToFind, Map<String, ArrayList<String>> options ,ArgsStatus status) {
        this.fileToFind = fileToFind;
        this.status = status;
        this.options = options;
    }
}
