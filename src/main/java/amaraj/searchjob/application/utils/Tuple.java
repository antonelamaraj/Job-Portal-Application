package amaraj.searchjob.application.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Tuple<A, B, C, D, E> {
    private final A id;
    private final B name;
    private final C email;
    private final D jobTitle;
    private final E dateAchived;
}
