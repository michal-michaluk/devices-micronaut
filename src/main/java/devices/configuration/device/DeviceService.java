package devices.configuration.device;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Singleton
@Transactional
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository repository;

    @Transactional(readOnly = true)
    public Optional<DeviceConfiguration> getDevice(String deviceId) {
        return repository.get(deviceId)
                .map(Device::toDeviceConfiguration);
    }

    public DeviceConfiguration createNewDevice(String deviceId, UpdateDevice update) {
        Device device = Device.newDevice(deviceId);
        update.apply(device);
        repository.save(device);
        return device.toDeviceConfiguration();
    }

    public Optional<DeviceConfiguration> updateDevice(String deviceId, UpdateDevice update) {
        return repository.get(deviceId)
                .map(device -> {
                    update.apply(device);
                    repository.save(device);
                    return device.toDeviceConfiguration();
                });
    }
}
