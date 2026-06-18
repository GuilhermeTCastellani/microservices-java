package br.edu.atitus.productservice.controllers;

import br.edu.atitus.productservice.clients.CurrencyClient;
import br.edu.atitus.productservice.clients.CurrencyResponse;
import br.edu.atitus.productservice.dtos.ProductDTO;
import br.edu.atitus.productservice.entities.ProductEntity;
import br.edu.atitus.productservice.repositories.ProductRepository;
import br.edu.atitus.productservice.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@EnableCaching
@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductRepository repository;
    private final CurrencyClient currencyClient;
    private final ReviewRepository reviewRepository;

    public ProductController(ProductRepository repository, CurrencyClient currencyClient,
                             ReviewRepository reviewRepository) {
        this.repository = repository;
        this.currencyClient = currencyClient;
        this.reviewRepository = reviewRepository;
    }

    @Value("${server.port}")
    private String port;

    // Monta o DTO de saída convertendo o preço para a moeda solicitada.
    // targetCurrency nulo => sem conversão (convertedPrice = -1).
    private ProductDTO toDTO(ProductEntity p, String targetCurrency) {
        String environment = "Product-service running on port: " + port;
        Double convertedPrice;
        String requestCurrency = targetCurrency;

        if (targetCurrency == null) {
            convertedPrice = -1.0;
        } else if (targetCurrency.equalsIgnoreCase(p.getCurrency())) {
            convertedPrice = p.getPrice();
        } else {
            CurrencyResponse currency = currencyClient.getCurrency(p.getCurrency(), targetCurrency);
            if (currency != null) {
                convertedPrice = currency.conversionRate() * p.getPrice();
                environment = environment + " - " + currency.environment();
            } else {
                convertedPrice = -1.0;
                environment = environment + " - Currency Fallback";
            }
        }

        // média das avaliações arredondada para 1 casa
        Double avg = reviewRepository.averageByProductId(p.getId());
        double rating = avg == null ? 0.0 : Math.round(avg * 10.0) / 10.0;
        int ratingCount = (int) reviewRepository.countByProductId(p.getId());

        return new ProductDTO(
                p.getId(),
                p.getDescription(),
                p.getBrand(),
                p.getModel(),
                p.getCategory(),
                p.getCurrency(),
                p.getPrice(),
                p.getStock(),
                p.getImageURL(),
                p.getSellerName(),
                rating,
                ratingCount,
                convertedPrice,
                environment,
                requestCurrency
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(
            @PathVariable Long id,
            @RequestParam String targetCurrency) throws Exception {

        ProductEntity entity = repository.findById(id)
                .orElseThrow(() -> new Exception("Product not found!"));

        return ResponseEntity.ok(toDTO(entity, targetCurrency.toUpperCase()));
    }

    @GetMapping("/noconverter/{idProduct}")
    public ResponseEntity<ProductDTO> getProductNoConverter(@PathVariable Long idProduct) throws Exception {
        ProductEntity product = repository.findById(idProduct)
                .orElseThrow(() -> new Exception("Produto não encontrado!"));

        return ResponseEntity.ok(toDTO(product, null));
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam String targetCurrency,
            @RequestParam(required = false) String category,
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "description",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) throws Exception {

        final String currency = targetCurrency.toUpperCase();

        Page<ProductEntity> products =
                (category != null && !category.isBlank())
                        ? repository.findByCategoryIgnoreCase(category, pageable)
                        : repository.findAll(pageable);

        Page<ProductDTO> productDTOs = products.map(product -> toDTO(product, currency));

        return ResponseEntity.ok(productDTOs);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e){
        String message = e.getMessage().replace("/r/n", "");
        return ResponseEntity.badRequest().body(message);
    }

}
