package analyser.nodes.type

import analyser.SymbolTable

object CharType : Type {
    val min: Int = 0;
    val max: Int = 255;
    override fun isValid(st: SymbolTable) = true

}