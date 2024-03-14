package com.paulbehofsics.dirscanner.core.dtos;

import java.time.LocalDateTime;

public record GetFolderDto(String path, String name, LocalDateTime modificationDate, LocalDateTime scanDate) {}
