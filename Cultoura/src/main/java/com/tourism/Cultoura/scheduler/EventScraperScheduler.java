package com.tourism.Cultoura.scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class EventScraperScheduler {

    private static final Logger logger = LoggerFactory.getLogger(EventScraperScheduler.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${python.script.path}")
    private String pythonScriptPath;
    
    @Value("${python.executable:python}")
    private String pythonExecutable;
    
    @Value("${scraper.cities:delhi,mumbai,bangalore}")
    private String cities;
    
    @Value("${scraper.pages:5}")
    private String pages;

    @Scheduled(fixedRate = 21600000)
    public void runEventScraper() {
        logger.info("Starting scheduled event scraper at {}", formatter.format(LocalDateTime.now()));
        
        String[] cityList = cities.split(",");
        for (String city : cityList) {
            try {
                logger.info("Scraping events for city: {}", city);
                executeScraperScript(city.trim(), pages);
            } catch (Exception e) {
                logger.error("Error scraping events for city: " + city, e);
            }
        }
        
        logger.info("Completed scheduled event scraping at {}", formatter.format(LocalDateTime.now()));
    }
    
    /**
     * Execute the Python scraper script with parameters
     */
    private void executeScraperScript(String city, String pages) throws IOException, InterruptedException {
        // Create a process builder to execute the Python script
        ProcessBuilder processBuilder = new ProcessBuilder(
                pythonExecutable,
                pythonScriptPath,
                "--city", city,
                "--pages", pages,
                "--auto-yes"  // Assume yes for prompts
        );
        Map<String, String> env = processBuilder.environment();
        env.put("PYTHONIOENCODING", "utf-8");
        // Redirect error stream to output stream
        processBuilder.redirectErrorStream(true);
        
        // Start the process
        Process process = processBuilder.start();
        
        // Capture and log the output
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("[Python Script Output] {}", line);
            }
        }
        
        // Wait for the process to complete
        int exitCode = process.waitFor();
        logger.info("Python script execution completed with exit code: {}", exitCode);
        
        if (exitCode != 0) {
            logger.error("Python script failed with exit code: {}", exitCode);
        }
    }
}