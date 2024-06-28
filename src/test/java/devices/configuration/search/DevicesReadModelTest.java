package devices.configuration.search;

import devices.configuration.communication.CommunicationFixture;
import devices.configuration.device.DeviceConfiguration;
import devices.configuration.device.DeviceFixture;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static devices.configuration.JsonAssert.assertThat;

@MicronautTest
class DevicesReadModelTest {

    @Inject
    DevicesReadModel projection;
    @Inject
    DevicesReadModel.DeviceReadsRepository repo;

    final String deviceId = "fixed-device-id";

    @BeforeEach
    void setUp() {
        repo.deleteAll();
    }

    @Test
    void findById() {
        givenDevice();

        Optional<DeviceDetails> read = projection.queryDetails(deviceId);

        assertThat(read).isExactlyLike("""
                {
                  "deviceId": "fixed-device-id",
                  "ownership": {
                    "operator": "Devicex.nl",
                    "provider": "public-devices"
                  },
                  "location": {
                    "street": "Rakietowa",
                    "houseNumber": "1A",
                    "city": "Wrocław",
                    "postalCode": "54-621",
                    "state": null,
                    "country": "POL",
                    "coordinates": {
                      "longitude": 51.09836221719513,
                      "latitude": 16.931752852309156
                    }
                  },
                  "openingHours": {
                    "alwaysOpen": true
                  },
                  "settings": {
                    "autoStart": false,
                    "remoteControl": false,
                    "billing": false,
                    "reimbursement": false,
                    "showOnMap": true,
                    "publicAccess": true
                  },
                  "violations": {
                    "operatorNotAssigned": false,
                    "providerNotAssigned": false,
                    "locationMissing": false,
                    "showOnMapButMissingLocation": false,
                    "showOnMapButNoPublicAccess": false
                  },
                  "visibility": {
                    "roamingEnabled": true,
                    "forCustomer": "USABLE_AND_VISIBLE_ON_MAP"
                  } ,
                  "boot": {
                    "protocol": "IoT16",
                    "vendor": "Garo",
                    "model": "CPF25 Family",
                    "serial": "820394A93203",
                    "firmware": "1.1"
                  }
                }
                """);
    }

    @Test
    void findAllPins() {
        DeviceConfiguration device = givenDevice();

        List<DevicePin> read = projection.queryPins(device.ownership().provider());

        assertThat(read).isExactlyLike("""
                [
                  {
                    "deviceId": "fixed-device-id",
                    "coordinates": {
                      "longitude": 51.09836221719513,
                      "latitude": 16.931752852309156
                    },
                    "statuses": [
                      "AVAILABLE",
                      "FAULTED"
                    ]
                  }
                ]
                """);
    }

    @Test
    void findAllSummary() {
        DeviceConfiguration device = givenDevice();

        Page<DeviceSummary> read = projection.querySummary(device.ownership().provider(), Pageable.from(0, 100));

        assertThat(read).isExactlyLike("""
                {
                  "content": [
                    {
                      "deviceId": "fixed-device-id",
                      "location": {
                        "street": "Rakietowa",
                        "houseNumber": "1A",
                        "city": "Wrocław",
                        "postalCode": "54-621",
                        "state": null,
                        "country": "POL",
                        "coordinates": {
                          "longitude": 51.09836221719513,
                          "latitude": 16.931752852309156
                        }
                      },
                      "statuses": [
                        "Available",
                        "Faulted"
                      ]
                    }
                  ],
                  "pageable": {
                    "requestTotal": true,
                    "max": 100,
                    "number": 0,
                    "sort": {
                      "orderBy": []
                    },
                    "size": 100,
                    "mode": "OFFSET"
                  },
                  "totalSize": 1,
                  "totalPages": 1,
                  "empty": false,
                  "size": 100,
                  "offset": 0,
                  "numberOfElements": 1,
                  "pageNumber": 0
                }
                """);
    }

    private DeviceConfiguration givenDevice() {
        DeviceConfiguration device = DeviceFixture.givenPublicDeviceConfiguration(deviceId);

        projection.projectionOf(device);
        projection.projectionOf(CommunicationFixture.boot(deviceId));
        projection.projectionOf(CommunicationFixture.statuses(deviceId));
        return device;
    }
}
