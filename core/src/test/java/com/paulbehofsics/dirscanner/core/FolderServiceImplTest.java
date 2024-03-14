package com.paulbehofsics.dirscanner.core;

import com.paulbehofsics.dirscanner.core.dtos.GetFolderDto;
import com.paulbehofsics.dirscanner.core.models.FileSystemNode;
import com.paulbehofsics.dirscanner.core.repositories.FileSystemNodeRepository;
import com.paulbehofsics.dirscanner.core.services.FolderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FolderServiceImplTest {
	@Mock
	FileSystemNodeRepository repository;

	@InjectMocks
	FolderServiceImpl folderService;

	@Test
	public void givenSingleDirectoryNode_whenGetFolders_thenReturnThatNode() {
		// GIVEN
		final String NODE_PATH = "/root/dir";
		final String NODE_NAME = "dir";
		final LocalDateTime DATETIME_NOW = LocalDateTime.now();
		FileSystemNode directoryNode = new FileSystemNode(null, new LinkedList<>(),
				NODE_PATH, NODE_NAME, null, 4000L, DATETIME_NOW, DATETIME_NOW,
				false, true, Path.of(NODE_PATH));
		given(repository.findAll()).willReturn(List.of(directoryNode));

		// WHEN
		List<GetFolderDto> result = folderService.getFolders();

		// THEN
		assertEquals(1, result.size());

		final GetFolderDto EXPECTED_DTO = new GetFolderDto(NODE_PATH, NODE_NAME, DATETIME_NOW, DATETIME_NOW);
		final GetFolderDto resultDto = result.getFirst();
		assertEquals(EXPECTED_DTO, resultDto);
	}

	@Test
	public void givenNoNodes_whenGetFolders_thenReturnEmptyList() {
		// GIVEN
		given(repository.findAll()).willReturn(List.of());

		// WHEN
		List<GetFolderDto> result = folderService.getFolders();

		// THEN
		assertTrue(result.isEmpty());
	}

	@Test
	public void givenOnlyFileNode_whenGetFolders_thenReturnEmptyList() {
		// GIVEN
		final String NODE_PATH = "/root/file.txt";
		final String NODE_NAME = "file.txt";
		final LocalDateTime DATETIME_NOW = LocalDateTime.now();
		FileSystemNode fileNode = new FileSystemNode(null, new LinkedList<>(),
				NODE_PATH, NODE_NAME, null, 200L, DATETIME_NOW, DATETIME_NOW,
				true, false, Path.of(NODE_PATH));
		given(repository.findAll()).willReturn(List.of(fileNode));

		// WHEN
		List<GetFolderDto> result = folderService.getFolders();

		// THEN
		assertTrue(result.isEmpty());
	}

	@Test
	public void givenDirectoryAndFileNode_whenGetFolders_thenReturnOnlyDirectoryNode() {
		// GIVEN
		final String DIR_NODE_PATH = "/root/dir";
		final String DIR_NODE_NAME = "dir";
		final LocalDateTime DATETIME_NOW = LocalDateTime.now();
		FileSystemNode directoryNode = new FileSystemNode(null, new LinkedList<>(),
				DIR_NODE_PATH, DIR_NODE_NAME, null, 4000L, DATETIME_NOW, DATETIME_NOW,
				false, true, Path.of(DIR_NODE_PATH));

		final String FILE_NODE_PATH = "/root/dir/file.txt";
		final String FILE_NODE_NAME = "file.txt";
		FileSystemNode fileNode = new FileSystemNode(null, new LinkedList<>(),
				FILE_NODE_PATH, FILE_NODE_NAME, null, 10000L, DATETIME_NOW, DATETIME_NOW,
				true, false, Path.of(FILE_NODE_PATH));

		given(repository.findAll()).willReturn(List.of(directoryNode, fileNode));

		// WHEN
		List<GetFolderDto> result = folderService.getFolders();

		// THEN
		assertEquals(1, result.size());

		final GetFolderDto EXPECTED_DTO = new GetFolderDto(DIR_NODE_PATH, DIR_NODE_NAME, DATETIME_NOW, DATETIME_NOW);
		final GetFolderDto resultDto = result.getFirst();
		assertEquals(EXPECTED_DTO, resultDto);
	}
}
