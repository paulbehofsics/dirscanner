package com.paulbehofsics.dirscanner.core.services;

import java.io.IOException;
import java.net.URI;

public interface DirectoryScannerService {
	void scanAndPersist(URI rootUri) throws IOException;

	void clearScanResults();
}
