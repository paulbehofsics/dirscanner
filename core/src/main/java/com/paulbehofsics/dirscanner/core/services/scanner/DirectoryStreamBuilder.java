package com.paulbehofsics.dirscanner.core.services.scanner;

import com.paulbehofsics.dirscanner.core.models.FileSystemNode;

import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

public interface DirectoryStreamBuilder {
	DirectoryStream<Path> buildStream(URI directoryUri) throws IOException;
}
