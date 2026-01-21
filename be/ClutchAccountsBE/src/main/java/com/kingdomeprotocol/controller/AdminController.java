package com.kingdomeprotocol.controller;

import java.time.LocalDateTime;
import java.util.Map;

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

import com.kingdomeprotocol.model.AccForSellModel;
import com.kingdomeprotocol.repository.AccForSellRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    
    private final AccForSellRepository accRepo;
    
    // Get all Valorant accounts
    @GetMapping("/accounts/valorant")
    public ResponseEntity<?> getAllValorantAccounts() {
        try {
            return ResponseEntity.ok(accRepo.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch accounts: " + e.getMessage()));
        }
    }
    
    // Get single Valorant account by ID
    @GetMapping("/accounts/valorant/{id}")
    public ResponseEntity<?> getValorantAccountById(@PathVariable int id) {
        try {
            return accRepo.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch account: " + e.getMessage()));
        }
    }
    
    // Create new Valorant account
    @PostMapping("/accounts/valorant")
    public ResponseEntity<?> createValorantAccount(@RequestBody AccForSellDTO dto) {
        try {
            AccForSellModel account = new AccForSellModel();
            account.setEmail(dto.email());
            account.setUsername(dto.username());
            account.setAccount_psw(dto.password());
            account.setPrice(dto.price());
            account.setListed_at(dto.listed_at() != null ? dto.listed_at() : LocalDateTime.now());
            account.setSold(dto.isSold() != null ? dto.isSold() : false);
            account.setLocked(dto.isLocked() != null ? dto.isLocked() : false);
            account.setLockedUntil(dto.lockedUntil());
            account.setAccount_type(dto.account_type());
            
            AccForSellModel saved = accRepo.save(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to create account: " + e.getMessage()));
        }
    }
    
    // Update Valorant account
    @PutMapping("/accounts/valorant/{id}")
    public ResponseEntity<?> updateValorantAccount(@PathVariable int id, @RequestBody AccForSellDTO dto) {
        try {
            return accRepo.findById(id)
                    .map(account -> {
                        if (dto.email() != null) account.setEmail(dto.email());
                        if (dto.username() != null) account.setUsername(dto.username());
                        if (dto.password() != null) account.setAccount_psw(dto.password());
                        if (dto.price() != null) account.setPrice(dto.price());
                        if (dto.listed_at() != null) account.setListed_at(dto.listed_at());
                        if (dto.isSold() != null) account.setSold(dto.isSold());
                        if (dto.isLocked() != null) account.setLocked(dto.isLocked());
                        account.setLockedUntil(dto.lockedUntil());
                        if (dto.account_type() != null) account.setAccount_type(dto.account_type());
                        
                        AccForSellModel updated = accRepo.save(account);
                        return ResponseEntity.ok(updated);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to update account: " + e.getMessage()));
        }
    }
    
    // Delete Valorant account
    @DeleteMapping("/accounts/valorant/{id}")
    public ResponseEntity<?> deleteValorantAccount(@PathVariable int id) {
        try {
            return accRepo.findById(id)
                    .map(account -> {
                        if (account.isSold()) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body(Map.of("error", "Cannot delete a sold account"));
                        }
                        accRepo.delete(account);
                        return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete account: " + e.getMessage()));
        }
    }
    
    // DTO for account creation/update
    public record AccForSellDTO(
        String email,
        String username,
        String password,
        Integer price,
        LocalDateTime listed_at,
        Boolean isSold,
        Boolean isLocked,
        LocalDateTime lockedUntil,
        String account_type
    ) {}
}
