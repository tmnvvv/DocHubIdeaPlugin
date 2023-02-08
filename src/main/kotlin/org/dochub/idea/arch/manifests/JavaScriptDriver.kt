package org.dochub.idea.arch.manifests

import java.io.FileReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import javax.script.Invocable
import javax.script.ScriptEngineManager


class JavaScriptDriver {
    private inner class CallbackLoad

    fun test() {
        val factory = ScriptEngineManager()
        val engine = factory.getEngineByName("JavaScript")
        val inv = engine as Invocable
        val jsonata = FileReader("jsonata.js")

        // load the JSONata processor
        engine.eval(jsonata)

        // read and JSON.parse the input data
        var sample: ByteArray? = ByteArray(0)
        try {
            sample = Files.readAllBytes(Paths.get("sample.json"))
            engine.put("adaptor", CallbackLoad())
            engine.put("input", String(sample))
            val inputjson = engine.eval("JSON.parse(input);")

            // query the data
            val expression = "\$sum(Account.Order.Product.(Price * Quantity))" // JSONata expression
            val expr = inv.invokeFunction("jsonata", expression)
            val resultjson = inv.invokeMethod(expr, "evaluate", inputjson)

            // JSON.stringify the result
            engine.put("resultjson", resultjson)
            val result = engine.eval("JSON.stringify(resultjson);")
            println(result)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }
    }
}
