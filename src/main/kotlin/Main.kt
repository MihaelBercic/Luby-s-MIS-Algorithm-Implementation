import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.random.Random

/**
 * Created by mihael
 * on 27/12/2022 at 14:01
 * using IntelliJ IDEA
 */
/**
 * [Luby's MIS algorithm](https://dl.acm.org/doi/pdf/10.1145/22145.22146)
 *
 */
fun main() {
    val graph = parseData("star.txt")
    val (active, activeEdges) = graph.copy(nodes = graph.nodes.toMutableList(), edges = graph.edges.toMutableSet())
    val independentSet = mutableListOf<Node>()
    runBlocking {
        while (active.isNotEmpty()) {
            val tempIndependentSet = Collections.synchronizedSet(mutableSetOf<Node>())
            active.forEach {
                launch {
                    if (it.degree == 0 || Random.nextDouble() <= it.probability) {
                        tempIndependentSet.add(it)
                    }
                }.join()
            }
            val edgesWithBothInTemp = activeEdges.filter { (first, second) ->
                tempIndependentSet.contains(first) && tempIndependentSet.contains(second)
            }
            edgesWithBothInTemp.forEach { edge ->
                launch {
                    val (first, second) = edge
                    if (first.degree <= second.degree) tempIndependentSet.remove(first)
                    else tempIndependentSet.remove(second)
                }.join()
            }
            val activeNeighbours = tempIndependentSet.flatMap(Node::neighbours).distinct()
            independentSet.addAll(tempIndependentSet)
            active.removeAll(independentSet + activeNeighbours)
            active.forEach { it.removeNeighbours(independentSet + activeNeighbours) }
            activeEdges.removeAll { (first, second) ->
                activeNeighbours.contains(first) || activeNeighbours.contains(second)
            }
        }
    }

    graph.edges.forEach { edge -> println("${edge.first.id} -- ${edge.second.id}") }
    independentSet.map(Node::id).sorted().forEach { println("$it [color=\"red\"]") }
}

fun parseData(fileName: String): Graph {
    val input = object {}::class.java.getResource(fileName)?.readText() ?: throw MissingResourceException("No such resource: $fileName", object {}::class.java.name, fileName)
    val lines = input.lines().map { it.split(" ").map { it.toInt() == 1 } }
    val size = lines.size;
    val nodes = (0 until size).map { Node(it) }.toMutableList()
    val edges = mutableSetOf<Edge>()
    lines.forEachIndexed { rowIndex, neighbourList ->
        val currentNode = nodes[rowIndex]
        neighbourList.forEachIndexed { columnIndex, isConnected ->
            if (isConnected) {
                val connectedToNode = nodes[columnIndex]
                val edge = Edge(currentNode, connectedToNode)
                currentNode.neighbours.add(connectedToNode)
                edges.add(edge)
            }
        }
    }
    return Graph(nodes, edges)
}