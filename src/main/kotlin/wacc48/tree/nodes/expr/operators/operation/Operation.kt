package wacc48.tree.nodes.expr.operators.operation
import wacc48.tree.nodes.expr.operators.operation.binary.AndOperation
import wacc48.tree.nodes.expr.operators.operation.binary.BinaryOperation
import wacc48.tree.nodes.expr.operators.operation.binary.DivideOperation
import wacc48.tree.nodes.expr.operators.operation.binary.EqualsOperation
import wacc48.tree.nodes.expr.operators.operation.binary.GreaterEqualThanOperation
import wacc48.tree.nodes.expr.operators.operation.binary.GreaterThanOperation
import wacc48.tree.nodes.expr.operators.operation.binary.LessEqualThanOperation
import wacc48.tree.nodes.expr.operators.operation.binary.LessThanOperation
import wacc48.tree.nodes.expr.operators.operation.binary.MinusOperation
import wacc48.tree.nodes.expr.operators.operation.binary.ModulusOperation
import wacc48.tree.nodes.expr.operators.operation.binary.MultiplyOperation
import wacc48.tree.nodes.expr.operators.operation.binary.NotEqualsOperation
import wacc48.tree.nodes.expr.operators.operation.binary.OrOperation
import wacc48.tree.nodes.expr.operators.operation.binary.PlusOperation
import wacc48.tree.nodes.expr.operators.operation.unary.ChrOperation
import wacc48.tree.nodes.expr.operators.operation.unary.LenOperation
import wacc48.tree.nodes.expr.operators.operation.unary.NegateOperation
import wacc48.tree.nodes.expr.operators.operation.unary.NotOperation
import wacc48.tree.nodes.expr.operators.operation.unary.OrdOperation
import wacc48.tree.nodes.expr.operators.operation.unary.UnaryOperation
import wacc48.tree.type.Type

interface Operation {
    val repr: String
    val expectedExprTypes: List<Type>
    val returnType: Type
}

private val binaryOperations = mapOf(
    "+" to PlusOperation,
    "-" to MinusOperation,
    "*" to MultiplyOperation,
    "/" to DivideOperation,
    "%" to ModulusOperation,
    ">" to GreaterThanOperation,
    "<" to LessThanOperation,
    ">=" to GreaterEqualThanOperation,
    "<=" to LessEqualThanOperation,
    "==" to EqualsOperation,
    "!=" to NotEqualsOperation,
    "&&" to AndOperation,
    "||" to OrOperation,
)

private val unaryOperations = mapOf(
    "-" to NegateOperation,
    "!" to NotOperation,
    "len" to LenOperation,
    "ord" to OrdOperation,
    "chr" to ChrOperation,
)

fun lookupBinary(representation: String) : BinaryOperation?
    = binaryOperations[representation]

fun lookupUnary(representation: String) : UnaryOperation?
    = unaryOperations[representation]
