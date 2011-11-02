package com.bandlem.examples.javassh;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.auth.gss.GSSAuthenticator;
import org.junit.Test;

public class KerberizedSshServerTest {

	@Test
	public void testServerNoPrincipal() throws UnknownHostException,
			SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		KerberizedSshServer server = new KerberizedSshServer(1234, "aFile",
				null);
		SshServer sshServer = server.getServer();
		GSSAuthenticator authenticator = sshServer.getGSSAuthenticator();
		assertNotNull(authenticator);
		String principal = (String) getField(authenticator,
				"servicePrincipalName");
		assertEquals(principal, "host/"
				+ InetAddress.getLocalHost().getCanonicalHostName());
		assertEquals(1234,sshServer.getPort());

	}

	@Test
	public void testServerWithPrincipal() throws UnknownHostException {
		KerberizedSshServer server = new KerberizedSshServer(1235, "aFile",
				"host/localhost");
		SshServer sshServer = server.getServer();
		GSSAuthenticator authenticator = sshServer.getGSSAuthenticator();
		assertNotNull(authenticator);
		String principal = (String) getField(authenticator,
				"servicePrincipalName");
		assertEquals(principal, "host/localhost");
		assertEquals(1235,sshServer.getPort());
	}

	private Object getField(Object object, String field) {
		try {
			Field f = object.getClass().getDeclaredField(field);
			f.setAccessible(true);
			return f.get(object);
		} catch (Exception e) {
			return null;
		}
	}

}
