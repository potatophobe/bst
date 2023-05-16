package ru.potatophobe.birarysearchtree

class SimpleBinarySearchTree<T : Comparable<T>> : Tree<T> {
    private var rootNode: Node<T>? = null

    override fun put(element: T) {
        return rootNode?.let { put(it, element) } ?: run { rootNode = Node(element) }
    }

    override fun contains(element: T): Boolean {
        return rootNode?.let { contains(it, element) } ?: false
    }

    override fun remove(element: T): Boolean {
        return rootNode?.let { node ->
            val comparison = element.compareTo(node.value)
            when {
                comparison < 0 -> node.left?.let { remove(it, element) } ?: false
                comparison > 0 -> node.right?.let { remove(it, element) } ?: false
                else -> {
                    removeRootNode()
                    true
                }
            }
        } ?: false
    }

    private fun put(node: Node<T>, element: T) {
        val comparison = element.compareTo(node.value)
        when {
            comparison < 0 -> node.left?.let { put(it, element) } ?: run { node.left = Node(element, node) }
            comparison > 0 -> node.right?.let { put(it, element) } ?: run { node.right = Node(element, node) }
            else -> node.value = element
        }
    }

    private fun contains(node: Node<T>, element: T): Boolean {
        val comparison = element.compareTo(node.value)
        return when {
            comparison < 0 -> node.left?.let { contains(it, element) } ?: false
            comparison > 0 -> node.right?.let { contains(it, element) } ?: false
            else -> true
        }
    }

    private fun remove(node: Node<T>, element: T): Boolean {
        val comparison = element.compareTo(node.value)
        return when {
            comparison < 0 -> node.left?.let { remove(it, element) } ?: false
            comparison > 0 -> node.right?.let { remove(it, element) } ?: false
            else -> {
                removeNode(node)
                true
            }
        }
    }

    private fun removeRootNode() {
        rootNode!!.left?.let { left ->
            rootNode!!.right?.let { right ->
                val maxLeftNode = findMaxNode(left)
                removeNode(maxLeftNode)
                maxLeftNode.parent = null
                if (left != maxLeftNode) {
                    maxLeftNode.left = left
                }
                maxLeftNode.right = right
                rootNode = maxLeftNode
            } ?: run {
                left.parent = null
                rootNode = left
            }
        } ?: rootNode!!.right?.let { right ->
            right.parent = null
            rootNode = right
        } ?: run {
            rootNode = null
        }
    }

    private fun removeNode(node: Node<T>) {
        node.left?.let { left ->
            node.right?.let { right ->
                val maxLeftNode = findMaxNode(left)
                removeNode(maxLeftNode)
                maxLeftNode.parent = node.parent
                if (left != maxLeftNode) {
                    maxLeftNode.left = left
                }
                maxLeftNode.right = right
                if (node.parent?.left == node) {
                    node.parent?.left = maxLeftNode
                } else if (node.parent?.right == node) {
                    node.parent?.right = maxLeftNode
                }
            } ?: run {
                left.parent = node.parent
                if (node.parent?.left == node) {
                    node.parent?.left = left
                } else if (node.parent?.right == node) {
                    node.parent?.right = left
                }
            }
        } ?: node.right?.let { right ->
            right.parent = node.parent
            if (node.parent?.left == node) {
                node.parent?.left = right
            } else if (node.parent?.right == node) {
                node.parent?.right = right
            }
        } ?: run {
            if (node.parent?.left == node) {
                node.parent?.left = null
            } else if (node.parent?.right == node) {
                node.parent?.right = null
            }
        }
    }

    private fun findMaxNode(node: Node<T>): Node<T> {
        return node.right?.let { findMaxNode(it) } ?: node
    }

    private data class Node<T>(
        var value: T,
        var parent: Node<T>? = null,
        var left: Node<T>? = null,
        var right: Node<T>? = null
    )
}
