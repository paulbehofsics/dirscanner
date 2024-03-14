package com.paulbehofsics.dirscanner.core.services.scanner;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class DirectoryStreamBuilderImpl implements DirectoryStreamBuilder {
	@Override
	public DirectoryStream<Path> buildStream(URI directoryUri) throws IOException {
		return Files.newDirectoryStream(Path.of(directoryUri));
	}
}
