package softwaredesign.extraction.metrics;

import softwaredesign.extraction.Commit;
import softwaredesign.extraction.types.MultipleData;
import softwaredesign.utilities.NameValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommitsPerDay extends MultipleData<Integer> {

    public CommitsPerDay(List<Commit> commits) {
        super(
                "Commits Per Day",
                "These are the commits added for each day of the week",
                extract(commits)
        );
    }

    private static List<NameValue<Integer>> extract(List<Commit> commits) {
        List<NameValue<Integer>> data = new ArrayList<>();
        Map<String, Integer> commitsPerDay = new HashMap<>();
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (String day : daysOfWeek) {
            commitsPerDay.put(day, 0);
        }

        for (Commit commit : commits) {
            String dayOfWeekUppercase = String.valueOf(commit.date.getDayOfWeek());
            String dayOfWeek = dayOfWeekUppercase.charAt(0) + dayOfWeekUppercase.substring(1).toLowerCase();
            int currentCommits = commitsPerDay.get(dayOfWeek);
            commitsPerDay.put(dayOfWeek, currentCommits + 1);
        }

        for (String day : daysOfWeek) {
            data.add(new NameValue<>(day, commitsPerDay.get(day)));
        }
        return data;
    }
}
