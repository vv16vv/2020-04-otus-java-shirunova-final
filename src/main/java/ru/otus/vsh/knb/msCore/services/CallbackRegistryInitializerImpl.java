package ru.otus.vsh.knb.msCore.services;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.CallbackRegistryImpl;

@Component
public class CallbackRegistryInitializerImpl implements CallbackRegistryInitializer {
    @Override
    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }
}
