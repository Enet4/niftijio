/*
 */
package niftijio;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import niftijio.niftijio.NiftiHeader;
import niftijio.niftijio.NiftiVolume;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Eduardo Pinho <eduardopinho@ua.pt>
 */
public class NiftiTest {
    
    private static final String TEST_FILE_NAME = "test.nii.gz";
    private InputStream example;
        
    @Before
    public void setUp() throws IOException {
        example = new GZIPInputStream(NiftiTest.class.getResourceAsStream(TEST_FILE_NAME));
    }
    
    @After
    public void tearDown() throws IOException {
        example.close();
    }

    @Test
    public void test1() throws IOException {
        NiftiHeader hdr = NiftiHeader.read(example, TEST_FILE_NAME);
        testHeader(hdr);
    }

    @Test
    public void test2() throws IOException {
        NiftiVolume volume = NiftiVolume.read(example, TEST_FILE_NAME);
        assertNotNull(volume);
        testHeader(volume.header);
        assertNotNull(volume.data);
    }

    @Test
    public void test3() throws IOException {
        // no file name, should still work
        NiftiVolume volume = NiftiVolume.read(example, null);
        assertNotNull(volume);
        testHeader(volume.header);
        assertNotNull(volume.data);
    }
    
    private void testHeader(NiftiHeader hdr) {
        assertNotNull(hdr);
        assertEquals(348, hdr.sizeof_hdr);
        assertTrue(hdr.vox_offset >= 348);
        assertEquals("n+1\0", hdr.magic.toString());
        assertEquals(8, hdr.dim.length);
        for (int i = 0 ; i < hdr.dim.length; i++) {
            assertTrue(hdr.dim[i] >= 0);
        }
    }
}
