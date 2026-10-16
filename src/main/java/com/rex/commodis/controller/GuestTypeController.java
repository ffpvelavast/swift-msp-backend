package com.rex.commodis.controller;

import com.rex.commodis.dto.GuestTypeDto;
import com.rex.commodis.service.GuestTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing GuestTypes.
 */
@RestController
@RequestMapping("/api/guest-types")
@RequiredArgsConstructor
public class GuestTypeController {

    private final GuestTypeService guestTypeService;

    /**
     * GET /api/guest-types
     * Fetch all guest types, translated based on the locale.
     */
    @GetMapping
    public ResponseEntity<List<GuestTypeDto>> getAllGuestTypes(
            @RequestParam(defaultValue = "en") String locale) {
        List<GuestTypeDto> guestTypes = guestTypeService.fetchAll(locale);
        return ResponseEntity.ok(guestTypes);
    }

    /**
     * GET /api/guest-types/{id}
     * Get a specific guest type by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GuestTypeDto> getGuestTypeById(@PathVariable Long id) {
        GuestTypeDto guestType = guestTypeService.getById(id);
        return ResponseEntity.ok(guestType);
    }

    /**
     * POST /api/guest-types
     * Create a new guest type.
     */
    // @PostMapping
    // public ResponseEntity<GuestTypeDto> createGuestType(@RequestBody GuestTypeDto dto) {
    //     GuestTypeDto created = guestTypeService.create(dto);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(created);
    // }

    /**
     * PUT /api/guest-types/{id}
     * Update an existing guest type by id.
     */
    // @PutMapping("/{id}")
    // public ResponseEntity<GuestTypeDto> updateGuestType(@PathVariable Long id, @RequestBody GuestTypeDto dto) {
    //     GuestTypeDto updated = guestTypeService.update(id, dto);
    //     return ResponseEntity.ok(updated);
    // }

    /**
     * DELETE /api/guest-types/{id}
     * Delete a guest type by id.
     */
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteGuestType(@PathVariable Long id) {
    //     guestTypeService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }
}
