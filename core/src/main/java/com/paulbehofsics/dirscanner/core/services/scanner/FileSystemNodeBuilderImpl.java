package com.paulbehofsics.dirscanner.core.services.scanner;

import com.paulbehofsics.dirscanner.core.models.FileSystemNode;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

@Component
public class FileSystemNodeBuilderImpl implements FileSystemNodeBuilder {
	/**
	 * Constructs a {@link FileSystemNode} based on the given path. The parent of the constructed node will be null
	 * and its child nodes will be an empty {@link List}. The filetype of the constructed node will be null,
	 * if the path corresponds to a directory.
	 *
	 * @param uri the URI to construct the node from.
	 * @return the constructed node.
	 * @throws IOException if reading file attributes for the given path fails.
	 */
	@Override
	public FileSystemNode buildFromUri(URI uri) throws IOException {
		Path path = Path.of(uri);
		File file = path.toFile();
		LocalDateTime lastModified = LocalDateTime.ofInstant(
				Files.getLastModifiedTime(path).toInstant(), ZoneId.systemDefault());
		String filetype = file.isDirectory() ? null : FilenameUtils.getExtension(file.getPath());
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

		return new FileSystemNode(
				null, new LinkedList<>(), file.getPath(), file.getName(), filetype, attr.size(),
				lastModified, LocalDateTime.now(), Files.isRegularFile(path), file.isDirectory(), path
		);
	}
}
