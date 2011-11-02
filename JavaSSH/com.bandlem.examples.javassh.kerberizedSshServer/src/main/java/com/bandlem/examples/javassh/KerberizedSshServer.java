package com.bandlem.examples.javassh;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.UserAuth;
import org.apache.sshd.server.auth.gss.GSSAuthenticator;
import org.apache.sshd.server.auth.gss.UserAuthGSS;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates the use of a Kerberised SSH Server with Apache Mina SSHD
 */
public class KerberizedSshServer {
	
	private Logger logger = LoggerFactory.getLogger(KerberizedSshServer.class);

	private SshServer server;

	public KerberizedSshServer(int port, String keytab, String principalName) throws UnknownHostException {
		this.server = SshServer.setUpDefaultServer();
		getServer().setPort(port);
		if (principalName == null) {
			principalName = "host/"
					+ InetAddress.getLocalHost().getCanonicalHostName();
			logger.debug("No principal specified; using " + principalName);
		}
		
		// The GSS Authenticator is used to authenticate against Kerberized
		// credentials
		GSSAuthenticator authenticator = new GSSAuthenticator();
		authenticator.setKeytabFile(keytab);
		authenticator.setServicePrincipalName(principalName);
		getServer().setGSSAuthenticator(authenticator);

		List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>(
				1);
		userAuthFactories.add(new UserAuthGSS.Factory());
		getServer().setUserAuthFactories(userAuthFactories);

		// You can also use the no-arg SimpleGeneratorHostKeyProvider, which
		// will generate
		// a new host key each time the server is started
		String hostKey = System.getProperty("java.io.tmpdir")
				+ "/KerberosSSHServer.hostKey";
		getServer().setKeyPairProvider(new SimpleGeneratorHostKeyProvider(hostKey));
		// server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());

		// Demonstrate an echo shell
		getServer().setShellFactory(new EchoShellFactory());
	}

	public void start() throws IOException {
		getServer().start();
	}

	/**
	 * Usage: java ... KerberizedSshServer portNumber /path/to/keytab
	 * [principal]
	 * 
	 * @param args
	 *            the port number to use, followed by the keytab needed for
	 *            Kerberised authentication, followed by the Kerberos principal
	 *            for the server to use (otherwise defaults to
	 *            host/canonical.host.name)
	 * @throws IOException
	 *             if there is an exception during IO at startup
	 * @throws NumberFormatException
	 *             if the port cannot be parsed
	 */
	public static void main(String args[]) throws NumberFormatException,
			IOException {
		if (args.length == 2) {
			new KerberizedSshServer(Integer.parseInt(args[0]), args[1], null)
					.start();
		} else if (args.length == 3) {
			new KerberizedSshServer(Integer.parseInt(args[0]), args[1], args[2])
					.start();
		} else {
			System.err
					.println("Usage: java KerberosSSHServer portNumber /path/to/keytab [principal]");
			System.err.println("");
			System.err
					.println("The keytab must have the Kerberos principal for the current host, e.g.");
			System.err.println("");
			System.err.println("    host/"
					+ InetAddress.getLocalHost().getHostName());
			System.err.println("    host/"
					+ InetAddress.getLocalHost().getCanonicalHostName());
			System.err.println("");
			System.err
					.println("Note that depending on how SSH is configured, either the connected host name");
			System.err
					.println("will be used, or the canonical host name will be used. You can specify the");
			System.err
					.println("principal to be used with the 3rd argument on the command line.");
		}
	}

	SshServer getServer() {
		return server;
	}
}
