package com.salvador.scanners;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 14/02/13
 * Time: 17:36
 *
 */
public class DefaultStepScannerTest {

    DefaultStepScanner scanner;

    @Before
    public void setUp() throws Exception {
        scanner = new DefaultStepScanner();
    }

    @Test
    public void testScan() throws Exception {
        final List<Class> classes = scanner.scan();
        assertThat(classes.get(0).getName(),is("test.steps.TestStep"));
    }
}
