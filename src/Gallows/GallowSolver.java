package Gallows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CommonFunctions {

	@SuppressWarnings("unchecked")
	public static <I> I combine(I... items) {
		for (I item : items)
			if (item != null)
				return item;
		return null;
	}
}

class GallowSolverState {

	public static String code = "patil.prithviraj@gmail.com";

	public enum Status {
		ALIVE, DEAD, FREE
	};

	public Status status;
	public String state;
	public String token;
	public int remaining_guesses;

}

public class GallowSolver {

	String WordFile = "resources/wordLists/";
	String FrequencyFile = "resources/frequencyList/";

	Set<Character> IgnoreSet = new HashSet<Character>();
	Set<Character> AddSet = new HashSet<Character>();
	HashMap<String, Integer> FrequencyMapper;
	HashMap<Integer, List<String>> WordMapper;

	GallowSolver() {

		WordMapper = new HashMap<Integer, List<String>>();

		for (File WordFile : new File(WordFile).listFiles()) {

			BufferedReader in;

			try {

				in = new BufferedReader(new FileReader(WordFile));
				for (String str; (str = in.readLine()) != null;) {

					str = str.trim();
					if (!WordMapper.containsKey(str.length())) {
						WordMapper.put(str.length(), new ArrayList<String>());
					}
					WordMapper.get(str.length()).add(str);

				}

				in.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FrequencyMapper = new HashMap<String, Integer>();

		for (File frequencyFile : new File(FrequencyFile).listFiles()) {
			try {

				BufferedReader in = new BufferedReader(new FileReader(frequencyFile));

				for (String str; (str = in.readLine()) != null;) {

					String[] tokens = str.split("\\s+");

					FrequencyMapper.put(
							tokens[1],
							Integer.parseInt(tokens[0])
									+ CommonFunctions.combine(FrequencyMapper.get(tokens[1]), 0));

				}

				in.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void AppendResult(char ch, boolean result) {
		if (result == true) {
			AddSet.add(ch);
		} else {
			IgnoreSet.add(ch);
		}
	}

	public char SolveNextCharacter(String state) {

		List<String> wordList = new ArrayList<String>(Arrays.asList(state.split("[^a-z_']+")));

		StringBuilder excludeString = new StringBuilder();
		Iterator<Character> it = IgnoreSet.iterator();

		while (it.hasNext())
			excludeString.append(it.next());

		double[] frequencyIndex = new double[26];

		for (String s : wordList) {
			if (!s.contains("_"))
				continue;

			Pattern regex = Pattern.compile(s.replace("_",
					(excludeString.length() > 0) ? String.format("[a-z&&[^%s]]", excludeString)
							: "[a-z]"));

			List<String> candidates = new ArrayList<String>();
			if (WordMapper.containsKey(s.length())) {
				for (String cand : WordMapper.get(s.length())) {
					Matcher m = regex.matcher(cand);
					if (m.find())
						candidates.add(cand);
				}
			}

			double[] currentFrequency = new double[26];
			for (String candidate : candidates) {
				int frequency = (FrequencyMapper.containsKey(candidate)) ? FrequencyMapper
						.get(candidate) : 1;
				for (int i = 0; i < candidate.length(); ++i) {
					int index = candidate.charAt(i) - (int) 'a';
					if (index < 0 || index >= frequencyIndex.length)
						continue;
					currentFrequency[index] += frequency;
				}
			}

			for (int i = 0; i < currentFrequency.length; ++i) {

				currentFrequency[i] /= candidates.size();

				frequencyIndex[i] += currentFrequency[i];
			}
		}

		char maxCharacter = 'a';
		double maxIndex = 0;
		for (int frequencyIterator = 0; frequencyIterator < frequencyIndex.length; frequencyIterator++) {
			if (frequencyIndex[frequencyIterator] > maxIndex) {
				char newc = (char) ((int) 'a' + frequencyIterator);
				if (AddSet.contains(newc) || IgnoreSet.contains(newc))
					continue;
				maxIndex = frequencyIndex[frequencyIterator];
				maxCharacter = newc;
			}
		}

		if (maxIndex == 0) {
			System.out.println("\nPseudo-Random Selection!");
			for (char c = 'a'; c <= 'z'; ++c) {
				if (!(AddSet.contains(c) || IgnoreSet.contains(c))) {
					return c;
				}
			}
		}

		return maxCharacter;
	}

}