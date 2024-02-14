package tools

import java.io.PrintWriter

fun main(args: Array<String>) {
    val outputDir = "src/main/kotlin/xyz/rohit/lox"
    defineAst(
        outputDir,
        "Expr",
        mutableListOf(
            "Binary : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal : Object value",
            "Unary : Token operator, Expr right",
        ),
    )
    defineAst(
        outputDir,
        "Stmt",
        mutableListOf(
            "Expression: Expr expression",
            "Print: Expr expression",
        ),
    )
}

fun defineAst(outputDir: String, baseName: String, types: List<String>) {
    val path = "$outputDir/$baseName.kt"
    val writer = PrintWriter(path, "UTF-8")

    writer.println("package xyz.rohit.lox")
    writer.println()
    writer.println("abstract class $baseName{")
    writer.println()
    writer.println("abstract fun <R> accept(visitor: Expr.Visitor<R>): R")
    defineVisitor(writer, baseName, types)

    for (type in types) {
        val className = type.split(":")[0].trim()
        val fields = type.split(":")[1].trim()

        defineType(writer, baseName, className, fields)
    }

    writer.println("}")
    writer.close()
}

fun defineType(writer: PrintWriter, baseName: String, className: String, fields: String) {
    // println(fields)
    val fieldLists = fields.split(", ")

    // println(fieldLists.toString())

    var thee = ""
    for (hello in fieldLists) {
        val nameAndTypes = hello.split(" ")
        // println(nameAndTypes)
        val name = nameAndTypes[1]
        val type = nameAndTypes[0]
        // println("val $name: $type, ")
        thee += "val $name: $type, "
    }

    writer.println(
        "class $className($thee): $baseName(){",
    )
    writer.println("override fun <R> accept(visitor: Visitor<R>): R {")
    writer.println("return visitor.visit$className$baseName(this)")
    writer.println("}")
    writer.println("}")
    writer.println()
}

fun defineVisitor(writer: PrintWriter, baseName: String, types: List<String>) {
    writer.println("    interface Visitor<R> {")

    for (type in types) {
        val typeName = type.split(":")[0].trim()
        writer.println("fun visit$typeName$baseName(${baseName.lowercase()}: $typeName): R")
    }
    writer.println(" }")
}
