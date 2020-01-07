package json_Converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PrintWebsiteHTML {

	public static void main (String[] args) {
		
		String websiteText = "";
		String websiteURL = "https://raw.communitydragon.org/latest/cdragon/tft/";
				//"https://raw.communitydragon.org/latest/cdragon/tft/en_us.json";
		String currentLine;
		
		try {			
			URL updateLoc = new URL(websiteURL);
			HttpsURLConnection connect = (HttpsURLConnection) updateLoc.openConnection();
			connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
			InputStream in = connect.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader bufIn = new BufferedReader(reader);
						
			do {
				currentLine = bufIn.readLine();
			    websiteText = websiteText.concat(currentLine + "\n");
			    
			    /*if (currentLine.contains("en_us.json")) {
			    	System.out.println(currentLine.substring(87,104) + "\n\n");
			    }*/
			    
				} while (currentLine != null);  

			} catch (MalformedURLException e) {
					System.out.println("Error connecting to update server (checking version).");
			} catch (IOException e) {
					System.out.println("IO Error.");
			}
		
		System.out.println(websiteText);
		//saveToFile(websiteText);
		
//<tr><td><a href="en_us.json" title="en_us.json">en_us.json</a></td><td>   200K</td><td>2019-Nov-02 03:59</td></tr>
	}
	
	
	private static void saveToFile(String siteText) {//------------------------------------------------------------------------------
		
		String filePath = "C:\\Users\\thema\\Desktop\\TFT Jsons\\Tests\\test1.json";
		
		try {
		FileOutputStream fileOut = new FileOutputStream(filePath);
		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		
		objectOut.writeObject(siteText);
		
		objectOut.close();
		
		}
		catch (IOException e) {
			System.out.println("Error saving to file");
		}
		
	}
}
				
				