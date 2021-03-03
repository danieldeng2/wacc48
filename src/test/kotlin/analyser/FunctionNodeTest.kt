package analyser

import tree.nodes.ProgNode
import tree.nodes.expr.BoolLiteral
import tree.nodes.function.FuncNode
import tree.nodes.function.MainNode
import tree.nodes.statement.IfNode
import tree.nodes.statement.ReturnNode
import tree.nodes.statement.SkipNode
import tree.nodes.statement.StatNode
import tree.type.BoolType
import tree.type.IntType
import tree.type.VoidType
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import tree.SymbolTable
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class FunctionNodeTest {
    private val trueBooleanNode = BoolLiteral(
        value = true,
        ctx = null
    )
    private val returnTrueNode = ReturnNode(
        value = trueBooleanNode,
        ctx = null
    )
    private val emptyMainNode = MainNode(
        SkipNode, null
    )


    // Creates a program with a single function and validates that this function returns
    private fun validateProgramWithFunctionBody(body: StatNode) {
        val funcNode = FuncNode(
            identifier = "func",
            paramList = emptyList(),
            retType = BoolType,
            body = body,
            ctx = null
        )
        val programNode = ProgNode(
            functions = listOf(funcNode),
            main = emptyMainNode,
            ctx = null
        )
        programNode.validate(SymbolTable(null), mutableMapOf())
    }

    @Test
    fun functionsCannotBeReDeclared() {
        val funcNode = FuncNode(
            identifier = "func",
            paramList = emptyList(),
            retType = VoidType,
            body = SkipNode,
            ctx = null
        )
        val funcNodeDuplicate = FuncNode(
            identifier = "func",
            paramList = emptyList(),
            retType = VoidType,
            body = SkipNode,
            ctx = null
        )
        val symbolTable = mutableMapOf<String, FuncNode>()
        symbolTable["func"] = funcNode

        val exception = assertFailsWith<SemanticsException> {
            funcNodeDuplicate.validatePrototype(symbolTable)
        }
        assertEquals(
            exception.message,
            "Illegal re-declaration of function func"
        )
    }

    @Test
    fun incorrectReturnTypeThrowsSemanticError() {
        val funcNode = FuncNode(
            identifier = "func",
            paramList = emptyList(),
            retType = IntType,
            body = returnTrueNode,
            ctx = null
        )
        val exception = assertFailsWith<SemanticsException> {
            funcNode.validate(SymbolTable(null), mutableMapOf())
        }
        assertEquals(
            exception.message,
            "The expected return type of Function func is: ${funcNode.retType}, actual return type: Bool"
        )
    }

    @Test
    fun correctReturnTypeValidates() {
        val funcNode = FuncNode(
            identifier = "func",
            paramList = emptyList(),
            retType = BoolType,
            body = returnTrueNode,
            ctx = null
        )
        funcNode.validate(SymbolTable(null), mutableMapOf())
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
            validateProgramWithFunctionBody(
                IfNode(
                    proposition = trueBooleanNode,
                    trueStat = returnTrueNode,
                    falseStat = SkipNode,
                    ctx = null
                )
            )
        }
    }

    @Test
    fun functionWithReturnIfStatValidates() {
        validateProgramWithFunctionBody(
            IfNode(
                proposition = trueBooleanNode,
                trueStat = returnTrueNode,
                falseStat = returnTrueNode,
                ctx = null
            )
        )
    }


}
