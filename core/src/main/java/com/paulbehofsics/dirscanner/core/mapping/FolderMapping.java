package com.paulbehofsics.dirscanner.core.mapping;

import com.paulbehofsics.dirscanner.core.dtos.GetFileSizeDto;
import com.paulbehofsics.dirscanner.core.dtos.GetFolderDto;
import com.paulbehofsics.dirscanner.core.models.FileSystemNode;

public class FolderMapping {
	public static GetFolderDto fileSystemNodeToGetFolderDto(FileSystemNode node) {
		return new GetFolderDto(node.getPath(), node.getName(), node.getModificationDate(), node.getScanDate());
	}

	public static GetFileSizeDto fileSystemNodeToGetFileSizeDto(FileSystemNode node) {
		return new GetFileSizeDto(node.getPath(), node.getName(), node.getSize(), node.getModificationDate(),
				node.getScanDate());
	}
}
