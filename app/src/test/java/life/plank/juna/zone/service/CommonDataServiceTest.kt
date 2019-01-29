package life.plank.juna.zone.service

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CommonDataServiceTest {

    @Test
    fun emailValidatorTest() {
        assertThat(CommonDataService.isValidEmail("email@example.com")).isTrue()
    }
}