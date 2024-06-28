package devices.configuration.search;

import devices.configuration.communication.BootNotification;
import devices.configuration.communication.DeviceStatuses;
import devices.configuration.device.DeviceConfiguration;
import devices.configuration.device.Ownership;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Singleton
@Transactional
@AllArgsConstructor
class DevicesReadModel {

    private final DeviceReadsRepository repository;

    @EventListener
    void projectionOf(DeviceConfiguration details) {
        DeviceReadsEntity entity = repository.findById(details.deviceId())
                .orElseGet(() -> new DeviceReadsEntity(details.deviceId()));

        entity
                .setOwnership(details.ownership())
                .setDetails(details)
                .setPin(DevicePin.ofNullable(details, entity.getStatuses()))
                .setSummary(DeviceSummary.ofNullable(details, entity.getStatuses()));

        repository.save(entity);
    }

    @EventListener
    public void projectionOf(BootNotification boot) {
        DeviceReadsEntity entity = repository.findById(boot.deviceId())
                .orElseGet(() -> new DeviceReadsEntity(boot.deviceId()));

        entity.setBoot(boot);

        repository.save(entity);
    }

    @EventListener
    public void projectionOf(DeviceStatuses statuses) {
        DeviceReadsEntity entity = repository.findById(statuses.deviceId())
                .orElseGet(() -> new DeviceReadsEntity(statuses.deviceId()));

        entity
                .setStatuses(statuses)
                .setPin(DevicePin.ofNullable(entity.getDetails(), statuses))
                .setSummary(DeviceSummary.ofNullable(entity.getDetails(), statuses));

        repository.save(entity);
    }

    @Transactional(readOnly = true)
    public Optional<DeviceDetails> queryDetails(String deviceId) {
        return repository.findById(deviceId)
                .map(entity -> new DeviceDetails(entity.details, entity.boot));
    }

    @Transactional(readOnly = true)
    public List<DevicePin> queryPins(String provider) {
        return repository.findAllByProvider(provider)
                .map(DeviceReadsEntity::getPin)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<DeviceSummary> querySummary(String provider, Pageable pageable) {
        return repository.findAllByProvider(provider, pageable)
                .map(DeviceReadsEntity::getSummary);
    }

    @Repository
    interface DeviceReadsRepository extends PageableRepository<DeviceReadsEntity, String> {
        Stream<DeviceReadsEntity> findAllByProvider(String provider);

        Page<DeviceReadsEntity> findAllByProvider(String provider, Pageable pageable);
    }

    @Data
    @Accessors(chain = true)
    @Entity
    @DynamicUpdate
    @Table(name = "search")
    @NoArgsConstructor
    static class DeviceReadsEntity {

        @Id
        private String deviceId;
        @Version
        private Long version;
        private String operator;
        private String provider;

        @JdbcTypeCode(SqlTypes.JSON)
        private DevicePin pin;
        @JdbcTypeCode(SqlTypes.JSON)
        private DeviceSummary summary;
        @JdbcTypeCode(SqlTypes.JSON)
        private DeviceConfiguration details;
        @JdbcTypeCode(SqlTypes.JSON)
        private DeviceStatuses statuses;
        @JdbcTypeCode(SqlTypes.JSON)
        private BootNotification boot;

        DeviceReadsEntity(String deviceId) {
            this.deviceId = deviceId;
        }

        DeviceReadsEntity setOwnership(Ownership ownership) {
            operator = ownership.operator();
            provider = ownership.provider();
            return this;
        }
    }
}
