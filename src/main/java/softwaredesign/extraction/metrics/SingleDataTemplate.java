package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.types.SingleData;

import java.util.List;

@SuppressWarnings("unused")
public class SingleDataTemplate extends SingleData<Integer> {
    public SingleDataTemplate(List<Commit> commits) {
        super(
                "Single Data Template",
                "Sample Description",
                extract(commits)
        );
    }

    private static Integer extract(List<Commit> commits) {

        // Implement extraction here and return value

        return commits.size();
    }
}
