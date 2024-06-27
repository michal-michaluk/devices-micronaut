package devices.configuration.intervals;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
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
@AllArgsConstructor
class IntervalRulesDocumentRepository implements IntervalRulesRepository {

    public static final String CONFIG_NAME = "IntervalRules";
    private final ConfigurationRepository repository;

    @Override
    public IntervalRules get() {
        return repository.findByName(CONFIG_NAME)
                .map(entity -> entity.configuration)
                .orElse(IntervalRules.defaultRules());
    }

    public void save(IntervalRules configuration) {
        var entity = repository.findByName(CONFIG_NAME)
                .orElseGet(() -> new IntervalRulesEntity(CONFIG_NAME));
        entity.configuration = configuration;
        repository.save(entity);
    }

    @Repository
    interface ConfigurationRepository extends CrudRepository<IntervalRulesEntity, String> {
        Optional<IntervalRulesEntity> findByName(String name);
    }

    @Getter
    @Entity
    @Table(name = "config")
    static class IntervalRulesEntity {
        @Id
        String name;
        @JdbcTypeCode(SqlTypes.JSON)
        IntervalRules configuration;

        IntervalRulesEntity(String name) {
            this.name = name;
        }

        public IntervalRulesEntity() {
        }
    }
}
