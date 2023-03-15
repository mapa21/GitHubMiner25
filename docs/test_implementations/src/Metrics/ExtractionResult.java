package Metrics;
import java.util.List;

import Metrics.Metric;

public class ExtractionResult {
    public List<Metric> metrics;
    String listHash;
    public ExtractionResult(List<Metric> metrics, String listHash) {
        this.metrics = metrics;
        this.listHash = listHash;
    }
}
