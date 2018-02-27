package life.plank.juna.zone;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Created by plank-hasan on 2/26/2018.
 */

public class BaseUnitTest {
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
}
