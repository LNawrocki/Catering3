package pl.nawrockiit.catering3.config;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, Integer> {
    Config getConfigById(Integer id);
}
