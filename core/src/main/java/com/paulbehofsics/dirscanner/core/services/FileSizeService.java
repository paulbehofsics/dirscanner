package com.paulbehofsics.dirscanner.core.services;

import com.paulbehofsics.dirscanner.core.dtos.GetFileSizeDto;

import java.util.List;

public interface FileSizeService {
	List<GetFileSizeDto> getFileSizes();

	List<GetFileSizeDto> getFileSizes(String filetype);
}
