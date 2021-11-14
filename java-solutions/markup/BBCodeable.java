package markup;

import java.util.List;

public interface BBCodeable {

    public List<? extends BBCodeable> getContent();

    default void toBBCode(StringBuilder dest) {
        for (BBCodeable j : getContent()) {
            j.toBBCode(dest);
        }
    }
}