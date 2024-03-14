package com.paulbehofsics.dirscanner.core.configs;

import com.paulbehofsics.dirscanner.core.services.DirectoryScannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class FolderScanConfig {}
