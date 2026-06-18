package br.edu.atitus.productservice.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.atitus.productservice.dtos.ReviewDTO;
import br.edu.atitus.productservice.dtos.ReviewInDTO;
import br.edu.atitus.productservice.dtos.ReviewResponseDTO;
import br.edu.atitus.productservice.entities.ReviewEntity;
import br.edu.atitus.productservice.repositories.ProductRepository;
import br.edu.atitus.productservice.repositories.ReviewRepository;

@RestController
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewController(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    // Público: lista as avaliações de um produto + média e total.
    @GetMapping("/products/{idProduct}/reviews")
    public ResponseEntity<ReviewResponseDTO> getReviews(@PathVariable Long idProduct) {
        List<ReviewDTO> items = reviewRepository
                .findByProductIdOrderByCreatedAtDesc(idProduct)
                .stream()
                .map(r -> new ReviewDTO(
                        r.getId(),
                        r.getUserName(),
                        r.getRating(),
                        r.getComment(),
                        r.getCreatedAt()))
                .toList();

        Double average = reviewRepository.averageByProductId(idProduct);
        int count = (int) reviewRepository.countByProductId(idProduct);

        return ResponseEntity.ok(new ReviewResponseDTO(average, count, items));
    }

    // Autenticado (rota /ws/): o usuário logado avalia o produto uma única vez.
    @PostMapping("/ws/products/{idProduct}/reviews")
    public ResponseEntity<ReviewDTO> postReview(
            @PathVariable Long idProduct,
            @RequestBody ReviewInDTO dto,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Email") String userEmail) throws Exception {

        if (dto.rating() == null || dto.rating() < 1 || dto.rating() > 5)
            throw new Exception("A nota deve estar entre 1 e 5.");

        if (!productRepository.existsById(idProduct))
            throw new Exception("Produto não encontrado!");

        if (reviewRepository.existsByProductIdAndUserId(idProduct, userId))
            throw new Exception("Você já avaliou este produto.");

        ReviewEntity review = new ReviewEntity();
        review.setProductId(idProduct);
        review.setUserId(userId);
        review.setUserName(userEmail);
        review.setRating(dto.rating());
        review.setComment(dto.comment());
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        ReviewDTO out = new ReviewDTO(
                review.getId(),
                review.getUserName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String message = e.getMessage() == null ? "Erro" : e.getMessage().replaceAll("[\\r\\n]", " ");
        return ResponseEntity.badRequest().body(message);
    }
}
