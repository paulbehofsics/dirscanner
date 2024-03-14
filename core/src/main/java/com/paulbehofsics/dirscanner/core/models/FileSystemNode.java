package com.paulbehofsics.dirscanner.core.models;

import jakarta.persistence.*;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "node")
public class FileSystemNode {
	@Id
	@SequenceGenerator(
			name = "node_sequence",
			sequenceName = "node_sequence"
	)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "node_sequence"
	)
	private Long id;
	@ManyToOne
	private FileSystemNode parent;
	@OneToMany(mappedBy = "parent")
	private List<FileSystemNode> childNodes;
	@Column(length = 1024)
	private String path;
	private String name;
	private String filetype;
	private Long size;
	private LocalDateTime modificationDate;
	private LocalDateTime scanDate;
	private boolean isRegularFile;
	private boolean isDirectory;
	@Transient
	private Path complexPath;

	public FileSystemNode() {}

	public FileSystemNode(Long id, FileSystemNode parent, List<FileSystemNode> childNodes, String path, String name,
						  String filetype, Long size, LocalDateTime modificationDate, LocalDateTime scanDate,
						  boolean isRegularFile, boolean isDirectory, Path complexPath) {
		this.id = id;
		this.parent = parent;
		this.childNodes = childNodes;
		this.path = path;
		this.name = name;
		this.filetype = filetype;
		this.size = size;
		this.modificationDate = modificationDate;
		this.scanDate = scanDate;
		this.isRegularFile = isRegularFile;
		this.isDirectory = isDirectory;
		this.complexPath = complexPath;
	}

	public FileSystemNode(FileSystemNode parent, List<FileSystemNode> childNodes, String path, String name,
						  String filetype, Long size, LocalDateTime modificationDate, LocalDateTime scanDate,
						  boolean isRegularFile, boolean isDirectory, Path complexPath) {
		this.parent = parent;
		this.childNodes = childNodes;
		this.path = path;
		this.name = name;
		this.filetype = filetype;
		this.size = size;
		this.modificationDate = modificationDate;
		this.scanDate = scanDate;
		this.isRegularFile = isRegularFile;
		this.isDirectory = isDirectory;
		this.complexPath = complexPath;
	}

	@Override
	public String toString() {
		return "FileSystemNode{" +
				"\n  id=" + id +
				",\n  parent='" + (parent == null ? "null" : parent.getName() == null ? "" : parent.getName()) + '\'' +
				",\n  path='" + path + '\'' +
				",\n  name='" + name + '\'' +
				",\n  filetype='" + filetype + '\'' +
				",\n  size=" + size +
				",\n  modificationDate=" + modificationDate +
				",\n  scanDate=" + scanDate +
				",\n  isRegularFile=" + isRegularFile +
				",\n  isDirectory=" + isDirectory +
				",\n  childNodes=["
				+ childNodes.stream()
				.map(FileSystemNode::getName)
				.map(name -> name == null ? "" : name)
				.map(str -> ('\'' + str + '\''))
				.reduce((node1, node2) -> node1 + ", " + node2)
				.orElse("")
				+ "]\n}";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FileSystemNode getParent() {
		return parent;
	}

	public void setParent(FileSystemNode parent) {
		this.parent = parent;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<FileSystemNode> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<FileSystemNode> childNodes) {
		this.childNodes = childNodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public LocalDateTime getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(LocalDateTime modificationDate) {
		this.modificationDate = modificationDate;
	}

	public LocalDateTime getScanDate() {
		return scanDate;
	}

	public void setScanDate(LocalDateTime scanDate) {
		this.scanDate = scanDate;
	}

	public boolean isRegularFile() {
		return isRegularFile;
	}

	public void setIsRegularFile(boolean isRegularFile) {
		this.isRegularFile = isRegularFile;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setIsDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public Path getComplexPath() {
		return complexPath;
	}

	public void setComplexPath(Path complexPath) {
		this.complexPath = complexPath;
	}
}
