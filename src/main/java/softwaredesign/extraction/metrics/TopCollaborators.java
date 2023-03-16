package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.MultipleData;
import softwaredesign.utilities.NameValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.min;

public class TopCollaborators extends MultipleData {
    protected String name = "Top Collaborators";
    protected String description = "Returns the list of Top 10 collaborators sorted by the number of commits in decreasing order";

    public TopCollaborators(List<Commit> commits) {
        List<NameValue<Integer>> data = new ArrayList<>();
        Map<String, Integer> collaboratorCommits = new HashMap<>();

        for (Commit commit : commits) {
            Integer currentCommits = collaboratorCommits.get(commit.authorName);
            if (currentCommits == null) {
                currentCommits = 0;
            }
            collaboratorCommits.put(commit.authorName, currentCommits + 1);
        }

        int collaboratorsToAdd = min(10, collaboratorCommits.size());

        for (var entry : collaboratorCommits.entrySet()) {
            data.add(new NameValue<>(entry.getKey(), entry.getValue()));
            if (data.size() == collaboratorsToAdd) {
                break;
            }
        }
        this.data = data;
    }
}
