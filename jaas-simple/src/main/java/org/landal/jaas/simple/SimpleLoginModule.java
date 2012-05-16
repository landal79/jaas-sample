package org.landal.jaas.simple;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class SimpleLoginModule implements LoginModule {

	public static final String USERNAME_EXPTECTED = "username";
	public static final char[] PASSWORD_EXPTECTED = { 'p', 'a', 's', 's', 'w',
			'o', 'r', 'd' };

	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<java.lang.String, ?> sharedState;
	private Map<java.lang.String, ?> options;

	private boolean succeeded = false;
	private boolean commitSucceeded = false;
	
	private String username;
	private char[] password;
	
	private SimplePrincipal userPrincipal;

	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<java.lang.String, ?> sharedState,
			Map<java.lang.String, ?> options) {

		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;
	}

	public boolean login() throws LoginException {

		// prompt for a user name and password
		if (callbackHandler == null) {
			throw new LoginException(
					"Error: no CallbackHandler available to garner authentication information from the user");
		}

		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("user name: ");
		callbacks[1] = new PasswordCallback("password: ", false);

		try {

			callbackHandler.handle(callbacks);

			username = extractUsername(callbacks);
			password = extractPassword(callbacks);

		} catch (IOException ioe) {
			throw new LoginException(ioe.getMessage());
		} catch (UnsupportedCallbackException uce) {
			throw new LoginException(
					"Error: "
							+ uce.getCallback().toString()
							+ " not available to garner authentication information from the user");
		}

		// verify the username/password
		if (checkUsername(username) && checkPassword(password)) {
			succeeded = true;
			return true;
		} else {
			succeeded = false;
			username = null;
			Arrays.fill(password, ' ');

			throw new FailedLoginException("Incorrect username/password");
		}
	}

	private char[] extractPassword(Callback[] callbacks) {
		char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();
		if (tmpPassword == null) {
			tmpPassword = new char[0];
		}		
		
		char[] password = Arrays.copyOf(tmpPassword, tmpPassword.length);		
		((PasswordCallback) callbacks[1]).clearPassword();

		return password;
	}

	private String extractUsername(Callback[] callbacks) {
		return ((NameCallback) callbacks[0]).getName();
	}

	private boolean checkUsername(String username) {
		return username.equals(USERNAME_EXPTECTED);
	}

	private boolean checkPassword(char[] password) {
		return Arrays.equals(PASSWORD_EXPTECTED, password);
	}


	public boolean commit() throws LoginException {
		if (succeeded == false) {
			return commitSucceeded = false;
		} else {
			// add a Principal (authenticated identity) to the Subject
			userPrincipal = new SimplePrincipal(username);
			if (!subject.getPrincipals().contains(userPrincipal))
				subject.getPrincipals().add(userPrincipal);

			// in any case, clean out state
			username = null;
			Arrays.fill(password, ' ');
			
			return commitSucceeded = true;
		}
	}

	public boolean abort() throws LoginException {
		if (succeeded == false) {
			return false;
		} else if (succeeded == true && commitSucceeded == false) {
			// login succeeded but overall authentication failed
			succeeded = false;
			username = null;
			Arrays.fill(password, ' ');
			userPrincipal = null;
		} else {
			// overall authentication succeeded and commit succeeded,
			// but someone else's commit failed
			logout();
		}
		return true;
	}

	public boolean logout() throws LoginException {

		subject.getPrincipals().remove(userPrincipal);
		
		
		succeeded = false;
		succeeded = commitSucceeded;
		username = null;
		Arrays.fill(password, ' ');
		userPrincipal = null;
		return true;
	}
	
	
}