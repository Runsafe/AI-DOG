package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;

import java.util.*;
import java.util.regex.Matcher;

public class Compliment extends ChatResponderRule implements IConfigurationChanged
{
	public Compliment(IServer server)
	{
		super("(?i).*(pretty|beautiful|awesome)\\s+dog", null, null, null, null, server);
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		if (!playerHits.containsKey(player))
		{
			playerHits.put(player, 0);
			return String.format("Hello %s, you are a %s!", player, this.createCompliment());
		}

		int hits = playerHits.get(player);

		if (hits == 4)
		{
			playerHits.put(player, 0);
			return String.format("%s, you are a really nice friend but we need to see other people.", player);
		}

		playerHits.put(player, playerHits.get(player) + 1);
		return String.format("Thanks %s, you are a %s.", player, this.createCompliment());
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.complimentTiers.clear();

		Map<String, List<String>> compliments = configuration.getConfigSectionsAsList("compliments");
		List<String> sortOrder = new ArrayList<>(compliments.keySet());
		Collections.sort(sortOrder);

		for (String tier : sortOrder)
			this.complimentTiers.add(compliments.get(tier));
	}

	private String createCompliment()
	{
		StringBuilder insult = new StringBuilder();

		for (List<String> tier : this.complimentTiers)
			insult.append(tier.get(this.random.nextInt(tier.size()))).append(" ");

		return insult.toString().trim();
	}

	private final List<List<String>> complimentTiers = new ArrayList<>();
	private final Random random = new Random();
	private final HashMap<String, Integer> playerHits = new HashMap<>();
}
