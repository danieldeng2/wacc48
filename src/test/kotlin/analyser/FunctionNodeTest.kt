package analyser

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
    private val emptyMainNode = FuncNode(
        "main",
        ParamListNode(emptyList(), null),
        VoidType,
        SkipNode,
        null
    )


    // Creates a program with a single function and validates that this function returns
    private fun validateProgramWithFunctionBody(body: StatNode) {
        val funcNode = FuncNode(
            identifier = "func",
            paramList = ParamListNode(
                params = emptyList(),
                ctx = null
            ),
            retType = BoolType,
            body = body,
            ctx = null
        )
        val programNode = ProgNode(
            functions = listOf(funcNode, emptyMainNode),
            ctx = null
        )
        programNode.validate(SymbolTable(null), SymbolTable(null))
    }

    @Test
    fun functionsCannotBeReDeclared() {
        val funcNode = FuncNode(
            identifier = "func",
            paramList = ParamListNode(
                params = emptyList(),
                ctx = null
            ),
            retType = VoidType,
            body = SkipNode,
            ctx = null
        )
        val funcNodeDuplicate = FuncNode(
            identifier = "func",
            paramList = ParamListNode(
                params = emptyList(),
                ctx = null
            ),
            retType = VoidType,
            body = SkipNode,
            ctx = null
        )
        val symbolTable = SymbolTable(null)
        symbolTable.add("func", funcNode)

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
            paramList = ParamListNode(
                params = emptyList(),
                ctx = null
            ),
            retType = IntType,
            body = returnTrueNode,
            ctx = null
        )
        val exception = assertFailsWith<SemanticsException> {
            funcNode.validate(SymbolTable(null), SymbolTable(null))
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
            paramList = ParamListNode(
                params = emptyList(),
                ctx = null
            ),
            retType = BoolType,
            body = returnTrueNode,
            ctx = null
        )
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
