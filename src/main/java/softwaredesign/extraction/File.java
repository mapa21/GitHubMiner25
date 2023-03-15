package softwaredesign.extraction;

public class File {
    public String name;
    public Integer sizeBeforeCommit;
    public Integer sizeAfterCommit;

    public File(String name, Integer sizeBeforeCommit, Integer sizeAfterCommit) {
        this.name = name;
        this.sizeBeforeCommit = sizeBeforeCommit;
        this.sizeAfterCommit = sizeAfterCommit;
    }
}
