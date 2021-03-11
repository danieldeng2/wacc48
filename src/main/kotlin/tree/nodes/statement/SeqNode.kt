package tree.nodes.statement

import tree.SymbolTable
import tree.nodes.function.FuncNode
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.CodeEvaluatorVisitor
import tree.nodes.expr.Literal

data class SeqNode(
    val sequence: List<StatNode>,
    val ctx: ParserRuleContext?,
) : StatNode, List<StatNode> {


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        sequence.forEach { it.validate(st, funTable) }
    }

    override val size: Int
        get() = sequence.size

    override fun contains(element: StatNode): Boolean =
        sequence.contains(element)

    override fun containsAll(elements: Collection<StatNode>) =
        sequence.containsAll(elements)

    override fun get(index: Int): StatNode =
        sequence[index]

    override fun indexOf(element: StatNode): Int =
        sequence.indexOf(element)

    override fun isEmpty(): Boolean =
        sequence.isEmpty()

    override fun iterator(): Iterator<StatNode> =
        sequence.iterator()

    override fun lastIndexOf(element: StatNode): Int =
        sequence.lastIndexOf(element)

    override fun listIterator(): ListIterator<StatNode> =
        sequence.listIterator()

    override fun listIterator(index: Int): ListIterator<StatNode> =
        sequence.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): List<StatNode> =
        sequence.subList(fromIndex, toIndex)

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateSeq(this)
    }

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor): Literal? {
        return visitor.translateSeq(this)
    }
}