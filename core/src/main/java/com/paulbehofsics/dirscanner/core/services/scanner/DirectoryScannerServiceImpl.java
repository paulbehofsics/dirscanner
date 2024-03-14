package com.paulbehofsics.dirscanner.core.services.scanner;

import com.paulbehofsics.dirscanner.core.models.FileSystemNode;
import com.paulbehofsics.dirscanner.core.repositories.FileSystemNodeRepository;
import com.paulbehofsics.dirscanner.core.services.DirectoryScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class DirectoryScannerServiceImpl implements DirectoryScannerService {
	private final FileSystemNodeRepository repository;
	private final DirectoryStreamBuilder directoryStreamBuilder;
	private final FileSystemNodeBuilder fileSystemNodeBuilder;

	@Autowired
	public DirectoryScannerServiceImpl(FileSystemNodeRepository repository,
									   DirectoryStreamBuilder directoryStreamBuilder,
									   FileSystemNodeBuilder fileSystemNodeBuilder) {
		this.repository = repository;
		this.directoryStreamBuilder = directoryStreamBuilder;
		this.fileSystemNodeBuilder = fileSystemNodeBuilder;
	}

	@Override
	@Transactional
	public void scanAndPersist(URI rootUri) throws IOException {
		Collection<FileSystemNode> nodes = getNodesFromUri(rootUri);
		repository.saveAll(nodes);
	}

	@Override
	@Transactional
	public void clearScanResults() {
		repository.deleteAll();
	}

	/**
	 * Gets all the file system nodes that are descendants of the given root path, including the node corresponding to
	 * the path itself.
	 *
	 * @param rootUri The URI to root the search at.
	 * @return All file system nodes corresponding to the root and its descendants.
	 * @throws IOException if building a node from a URI fails.
	 */
	private List<FileSystemNode> getNodesFromUri(URI rootUri) throws IOException {
		List<FileSystemNode> collectedNodes = new LinkedList<>();
		Queue<FileSystemNode> remainingDirectories = new LinkedList<>();

		FileSystemNode rootNode = fileSystemNodeBuilder.buildFromUri(rootUri);
		collectedNodes.add(rootNode);
		remainingDirectories.add(rootNode);

		while (!remainingDirectories.isEmpty()) {
			FileSystemNode currentDirNode = remainingDirectories.poll();
			scanPath(currentDirNode, collectedNodes, remainingDirectories);
		}

		return collectedNodes;
	}

	/**
	 * Adds file system nodes corresponding to all children of the current node to the given
	 * collected nodes. Furthermore, adds all of these nodes, that represent directories,
	 * also to the remaining directories.
	 *
	 * @param currentNode          The current node the scan is started from.
	 * @param collectedNodes       Where to add children of the current node to.
	 * @param remainingDirectories Where to add the children representing directories.
	 * @throws IOException if building a directory stream fails or building a node from a URI fails.
	 */
	private void scanPath(FileSystemNode currentNode, List<FileSystemNode> collectedNodes,
						  Queue<FileSystemNode> remainingDirectories) throws IOException {
		URI currentNodeUri = currentNode.getComplexPath().toUri();

		try (DirectoryStream<Path> pathStream = directoryStreamBuilder.buildStream(currentNodeUri)) {
			for (Path childPath : pathStream) {
				try {
					FileSystemNode nextNode = fileSystemNodeBuilder.buildFromUri(childPath.toUri());

					currentNode.getChildNodes().add(nextNode);
					nextNode.setParent(currentNode);
					collectedNodes.add(nextNode);

					if (nextNode.isDirectory()) {
						remainingDirectories.add(nextNode);
					}
				} catch (IOException ignored) {
				}
			}
		}
	}
}
