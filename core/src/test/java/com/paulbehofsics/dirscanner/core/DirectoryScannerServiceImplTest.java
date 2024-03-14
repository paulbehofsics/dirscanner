package com.paulbehofsics.dirscanner.core;

import com.paulbehofsics.dirscanner.core.models.FileSystemNode;
import com.paulbehofsics.dirscanner.core.repositories.FileSystemNodeRepository;
import com.paulbehofsics.dirscanner.core.services.scanner.DirectoryScannerServiceImpl;
import com.paulbehofsics.dirscanner.core.services.scanner.DirectoryStreamBuilder;
import com.paulbehofsics.dirscanner.core.services.scanner.FileSystemNodeBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DirectoryScannerServiceImplTest {
	private final String URI_SCHEME_PREFIX = "file://";
	private final String ROOT_DIR_NAME = "root";
	private final String SUB_DIR_NAME = "dir";
	private final String FILE_1_NAME = "file1.txt";
	private final String FILE_2_NAME = "file2.pdf";
	private final String ROOT_DIR_EXPR = "/root";
	private final String SUB_DIR_EXPR = "/root/dir";
	private final String FILE_1_EXPR = "/root/file1.txt";
	private final String FILE_2_EXPR = "/root/dir/file2.pdf";
	private final URI ROOT_DIR_URI = URI.create(URI_SCHEME_PREFIX + ROOT_DIR_EXPR);
	private final URI ROOT_DIR_URI_TRAILING_SLASH = URI.create(URI_SCHEME_PREFIX + ROOT_DIR_EXPR + "/");
	private final URI SUB_DIR_URI = URI.create(URI_SCHEME_PREFIX + SUB_DIR_EXPR);
	private final URI SUB_DIR_URI_TRAILING_SLASH = URI.create(URI_SCHEME_PREFIX + SUB_DIR_EXPR + "/");
	private final URI FILE_1_URI = URI.create(URI_SCHEME_PREFIX + FILE_1_EXPR);
	private final DirectoryStream<Path> ROOT_DIR_STREAM = new DirectoryStream<>() {
		@Override
		public Iterator<Path> iterator() {
			return List.of(Path.of(FILE_1_URI), Path.of(SUB_DIR_URI)).iterator();
		}

		@Override
		public void close() {}
	};
	private final URI FILE_2_URI = URI.create(URI_SCHEME_PREFIX + FILE_2_EXPR);
	private final DirectoryStream<Path> SUB_DIR_STREAM = new DirectoryStream<>() {
		@Override
		public Iterator<Path> iterator() {
			return List.of(Path.of(FILE_2_URI)).iterator();
		}

		@Override
		public void close() {}
	};

	@Mock
	FileSystemNodeRepository repository;
	@Mock
	DirectoryStreamBuilder directoryStreamBuilder;
	@Mock
	FileSystemNodeBuilder fileSystemNodeBuilder;

	@InjectMocks
	DirectoryScannerServiceImpl directoryScannerService;

	@Captor
	ArgumentCaptor<FileSystemNode> nodeCaptor;
	@Captor
	ArgumentCaptor<Collection<FileSystemNode>> nodeCollectionCaptor;

	@Test
	public void givenExampleDirectory_whenScanAndPersist_thenDirectoryScannedAndNodesPersisted() throws Exception {
		// GIVEN
		given(directoryStreamBuilder.buildStream(or(eq(ROOT_DIR_URI), eq(ROOT_DIR_URI_TRAILING_SLASH))))
				.willReturn(ROOT_DIR_STREAM);
		given(directoryStreamBuilder.buildStream(or(eq(SUB_DIR_URI), eq(SUB_DIR_URI_TRAILING_SLASH))))
				.willReturn(SUB_DIR_STREAM);

		given(fileSystemNodeBuilder.buildFromUri(SUB_DIR_URI))
				.willReturn(new FileSystemNode(null, new LinkedList<>(),
						ROOT_DIR_URI.getPath(), SUB_DIR_NAME, null, 4000L,
						LocalDateTime.now(), LocalDateTime.now(), false, true,
						Path.of(SUB_DIR_EXPR)));
		given(fileSystemNodeBuilder.buildFromUri(FILE_1_URI))
				.willReturn(new FileSystemNode(null, new LinkedList<>(),
						ROOT_DIR_URI.getPath(), FILE_1_NAME, "txt", 10000L,
						LocalDateTime.now(), LocalDateTime.now(), true, false,
						Path.of(FILE_1_EXPR)));
		given(fileSystemNodeBuilder.buildFromUri(FILE_2_URI))
				.willReturn(new FileSystemNode(null, new LinkedList<>(),
						ROOT_DIR_URI.getPath(), FILE_2_NAME, "pdf", 20000L,
						LocalDateTime.now(), LocalDateTime.now(), true, false,
						Path.of(FILE_2_EXPR)));
		given(fileSystemNodeBuilder.buildFromUri(ROOT_DIR_URI))
				.willReturn(new FileSystemNode(null, new LinkedList<>(),
						ROOT_DIR_URI.getPath(), ROOT_DIR_NAME, null, 4000L,
						LocalDateTime.now(), LocalDateTime.now(), false, true,
						Path.of(ROOT_DIR_EXPR)));

		// WHEN
		directoryScannerService.scanAndPersist(ROOT_DIR_URI);

		// THEN
		verify(repository, atLeast(0)).save(nodeCaptor.capture());
		verify(repository, atLeast(0)).saveAll(nodeCollectionCaptor.capture());
		assertTrue(!nodeCaptor.getAllValues().isEmpty() || !nodeCollectionCaptor.getAllValues().isEmpty());

		List<FileSystemNode> capturedNodes = new LinkedList<>(nodeCaptor.getAllValues());
		nodeCollectionCaptor.getAllValues().forEach(capturedNodes::addAll);
		assertEquals(4, capturedNodes.size());

		Predicate<FileSystemNode> isRootDirNode = node -> ROOT_DIR_NAME.equals(node.getName())
				&& node.getParent() == null
				&& node.getChildNodes().size() == 2;
		assertFalse(capturedNodes.stream()
				.filter(isRootDirNode)
				.toList().isEmpty());

		Predicate<FileSystemNode> isSubDirNode = node -> SUB_DIR_NAME.equals(node.getName())
				&& ROOT_DIR_NAME.equals(node.getParent().getName())
				&& node.getChildNodes().size() == 1;
		assertFalse(capturedNodes.stream()
				.filter(isSubDirNode)
				.toList().isEmpty());

		Predicate<FileSystemNode> isFile1Node = node -> FILE_1_NAME.equals(node.getName())
				&& ROOT_DIR_NAME.equals(node.getParent().getName())
				&& node.getChildNodes().isEmpty();
		assertFalse(capturedNodes.stream()
				.filter(isFile1Node)
				.toList().isEmpty());

		Predicate<FileSystemNode> isFile2Node = node -> FILE_2_NAME.equals(node.getName())
				&& SUB_DIR_NAME.equals(node.getParent().getName())
				&& node.getChildNodes().isEmpty();
		assertFalse(capturedNodes.stream()
				.filter(isFile2Node)
				.toList().isEmpty());
	}
}
