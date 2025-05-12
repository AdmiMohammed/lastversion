package com.example.einternmatchback.dashboard.service;

import com.example.einternmatchback.dashboard.dto.DashboardStatsDTO;
import com.example.einternmatchback.dashboard.dto.OfferStatsDTO;
import com.example.einternmatchback.dashboard.repository.DashboardRepository;
import com.example.einternmatchback.AjoutOffers.repo.CompanyRepository;
import com.example.einternmatchback.Postulation.Entity.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final CompanyRepository companyRepository;

    public DashboardStatsDTO getCompanyDashboardStats(Integer companyId) {
        // Vérifier que la company existe
        if (!companyRepository.existsById(companyId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Company with id " + companyId + " not found"
            );
        }

        try {
            Long totalOffers = safeCount(() -> dashboardRepository.countOffersByCompany(companyId));
            Long activeOffers = safeCount(() -> dashboardRepository.countActiveOffersByCompany(companyId));
            Long totalApplications = safeCount(() -> dashboardRepository.countApplicationsByCompany(companyId));
            Long totalFavorites = safeCount(() -> dashboardRepository.countFavoritesByCompany(companyId)); // Nouveau

            Long pendingApplications = safeCount(() ->
                    dashboardRepository.countApplicationsByStatusAndCompany(companyId, ApplicationStatus.PENDING));
            Long acceptedApplications = safeCount(() ->
                    dashboardRepository.countApplicationsByStatusAndCompany(companyId, ApplicationStatus.ACCEPTED));
            Long rejectedApplications = safeCount(() ->
                    dashboardRepository.countApplicationsByStatusAndCompany(companyId, ApplicationStatus.REJECTED));

            Double acceptanceRate = totalApplications > 0 ?
                    (acceptedApplications.doubleValue() / totalApplications.doubleValue()) * 100 : 0.0;

            List<OfferStatsDTO> topOffers = safeGetTopOffers(companyId);
            Map<String, Long> applicationsByStatus = safeGetApplicationsByStatus(companyId);
            Map<String, Long> offersByType = safeGetOffersByType(companyId);
            Map<String, Long> applicationsOverTime = safeGetApplicationsOverTime(companyId);
            Map<String, Long> candidatesByField = safeGetCandidatesByField(companyId);

            return DashboardStatsDTO.builder()
                    .totalOffers(totalOffers)
                    .activeOffers(activeOffers)
                    .totalApplications(totalApplications)
                    .pendingApplications(pendingApplications)
                    .acceptedApplications(acceptedApplications)
                    .rejectedApplications(rejectedApplications)
                    .acceptanceRate(acceptanceRate)
                    .totalFavorites(totalFavorites)
                    .topOffers(topOffers)
                    .applicationsByStatus(applicationsByStatus)
                    .offersByType(offersByType)
                    .applicationsOverTime(applicationsOverTime)
                    .candidatesByField(candidatesByField)
                    .build();

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error generating dashboard stats: " + e.getMessage()
            );
        }
    }

    // Méthodes utilitaires pour gérer les cas vides/nuls
    private Long safeCount(CountSupplier supplier) {
        try {
            Long count = supplier.get();
            return count != null ? count : 0L;
        } catch (Exception e) {
            return 0L;
        }
    }

    private List<OfferStatsDTO> safeGetTopOffers(Integer companyId) {
        try {
            return dashboardRepository.findTopOffersByApplicationCount(companyId).stream()
                    .map(data -> OfferStatsDTO.builder()
                            .title((String) data.getOrDefault("title", "Unknown"))
                            .applicationCount((Long) data.getOrDefault("applicationCount", 0L))
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Map<String, Long> safeGetApplicationsByStatus(Integer companyId) {
        try {
            return dashboardRepository.countApplicationsByStatus(companyId).stream()
                    .collect(Collectors.toMap(
                            data -> ((ApplicationStatus) data.get("status")).name(),
                            data -> (Long) data.getOrDefault("count", 0L)
                    ));
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private Map<String, Long> safeGetOffersByType(Integer companyId) {
        try {
            return dashboardRepository.countOffersByType(companyId).stream()
                    .collect(Collectors.toMap(
                            data -> (String) data.getOrDefault("type", "Unknown"),
                            data -> (Long) data.getOrDefault("count", 0L)
                    ));
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private Map<String, Long> safeGetApplicationsOverTime(Integer companyId) {
        try {
            return dashboardRepository.countApplicationsOverTime(companyId).stream()
                    .collect(Collectors.toMap(
                            data -> (String) data.getOrDefault("month", "Unknown"),
                            data -> (Long) data.getOrDefault("count", 0L)
                    ));
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private Map<String, Long> safeGetCandidatesByField(Integer companyId) {
        try {
            return dashboardRepository.countCandidatesByField(companyId).stream()
                    .collect(Collectors.toMap(
                            data -> (String) data.getOrDefault("field", "Unknown"),
                            data -> (Long) data.getOrDefault("count", 0L)
                    ));
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    @FunctionalInterface
    private interface CountSupplier {
        Long get();
    }
}