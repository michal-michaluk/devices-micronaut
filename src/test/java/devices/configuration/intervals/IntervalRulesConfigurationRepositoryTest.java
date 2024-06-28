package devices.configuration.intervals;

import devices.configuration.TestTransactions;
import devices.configuration.tools.FeatureConfiguration;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static devices.configuration.JsonAssert.assertThat;

@MicronautTest
class IntervalRulesConfigurationRepositoryTest {

    @Inject
    private FeatureConfiguration repository;

    @Inject
    private IntervalRulesConfigurationRepository subject;

    @Inject
    TestTransactions tx;

    @Test
    void shouldSaveAndLoadIntervalRules() {
        // when
        tx.transactional(() -> repository.save("IntervalRules", IntervalRulesFixture.currentRules()));
        var result = tx.transactional(() -> subject.get());

        // then
        assertThat(result).hasFieldsLike(IntervalRulesFixture.currentRules());
        assertThat(result).isExactlyLike("""
                {
                  "byIds": [
                    {
                      "seconds": 600,
                      "devices": [
                        "ALF-2844179",
                        "ALF-9571445",
                        "CS_7155_CGC100",
                        "EVB-P4562137",
                        "EVB-P9287312"
                      ]
                    },
                    {
                      "seconds": 2700,
                      "devices": [
                        "EVB-P0984003",
                        "EVB-P15079256",
                        "EVB-P1515526",
                        "EVB-P1515640",
                        "t53_8264_019"
                      ]
                    }
                  ],
                  "byModel": [
                    {
                      "seconds": 60,
                      "vendor": "Alfen BV",
                      "model": "NG920-5250[6-9]",
                      "firmware": null
                    },
                    {
                      "seconds": 10,
                      "vendor": "ChargeStorm AB",
                      "model": "Chargestorm Connected",
                      "firmware": "1[.]2[.].*"
                    },
                    {
                      "seconds": 60,
                      "vendor": "ChargeStorm AB",
                      "model": "Chargestorm Connected",
                      "firmware": null
                    },
                    {
                      "seconds": 120,
                      "vendor": "EV-BOX",
                      "model": "G3-M5320E-F2.*",
                      "firmware": null
                    }
                  ],
                  "defSeconds": 1800
                }
                """);
    }

}
