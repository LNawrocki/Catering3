package pl.nawrockiit.catering3.price;

import java.util.List;

public interface PriceService {

    List<Price> findAll();

    void deleteByPriceId(Integer priceId);

    Price save(Price price);
}
