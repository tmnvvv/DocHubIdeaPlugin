package org.dochub.idea.arch.indexing

import com.intellij.util.indexing.*
import com.intellij.util.io.DataExternalizer
import java.io.DataInput
import java.io.DataOutput
import java.io.IOException

class DocHubIndex : SingleEntryFileBasedIndexExtension<DocHubIndexData>() {
    private val myDataIndexer: SingleEntryIndexer<DocHubIndexData?> =
        object : SingleEntryIndexer<DocHubIndexData?>(false) {
            override fun computeValue(inputData: FileContent): DocHubIndexData {
                return DocHubIndexData(inputData.psiFile)
            }
        }
    private val myValueExternalizer: DataExternalizer<DocHubIndexData> = object : DataExternalizer<DocHubIndexData> {
        @Throws(IOException::class)
        override fun save(out: DataOutput, value: DocHubIndexData?) {
            if (value != null) {
                value.stringify(out)
            }
        }

        @Throws(IOException::class)
        override fun read(`in`: DataInput): DocHubIndexData? {
            try {
                return DocHubIndexData(`in`)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
            return null
        }
    }
    private val myInputFilter =
        FileBasedIndex.InputFilter { file -> file.path.endsWith(".yaml") }

    override fun getName(): ID<Int, DocHubIndexData> {
        return INDEX_ID
    }

    override fun getIndexer(): SingleEntryIndexer<DocHubIndexData?> {
        return myDataIndexer
    }

    override fun getValueExternalizer(): DataExternalizer<DocHubIndexData> {
        return myValueExternalizer
    }

    override fun getVersion(): Int {
        return 7
    }

    override fun getInputFilter(): FileBasedIndex.InputFilter {
        return myInputFilter
    }

    companion object {
        val INDEX_ID: ID<Int, DocHubIndexData> = ID.create<Int, DocHubIndexData>("DocHubYamlIndex")
    }
}
