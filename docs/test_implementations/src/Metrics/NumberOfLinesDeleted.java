package Metrics;

public class NumberOfLinesDeleted extends SingleData {
    public NumberOfLinesDeleted(Commit[] commits) {
        super(commits[0].value);
    }

    private String description = "Deletions wow";
}
