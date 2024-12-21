package org.safecircle.backend.services;

import org.safecircle.backend.dto.BlacklistDTO;
import org.safecircle.backend.models.Blacklist;
import org.safecircle.backend.models.User;
import org.safecircle.backend.repositories.BlacklistRepository;
import org.safecircle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlacklistService {
    private final BlacklistRepository blacklistRepository;
    private final UserRepository userRepository;

    @Autowired
    public BlacklistService(BlacklistRepository blacklistRepository, UserRepository userRepository) {
        this.blacklistRepository = blacklistRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<BlacklistDTO> showBlacklist(long blacklistId) {
        if (!blacklistRepository.existsByBlacklistId(blacklistId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Blacklist blacklist = blacklistRepository.findByBlacklistId(blacklistId).getFirst();
        BlacklistDTO blacklistDTO = new BlacklistDTO(
                blacklist.getDescription(),
                blacklist.getStartDate(),
                blacklist.getEndDate(),
                blacklist.getUser().getUserId()
        );
        blacklistDTO.setBlacklistId(blacklistId);
        blacklistDTO.setCreatedAt(blacklist.getCreatedAt());
        return ResponseEntity.status(HttpStatus.OK).body(blacklistDTO);
    }

    public ResponseEntity<List<BlacklistDTO>> showAllBlacklistsFromUser(long userId) {
        if (!userRepository.existsByUserId(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<Blacklist> listBlacklists = blacklistRepository.findByUser(userRepository.findByUserId(userId).getFirst());
        List<BlacklistDTO> blacklistDTOs = new ArrayList<>();
        for (Blacklist blacklist : listBlacklists) {
            BlacklistDTO blacklistDTO = new BlacklistDTO(
                    blacklist.getDescription(),
                    blacklist.getStartDate(),
                    blacklist.getEndDate(),
                    blacklist.getUser().getUserId()
            );
            blacklistDTO.setBlacklistId(blacklist.getBlacklistId());
            blacklistDTO.setCreatedAt(blacklist.getCreatedAt());
            blacklistDTOs.add(blacklistDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(blacklistDTOs);
    }

    public ResponseEntity<String> createBlacklist(BlacklistDTO blacklistDTO) {
        try{
            if(!userRepository.existsByUserId(blacklistDTO.getUserId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            if(blacklistDTO.getEndDate().isBefore(blacklistDTO.getStartDate())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("End date is after start date");
            }
            User user = userRepository.findByUserId(blacklistDTO.getUserId()).getFirst();
            Blacklist blacklist = new Blacklist(blacklistDTO.getDescription(), blacklistDTO.getStartDate(), blacklistDTO.getEndDate(), user);
            blacklistRepository.save(blacklist);
            return ResponseEntity.status(HttpStatus.CREATED).body("Blacklist successfully created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong while creating blacklist");
        }
    }

    public ResponseEntity<String> updateBlacklist(long blacklistId, String description, LocalDate startDate, LocalDate endDate) {
        try{
            if(!blacklistRepository.existsByBlacklistId(blacklistId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blacklist not found");
            }
            boolean isChanged = false;

            Blacklist blacklist = blacklistRepository.findByBlacklistId(blacklistId).getFirst();

            if(description != null && !description.isEmpty()) {
                blacklist.setDescription(description.trim());
                isChanged = true;
            }
            if(startDate != null) {
                if(endDate != null) {
                    if(startDate.isBefore(endDate)) {
                        blacklist.setStartDate(startDate);
                        isChanged = true;
                    }
                    else{
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date can't be after end date");
                    }
                }
                else {
                    if(startDate.isBefore(blacklist.getEndDate())) {
                        blacklist.setStartDate(startDate);
                        isChanged = true;
                    }
                    else{
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date can't be after end date");
                    }
                }
            }
            if(endDate != null) {
                if (startDate != null) {
                    blacklist.setEndDate(endDate);
                }
                else {
                    if(endDate.isAfter(blacklist.getStartDate())) {
                        blacklist.setEndDate(endDate);
                        isChanged = true;
                    }
                    else{
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date can't be before start date");
                    }
                }
            }
            if(isChanged) {
                blacklistRepository.save(blacklist);
                return ResponseEntity.status(HttpStatus.OK).body("Blacklist successfully updated");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nothing to update");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while updating this blacklist");
        }
    }

    public ResponseEntity<String> deleteBlacklist(long blacklistId) {
        try{
            if(!blacklistRepository.existsByBlacklistId(blacklistId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blacklist not found");
            }
            Blacklist blacklist = blacklistRepository.findByBlacklistId(blacklistId).getFirst();
            blacklistRepository.delete(blacklist);
            return ResponseEntity.status(HttpStatus.OK).body("Blacklist successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while deleting this blacklist");
        }
    }
}
