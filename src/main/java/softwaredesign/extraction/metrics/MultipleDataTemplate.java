package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.types.MultipleData;
import softwaredesign.utilities.NameValue;

import java.util.List;

@SuppressWarnings("unused")
public class MultipleDataTemplate extends MultipleData<Integer> {
    public MultipleDataTemplate(List<Commit> commits) {
        super(
                "Multiple Data Template",
                "Sample Description",
                extract(commits)
        );
    }

    private static List<NameValue<Integer>> extract(List<Commit> commits) {

        // Implement extraction here and return list of NameValues

        return List.of(new NameValue<>("Test", commits.size()));
    }
}
