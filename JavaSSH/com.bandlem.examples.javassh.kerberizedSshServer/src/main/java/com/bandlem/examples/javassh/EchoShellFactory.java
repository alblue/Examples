package com.bandlem.examples.javassh;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;

/**
 * Instantiates and returns an {@see EchoCommand}
 */
public class EchoShellFactory implements Factory<Command> {

	public Command create() {
		return new EchoCommand();
	}

}
