package br.edu.atitus.productservice.dtos;

public record ProductDTO(
        Long id,
        String description,
        String brand,
        String model,
        String category,
        String currency,
        Double price,
        Integer stock,
        String imageURL,
        String sellerName,
        Double rating,
        Integer ratingCount,
        Double convertedPrice,
        String environment,
        String requestCurrency
) {
}
