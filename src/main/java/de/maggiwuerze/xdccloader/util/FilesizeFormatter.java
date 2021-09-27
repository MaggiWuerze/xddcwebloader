package de.maggiwuerze.xdccloader.util;

import java.text.DecimalFormat;

public class FilesizeFormatter {

	public static String createAutoReadableString(long sizeInBytes) {

		String hrSize;

		double b = sizeInBytes;
		double k = sizeInBytes / 1024.0;
		double m = ((sizeInBytes / 1024.0) / 1024.0);
		double g = (((sizeInBytes / 1024.0) / 1024.0) / 1024.0);
		double t = ((((sizeInBytes / 1024.0) / 1024.0) / 1024.0) / 1024.0);

		DecimalFormat dec = new DecimalFormat("0.00");

		if (t > 1) {
			hrSize = dec.format(t).concat(" TB");
		} else if (g > 1) {
			hrSize = dec.format(g).concat(" GB");
		} else if (m > 1) {
			hrSize = dec.format(m).concat(" MB");
		} else if (k > 1) {
			hrSize = dec.format(k).concat(" KB");
		} else {
			hrSize = dec.format(b).concat(" Bytes");
		}

		return hrSize;
	}
}
