package devices.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.runtime.Micronaut;
import jakarta.inject.Singleton;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
