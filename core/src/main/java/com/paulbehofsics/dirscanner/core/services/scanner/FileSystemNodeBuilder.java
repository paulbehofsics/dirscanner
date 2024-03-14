package com.paulbehofsics.dirscanner.core.services.scanner;

import com.paulbehofsics.dirscanner.core.models.FileSystemNode;

import java.io.IOException;
import java.net.URI;

public interface FileSystemNodeBuilder {
	FileSystemNode buildFromUri(URI uri) throws IOException;
}
