package com.paulbehofsics.dirscanner.core.services;

import com.paulbehofsics.dirscanner.core.dtos.GetFolderDto;

import java.util.List;

public interface FolderService {
	List<GetFolderDto> getFolders();
}
