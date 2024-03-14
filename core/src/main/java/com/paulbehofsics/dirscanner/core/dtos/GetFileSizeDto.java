package com.paulbehofsics.dirscanner.core.dtos;

import java.time.LocalDateTime;

public record GetFileSizeDto(String path, String name, Long size, LocalDateTime modificationDate,
							 LocalDateTime scanDate) {}
