package net.smartology.server.configuration;

import ro.pippo.core.Application;
import ro.pippo.core.Initializer;
import ro.pippo.freemarker.FreemarkerTemplateEngine;

public class FreemarkerInitializer implements Initializer {

    @Override
    public void init(Application application) {
        application.registerTemplateEngine(FreemarkerTemplateEngine.class);
    }

    @Override
    public void destroy(Application application) {
        // do nothing
    }

}
