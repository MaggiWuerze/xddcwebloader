package de.maggiwuerze.xdccloader.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class IpHelper {

	public static InetAddress getPublicIp() {
		String urlString = "http://checkip.amazonaws.com/";
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
			return InetAddress.getByName(br.readLine());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
