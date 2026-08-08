package com.andreea.website_tech_scraper.service;

import com.andreea.website_tech_scraper.dto.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Analyzes domains to detect technologies using rules.
 */
@Service
public class TechDetectionService {

    private final HttpClient httpClient;

    public TechDetectionService() {
        this.httpClient = HttpClient.newBuilder()
                .sslContext(createInsecureSslContext())
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    private SSLContext createInsecureSslContext() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create insecure SSL context", e);
        }
    }

    public DomainResultDTO analyzeDomain(String domain, List<TechRuleDTO> rules) {
        String url = domain.startsWith("http") ? domain : "https://" + domain;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Document doc = Jsoup.parse(response.body());

            List<TechnologyDetectionDTO> detectedTechs = new ArrayList<>();

            for (TechRuleDTO rule : rules) {
                List<DetectionEvidenceDTO> evidences = new ArrayList<>();

                if (rule.getHeaders() != null) {
                    HttpHeaders headers = response.headers();
                    rule.getHeaders().forEach((headerKey, regex) -> {
                        Optional<String> headerValue = headers.firstValue(headerKey);
                        if (headerValue.isPresent() && Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(headerValue.get()).find()) {
                            evidences.add(DetectionEvidenceDTO.builder()
                                    .location("HTTP Header")
                                    .key(headerKey)
                                    .matchedSnippet(headerValue.get())
                                    .build());
                        }
                    });
                }

                if (rule.getMetaGenerator() != null) {
                    Elements metaTags = doc.select("meta[name=generator], meta[name=Generator]");
                    for (Element meta : metaTags) {
                        String content = meta.attr("content");
                        if (Pattern.compile(rule.getMetaGenerator(), Pattern.CASE_INSENSITIVE).matcher(content).find()) {
                            evidences.add(DetectionEvidenceDTO.builder()
                                    .location("Meta Generator Tag")
                                    .key("generator")
                                    .matchedSnippet(content)
                                    .build());
                        }
                    }
                }

                if (rule.getScriptSrc() != null) {
                    Elements scripts = doc.select("script[src]");
                    Pattern pattern = Pattern.compile(rule.getScriptSrc(), Pattern.CASE_INSENSITIVE);
                    for (Element script : scripts) {
                        String src = script.attr("src");
                        if (pattern.matcher(src).find()) {
                            evidences.add(DetectionEvidenceDTO.builder()
                                    .location("Script Tag Src")
                                    .key("src")
                                    .matchedSnippet(src)
                                    .build());
                        }
                    }
                }

                if (rule.getHtmlBody() != null) {
                    Pattern pattern = Pattern.compile(rule.getHtmlBody(), Pattern.CASE_INSENSITIVE);
                    if (pattern.matcher(response.body()).find()) {
                        evidences.add(DetectionEvidenceDTO.builder()
                                .location("HTML Body / Inline Script")
                                .key("html")
                                .matchedSnippet("Match found for pattern: " + rule.getHtmlBody())
                                .build());
                    }
                }

                if (!evidences.isEmpty()) {
                    detectedTechs.add(TechnologyDetectionDTO.builder()
                            .name(rule.getName())
                            .category(rule.getCategory())
                            .detectionEvidence(evidences)
                            .build());
                }
            }

            return DomainResultDTO.builder()
                    .domain(domain)
                    .status("SUCCESS")
                    .detectedTechnologiesCount(detectedTechs.size())
                    .technologies(detectedTechs)
                    .build();

        } catch (Exception e) {
            return DomainResultDTO.builder()
                    .domain(domain)
                    .status("FAILED")
                    .errorMessage(e.getMessage())
                    .detectedTechnologiesCount(0)
                    .technologies(Collections.emptyList())
                    .build();
        }
    }
}