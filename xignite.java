import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 

 /** 
 * The Xignite class implements an application that
 * makes a rest call to 
 * xigniteGlobalCurrencies ->  ListCurrencies.
 * It receives Json data from the service.
 * It then parses the data and displays it to console.
 **
 * Documentation for xigniteGlobalCurrencies:
 * https://www.xignite.com/product/forex/api/ListCurrencies/
 **
 * Make sure to include Json library (provided here) in classpath.
 **
 * Put your API token into the api token field in config.properties file
 **/

public class xignite {
 
	public static void main(String[] args) {

		JSONParser parser = new JSONParser();
 
		try {
		  	Properties configFile = new Properties();
		  	InputStream input = new FileInputStream("config.properties");
			configFile.load(input);

	 		String api_token = configFile.getProperty("API_TOKEN");
	 		System.out.println(api_token);

			URL url = new URL("http://globalcurrencies.xignite.com/xGlobalCurrencies.json/ListCurrencies?_token="+ api_token);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
	 
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error: "
						+ conn.getResponseCode() + " " + conn.getResponseString());
			}
	 
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
	 
			String output="";
			String fuls = "";
			System.out.println("Output from Server .... \n");
			
			while ((output = br.readLine()) != null) {
				fuls = fuls + output;
			}

			
	 		Object obj = parser.parse(fuls); 
	 		JSONObject jsonObject = (JSONObject) obj; // parse entire json response into JSONObject
	 		JSONArray currencies = (JSONArray) jsonObject.get("CurrencyList"); // get the currencies list
	 		
	 		int j = 0;

			while (j < currencies.size()) {
				System.out.println("\n next:");
				JSONObject currency = (JSONObject) currencies.get(j);
				String name = (String) currency.get("Name");
				String symbol = (String) currency.get("Symbol");

				System.out.println(name);
				System.out.println(symbol);

				j++;
			}
	 
		  } finally {

		  	conn.disconnect();

		  } catch (MalformedURLException e) {
	 
			e.printStackTrace();
	 
		  } catch (IOException e) {
	 
			e.printStackTrace();
	 
		  } catch (ParseException e) {
			e.printStackTrace();
		}
 
	}
 
}