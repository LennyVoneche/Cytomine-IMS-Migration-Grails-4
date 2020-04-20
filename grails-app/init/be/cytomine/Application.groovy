package be.cytomine

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

import groovy.transform.CompileStatic

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

@CompileStatic
class Application extends GrailsAutoConfiguration {
    static Logger logger = Logger.getLogger(Application.class);

    static void main(String[] args) {
        BasicConfigurator.configure();
        GrailsApp.run(Application, args)
    }
}