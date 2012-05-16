package org.landal.jaas.main;


import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.landal.jaas.simple.ConsoleCallbackHandler;

public class SimpleJaasMain {

	public static void main(String[] args) {

		LoginContext lc = null;
		try {
			lc = new LoginContext("Sample", new ConsoleCallbackHandler());
		} catch (LoginException le) {
			System.err
					.println("Cannot create LoginContext. " + le.getMessage());
			System.exit(-1);
		} catch (SecurityException se) {
			System.err
					.println("Cannot create LoginContext. " + se.getMessage());
			System.exit(-1);
		}

		int i;
		for (i = 0; i < 3; i++) {
			try {

				// attempt authentication
				lc.login();

				// if we return with no exception, authentication succeeded
				break;

			} catch (LoginException le) {

				System.err.println("Authentication failed:");
				System.err.println("  " + le.getMessage());
				try {
					Thread.currentThread().sleep(3000);
				} catch (Exception e) {
					
				}

			}
		}

		if (i == 3) {
			System.out.println("Sorry");
			System.exit(-1);
		}

		System.out.println("Authentication succeeded!");

	}
}
