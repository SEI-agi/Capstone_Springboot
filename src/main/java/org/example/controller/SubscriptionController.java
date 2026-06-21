package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.SubscriptionCreateRequest;
import org.example.dto.SubscriptionResponse;
import org.example.dto.SubscriptionUpdateRequest;
import org.example.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> allSubscriptions() {
        return ResponseEntity.ok(subscriptionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> subscriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.findById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<SubscriptionResponse>> subscriptionsByPatient(@PathVariable Long patientId) {
        // TODO: enforce that patients can only access their own subscriptions once authentication is added.
        return ResponseEntity.ok(subscriptionService.findByPatientId(patientId));
    }

    @PostMapping("/patient/{patientId}/package/{packageId}")
    public ResponseEntity<SubscriptionResponse> createSubscription(@PathVariable Long patientId,
                                                                   @PathVariable Long packageId,
                                                                   @Valid @RequestBody SubscriptionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.create(patientId, packageId, request));
    }

    @PutMapping("/{subscriptionId}/medical-staff/{staffId}")
    public ResponseEntity<SubscriptionResponse> assignMedicalStaff(@PathVariable Long subscriptionId, @PathVariable Long staffId) {
        return ResponseEntity.ok(subscriptionService.assignMedicalStaff(subscriptionId, staffId));
    }

    @PutMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> updateSubscription(@PathVariable Long subscriptionId,
                                                                    @Valid @RequestBody SubscriptionUpdateRequest request) {
        return ResponseEntity.ok(subscriptionService.update(subscriptionId, request));
    }

    @PutMapping("/{subscriptionId}/cancel")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(@PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.cancel(subscriptionId));
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.delete(subscriptionId);
        return ResponseEntity.noContent().build();
    }
}