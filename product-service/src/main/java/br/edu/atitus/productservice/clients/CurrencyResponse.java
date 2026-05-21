package br.edu.atitus.productservice.clients;

public record CurrencyResponse(
        String SourceCurrency,
        String targetCurrency,
        Double conversionRate,
        String environment) {
}
