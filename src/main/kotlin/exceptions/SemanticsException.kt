package exceptions

import org.antlr.v4.runtime.ParserRuleContext

class SemanticsException(message: String, ctx: ParserRuleContext?) :
    Exception(
        if (ctx != null) {
            "line ${ctx.start.line}:${ctx.start.charPositionInLine} $message"
        } else {
            message
        }
    )