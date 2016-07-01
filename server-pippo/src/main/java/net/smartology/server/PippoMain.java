package net.smartology.server;

import net.smartology.server.routes.WebApp;
import ro.pippo.core.Pippo;

public class PippoMain {

    public static void main(String args[]) {
        Pippo pippo = new Pippo(new WebApp());
        pippo.start();
    }

}
