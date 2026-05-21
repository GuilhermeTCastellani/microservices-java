package br.edu.atitus.currencyservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cbc-client", url = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata")
public interface BCBClient {
    //consulta Data fixa 15/05/2026
    @GetMapping("/CotacaoMoedaDia(moeda=@moeda,dataCotacao=@dataCotacao)?%40moeda=%22{moeda}%22&%40dataCotacao='15-05-2026'&%24format=json")
    BCBResponse getBCBCurrency (@PathVariable String moeda);
}
