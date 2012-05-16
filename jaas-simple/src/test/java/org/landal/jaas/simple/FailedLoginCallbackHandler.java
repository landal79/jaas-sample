package org.landal.jaas.simple;
import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * 
 * 
 *
 */
public class FailedLoginCallbackHandler implements CallbackHandler {

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		if (callbacks[0] instanceof NameCallback) {
			NameCallback nc = (NameCallback) callbacks[0];
			nc.setName("user");
		}

		if (callbacks[1] instanceof PasswordCallback) {		
			PasswordCallback pc = (PasswordCallback) callbacks[1];
			pc.setPassword("wrongPassword".toCharArray());
		}

	}

}
