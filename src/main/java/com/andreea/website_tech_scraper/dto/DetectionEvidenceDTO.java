package com.andreea.website_tech_scraper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detection evidence DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectionEvidenceDTO {
    private String location;
    private String key;
    private String matchedSnippet;
}
