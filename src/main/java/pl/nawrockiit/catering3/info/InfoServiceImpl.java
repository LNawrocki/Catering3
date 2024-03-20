package pl.nawrockiit.catering3.info;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InfoServiceImpl implements InfoService {

    private final InfoRepository infoRepository;

    @Override
    public List<Info> findAll() {
        return infoRepository.findAll();
    }

    @Override
    public void save(Info info) {
        infoRepository.save(info);
    }

    @Override
    public Info getInfoByInfoId(Integer infoId) {
        return infoRepository.getInfoByInfoId(infoId);
    }

    @Override
    public void delete(Info info) {
        infoRepository.delete(info);
    }


}
