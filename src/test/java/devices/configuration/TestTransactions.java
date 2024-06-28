package devices.configuration;

import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.hibernate.Session;

import java.util.function.Supplier;

@Singleton
public class TestTransactions {

    @Inject
    TransactionOperations<Session> transactionManager;

    public void transactional(Runnable block) {
        transactionManager.executeWrite(status -> {
            block.run();
            return null;
        });
    }

    public <T> T transactional(Supplier<T> block) {
        return transactionManager.executeWrite(status -> block.get());
    }
}
