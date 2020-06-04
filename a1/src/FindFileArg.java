public class FindFileArg {
    public String name;
    public String description;
    public int minParams;
    public int maxParams;
    FindFileArg(String name, String description, int minParams, int maxParams) {
        this.name = name;
        this.description = description;
        this.minParams = minParams;
        this.maxParams = maxParams;
    }
}
