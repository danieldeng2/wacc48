package wacc48.analyser.exceptions

import org.antlr.v4.runtime.ParserRuleContext

enum class IssueType {
    SYNTAX,
    SEMANTICS
}

class Issue(
    val msg: String,
    val type: IssueType,
    val ctx: ParserRuleContext?
) {
    override fun toString(): String {
        var message = "${type.name} ERROR \n"

        if (ctx != null)
            message += "Line ${ctx.start.line}:${ctx.start.charPositionInLine} "

        message += msg

        return message
    }
}

fun MutableList<Issue>.addSemantic(
    msg: String,
    ctx: ParserRuleContext? = null
) = this.add(Issue(msg, IssueType.SEMANTICS, ctx))

fun MutableList<Issue>.addSyntax(
    msg: String,
    ctx: ParserRuleContext? = null
) = this.add(Issue(msg, IssueType.SYNTAX, ctx))