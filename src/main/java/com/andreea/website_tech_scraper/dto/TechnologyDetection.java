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
public class TechnologyDetection {
    private String name;
    private String category;
    private List<DetectionEvidence> detectionEvidence;
}
