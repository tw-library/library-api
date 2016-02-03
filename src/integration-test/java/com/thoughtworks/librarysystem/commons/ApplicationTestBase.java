package com.thoughtworks.librarysystem.commons;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.Map;

public class ApplicationTestBase {

    protected  String loadFixture(String fixture, Map<String, Object> inputs) throws Exception {
        VelocityEngine ve = createVelocity();

        String fileName = String.format("templates/%s", fixture);

        Template template = ve.getTemplate(fileName);
        VelocityContext context = new VelocityContext();

        for(String key: inputs.keySet()) {
            context.put(key, inputs.get(key));
        }

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        return writer.toString();
    }

    private VelocityEngine createVelocity() throws Exception {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        return ve;
    }
}
