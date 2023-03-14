package softwaredesign.metrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final public class Extractor {
    Extractor instance = new Extractor();
    private Set<String> metricTypes = new HashSet<>();
    List<Class<? extends Metric>> classList = new ArrayList<>();



    private Extractor() {

    }
}
