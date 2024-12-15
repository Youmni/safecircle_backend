package org.safecircle.backend.controllers;

import org.safecircle.backend.dto.InvitationDTO;
import org.safecircle.backend.models.Invitation;
import org.safecircle.backend.services.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/invitation")
public class InvitationController {
    private final InvitationService invitationService;

    @Autowired
    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @CrossOrigin
    @GetMapping(value = "/{invitationId}")
    public ResponseEntity<InvitationDTO> showInvitation(@PathVariable long invitationId) {
        return invitationService.showInvitation(invitationId);
    }

    @CrossOrigin
    @GetMapping(value = "/showAll/{receiverId}")
    public ResponseEntity<List<InvitationDTO>> showAllInvitations(@PathVariable long receiverId) {
        return invitationService.showAllInvitations(receiverId);
    }

    @CrossOrigin
    @PostMapping(value = "/create/{circleId}/from/{senderId}/to/{receiverId}")
    public ResponseEntity<String> createInvitation(@PathVariable long senderId, @PathVariable long receiverId, @PathVariable long circleId) {
        return invitationService.createInvitation(senderId, receiverId, circleId);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{invitationId}/delete")
    public ResponseEntity<String> deleteInvitation(@PathVariable long invitationId) {
        return invitationService.deleteInvitation(invitationId);
    }

    @CrossOrigin
    @PutMapping(value = "/{invitationId}/{circleId}/{receiverId}/accept")
    public ResponseEntity<String> acceptInvitation(@PathVariable long invitationId, @PathVariable long circleId, @PathVariable List<Long> receiverId) {
        return invitationService.acceptInvitation(invitationId, receiverId, circleId);
    }

    @CrossOrigin
    @PutMapping(value = "/{invitationId}/decline")
    public ResponseEntity<String> declineInvitation(@PathVariable long invitationId) {
        return invitationService.declineInvitation(invitationId);
    }
}
