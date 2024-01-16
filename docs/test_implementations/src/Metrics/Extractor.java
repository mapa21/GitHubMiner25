package Metrics;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final public class Extractor {
//    private static Extractor instance = new Extractor();
    public Extractor() {
        classList.add(NumberOfLinesAdded.class);
        classList.add(NumberOfLinesDeleted.class);
        /*
        ...
        classes.add(YourClassName.class);
        */
        System.out.println(classList);
//        classList.forEach((e) -> metricTypes.add(e.getName()));
        for (Class<? extends Metric> c : classList) {
            System.out.println(c.getName());

//            metricTypes.add(c.getName());
        }
    }
//    public static Extractor getInstance() {
//        return instance;
//    }
    private List<Class<? extends Metric>> classList = new ArrayList<>();
    public Set<String> metricTypes = new HashSet<String>();
    public ExtractionResult extractMetrics(String path) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        Metrics.Commit[] commits = {new Commit(1)};
        List<Metric> metrics = new ArrayList<Metric>();


        for (Class<? extends Metric> type : classList) {
//            System.out.println(type.getConstructor(Commit[].class));
            metrics.add((Metric) type.getConstructor(Commit[].class).newInstance((Object) commits));

        }

        return new ExtractionResult(metrics, "absc");
    }
}

