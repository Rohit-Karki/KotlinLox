package tools

abstract class Pastry {
    abstract fun <R> accept(pastryVisitor: PastryVisitor<R>): R
}
class Beignet : Pastry() {
    override fun <R> accept(pastryVisitor: PastryVisitor<R>): R {
        return pastryVisitor.visitBeignet(this)
    }
}
class Cruller : Pastry() {
    override fun <R> accept(pastryVisitor: PastryVisitor<R>): R {
        return pastryVisitor.visitCruller(this)
    }
}

interface PastryVisitor<R> {
    fun visitBeignet(beignet: Beignet?): R
    fun visitCruller(cruller: Cruller?): R
}
