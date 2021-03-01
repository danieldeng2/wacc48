package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class SeqNode(
    val sequence: List<StatNode>,
    override val ctx: ParserRuleContext?,
) : StatNode, List<StatNode> {
    override lateinit var st: SymbolTable


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
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

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        val instructions = mutableListOf<Instruction>()
        sequence.forEach { instructions.addAll(it.translate(ctx)) }
        return instructions
    }
}