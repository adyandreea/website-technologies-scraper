package com.andreea.website_tech_scraper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainResult {
    private String domain;
    private String status;
    private String errorMessage;
    private int detectedTechnologiesCount;
    private List<TechnologyDetection> technologies;
}
