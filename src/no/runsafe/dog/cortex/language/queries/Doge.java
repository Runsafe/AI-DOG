package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IServer;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

public class Doge extends ChatResponderRule
{
	public Doge(IServer server)
	{
		super("(?i)(.*)#doge$", null, null, null, null, server);
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		boolean hasWow = false;
		List<String> phraseBin = new ArrayList<>(phrases);
		List<String> responses = new ArrayList<>(0);
		String[] words = message.group(1).trim().toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+");

		for (String word : words)
		{
			if (word.length() > 3 && !excludedWords.contains(word) && !phrases.contains(word))
			{
				String newPhrase = getPhrase(phraseBin);
				if (newPhrase == null) break;

				responses.add(newPhrase + ' ' + word + '.');
			}
			else if (!hasWow)
			{
				hasWow = true;
				responses.add("wow.");
			}
		}

		if (!responses.isEmpty())
			return StringUtils.join(responses, ' ');

		return null;
	}

	private String getPhrase(List<String> phraseBin)
	{
		if (phraseBin.isEmpty())
			return null;

		int randomIndex = random.nextInt(phraseBin.size());
		String phrase = phraseBin.get(randomIndex);

		phraseBin.remove(randomIndex);

		return phrase;
	}

	private final Random random = new Random();
	private final static List<String> phrases = new ArrayList<>();
	private final static List<String> excludedWords = new ArrayList<>(6);

	static
	{
		excludedWords.add("and");
		excludedWords.add("then");
		excludedWords.add("when");
		excludedWords.add("where");
		excludedWords.add("from");
		excludedWords.add("have");
		excludedWords.add("what");
		excludedWords.add("this");
	}

	static
	{
		phrases.add("much");
		phrases.add("very");
		phrases.add("so");
		phrases.add("such");
	}
}
