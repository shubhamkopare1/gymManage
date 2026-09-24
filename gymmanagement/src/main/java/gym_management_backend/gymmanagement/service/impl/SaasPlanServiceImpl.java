package gym_management_backend.gymmanagement.service.impl;

import gym_management_backend.gymmanagement.dto.response.SaasPlanResponseDto;
import gym_management_backend.gymmanagement.entity.SaasPlan;
import gym_management_backend.gymmanagement.exception.ResourceNotFoundException;
import gym_management_backend.gymmanagement.mapper.SaasPlanMapper;
import gym_management_backend.gymmanagement.repository.SaasPlanRepository;
import gym_management_backend.gymmanagement.service.SaasPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import gym_management_backend.gymmanagement.dto.request.SaasPlanRequestDto;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaasPlanServiceImpl
        implements SaasPlanService {

    private final SaasPlanRepository saasPlanRepository;

    @Override
    public SaasPlanResponseDto createPlan(
            SaasPlanRequestDto request
    ) {

        if (saasPlanRepository.existsByPlanType(
                request.getPlanType()
        )) {
            throw new RuntimeException(
                    "SaaS plan already exists"
            );
        }

        SaasPlan plan = SaasPlan.builder()
                .planType(request.getPlanType())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .durationInDays(request.getDurationInDays())
                .maxMembers(request.getMaxMembers())
                .maxTrainers(request.getMaxTrainers())
                .active(true)
                .build();

        SaasPlan savedPlan =
                saasPlanRepository.save(plan);

        return SaasPlanMapper.toResponse(savedPlan);
    }

    @Override
    public List<SaasPlanResponseDto> getAllPlans() {

        return saasPlanRepository.findAll()
                .stream()
                .map(SaasPlanMapper::toResponse)
                .toList();
    }

    @Override
    public SaasPlanResponseDto getPlanById(
            UUID planId
    ) {

        SaasPlan plan =
                saasPlanRepository.findById(planId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "SaaS plan not found"
                                )
                        );

        return SaasPlanMapper.toResponse(plan);
    }

    @Override
    public SaasPlanResponseDto updatePlan(
            UUID planId,
            SaasPlanRequestDto request
    ) {

        SaasPlan plan =
                saasPlanRepository.findById(planId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "SaaS plan not found"
                                )
                        );

        SaasPlanMapper.updateEntity(
                plan,
                request
        );

        return SaasPlanMapper.toResponse(
                saasPlanRepository.save(plan)
        );
    }
    @Override
    public List<SaasPlanResponseDto> getActivePlans() {

        return saasPlanRepository
                .findAllByActiveTrue()
                .stream()
                .map(SaasPlanMapper::toResponse)
                .toList();
    }
    @Override
    public void deletePlan(UUID planId) {

        SaasPlan plan =
                saasPlanRepository.findById(planId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "SaaS plan not found"
                                )
                        );

        plan.setActive(false);

        saasPlanRepository.save(plan);
    }
}