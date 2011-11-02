package com.bandlem.examples.javassh;

import static org.junit.Assert.*;

import org.junit.Test;

public class EchoShellFactoryTest {

	@Test
	public void test() {
		EchoShellFactory echoShellFactory = new EchoShellFactory();
		assertTrue(echoShellFactory.create() instanceof EchoCommand);
	}

}
