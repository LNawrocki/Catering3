package pl.nawrockiit.catering3.info;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoRepository extends JpaRepository<Info, Integer> {

    Info getInfoByInfoId(Integer infoId);
}
