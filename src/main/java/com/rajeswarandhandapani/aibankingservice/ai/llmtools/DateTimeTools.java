package com.rajeswarandhandapani.aibankingservice.ai.llmtools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;

import java.time.ZonedDateTime;

public class DateTimeTools {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeTools.class);

    @Tool(
            name = "getCurrentDateTime",
            description = "Fetches the current date and time in ISO 8601 format."
    )
    public static String getCurrentDateTime() {
        logger.info("Fetching current date and time");
        ZonedDateTime now = ZonedDateTime.now();
        return now.toString(); // Returns date and time in ISO 8601 format
    }
}
