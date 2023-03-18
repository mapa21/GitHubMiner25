package softwaredesign.extraction;

import java.util.List;
import java.util.Map;

public class ExtractionResult {
    public final Map<String, Metric> metrics;
    public final String listHash;
    public ExtractionResult(Map<String, Metric> metrics, String listHash) {
        this.metrics = metrics;
        this.listHash = listHash;
    }
}
