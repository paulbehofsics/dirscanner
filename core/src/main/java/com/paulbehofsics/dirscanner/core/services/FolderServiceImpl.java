package com.paulbehofsics.dirscanner.core.services;

import com.paulbehofsics.dirscanner.core.dtos.GetFolderDto;
import com.paulbehofsics.dirscanner.core.mapping.FolderMapping;
import com.paulbehofsics.dirscanner.core.models.FileSystemNode;
import com.paulbehofsics.dirscanner.core.repositories.FileSystemNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {
	private final FileSystemNodeRepository repository;

	@Autowired
	public FolderServiceImpl(FileSystemNodeRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<GetFolderDto> getFolders() {
		List<FileSystemNode> nodes = repository.findAll();

		return nodes.stream()
				.filter(FileSystemNode::isDirectory)
				.map(FolderMapping::fileSystemNodeToGetFolderDto)
				.sorted(Comparator.comparing(GetFolderDto::name))
				.toList();
	}
}
