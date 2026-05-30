package br.edu.atitus.currencyservice.controllers;

import br.edu.atitus.currencyservice.clients.BCBClientService;
import br.edu.atitus.currencyservice.clients.BCBResponse;
import br.edu.atitus.currencyservice.dtos.CurrencyDTO;
import br.edu.atitus.currencyservice.entities.CurrencyEntity;
import br.edu.atitus.currencyservice.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableCaching
@RestController
@RequestMapping("currency")
public class CurrencyController {
    private final CurrencyRepository repository;
    private final BCBClientService bcbClientService;
    private final CacheManager cacheManager;

    @Value("${server.port}")
    private String port;

    @Value("${convert.sleep:0}")
    private int sleep;

    public CurrencyController(CurrencyRepository repository, BCBClientService bcbClientService, CacheManager cacheManager) {
        this.repository = repository;
        this.bcbClientService = bcbClientService;
        this.cacheManager = cacheManager;
    }

    @GetMapping("/convert")
    public ResponseEntity<CurrencyDTO> getConvert(@RequestParam String source, @RequestParam String target) throws Exception {

        Thread.sleep(sleep);

        source = source.toUpperCase();
        target = target.toUpperCase();

        String dataSource = "Cache";
        String nameCache = "Currency";
        //Desabilitar o cache para vizualisar melhor o load balanced
        //CurrencyEntity currency = cacheManager.getCache(nameCache).get(source + target, CurrencyEntity.class);
        CurrencyEntity currency = null;
        if (currency == null) {
            currency = new CurrencyEntity();
            currency.setSourceCurrency(source);
            currency.setTargetCurrency(target);
            if (source.equals(target)) {
                currency.setConversionRate(1.0);
            } else {
                try {
                    Double sourceRate = 1.0;
                    Double targetRate = 1.0;
                    if (!source.equals("BRL")) {
                        BCBResponse response = bcbClientService.getBCBCurrency(source);
                        if (response.value().isEmpty()) throw new Exception("Currency not found for " + source);
                        sourceRate = response.value().get(0).cotacaoVenda();
                    }
                    if (!target.equals("BRL")) {
                        BCBResponse response = bcbClientService.getBCBCurrency(target);
                        if (response.value().isEmpty()) throw new Exception("Currency not found for " + target);
                        targetRate = response.value().get(0).cotacaoVenda();
                    }
                    currency.setConversionRate(sourceRate / targetRate);
                    dataSource = "Banco Central do Brasil";
                } catch (Exception e) {
                    currency = repository.findBySourceCurrencyAndTargetCurrency(source, target)
                            .orElseThrow(() -> new Exception("Currency not found"));
                    dataSource = "Local database";
                }
            }
        }

        String environment = "Currency Service running on port " + port + " - " + dataSource;
        CurrencyDTO dto = new CurrencyDTO(
                currency.getSourceCurrency(),
                currency.getTargetCurrency(),
                currency.getConversionRate(),
                environment
        );

        return ResponseEntity.ok(dto);
    }
}
