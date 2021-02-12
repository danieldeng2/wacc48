import analyser.SymbolTable
import org.junit.Test
import analyser.nodes.ProgNode
import analyser.nodes.expr.BoolLiteral
import analyser.nodes.statement.*
import exceptions.SemanticsException
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GlobalReturnTest {
    private val trueBooleanNode = BoolLiteral(true, null)
    private val returnNode = ReturnNode(trueBooleanNode, null)
    private val skipNode = SkipNode

    // Returns True for a program with global return (semanticError)
    private fun globalReturnSemanticErrorThrown(statNode: StatNode): Boolean {
        val programNode = ProgNode(statNode, emptyList(), null)
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
        val exitNode = ExitNode(trueBooleanNode, null)
        assertFalse(globalReturnSemanticErrorThrown(exitNode))
    }

    @Test
    fun globalReturnInIfStatFirstThrowsSemanticError() {
        val ifNode = IfNode(trueBooleanNode, returnNode, skipNode, null)
        assertTrue(globalReturnSemanticErrorThrown(ifNode))
    }

    @Test
    fun globalReturnInIfStatSecondThrowsSemanticError() {
        val ifNode = IfNode(trueBooleanNode, skipNode, returnNode, null)
        assertTrue(globalReturnSemanticErrorThrown(ifNode))
    }

    @Test
    fun noGlobalReturnIfStatValidates() {
        val ifNode = IfNode(trueBooleanNode, skipNode, skipNode, null)
        assertFalse(globalReturnSemanticErrorThrown(ifNode))
    }

    @Test
    fun globalReturnSeqStatFirstThrowsSemanticError() {
        val seqNode = SeqNode(returnNode, skipNode, null)
        assertTrue(globalReturnSemanticErrorThrown(seqNode))
    }

    @Test
    fun globalReturnSeqStatSecondThrowsSemanticError() {
        val seqNode = SeqNode(skipNode, returnNode, null)
        assertTrue(globalReturnSemanticErrorThrown(seqNode))
    }

    @Test
    fun noGlobalReturnSeqStatValidates() {
        val seqNode = SeqNode(skipNode, skipNode, null)
        assertFalse(globalReturnSemanticErrorThrown(seqNode))
    }

    @Test
    fun globalReturnBeginStatThrowsSemanticError() {
        val beginNode = BeginNode(returnNode, null)
        assertTrue(globalReturnSemanticErrorThrown(beginNode))
    }

    @Test
    fun noGlobalReturnBeginStatValidates() {
        val beginNode = BeginNode(skipNode, null)
        assertFalse(globalReturnSemanticErrorThrown(beginNode))
    }

    @Test
    fun globalReturnWhileStatThrowsSemanticError() {
        val whileNode = WhileNode(trueBooleanNode, returnNode, null)
        assertTrue(globalReturnSemanticErrorThrown(whileNode))
    }

    @Test
    fun noGlobalReturnWhileStatValidates() {
        val whileNode = WhileNode(trueBooleanNode, skipNode, null)
        assertFalse(globalReturnSemanticErrorThrown(whileNode))
    }
}