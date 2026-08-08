package com.andreea.website_tech_scraper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Technology detection DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyDetectionDTO {
    private String name;
    private String category;
    private List<DetectionEvidenceDTO> detectionEvidence;
}
