package com.paulbehofsics.dirscanner.core;

import com.paulbehofsics.dirscanner.core.dtos.GetFileSizeDto;
import com.paulbehofsics.dirscanner.core.models.FileSystemNode;
import com.paulbehofsics.dirscanner.core.repositories.FileSystemNodeRepository;
import com.paulbehofsics.dirscanner.core.services.FileSizeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FileSizeServiceImplTest {
	@Mock
	FileSystemNodeRepository repository;

	@InjectMocks
	FileSizeServiceImpl fileSizeService;

	@Test
	public void givenNoNodes_whenGetFileSizes_thenReturnEmptyList() {
		// GIVEN
		given(repository.findAll()).willReturn(List.of());

		// WHEN
		List<GetFileSizeDto> result = fileSizeService.getFileSizes();

		// THEN
		assertTrue(result.isEmpty());
	}

	@Test
	public void givenDirectoryWithFile_whenGetFileSizes_thenReturnCorrectDto() {
		// GIVEN
		final String DIR_NODE_PATH = "/root/dir";
		final String DIR_NODE_NAME = "dir";
		final long DIR_NODE_SIZE = 4000L;
		final LocalDateTime DATETIME_NOW = LocalDateTime.now();
		FileSystemNode directoryNode = new FileSystemNode(null, new LinkedList<>(),
				DIR_NODE_PATH, DIR_NODE_NAME, null, DIR_NODE_SIZE, DATETIME_NOW, DATETIME_NOW,
				false, true, Path.of(DIR_NODE_PATH));

		final String FILE_NODE_PATH = "/root/dir/file.txt";
		final String FILE_NODE_NAME = "file.txt";
		final long FILE_NODE_SIZE = 10000L;
		FileSystemNode fileNode = new FileSystemNode(directoryNode, new LinkedList<>(),
				FILE_NODE_PATH, FILE_NODE_NAME, "txt", FILE_NODE_SIZE, DATETIME_NOW, DATETIME_NOW,
				true, false, Path.of(FILE_NODE_PATH));

		directoryNode.getChildNodes().add(fileNode);
		given(repository.findAll()).willReturn(List.of(directoryNode, fileNode));

		// WHEN
		List<GetFileSizeDto> result = fileSizeService.getFileSizes();

		// THEN
		assertEquals(1, result.size());
		assertThat(result, contains(new GetFileSizeDto(DIR_NODE_PATH, DIR_NODE_NAME,
				DIR_NODE_SIZE + FILE_NODE_SIZE, DATETIME_NOW, DATETIME_NOW)));
	}

	@Test
	public void givenDirectoryWithSubdirectoryContainingFile_whenGetFileSizes_thenReturnCorrectDtos() {
		// GIVEN
		final String ROOT_DIR_NODE_PATH = "/root";
		final String ROOT_DIR_NODE_NAME = "root";
		final long ROOT_DIR_NODE_SIZE = 4000L;
		final LocalDateTime DATETIME_NOW = LocalDateTime.now();
		FileSystemNode rootDirectoryNode = new FileSystemNode(null, new LinkedList<>(),
				ROOT_DIR_NODE_PATH, ROOT_DIR_NODE_NAME, null, ROOT_DIR_NODE_SIZE, DATETIME_NOW, DATETIME_NOW,
				false, true, Path.of(ROOT_DIR_NODE_PATH));

		final String SUB_DIR_NODE_PATH = "/root/dir";
		final String SUB_DIR_NODE_NAME = "dir";
		final long SUB_DIR_NODE_SIZE = 4001L;
		FileSystemNode subDirectoryNode = new FileSystemNode(rootDirectoryNode, new LinkedList<>(),
				SUB_DIR_NODE_PATH, SUB_DIR_NODE_NAME, null, SUB_DIR_NODE_SIZE, DATETIME_NOW, DATETIME_NOW,
				false, true, Path.of(SUB_DIR_NODE_PATH));

		final String FILE_NODE_PATH = "/root/dir/file.txt";
		final String FILE_NODE_NAME = "file.txt";
		final long FILE_NODE_SIZE = 10000L;
		FileSystemNode fileNode = new FileSystemNode(subDirectoryNode, new LinkedList<>(),
				FILE_NODE_PATH, FILE_NODE_NAME, "txt", FILE_NODE_SIZE, DATETIME_NOW, DATETIME_NOW,
				true, false, Path.of(FILE_NODE_PATH));

		rootDirectoryNode.getChildNodes().add(subDirectoryNode);
		subDirectoryNode.getChildNodes().add(fileNode);
		given(repository.findAll()).willReturn(List.of(rootDirectoryNode, subDirectoryNode, fileNode));

		// WHEN
		List<GetFileSizeDto> result = fileSizeService.getFileSizes();

		// THEN
		assertEquals(2, result.size());
		assertThat(result, contains(
				new GetFileSizeDto(ROOT_DIR_NODE_PATH, ROOT_DIR_NODE_NAME,
						ROOT_DIR_NODE_SIZE + SUB_DIR_NODE_SIZE + FILE_NODE_SIZE, DATETIME_NOW, DATETIME_NOW),
				new GetFileSizeDto(SUB_DIR_NODE_PATH, SUB_DIR_NODE_NAME,
						SUB_DIR_NODE_SIZE + FILE_NODE_SIZE, DATETIME_NOW, DATETIME_NOW)));
	}

	@Test
	public void givenDirectoryWithSubdirectoryContainingFile_whenGetFileSizesWithFiletype_thenReturnCorrectDtos() {
		// GIVEN
		final String ROOT_DIR_NODE_PATH = "/root";
		final String ROOT_DIR_NODE_NAME = "root";
		final long ROOT_DIR_NODE_SIZE = 4000L;
		final LocalDateTime DATETIME_NOW = LocalDateTime.now();
		FileSystemNode rootDirectoryNode = new FileSystemNode(null, new LinkedList<>(),
				ROOT_DIR_NODE_PATH, ROOT_DIR_NODE_NAME, null, ROOT_DIR_NODE_SIZE, DATETIME_NOW, DATETIME_NOW,
				false, true, Path.of(ROOT_DIR_NODE_PATH));

		final String SUB_DIR_NODE_PATH = "/root/dir";
		final String SUB_DIR_NODE_NAME = "dir";
		final long SUB_DIR_NODE_SIZE = 4001L;
		FileSystemNode subDirectoryNode = new FileSystemNode(rootDirectoryNode, new LinkedList<>(),
				SUB_DIR_NODE_PATH, SUB_DIR_NODE_NAME, null, SUB_DIR_NODE_SIZE, DATETIME_NOW, DATETIME_NOW,
				false, true, Path.of(SUB_DIR_NODE_PATH));

		final String FILE_NODE_PATH = "/root/dir/file.txt";
		final String FILE_NODE_NAME = "file.txt";
		final long FILE_NODE_SIZE = 10000L;
		FileSystemNode fileNode = new FileSystemNode(subDirectoryNode, new LinkedList<>(),
				FILE_NODE_PATH, FILE_NODE_NAME, "txts", FILE_NODE_SIZE, DATETIME_NOW, DATETIME_NOW,
				true, false, Path.of(FILE_NODE_PATH));

		rootDirectoryNode.getChildNodes().add(subDirectoryNode);
		subDirectoryNode.getChildNodes().add(fileNode);
		given(repository.findAll()).willReturn(List.of(rootDirectoryNode, subDirectoryNode, fileNode));

		// WHEN
		List<GetFileSizeDto> result = fileSizeService.getFileSizes("png");

		// THEN
		assertEquals(2, result.size());
		assertThat(result, contains(
				new GetFileSizeDto(ROOT_DIR_NODE_PATH, ROOT_DIR_NODE_NAME,
						ROOT_DIR_NODE_SIZE + SUB_DIR_NODE_SIZE, DATETIME_NOW, DATETIME_NOW),
				new GetFileSizeDto(SUB_DIR_NODE_PATH, SUB_DIR_NODE_NAME,
						SUB_DIR_NODE_SIZE, DATETIME_NOW, DATETIME_NOW)));
	}

	@Test
	public void givenDirectoryWithFilesOfDifferentType_whenGetFileSizesWithFiletype_thenReturnCorrectDto() {
		// GIVEN
		final String DIR_NODE_PATH = "/root";
		final String DIR_NODE_NAME = "root";
		final long DIR_NODE_SIZE = 4000L;
		final LocalDateTime DATETIME_NOW = LocalDateTime.now();
		FileSystemNode directoryNode = new FileSystemNode(null, new LinkedList<>(),
				DIR_NODE_PATH, DIR_NODE_NAME, null, DIR_NODE_SIZE, DATETIME_NOW, DATETIME_NOW,
				false, true, Path.of(DIR_NODE_PATH));

		final String FILE_NODE_1_PATH = "/root/dir/file.txt";
		final String FILE_NODE_1_NAME = "file.txt";
		final long FILE_NODE_1_SIZE = 10000L;
		FileSystemNode fileNode1 = new FileSystemNode(directoryNode, new LinkedList<>(),
				FILE_NODE_1_PATH, FILE_NODE_1_NAME, "txt", FILE_NODE_1_SIZE, DATETIME_NOW, DATETIME_NOW,
				true, false, Path.of(FILE_NODE_1_PATH));

		final String FILE_NODE_2_PATH = "/root/dir/file.png";
		final String FILE_NODE_2_NAME = "file.png";
		final long FILE_NODE_2_SIZE = 20000L;
		FileSystemNode fileNode2 = new FileSystemNode(directoryNode, new LinkedList<>(),
				FILE_NODE_2_PATH, FILE_NODE_2_NAME, "png", FILE_NODE_2_SIZE, DATETIME_NOW, DATETIME_NOW,
				true, false, Path.of(FILE_NODE_2_PATH));

		directoryNode.getChildNodes().add(fileNode1);
		directoryNode.getChildNodes().add(fileNode2);
		given(repository.findAll()).willReturn(List.of(directoryNode, fileNode1, fileNode2));

		// WHEN
		List<GetFileSizeDto> result = fileSizeService.getFileSizes("png");

		// THEN
		assertEquals(1, result.size());
		assertThat(result, contains(
				new GetFileSizeDto(DIR_NODE_PATH, DIR_NODE_NAME,
						DIR_NODE_SIZE + FILE_NODE_2_SIZE, DATETIME_NOW, DATETIME_NOW)));
	}
}
