package com.paulbehofsics.dirscanner.core.repositories;

import com.paulbehofsics.dirscanner.core.models.FileSystemNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileSystemNodeRepository extends JpaRepository<FileSystemNode, Long> {}
