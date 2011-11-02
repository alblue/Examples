package com.bandlem.examples.javassh;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class EchoCommandTest {

	@Test
	public void testEchoCommand() throws IOException, InterruptedException {
		EchoCommand cmd = new EchoCommand();
		ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { 'a','b','c' });
		ByteArrayOutputStream baos = new ByteArrayOutputStream(3);
		cmd.setInputStream(bais);
		cmd.setOutputStream(baos);
		cmd.start(null);
		Thread.sleep(100);
		assertEquals(0,bais.available());
		assertEquals(3,baos.size());
		byte[] ba = baos.toByteArray();
		assertEquals('a',ba[0]);
		assertEquals('b',ba[1]);
		assertEquals('c',ba[2]);
	}
}
