package devices.configuration.intervals;

import devices.configuration.JsonAssert;
import devices.configuration.tools.FeatureConfiguration;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.TransactionMode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest(transactionMode = TransactionMode.SEPARATE_TRANSACTIONS)
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

    @Inject
    FeatureConfiguration repository;

    @Test
    void putDefaultIntervalRules() throws Exception {
        givenIntervalRules(IntervalRules.defaultRules());

        assertRulesSaved(IntervalRules.defaultRules());
    }

    @Test
    void putCurrentIntervalRules() throws Exception {
        givenIntervalRules(IntervalRulesFixture.currentRules());

        assertRulesSaved(IntervalRulesFixture.currentRules());
    }

    private void givenIntervalRules(IntervalRules rules) {
//        Mockito.when(repository.get("IntervalRules")).thenReturn(new FeatureConfiguration.Configuration("IntervalRules", JsonConfiguration.jsonNode(rules)));
        rest.put(rules);
    }

    private void assertRulesSaved(IntervalRules rules) {
//        Mockito.verify(repository).save("IntervalRules", rules);

        JsonAssert.assertThat(rest.get())
                .isExactlyLike(rules);
    }

//    @MockBean
//    IntervalRulesDocumentRepository dependency() {
//        return Mockito.mock(IntervalRulesDocumentRepository.class);
//    }

}
