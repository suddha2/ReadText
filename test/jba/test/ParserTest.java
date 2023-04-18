package jba.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import jba.GridHeader;
import jba.Parser;

public class ParserTest {

	@Test
	public void testParse() {
		fail("Not yet implemented");
	}
	@Test
	public void testProcessDataRow() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class[] params = new Class[3];
		params[0] = String.class;
		params[1] = GridHeader.class;
		params[2] = Integer.class ;
		
		Class<Parser> cl = Parser.class;
		Method method = cl.getDeclaredMethod("processDataRow", params);
		method.setAccessible(true);
		
		assertEquals(74, method.invoke(null, 37));
	}

}
