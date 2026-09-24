package gym_management_backend.gymmanagement.controller;

import gym_management_backend.gymmanagement.dto.request.MemberRequestDto;
import gym_management_backend.gymmanagement.dto.response.MemberResponseDto;
import gym_management_backend.gymmanagement.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gym/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    @GetMapping("/me")
    public MemberResponseDto getMyProfile() {
        return memberService.getMyProfile();
    }
    @PostMapping
    public ResponseEntity<MemberResponseDto> addMember(
            @Valid @RequestBody MemberRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberService.addMember(request));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {

        return ResponseEntity.ok(
                memberService.getAllMembers()
        );
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMemberById(
            @PathVariable UUID memberId
    ) {

        return ResponseEntity.ok(
                memberService.getMemberById(memberId)
        );
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @PathVariable UUID memberId,
            @Valid @RequestBody MemberRequestDto request
    ) {

        return ResponseEntity.ok(
                memberService.updateMember(
                        memberId,
                        request
                )
        );
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable UUID memberId
    ) {

        memberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }
}