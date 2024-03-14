package com.paulbehofsics.dirscanner.core.tasks;

import com.paulbehofsics.dirscanner.core.services.DirectoryScannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class FolderScanTask {
	private final Logger LOGGER = LoggerFactory.getLogger(FolderScanTask.class);
	private final DirectoryScannerService directoryScannerService;

	@Value("${dirscanner.root}")
	private String rootUriExpression;
	@Value("${dirscanner.delay.minutes}")
	private String scanDelayMinutes;

	@Autowired
	public FolderScanTask(DirectoryScannerService directoryScannerService) {
		this.directoryScannerService = directoryScannerService;
	}

	@Transactional
	@Scheduled(fixedDelayString = "${dirscanner.delay.minutes:5}", timeUnit = TimeUnit.MINUTES)
	public void directoryScanTask() {
		LOGGER.info("Scanning with delay of " + scanDelayMinutes + " minute(s)");
		try {
			final String URI_SCHEME_EXPRESSION = "file:";
			final URI rootUri = URI.create(URI_SCHEME_EXPRESSION + rootUriExpression);

			directoryScannerService.clearScanResults();
			directoryScannerService.scanAndPersist(rootUri);
			LOGGER.info("Scan successful");
		} catch (IOException e) {
			LOGGER.error("Directory scanning failed: " + e);
		}
	}
}
