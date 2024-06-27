package devices.configuration.tools;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Optional;

@Singleton
@Transactional
@AllArgsConstructor
public class FeatureConfiguration {

    private final ConfigurationRepository repository;

    public Configuration get(String name) {
        return repository.findByName(name)
                .map(entity -> new Configuration(entity.name, entity.configuration))
                .orElseGet(() -> new Configuration(name, null));
    }

    public Configuration save(String name, Object configuration) {
        var entity = repository.findByName(name)
                .orElseGet(() -> new ConfigurationEntity(name));
        JsonNode json = JsonConfiguration.jsonNode(configuration);
        entity.configuration = json;
        repository.save(entity);
        return new Configuration(name, json);
    }

    public record Configuration(String name, JsonNode configuration) {
        public <T> Optional<T> as(Class<T> type) {
            if (configuration == null) {
                return Optional.empty();
            } else {
                return Optional.of(JsonConfiguration.parse(configuration, type));
            }
        }

        public JsonNode raw() {
            return configuration;
        }
    }

    @Repository
    interface ConfigurationRepository extends CrudRepository<ConfigurationEntity, String> {
        Optional<ConfigurationEntity> findByName(String name);
    }

    @Getter
    @Entity
    @Table(name = "config")
    static class ConfigurationEntity {
        @Id
        String name;
        @JdbcTypeCode(SqlTypes.JSON)
        JsonNode configuration;

        ConfigurationEntity(String name) {
            this.name = name;
        }

        public ConfigurationEntity() {
        }
    }
}
