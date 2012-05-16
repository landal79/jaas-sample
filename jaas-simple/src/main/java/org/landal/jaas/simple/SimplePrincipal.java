package org.landal.jaas.simple;

import java.security.Principal;

public class SimplePrincipal implements Principal, java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	private String name;

	public SimplePrincipal(String name) {
		if (name == null){
			throw new NullPointerException("illegal null input");
		}

		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return ("SamplePrincipal:  " + name);
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (this == o)
			return true;

		if (!(o instanceof SimplePrincipal))
			return false;
		SimplePrincipal that = (SimplePrincipal) o;

		if (this.getName().equals(that.getName()))
			return true;
		return false;
	}

	public int hashCode() {
		return name.hashCode();
	}

}
