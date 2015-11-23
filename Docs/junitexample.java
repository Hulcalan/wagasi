import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.TestCase;


public class TestFailure extends TestCase {

	@Test
	public void testFailure() throws Exception {
		fail();
	}
}
