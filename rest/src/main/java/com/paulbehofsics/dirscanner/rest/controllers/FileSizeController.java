package com.paulbehofsics.dirscanner.rest.controllers;

import com.paulbehofsics.dirscanner.core.dtos.GetFileSizeDto;
import com.paulbehofsics.dirscanner.core.services.FileSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/filesizes")
public class FileSizeController {
	private final FileSizeService fileSizeService;

	@Autowired
	public FileSizeController(FileSizeService fileSizeService) {
		this.fileSizeService = fileSizeService;
	}

	@GetMapping
	public List<GetFileSizeDto> getFileSizes(@RequestParam(required = false) String filetype) {
		if (filetype == null) {
			return fileSizeService.getFileSizes();
		} else {
			return fileSizeService.getFileSizes(filetype);
		}
	}
}
