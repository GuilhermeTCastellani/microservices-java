package br.edu.atitus.authservice.controllers;

import br.edu.atitus.authservice.dtos.AddressInDTO;
import br.edu.atitus.authservice.entities.AddressEntity;
import br.edu.atitus.authservice.repositories.AddressRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ws/addresses")
public class WsAddressController {

    private final AddressRepository repository;

    public WsAddressController(AddressRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<AddressEntity>> list(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(repository.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<AddressEntity> create(
            @RequestBody AddressInDTO dto,
            @RequestHeader("X-User-Id") Long userId) {
        var entity = new AddressEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(userId);
        repository.save(entity);
        return ResponseEntity.status(201).body(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressEntity> update(
            @PathVariable Long id,
            @RequestBody AddressInDTO dto,
            @RequestHeader("X-User-Id") Long userId) throws Exception {
        var entity = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new Exception("Endereço não encontrado!"));
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUserId(userId);
        repository.save(entity);
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) throws Exception {
        var entity = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new Exception("Endereço não encontrado!"));
        repository.delete(entity);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
