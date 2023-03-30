package softwaredesign.extraction;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

public class Commit {
    public final String hash;
    public final ZonedDateTime date;
    public final String authorName;
    public final String authorEmail;
    public final String description;
    public final FileStats fileStats;
    public final Boolean isMerge;

    public Commit(String hash, ZonedDateTime date, String authorName, String authorEmail, String description, FileStats fileStats, Boolean isMerge) {
        this.hash = hash;
        this.date = date;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.description = description;
        this.fileStats = fileStats;
        this.isMerge = isMerge;
    }

    public static class FileStats {
        @Getter @Setter
        private int filesModified = 0;
        @Getter @Setter
        private int insertions = 0;
        @Getter @Setter
        private int deletions = 0;
        public FileStats() {
            //
        }

        public FileStats(int filesModified, int insertions, int deletions) {
            this.filesModified = filesModified;
            this.insertions = insertions;
            this.deletions = deletions;
        }

        public void incrFilesModified(int i) {
            filesModified += i;
        }
        public void incrInsertions(int i) {
            insertions += i;
        }
        public void incrDeletions(int i) {
            deletions += i;
        }
    }
}
