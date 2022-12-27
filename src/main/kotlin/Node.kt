/**
 * Created by mihael
 * on 27/12/2022 at 18:25
 * using IntelliJ IDEA
 */
data class Node(val id: Int, val neighbours: MutableList<Node> = mutableListOf()) {

    val degree get() = neighbours.size
    val probability get() = 1.0 / (2 * neighbours.size)

    fun removeNeighbours(nodes: Collection<Node>) {
        nodes.forEach { node ->
            neighbours.remove(node)
            node.neighbours.remove(this)
        }
    }

    override fun toString(): String = "Node($id, neighbours = ${neighbours.size})"
    override fun hashCode(): Int = id
    override fun equals(other: Any?): Boolean = if (other is Node) other.hashCode() == hashCode() else false
}