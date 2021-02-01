import antlr.WACCParser
import antlr.WACCParserBaseVisitor

class ParserVisitor : WACCParserBaseVisitor<Boolean>() {

    override fun visitProg(ctx: WACCParser.ProgContext?): Boolean {
        println("visitProg")

        visitChildren(ctx)
        return true
    }

    override fun visitFunc(ctx: WACCParser.FuncContext?): Boolean {
        println("visitFunc ${ctx?.IDENT()?.text}")
        return true
    }


}