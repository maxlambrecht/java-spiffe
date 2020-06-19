package io.spiffe.helper.cli;

import io.spiffe.exception.SocketEndpointAddressException;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class RunnerTest {

    @Test
    void test_Main_KeyStorePathIsMissing() throws KeyStoreException, SocketEndpointAddressException, URISyntaxException {
        final Path path = Paths.get(toUri("testdata/cli/missing-keystorepath.conf"));
        try {
            Runner.main("-c", path.toString());
            fail("expected exception: property is missing");
        } catch (IllegalArgumentException e) {
            assertEquals("keyStorePath config is missing", e.getMessage());
        }
    }

    @Test
    void test_Main_KeyStorePassIsMissing() throws KeyStoreException, SocketEndpointAddressException, URISyntaxException {
        final Path path = Paths.get(toUri("testdata/cli/missing-keystorepass.conf"));
        try {
            Runner.main("-c", path.toString());
            fail("expected exception: property is missing");
        } catch (IllegalArgumentException e) {
            assertEquals("keyStorePass config is missing", e.getMessage());
        }
    }

    @Test
    void test_Main_KeyPassIsMissing() throws KeyStoreException, SocketEndpointAddressException, URISyntaxException {
        final Path path = Paths.get(toUri("testdata/cli/missing-keypass.conf"));
        try {
            Runner.main("-c", path.toString());
            fail("expected exception: property is missing");
        } catch (IllegalArgumentException e) {
            assertEquals("keyPass config is missing", e.getMessage());
        }
    }

    @Test
    void test_Main_TrustStorePathIsMissing() throws KeyStoreException, SocketEndpointAddressException, URISyntaxException {
        final Path path = Paths.get(toUri("testdata/cli/missing-truststorepath.conf"));
        try {
            Runner.main("-c", path.toString());
            fail("expected exception: property is missing");
        } catch (IllegalArgumentException e) {
            assertEquals("trustStorePath config is missing", e.getMessage());
        }
    }

    @Test
    void test_Main_TrustStorePassIsMissing() throws KeyStoreException, SocketEndpointAddressException, URISyntaxException {
        final Path path = Paths.get(toUri("testdata/cli/missing-truststorepass.conf"));
        try {
            Runner.main("-c", path.toString());
            fail("expected exception: property is missing");
        } catch (IllegalArgumentException e) {
            assertEquals("trustStorePass config is missing", e.getMessage());
        }
    }

    @Test
    void testGetCliConfigOption_abbreviated() {
        String option = null;
        try {
            option = Runner.getCliConfigOption("-c", "example");
        } catch (ParseException e) {
            fail(e);
        }
        assertEquals("example", option);
    }

    @Test
    void testGetCliConfigOption() {
        String option = null;
        try {
            option = Runner.getCliConfigOption("--config", "example");
        } catch (ParseException e) {
            fail(e);
        }
        assertEquals("example", option);
    }

    @Test
    void testGetCliConfigOption_nonExistent() {
        try {
            Runner.getCliConfigOption("--unknown", "example");
            fail("expected parse exception");
        } catch (ParseException e) {
            assertEquals("Unrecognized option: --unknown", e.getMessage());
        }
    }

    @Test
    void test_ParseConfigFile() throws URISyntaxException, IOException {
        final Path path = Paths.get(toUri("testdata/cli/correct.conf"));
        final Properties properties = Runner.parseConfigFile(path);

        assertEquals("keystore123.p12", properties.getProperty("keyStorePath"));
        assertEquals("example123", properties.getProperty("keyStorePass"));
        assertEquals("pass123", properties.getProperty("keyPass"));
        assertEquals("truststore123.p12", properties.getProperty("trustStorePath"));
        assertEquals("otherpass123", properties.getProperty("trustStorePass"));
        assertEquals("jks", properties.getProperty("keyStoreType"));
        assertEquals("other_alias", properties.getProperty("keyAlias"));
        assertEquals("unix:/tmp/agent.sock", properties.getProperty("spiffeSocketPath"));
    }

    private URI toUri(String path) throws URISyntaxException {
        return getClass().getClassLoader().getResource(path).toURI();
    }
}