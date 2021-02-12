import analyser.SymbolTable
import analyser.nodes.ProgNode
import analyser.nodes.expr.BoolLiteral
import analyser.nodes.function.FuncNode
import analyser.nodes.function.ParamListNode
import analyser.nodes.statement.IfNode
import analyser.nodes.statement.ReturnNode
import analyser.nodes.statement.SkipNode
import analyser.nodes.statement.StatNode
import analyser.nodes.type.BoolType
import analyser.nodes.type.IntType
import analyser.nodes.type.VoidType
import exceptions.SemanticsException
import exceptions.SyntaxException
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class FunctionNodeTest {
    private val trueBooleanNode = BoolLiteral(true, null)
    private val returnTrueNode = ReturnNode(trueBooleanNode, null)


    // Creates a program with a single function and validates that this function returns
    private fun validateProgramWithFunctionBody(body: StatNode) {
        val funcNode = FuncNode("func", ParamListNode(emptyList(), null), BoolType, body, null)
        val programNode = ProgNode(SkipNode, listOf(funcNode), null)
        programNode.validate(SymbolTable(null), SymbolTable(null))
    }

    @Test
    fun functionsCannotBeReDeclared() {
        val funcNode = FuncNode("func", ParamListNode(emptyList(), null), VoidType, SkipNode, null)
        val funcNodeDuplicate = FuncNode("func", ParamListNode(emptyList(), null), VoidType, SkipNode, null)
        val symbolTable = SymbolTable(null)
        symbolTable.add("func", funcNode);

        val exception = assertFailsWith<SemanticsException> {
            funcNodeDuplicate.validatePrototype(symbolTable)
        }
        assertEquals(exception.message, "Illegal re-declaration of function func")
    }

    @Test
    fun incorrectReturnTypeThrowsSemanticError() {
        val funcNode = FuncNode("func", ParamListNode(emptyList(), null), IntType, returnTrueNode, null)
        val exception = assertFailsWith<SemanticsException> {
            funcNode.validate(SymbolTable(null), SymbolTable(null))
        }
        assertEquals(exception.message, "The expected return type of Function func is: Bool Actual return type: Int")
    }

    @Test
    fun correctReturnTypeValidates() {
        val funcNode = FuncNode("func", ParamListNode(emptyList(), null), BoolType, returnTrueNode, null)
        funcNode.validate(SymbolTable(null), SymbolTable(null))
    }

    @Test
    fun functionWithoutReturnThrowsSyntaxError() {
        assertFailsWith<SyntaxException> {
            validateProgramWithFunctionBody(SkipNode)
        }
    }

    @Test
    fun functionWithReturnValidates() {
        validateProgramWithFunctionBody(returnTrueNode)
    }

    @Test
    fun functionWithoutReturnIfStatThrowsSyntaxError() {
        assertFailsWith<SyntaxException> {
            validateProgramWithFunctionBody(IfNode(trueBooleanNode, returnTrueNode, SkipNode, null))
        }
    }

    @Test
    fun functionWithReturnIfStatValidates(){
        validateProgramWithFunctionBody(IfNode(trueBooleanNode, returnTrueNode, returnTrueNode, null))
    }


}
