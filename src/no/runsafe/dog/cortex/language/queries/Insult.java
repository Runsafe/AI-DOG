package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Insult extends ChatResponderRule implements IConfigurationChanged
{
	public Insult()
	{
		super(null, null, null, null);
	}

	@Override
	public String getResponse(String player, String message)
	{
		Matcher results = question.matcher(message);

		if (results.matches())
			return String.format("%s, you're nothing but a %s.", player, this.createInsult());

		return null;
	}

	private String createInsult()
	{
		StringBuilder insult = new StringBuilder();

		for (List<String> tier : this.insultTiers)
			insult.append(tier.get(this.random.nextInt(tier.size()))).append(" ");

		return insult.toString().trim();
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.insultTiers.clear();
		Map<String, List<String>> insults = configuration.getConfigSectionsAsList("insults");
		for (List<String> insultTier : insults.values())
			this.insultTiers.add(0, insultTier);
	}

	private static final Pattern question = Pattern.compile("(?i).*(darn|fuck|screw|damn)\\s(you\\s|off\\s|)dog.*");
	private List<List<String>> insultTiers = new ArrayList<List<String>>();
	private Random random = new Random();
}
