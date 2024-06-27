package devices.configuration.communication.protocols.iot20;

import devices.configuration.intervals.IntervalsService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
class IoT20Controller {
    private final IntervalsService intervals;

    @Post(uri = "/protocols/iot20/bootnotification/{deviceId}",
            consumes = "application/json", produces = "application/json")
    BootNotificationResponse handleBootNotification(@PathVariable String deviceId,
                                                    @Body BootNotificationRequest request) {
        return BootNotificationResponse.builder()
                .currentTime(Instant.now().toString())
                .interval((int) intervals.calculateInterval(request.toBootNotificationEvent(deviceId)).getSeconds())
                .status(BootNotificationResponse.Status.Accepted)
                .build();
    }
}
