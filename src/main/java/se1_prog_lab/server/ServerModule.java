package se1_prog_lab.server;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import se1_prog_lab.server.interfaces.*;
import se1_prog_lab.shared.api.LengthEOTWrapper;
import se1_prog_lab.shared.api.EOTWrapper;

/**
 * Серверный модуль для Guice Dependency Injection.
 */
public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Server.class).to(ServerApp.class);
        bind(CollectionWrapper.class).to(VectorWrapper.class);
        bind(EOTWrapper.class).to(LengthEOTWrapper.class);
        bind(ServerCommandReceiver.class).to(ServerCommandReceiverImpl.class);
        bind(DatabaseManager.class).to(DatabaseManagerImpl.class);
        bind(AuthManager.class).to(AuthManagerImpl.class);
        bind(SecurePassword.class).to(PBKDF2SecurePassword.class);
        install(new FactoryModuleBuilder()
                .implement(ClientHandler.class, ClientHandlerImpl.class)
                .build(ClientHandlerFactory.class));
    }
}
