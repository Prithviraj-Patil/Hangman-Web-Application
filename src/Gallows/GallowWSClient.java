package Gallows;

import java.io.*;
import java.net.URL;

import com.google.gson.Gson;

public class GallowWSClient {

	public static GallowSolverState initiateGallows() {

		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new InputStreamReader(new URL(
					"http://gallows.hulu.com/play?code=" + GallowSolverState.code).openStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sBuilder = new StringBuilder();
		try {
			String s;
			while ((s = bReader.readLine()) != null) {
				sBuilder.append(s);
			}

			bReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GallowSolverState GallowBot = new Gson().fromJson(sBuilder.toString(),
				GallowSolverState.class);
		if (GallowBot.state != null) {
			GallowBot.state = GallowBot.state.toLowerCase();
		} else {
			System.out.println("\nInvalid token provided.. Exiting..");
			System.exit(0);
		}
		return GallowBot;

	}

	public static GallowSolverState JSONRequest(String token, char ch) {

		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new InputStreamReader(new URL(
					"http://gallows.hulu.com/play?code=" + GallowSolverState.code + "&token="
							+ token + "&guess=" + ch).openStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sBuilder = new StringBuilder();
		try {
			String s;
			while ((s = bReader.readLine()) != null) {
				sBuilder.append(s);
			}

			bReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GallowSolverState GallowBot = new Gson().fromJson(sBuilder.toString(),
				GallowSolverState.class);
		if (GallowBot.state != null) {
			GallowBot.state = GallowBot.state.toLowerCase();
		}
		return GallowBot;
	}
}