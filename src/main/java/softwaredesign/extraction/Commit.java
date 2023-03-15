package softwaredesign.extraction;

import java.util.Date;
import java.util.List;

public class Commit {
    public String authorName;
    public String authorEmail;
    public Date date;
    public String description;
    public List<File> files;
    public String hash;
    public Integer additions;
    public Integer deletions;
    public Boolean isMerge;
}
