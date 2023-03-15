package softwaredesign.Utilitites;

import jline.console.completer.StringsCompleter;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


import static jline.internal.Preconditions.checkNotNull;

/* very similar to StringsCompleter class, but uses case-insensitive check */

public class CaseInsensitiveCompleterV2 extends StringsCompleter {
    public CaseInsensitiveCompleterV2(final Collection<String> strings) {
        super(strings);
    }

    @Override
    public int complete(final String buffer, final int cursor, final List<CharSequence> candidates){
        checkNotNull(candidates);

        SortedSet<String> strings = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        strings.addAll(this.getStrings());

        if(buffer == null) {
            candidates.addAll(strings);
        }
        else {
            for (String match : strings.tailSet(buffer)) {
                if (!match.toLowerCase().startsWith(buffer.toLowerCase())) {
                    break;
                }
                candidates.add(match);
            }

        }
        return candidates.isEmpty() ? -1 : 0;
    }
}
