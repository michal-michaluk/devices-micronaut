package devices.configuration.intervals;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
class IntervalRulesController {

    private final IntervalRulesConfigurationRepository intervalRules;

    @Get(uri = "/configs/IntervalRules",
            produces = MediaType.APPLICATION_JSON)
    IntervalRules get() {
        return intervalRules.get();
    }

    @Put(uri = "/configs/IntervalRules",
            consumes = MediaType.APPLICATION_JSON)
    void put(@Body @Valid IntervalRules configuration) {
        intervalRules.save(configuration);
    }
}
