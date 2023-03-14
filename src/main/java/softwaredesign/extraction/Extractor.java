package softwaredesign.extraction;

import softwaredesign.extraction.metrics.TopCollaborators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final public class Extractor {
    // attributes
    Extractor instance = new Extractor();
    final private Set<String> metricTypes = new HashSet<>();
    List<Class<? extends Metric>> classList = new ArrayList<>();

    // getters:
    Extractor getInstance() {
        return instance;
    }
    Set<String> getMetricTypes() {
        return new HashSet<>(metricTypes);
    }

    private Extractor() {
        classList.add(TopCollaborators.class);
    }
}
