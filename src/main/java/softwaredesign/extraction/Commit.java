package softwaredesign.extraction;

import java.time.ZonedDateTime;
import java.util.List;

public class Commit {
    public String authorName;
    public String authorEmail;
    public ZonedDateTime date;
    public String description;
    public List<File> files;
    public String hash;
    public Integer insertions;
    public Integer deletions;
    public Boolean isMerge;

    public Commit(String authorName, String authorEmail, ZonedDateTime date, String description, List<File> files, String hash, Integer insertions, Integer deletions, Boolean isMerge) {
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.date = date;
        this.description = description;
        this.files = files;
        this.hash = hash;
        this.insertions = insertions;
        this.deletions = deletions;
        this.isMerge = isMerge;
    }
}
