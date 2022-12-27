/**
 * Created by mihael
 * on 27/12/2022 at 18:25
 * using IntelliJ IDEA
 */
data class Edge(val first: Node, val second: Node) {
    override fun hashCode(): Int = first.hashCode() + second.hashCode()
    override fun equals(other: Any?): Boolean = if (other is Edge) other.hashCode() == hashCode() else false
}