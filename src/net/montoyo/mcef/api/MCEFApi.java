package net.montoyo.mcef.api;

public class MCEFApi {
	
	/**
	 * Call this to get the API instance.
	 * @return the MCEF API or null if something failed.
	 */
	public static API getAPI() {
		try {
			Class cls = Class.forName("net.montoyo.mcef.MCEF");
			return (API) cls.getField("PROXY").get(null);
		} catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

}
