package org.dochub.idea.arch.manifests

import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import org.eclipse.elk.alg.layered.options.LayeredOptions
import org.eclipse.elk.core.data.LayoutMetaDataService
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.Charset

// https://plantuml.com/ru/api
// https://www.eclipse.org/forums/index.php/t/129584/


class PlantUMLDriver {

    fun init() {
        System.setProperty(
            "org.eclipse.emf.ecore.EPackage.Registry.INSTANCE",
            "org.eclipse.emf.ecore.impl.EPackageRegistryImpl"
        )
        LayoutMetaDataService.getInstance().registerLayoutMetaDataProviders(LayeredOptions())
    }
    companion object {
        fun makeSVG(source: String): String? {
            var result: String? = null
            val reader = SourceStringReader(source)
            val os = ByteArrayOutputStream()
            try {
                reader.outputImage(os, FileFormatOption(FileFormat.SVG))
                os.close()
                result = String(os.toByteArray(), Charset.forName("UTF-8"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return result
        }
    }
}
