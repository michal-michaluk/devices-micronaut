package devices.configuration.intervals;

import devices.configuration.communication.BootNotification;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

import java.time.Duration;

@Singleton
@Transactional(readOnly = true)
@AllArgsConstructor
public class IntervalsService {
    private final IntervalRulesRepository repository;

    public Duration calculateInterval(BootNotification boot) {
        IntervalRules rules = repository.get();
        return rules.calculateInterval(boot);
    }
}
