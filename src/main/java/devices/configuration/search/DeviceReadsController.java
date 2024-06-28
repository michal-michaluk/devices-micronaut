package devices.configuration.search;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
class DeviceReadsController {

    private final DevicesReadModel reads;

    @Get(uri = "/devices",
            produces = "application/vnd.device.summary+json")
    Page<DeviceSummary> getSummary(String provider, Pageable pageable) {
        return reads.querySummary(provider, pageable);
    }

    @Get(uri = "/devices",
            produces = "application/vnd.device.pin+json")
    List<DevicePin> getPins(String provider) {
        return reads.queryPins(provider);
    }

    @Get(uri = "/devices/{deviceId}",
            produces = MediaType.APPLICATION_JSON)
    Optional<DeviceDetails> getDetails(@PathVariable String deviceId) {
        return reads.queryDetails(deviceId);
    }
}
