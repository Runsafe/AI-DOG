package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;

import java.util.*;
import java.util.regex.Matcher;

public class Insult extends ChatResponderRule implements IConfigurationChanged
{
	public Insult(IServer server)
	{
		super("(?i).*(darn|fuck|screw|damn)\\s(you\\s|off\\s|)dog.*", null, null, null, null, server);
	}

	@Override
	public String getResponse(String player, Matcher message)
	{
		if (!playerHits.containsKey(player))
		{
			playerHits.put(player, 0);
			return String.format("Well, this is out of the blue! You are just a %s, %s.", createInsult(), player);
		}
		else
		{
			int hits = playerHits.get(player);

			if (hits == 4)
			{
				playerHits.put(player, 0);
				return String.format("Getting real tired of your shit, %s.", player);
			}
			else
			{
				playerHits.put(player, playerHits.get(player) + 1);
				return String.format("Well, %s, you are just a %s.", player, createInsult());
			}
		}
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.insultTiers.clear();

		List<String> sortOrder = new ArrayList<String>();
		Map<String, List<String>> insults = configuration.getConfigSectionsAsList("insults");

		sortOrder.addAll(insults.keySet());
		Collections.sort(sortOrder);

		for (String tier : sortOrder)
			this.insultTiers.add(insults.get(tier));
	}

	private String createInsult()
	{
		StringBuilder insult = new StringBuilder();

		for (List<String> tier : this.insultTiers)
			insult.append(tier.get(this.random.nextInt(tier.size()))).append(" ");

		return insult.toString().trim();
	}

	private final List<List<String>> insultTiers = new ArrayList<List<String>>();
	private final Random random = new Random();
	private final HashMap<String, Integer> playerHits = new HashMap<String, Integer>();
}
