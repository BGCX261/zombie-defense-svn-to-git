package com.tfu.dg.framework;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TrixelLoader {
	public static Trixel loadTrix(InputStream in) {
		String line = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuffer b = new StringBuffer();
			String l = reader.readLine();
			while(l != null) {
				b.append(l);
				b.append("\n");
				l = reader.readLine();
			}
			line = b.toString();
			reader.close();
		} catch (Exception ex) {
			return null;
		}
		
		return null;
	}
}
