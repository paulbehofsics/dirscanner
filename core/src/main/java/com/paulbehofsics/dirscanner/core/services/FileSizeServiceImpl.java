package com.paulbehofsics.dirscanner.core.services;

import com.paulbehofsics.dirscanner.core.dtos.GetFileSizeDto;
import com.paulbehofsics.dirscanner.core.mapping.FolderMapping;
import com.paulbehofsics.dirscanner.core.models.FileSystemNode;
import com.paulbehofsics.dirscanner.core.repositories.FileSystemNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

@Service
public class FileSizeServiceImpl implements FileSizeService {
	private final FileSystemNodeRepository repository;

	@Autowired
	public FileSizeServiceImpl(FileSystemNodeRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<GetFileSizeDto> getFileSizes() {
		List<FileSystemNode> nodes = repository.findAll();
		aggregateFileSizes(nodes);
		return extractFileSizeDtos(nodes);
	}

	@Override
	public List<GetFileSizeDto> getFileSizes(String filetype) {
		List<FileSystemNode> nodes = repository.findAll();
		aggregateFileSizes(nodes, filetype);
		return extractFileSizeDtos(nodes);
	}

	private List<GetFileSizeDto> extractFileSizeDtos(List<FileSystemNode> nodes) {
		return nodes.stream()
				.filter(FileSystemNode::isDirectory)
				.map(FolderMapping::fileSystemNodeToGetFileSizeDto)
				.sorted(Comparator.comparing(GetFileSizeDto::size).reversed())
				.toList();
	}

	private void aggregateFileSizes(List<FileSystemNode> nodes) {
		aggregateFileSizes(nodes, node -> node.getParent() != null);
	}

	private void aggregateFileSizes(List<FileSystemNode> nodes, String filetype) {
		Predicate<FileSystemNode> shouldAggregate = node -> {
			boolean hasParent = node.getParent() != null;
			boolean isDirectory = node.isDirectory();
			boolean hasFiletype = filetype.equals(node.getFiletype());
			return hasParent && (isDirectory || hasFiletype);
		};
		aggregateFileSizes(nodes, shouldAggregate);
	}

	/**
	 * Update all node sizes to reflect their size including the total aggregated size of all their descendants.
	 *
	 * @param nodes The nodes for which to update the file sizes.
	 * @param shouldAggregate How to determine which nodes to aggregate into their parent node.
	 */
	private void aggregateFileSizes(List<FileSystemNode> nodes, Predicate<FileSystemNode> shouldAggregate) {
		// Start from the leaves and work towards the root
		Stack<FileSystemNode> remainingNodes = buildAggregationStack(nodes);

		while (!remainingNodes.isEmpty()) {
			FileSystemNode currentNode = remainingNodes.pop();
			if (shouldAggregate.test(currentNode)) {
				FileSystemNode parent = currentNode.getParent();
				parent.setSize(parent.getSize() + currentNode.getSize());
			}
		}
	}

	/**
	 * Builds a stack from all given nodes, where each node is below all its child nodes. If the given nodes don't
	 * contain a root node, an empty stack will be returned.
	 *
	 * @param nodes The nodes to build the stack from.
	 * @return The constructed stack of nodes.
	 */
	private Stack<FileSystemNode> buildAggregationStack(List<FileSystemNode> nodes) {
		Stack<FileSystemNode> stack = new Stack<>();
		Queue<FileSystemNode> remainingNodes = new LinkedList<>();

		try {
			FileSystemNode root = nodes.stream()
					.filter(node -> node.getParent() == null)
					.toList().getFirst();
			remainingNodes.add(root);
		} catch (NoSuchElementException ignored) {
		}

		while (!remainingNodes.isEmpty()) {
			FileSystemNode currentNode = remainingNodes.poll();
			stack.push(currentNode);
			remainingNodes.addAll(currentNode.getChildNodes());
		}

		return stack;
	}
}
