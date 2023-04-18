package jba;

import java.io.FileInputStream;
import java.util.Properties;

public class PrecipitationApp {

	public static void main(String[] args) {
		try {
			Properties appProps = new Properties();
			appProps.load(new FileInputStream("app.properties"));
			Parser parser = new Parser();
			parser.parse(appProps);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
