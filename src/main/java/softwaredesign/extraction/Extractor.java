package softwaredesign.extraction;

import softwaredesign.extraction.metrics.NumberOfLinesAdded;
import softwaredesign.extraction.metrics.TopCollaborators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Extractor {
    // attributes
    private final Extractor instance = new Extractor();
    private final Set<String> metricTypes = new HashSet<>();
    private String listHash;
    List<Class<? extends Metric>> classList = new ArrayList<>();

    // getters:
    public Extractor getInstance() {
        return instance;
    }
    public Set<String> getMetricTypes() {
        return new HashSet<>(metricTypes);
    }
    public String getListHash() {
        return listHash;
    }

    public ExtractionResult extractMetrics(String path) {
        Commit[] commits = {new Commit()}; //placeholder, replaces by extraction of commits
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
