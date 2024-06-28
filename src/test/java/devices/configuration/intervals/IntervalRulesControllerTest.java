package devices.configuration.intervals;

import devices.configuration.JsonAssert;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest
class IntervalRulesControllerTest {

    @Client("/")
    interface IntervalRulesClient {
        @Get(uri = "/configs/IntervalRules", produces = MediaType.APPLICATION_JSON)
        String get();

        @Put(uri = "/configs/IntervalRules", consumes = MediaType.APPLICATION_JSON)
        void put(@Body IntervalRules configuration);
    }

    @Inject
    private IntervalRulesClient rest;

    @Test
    void putDefaultIntervalRules() throws Exception {
        givenIntervalRules(IntervalRules.defaultRules());

        thenRulesAreSaved(IntervalRules.defaultRules());
    }

    @Test
    void putCurrentIntervalRules() throws Exception {
        givenIntervalRules(IntervalRulesFixture.currentRules());

        thenRulesAreSaved(IntervalRulesFixture.currentRules());
    }

    private void givenIntervalRules(IntervalRules rules) {
        rest.put(rules);
    }

    private void thenRulesAreSaved(IntervalRules rules) {
        JsonAssert.assertThat(rest.get())
                .isExactlyLike(rules);
    }
}
