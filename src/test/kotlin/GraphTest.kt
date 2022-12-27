import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Created by mihael
 * on 27/12/2022 at 17:21
 * using IntelliJ IDEA
 */
class GraphTest {
    @Test
    fun edgeTest() {
        val nodeA = Node(0)
        val nodeB = Node(1)
        val edgeA = Edge(nodeA, nodeB)
        val edgeB = Edge(nodeB, nodeA)
        assertEquals(edgeA, edgeB)
    }
}