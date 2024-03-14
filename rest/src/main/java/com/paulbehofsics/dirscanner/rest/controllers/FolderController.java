package com.paulbehofsics.dirscanner.rest.controllers;

import com.paulbehofsics.dirscanner.core.dtos.GetFolderDto;
import com.paulbehofsics.dirscanner.core.services.DirectoryScannerService;
import com.paulbehofsics.dirscanner.core.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/folders")
public class FolderController {
	private final FolderService folderService;
	private final DirectoryScannerService directoryScannerService;

	@Autowired
	public FolderController(FolderService folderService, DirectoryScannerService directoryScannerService) {
		this.folderService = folderService;
		this.directoryScannerService = directoryScannerService;
	}

	@GetMapping
	public List<GetFolderDto> getFolders() {
		return folderService.getFolders();
	}

	@DeleteMapping
	public void deleteFolders() {
		directoryScannerService.clearScanResults();
	}
}
