package br.edu.atitus.productservice.clients;

import org.springframework.stereotype.Component;

@Component
public class CurrencyClientFalback implements CurrencyClient{
    @Override
    public CurrencyResponse getCurrency(String source, String target) {
        return null;
    }
}
