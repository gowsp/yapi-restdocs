package io.github.gowsp.module.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

public class ConfigModule extends AbstractModule {
    private final Config config;

    public ConfigModule(Config config) {
        this.config = config;
    }

    @Provides
    @Singleton
    public YApiConfig getConfig() {
        return config.getYapi();
    }
}
