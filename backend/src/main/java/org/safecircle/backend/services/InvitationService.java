package org.safecircle.backend.services;

import org.safecircle.backend.dtos.InvitationDTO;
import org.safecircle.backend.enums.InvitationStatus;
import org.safecircle.backend.models.Circle;
import org.safecircle.backend.models.Invitation;
import org.safecircle.backend.models.User;
import org.safecircle.backend.repositories.CircleRepository;
import org.safecircle.backend.repositories.InvitationRepository;
import org.safecircle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final CircleRepository circleRepository;
    private CircleService circleService;

    @Autowired
    public InvitationService(InvitationRepository invitationRepository, UserRepository userRepository, CircleRepository circleRepository, CircleService circleService) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.circleRepository = circleRepository;
        this.circleService = circleService;
    }

    public ResponseEntity<InvitationDTO> showInvitation(long invitationId) {
        if(!invitationRepository.existsById(invitationId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Invitation invitation = invitationRepository.findByInvitationId(invitationId).getFirst();
        InvitationDTO invitationDTO = new InvitationDTO();
        invitationDTO.setInvitationId(invitationId);
        invitationDTO.setStatus(invitation.getStatus());
        invitationDTO.setCreatedAt(invitation.getCreatedAt());
        invitationDTO.setCircleId(invitation.getCircle().getCircleId());
        invitationDTO.setCircleName(invitation.getCircle().getCircleName());
        invitationDTO.setSenderId(invitation.getSender().getUserId());
        invitationDTO.setReceiverId(invitation.getReceiver().getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(invitationDTO);
    }

    public ResponseEntity<List<InvitationDTO>> showAllInvitations(long receiverId) {
        if(!userRepository.existsById(receiverId)) {
            ResponseEntity.badRequest().body("Receiver not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        User receiver = userRepository.findById(receiverId).get();
        List<Invitation> invitations = invitationRepository.findInvitationsByReceiver(receiver);
        List<Invitation> filteredInvitations = new ArrayList<>();
        for (Invitation invitation : invitations) {
            if(invitation.getStatus() == InvitationStatus.PENDING) {
                filteredInvitations.add(invitation);
            }
        }
        List<InvitationDTO> invitationDTOs = new ArrayList<>();
        for (Invitation invitation : filteredInvitations) {
            InvitationDTO invitationDTO = new InvitationDTO();
            invitationDTO.setInvitationId(invitation.getInvitationId());
            invitationDTO.setStatus(invitation.getStatus());
            invitationDTO.setCreatedAt(invitation.getCreatedAt());
            invitationDTO.setCircleId(invitation.getCircle().getCircleId());
            invitationDTO.setCircleName(invitation.getCircle().getCircleName());
            invitationDTO.setReceiverId(invitation.getReceiver().getUserId());
            invitationDTO.setSenderId(invitation.getSender().getUserId());
            invitationDTOs.add(invitationDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(invitationDTOs);
    }

    public ResponseEntity<String> createInvitation(long senderId, long receiverId, long circleId) {
        if(!userRepository.existsByUserId(senderId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sender not found");
        }
        if(!userRepository.existsByUserId(receiverId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receiver not found");
        }
        if(!circleRepository.existsById(circleId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Circle not found");
        }
        User sender = userRepository.findByUserId(senderId).getFirst();
        User receiver = userRepository.findByUserId(receiverId).getFirst();
        Circle circle = circleRepository.findById(circleId).get();
        Invitation invitation = new Invitation(InvitationStatus.PENDING);
        invitation.setSender(sender);
        invitation.setReceiver(receiver);
        invitation.setCircle(circle);
        invitationRepository.save(invitation);

        return ResponseEntity.status(HttpStatus.CREATED).body("Invitation is created");
    }

    public ResponseEntity<String> deleteInvitation(long invitationId) {
        if(!invitationRepository.existsById(invitationId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation not found");
        }
        invitationRepository.deleteById(invitationId);
        return ResponseEntity.status(HttpStatus.OK).body("Invitation is deleted");
    }

    public ResponseEntity<String> acceptInvitation(long invitationId, List<Long> receiverId, long circleId) {
        if(!invitationRepository.existsById(invitationId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation not found");
        }
        if(receiverId.size() > 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Multiple user IDs are not allowed");
        }
        if (!userRepository.existsById(receiverId.getFirst())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if(!circleRepository.existsById(circleId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Circle not found");
        }
        Invitation invitation = invitationRepository.findByInvitationId(invitationId).getFirst();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
        circleService.addUsersToCircle(circleId, receiverId);
        return ResponseEntity.status(HttpStatus.OK).body("Invitation is accepted");
    }

    public ResponseEntity<String> declineInvitation(long invitationId) {
        if(!invitationRepository.existsById(invitationId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation not found");
        }
        Invitation invitation = invitationRepository.findByInvitationId(invitationId).getFirst();
        invitation.setStatus(InvitationStatus.DECLINED);
        invitationRepository.save(invitation);
        return ResponseEntity.status(HttpStatus.OK).body("Invitation is declined");
    }
}
