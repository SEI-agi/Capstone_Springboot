package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.RatingCreateRequest;
import org.example.dto.RatingResponse;
import org.example.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public ResponseEntity<List<RatingResponse>> allRatings() {
        return ResponseEntity.ok(ratingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> ratingById(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.findById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RatingResponse>> ratingsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(ratingService.findByPatientId(patientId));
    }

    @GetMapping("/medical-staff/{staffId}")
    public ResponseEntity<List<RatingResponse>> ratingsByMedicalStaff(@PathVariable Long staffId) {
        return ResponseEntity.ok(ratingService.findByMedicalStaffId(staffId));
    }

    @PostMapping
    public ResponseEntity<RatingResponse> createRating(@Valid @RequestBody RatingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}