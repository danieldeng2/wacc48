import analyser.SymbolTable
import org.junit.Test
import analyser.nodes.ProgNode
import analyser.nodes.expr.BoolLiteral
import analyser.nodes.statement.*
import exceptions.SemanticsException
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GlobalReturnTest {
    private val trueBooleanNode = BoolLiteral(
        value = true,
        ctx = null
    )
    private val returnNode = ReturnNode(
        value = trueBooleanNode,
        ctx = null
    )
    private val skipNode = SkipNode

    // Returns True for a program with global return (semanticError)
    private fun globalReturnSemanticErrorThrown(statNode: StatNode): Boolean {
        val programNode = ProgNode(
            body = statNode,
            functions = emptyList(),
            ctx = null
        )
        try {
            programNode.validate(SymbolTable(null), SymbolTable(null))
        } catch (e: SemanticsException) {
            if (e.message.equals("Cannot return in global context")) {
                return true
            }
        }
        return false
    }

    @Test
    fun globalReturnThrowsSemanticError() {
        assertTrue(globalReturnSemanticErrorThrown(returnNode))
    }

    @Test
    fun globalExitValidates() {
        val exitNode = ExitNode(
            value = trueBooleanNode,
            ctx = null
        )
        assertFalse(globalReturnSemanticErrorThrown(exitNode))
    }

    @Test
    fun globalReturnInIfStatFirstThrowsSemanticError() {
        val ifNode = IfNode(
            proposition = trueBooleanNode,
            trueStat = returnNode,
            falseStat = skipNode,
            ctx = null
        )
        assertTrue(globalReturnSemanticErrorThrown(ifNode))
    }

    @Test
    fun globalReturnInIfStatSecondThrowsSemanticError() {
        val ifNode = IfNode(
            proposition = trueBooleanNode,
            trueStat = skipNode,
            falseStat = returnNode,
            ctx = null
        )
        assertTrue(globalReturnSemanticErrorThrown(ifNode))
    }

    @Test
    fun noGlobalReturnIfStatValidates() {
        val ifNode = IfNode(
            proposition = trueBooleanNode,
            trueStat = skipNode,
            falseStat = skipNode,
            ctx = null
        )
        assertFalse(globalReturnSemanticErrorThrown(ifNode))
    }

    @Test
    fun globalReturnSeqStatFirstThrowsSemanticError() {
        val seqNode = SeqNode(
            firstStat = returnNode,
            secondStat = skipNode,
            ctx = null
        )
        assertTrue(globalReturnSemanticErrorThrown(seqNode))
    }

    @Test
    fun globalReturnSeqStatSecondThrowsSemanticError() {
        val seqNode = SeqNode(
            firstStat = skipNode,
            secondStat = returnNode,
            ctx = null
        )
        assertTrue(globalReturnSemanticErrorThrown(seqNode))
    }

    @Test
    fun noGlobalReturnSeqStatValidates() {
        val seqNode = SeqNode(
            firstStat = skipNode,
            secondStat = skipNode,
            ctx = null
        )
        assertFalse(globalReturnSemanticErrorThrown(seqNode))
    }

    @Test
    fun globalReturnBeginStatThrowsSemanticError() {
        val beginNode = BeginNode(
            stat = returnNode,
            ctx = null
        )
        assertTrue(globalReturnSemanticErrorThrown(beginNode))
    }

    @Test
    fun noGlobalReturnBeginStatValidates() {
        val beginNode = BeginNode(
            stat = skipNode,
            ctx = null
        )
        assertFalse(globalReturnSemanticErrorThrown(beginNode))
    }

    @Test
    fun globalReturnWhileStatThrowsSemanticError() {
        val whileNode = WhileNode(
            proposition = trueBooleanNode,
            body = returnNode,
            ctx = null
        )
        assertTrue(globalReturnSemanticErrorThrown(whileNode))
    }

    @Test
    fun noGlobalReturnWhileStatValidates() {
        val whileNode = WhileNode(
            proposition = trueBooleanNode,
            body = skipNode,
            ctx = null
        )
        assertFalse(globalReturnSemanticErrorThrown(whileNode))
    }
}