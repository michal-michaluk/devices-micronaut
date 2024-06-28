package devices.configuration.device;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
class DeviceController {

    private final DeviceService service;

    @Patch(uri = "/devices/{deviceId}",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    DeviceConfiguration patchStation(@PathVariable String deviceId,
                                     @Body @Valid UpdateDevice update) {
        return service.updateDevice(deviceId, update)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Device not found"));
    }
}
