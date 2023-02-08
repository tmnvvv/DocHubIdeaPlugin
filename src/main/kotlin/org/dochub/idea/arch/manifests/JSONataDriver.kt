package org.dochub.idea.arch.manifests

import com.api.jsonata4java.expressions.EvaluateException
import com.api.jsonata4java.expressions.EvaluateRuntimeException
import com.api.jsonata4java.expressions.Expressions
import com.api.jsonata4java.expressions.ParseException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import net.minidev.json.JSONObject
import org.yaml.snakeyaml.Yaml
import java.io.IOException

class JSONataDriver {
    fun test() {
        var expr: Expressions? = null
        val mapper = ObjectMapper()
        var jsonObj: JsonNode? = null
        val json = "{ \"a\":1, \"b\":2, \"c\":[1,2,3,4,5] }"
        val expression = "\$sum(c)"
        try {
            jsonObj = mapper.readTree(json)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        try {
            println(
                """
                    Using json:
                    ${mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj)}
                    """.trimIndent()
            )
            println("expression=$expression")
            expr = Expressions.parse(expression)
        } catch (e: ParseException) {
            System.err.println(e.localizedMessage)
        } catch (ere: EvaluateRuntimeException) {
            println(ere.localizedMessage)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            println("evaluate returns:")
            val result = expr!!.evaluate(jsonObj)
            if (result == null) {
                println("** no match **")
            } else {
                println("" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result))
            }
        } catch (e: EvaluateException) {
            System.err.println(e.localizedMessage)
        } catch (e: JsonProcessingException) {
            System.err.println(e.localizedMessage)
        }
    }

    companion object {
        private fun convertToJson(yamlString: String): String {
            val yaml = Yaml()
            val map = yaml.load<Any>(yamlString) as Map<String, Any?>
            val jsonObject = JSONObject(map)
            return jsonObject.toString()
        }
    }
}
