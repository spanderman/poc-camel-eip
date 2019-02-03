package poc.camel.eip.test;

import org.junit.Test;

import poc.camel.eip.MainApp;

/**
 * Test class added ONLY to cover main() invocation not covered by application
 * tests.
 */
public class MainAppIT {

    /**
     * Main method to run the application.
     */
    @Test
    public void main() {
        MainApp.main(new String[] {});
    }
}
