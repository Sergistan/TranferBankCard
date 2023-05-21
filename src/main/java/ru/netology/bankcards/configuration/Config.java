package ru.netology.bankcards.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import ru.netology.bankcards.repository.BankCardsRepository;

@Configuration
public class Config{

    @Bean
    public Environment createEnv() {
        return new Environment() {
            @Override
            public String[] getActiveProfiles() {
                return new String[0];
            }

            @Override
            public String[] getDefaultProfiles() {
                return new String[0];
            }

            @Override
            public boolean acceptsProfiles(String... profiles) {
                return false;
            }

            @Override
            public boolean acceptsProfiles(Profiles profiles) {
                return false;
            }

            @Override
            public boolean containsProperty(String key) {
                return false;
            }

            @Override
            public String getProperty(String key) {
                return null;
            }

            @Override
            public String getProperty(String key, String defaultValue) {
                return null;
            }

            @Override
            public <T> T getProperty(String key, Class<T> targetType) {
                return null;
            }

            @Override
            public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
                return null;
            }

            @Override
            public String getRequiredProperty(String key) throws IllegalStateException {
                return null;
            }

            @Override
            public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
                return null;
            }

            @Override
            public String resolvePlaceholders(String text) {
                return null;
            }

            @Override
            public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
                return null;
            }
        };
    }


    @Bean
    public BankCardsRepository repository() {
        BankCardsRepository bankCardsRepository = new BankCardsRepository(createEnv());
        bankCardsRepository.initRepository();
        return bankCardsRepository;
    }

}
