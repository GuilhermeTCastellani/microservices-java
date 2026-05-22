package br.edu.atitus.currencyservice.clients;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class BCBClientService {

    private final BCBClient bcbClient;

    public BCBClientService(BCBClient bcbClient) {
        this.bcbClient = bcbClient;
    }

    @Cacheable("bcb-currency")
    public BCBResponse getBCBCurrency(String moeda) {
        return bcbClient.getBCBCurrency(moeda);
    }
}
