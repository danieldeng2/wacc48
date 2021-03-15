package wacc48.tree.type

interface Type {
    val reserveStackSize: Int
}

interface GenericPair : Type

interface Typable {
    var type: Type
}