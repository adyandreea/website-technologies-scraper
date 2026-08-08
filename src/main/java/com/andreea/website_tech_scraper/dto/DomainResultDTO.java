package com.andreea.website_tech_scraper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Domain result DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainResultDTO {
    private String domain;
    private String status;
    private String errorMessage;
    private int detectedTechnologiesCount;
    private List<TechnologyDetectionDTO> technologies;
}
