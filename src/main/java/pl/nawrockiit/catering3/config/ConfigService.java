package pl.nawrockiit.catering3.config;

import java.util.List;

public interface ConfigService {

    Boolean editModeStatus();
    Config getConfig();
    Config save(Config config);

    List<Config> findAll();

}
