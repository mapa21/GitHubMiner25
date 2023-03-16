package softwaredesign.extraction;

import lombok.Getter;
import softwaredesign.extraction.metrics.NumberOfLinesAdded;
import softwaredesign.extraction.metrics.TopCollaborators;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

public final class Extractor {
    // attributes
    @Getter
    private final Extractor instance = new Extractor();
    @Getter
    private final Set<String> metricTypes = new HashSet<>();
    @Getter
    private String listHash;
    List<Class<? extends Metric>> classList = new ArrayList<>();

    public ExtractionResult extractMetrics(String path) {
        Commit[] commits = {new Commit("Tester", "tester@vu.nl", ZonedDateTime.parse("2011-12-03T10:15:30+01:00"), "Created project", Arrays.asList(new File("a.cpp", 0, 15), new File("b.S", 12, 14), new File("c.py", 22, 0)), "129ac84eb6a", 15, 2, Boolean.FALSE)}; //placeholder, replaces by extraction of commits
        List<Metric> metrics = new ArrayList<>();

        for (Class<? extends Metric> metric : classList) {
            try {
                metrics.add(metric.getConstructor(Commit[].class).newInstance((Object) commits));
            }
            catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                //TODO: handle exception? => Should never occur
            }
        }

        return new ExtractionResult(metrics, listHash);
    }

    private Extractor() {
        classList.add(TopCollaborators.class);
        classList.add(NumberOfLinesAdded.class);
        // ...

        for (Class<? extends Metric> c : classList) {
            System.out.println(c.getName());
            // TODO: remove unnecessary package information from string and add to metricTypes list
        }

        // TODO: hash contents of list and save result in listHash

    }
}
