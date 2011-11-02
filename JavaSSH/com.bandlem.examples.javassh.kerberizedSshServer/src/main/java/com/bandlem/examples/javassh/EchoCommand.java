package com.bandlem.examples.javassh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

/**
 * Takes any input and copies it to the output, byte by byte, flushing after each one.
 * Obviously this isn't performant but is designed to demonstrate the concept.
 */
public class EchoCommand implements Command, Runnable {

	private InputStream in;

	private OutputStream out;

	public void destroy() {
	}

	public void setErrorStream(OutputStream err) {
	}

	public void setExitCallback(ExitCallback callback) {
	}

	public void setInputStream(InputStream in) {
		this.in = in;
	}

	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

	/**
	 * When we start, kick off a thread to copy in to out
	 */
	public void start(Environment env) throws IOException {
		new Thread(this).start();
	}

	/**
	 * Used as a runnable to copy input data from 'in' to 'out'
	 */
	public void run() {
		int read;
		try {
			while ((read = in.read()) != -1) {
				out.write(read);
				out.flush();
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
