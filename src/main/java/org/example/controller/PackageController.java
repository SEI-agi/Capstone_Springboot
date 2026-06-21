package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.PackageCreateRequest;
import org.example.dto.PackageResponse;
import org.example.dto.PackageUpdateRequest;
import org.example.service.PackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping
    public ResponseEntity<List<PackageResponse>> allPackages() {
        return ResponseEntity.ok(packageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageResponse> packageById(@PathVariable Long id) {
        return ResponseEntity.ok(packageService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PackageResponse> createPackage(@Valid @RequestBody PackageCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packageService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageResponse> updatePackage(@PathVariable Long id, @Valid @RequestBody PackageUpdateRequest request) {
        return ResponseEntity.ok(packageService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        packageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}