package softwaredesign;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.Metric;
import softwaredesign.extraction.metrics.*;

import java.util.List;

public class MetricFactory {

    public enum MetricType {
        COMMITS_PER_DAY,
        NUMBER_OF_FILES_MODIFIED,
        NUMBER_OF_LINES_ADDED,
        NUMBER_OF_LINES_DELETED,
        NUMBER_OF_MERGES,
        TOP_COLLABORATORS
    }

    public static Metric getMetric(MetricType metricType, List<Commit> commits) {
        switch (metricType) {
            case COMMITS_PER_DAY:
                return new CommitsPerDay(commits);
            case NUMBER_OF_FILES_MODIFIED:
                return new NumberOfFilesModified(commits);
            case NUMBER_OF_LINES_ADDED:
                return new NumberOfLinesAdded(commits);
            case NUMBER_OF_LINES_DELETED:
                return new NumberOfLinesDeleted(commits);
            case NUMBER_OF_MERGES:
                return new NumberOfMerges(commits);
            case TOP_COLLABORATORS:
                return new TopCollaborators(commits);
        }
        return null;
    }
}
