package br.edu.atitus.productservice.dtos;

import java.util.List;

public record ReviewResponseDTO(
        Double average,
        Integer count,
        List<ReviewDTO> items
) {
}
