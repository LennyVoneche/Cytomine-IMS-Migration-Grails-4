package be.cytomine.formats.supported.digitalpathology

/*
 * Copyright (c) 2009-2019. Authors: see NOTICE file.
 *
 * Licensed under the GNU Lesser General Public License, Version 2.1 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-2.1.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import be.cytomine.formats.tools.detectors.OpenSlideDetector
import groovy.util.logging.Log4j
import utils.MimeTypeUtils

@Log4j
class PhilipsTIFFFormat extends OpenSlideFormat implements OpenSlideDetector {

    String vendor = "philips"
    String customExtension = "ptiff"

    // https://openslide.org/formats/philips/
    // Associated labels: label, macro
    PhilipsTIFFFormat() {
        super()
        extensions = ["tiff", customExtension]
        mimeType = MimeTypeUtils.MIMETYPE_PTIFF
    }

    /*
    Why it has been used instead of OpenSlide implementation ?

    BufferedImage associated(String label) {
        def tiffinfoExecutable = Holders.config.cytomine.tiffinfo
        String tiffinfo = "$tiffinfoExecutable ${this.file.absolutePath}".execute().text
        int numberOfTIFFDirectories = tiffinfo.count("TIFF Directory")
        if (label == "label") {
            //last directory
            getTIFFSubImage(numberOfTIFFDirectories - 1)
        } else if (label == "macro") {
            //next to last directory
            getTIFFSubImage(numberOfTIFFDirectories - 2)
        } else {
            thumb(512)
        }
    }

    private BufferedImage getTIFFSubImage(int index) {
        boolean convertSuccessfull = true

        println ImageIO.getReaderFormatNames()
        String source = this.file.absolutePath
        File target = File.createTempFile("label", ".jpg")
        String targetPath = target.absolutePath

        println "target=" + target.getPath()
        def vipsExecutable = Holders.config.cytomine.vips
        def command = """$vipsExecutable im_copy $source:$index $targetPath"""
        println command
        convertSuccessfull &= ProcUtils.executeOnShell(command) == 0

        BufferedImage labelImage = null
        if (convertSuccessfull) {
            println targetPath
            println new File(targetPath).exists()
            labelImage = ImageIO.read(target)
            //labelImage = rotate90ToRight(labelImage)
            assert(labelImage)
        }
        target.delete()
        return labelImage
    }
    */
}
