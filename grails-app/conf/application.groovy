//image manipulation executable
cytomine.vips = "vips"
cytomine.tiffinfo = "tiffinfo"
cytomine.identify = "identify"
cytomine.vipsthumbnail = "vipsthumbnail"

/******************************************************************************
 * EXTERNAL configuration
 ******************************************************************************/
grails.config.locations = [""]
environments {
    production {
        grails.config.locations = ["file:${userHome}/.grails/ims-config.groovy"]
    }
    development {
        // Update the file path so that it matches the generated configuration file in your bootstrap
        grails.config.locations = ["file:${userHome}/dev/Cytomine-bootstrap/configs/ims/ims-config.groovy"]
    }
}
println "External configuration file : ${grails.config.locations}"
File configFile = new File(grails.config.locations.first().minus("file:") as String)
println "Found configuration file ? ${configFile.exists()}"

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
        all          : '*/*',
        atom         : 'application/atom+xml',
        css          : 'text/css',
        csv          : 'text/csv',
        form         : 'application/x-www-form-urlencoded',
        html         : ['text/html', 'application/xhtml+xml'],
        js           : 'text/javascript',
        json         : ['application/json', 'text/json'],
        multipartForm: 'multipart/form-data',
        rss          : 'application/rss+xml',
        text         : 'text/plain',
        xml          : ['text/xml', 'application/xml'],
        jpg          : 'image/jpeg',
        png          : 'image/png',
        tiff         : 'image/tiff'
]

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart = false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = "true"
        grails.serverURL = "http://localhost:9080"
        server.port = 9080
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = "http://image.cytomine.be"
    }
}
cytomine.ims.charset = "UTF-8"

cytomine.ims.server.url = "http://localhost-ims"
cytomine.ims.server.core.url = "http://localhost-core"

//TODO: Migration
//cytomine.ims.server.publicKey = ""
//cytomine.ims.server.privateKey = ""
cytomine.ims.server.publicKey = "bd49317e-da66-41e7-ab03-f4de656c4b5d"
cytomine.ims.server.privateKey = "2459fd25-ed0f-4ea2-98bc-e54b41e035bf"

cytomine.ims.path.buffer = "/tmp/uploaded"
cytomine.ims.path.storage = "/data/images"

cytomine.ims.conversion.vips.compression = "lzw"
cytomine.ims.conversion.vips.executable = "vips"
cytomine.ims.conversion.unzip.executable = "unzip"
cytomine.ims.conversion.gdal.executable = "gdal_translate"
cytomine.ims.conversion.ffmpeg.executable = "ffmpeg"
cytomine.ims.conversion.bioformats.enabled = true
cytomine.ims.conversion.bioformats.hostname = "bioformat"
cytomine.ims.conversion.bioformats.port = 4321

cytomine.ims.detection.tiffinfo.executable = "tiffinfo"
cytomine.ims.detection.identify.executable = "identify"
cytomine.ims.detection.gdal.executable = "gdalinfo"
cytomine.ims.detection.ffprobe.executable = "ffprobe"
cytomine.ims.metadata.exiftool.executable = "exiftool"

cytomine.ims.pyramidalTiff.iip.url = "http://localhost-iip-cyto/fcgi-bin/iipsrv.fcgi"
cytomine.ims.openslide.iip.url = "http://localhost-iip-cyto/fcgi-bin/iipsrv.fcgi"
cytomine.ims.jpeg2000.iip.url = "http://localhost-iip-jp2000/fcgi-bin/iipsrv.fcgi"
cytomine.ims.jpeg2000.enabled = true


cytomine.ims.crop.maxSize = 15000 // in pixels
cytomine.ims.deleteJob.frequency = 600 // in seconds

cytomine.ims.hdf5.maxBurstSize = 512 // Mbytes
cytomine.ims.hdf5.maxBlockSize = 15 // Mbytes

cytomine.ims.tile.size = 256
cytomine.ims.hdf5.queueLimit = 100

cytomine.ims.upload.nThreadsPool = 10

//// Rest API Doc plugin
//grails.plugins.restapidoc.docVersion = "0.1"
//grails.plugins.restapidoc.basePath = cytomine.ims.server.url
//grails.plugins.restapidoc.grailsDomainDefaultType = "int"


dataSource {
    pooled = true
//    driverClassName = "org.postgresql.Driver"
//    username = "postgres"
//    dialect = org.hibernatespatial.postgis.PostgisDialect
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            pooled = true
            properties {
                maxActive = -1
                minEvictableIdleTimeMillis=1800000
                timeBetweenEvictionRunsMillis=1800000
                numTestsPerEvictionRun=3
                testOnBorrow=true
                testWhileIdle=true
                testOnReturn=true
                validationQuery="SELECT 1"
            }
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            pooled = true
            properties {
                maxActive = -1
                minEvictableIdleTimeMillis=1800000
                timeBetweenEvictionRunsMillis=1800000
                numTestsPerEvictionRun=3
                testOnBorrow=true
                testWhileIdle=true
                testOnReturn=true
                validationQuery="SELECT 1"
            }
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:prodDB;MVCC=TRUE;LOCK_TIMEOUT=10000"
            pooled = true
            properties {
                maxActive = -1
                minEvictableIdleTimeMillis=1800000
                timeBetweenEvictionRunsMillis=1800000
                numTestsPerEvictionRun=3
                testOnBorrow=true
                testWhileIdle=true
                testOnReturn=true
                validationQuery="SELECT 1"
            }
        }
    }
}
