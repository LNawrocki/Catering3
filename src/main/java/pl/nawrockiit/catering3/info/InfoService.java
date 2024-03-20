package pl.nawrockiit.catering3.info;

import org.apache.commons.math3.ode.ODEIntegrator;

import java.util.List;

public interface InfoService {
    List<Info> findAll();
    void save(Info info);
    Info getInfoByInfoId(Integer infoId);
    void delete(Info info);
}
