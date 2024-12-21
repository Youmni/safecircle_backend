package org.safecircle.backend.controllers;

import jakarta.validation.Valid;
import org.safecircle.backend.dto.BlacklistDTO;
import org.safecircle.backend.models.Blacklist;
import org.safecircle.backend.services.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/blacklist")
public class BlacklistController {
    private final BlacklistService blacklistService;

    @Autowired
    public BlacklistController(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @CrossOrigin
    @GetMapping(value = "/{blacklistId}")
    public ResponseEntity<BlacklistDTO> showBlacklist(@PathVariable long blacklistId) {
        return blacklistService.showBlacklist(blacklistId);
    }

    @CrossOrigin
    @GetMapping(value = "/showAll/{userId}")
    public ResponseEntity<List<BlacklistDTO>> showAllBlacklistFromUser(@PathVariable long userId) {
        return blacklistService.showAllBlacklistsFromUser(userId);
    }

    @CrossOrigin
    @PostMapping(value = "/create")
    public ResponseEntity<String> createBlacklist(@Valid @RequestBody BlacklistDTO blacklistDTO) {
        return blacklistService.createBlacklist(blacklistDTO);
    }

    @CrossOrigin
    @PutMapping(value = "/{blacklistId}/update")
    public ResponseEntity<String> updateBlacklist(
            @PathVariable long blacklistId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            ) {
        return blacklistService.updateBlacklist(blacklistId, description, startDate, endDate);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{blacklistId}/delete")
    public ResponseEntity<String> deleteBlacklist(@PathVariable long blacklistId) {
        return blacklistService.deleteBlacklist(blacklistId);
    }
}
