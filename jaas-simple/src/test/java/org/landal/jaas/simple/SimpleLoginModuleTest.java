package org.landal.jaas.simple;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;

import org.junit.Test;

public class SimpleLoginModuleTest {

	@Test
	public void testLoginSuccess() throws Exception {
		LoginContext lc = new LoginContext("Sample",
				new SuccessLoginCallbackHandler());
		lc.login();
	}

	@Test(expected = FailedLoginException.class)
	public void testLoginFailed() throws Exception {
		LoginContext lc = new LoginContext("Sample",
				new FailedLoginCallbackHandler());
		lc.login();
	}

}
