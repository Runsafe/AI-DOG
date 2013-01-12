package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.command.ICommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafeAmbiguousPlayer;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Seen extends ChatResponderRule
{
	public Seen()
	{
		super(null, null, null, null);
	}

	@Override
	public String getResponse(String player, String message)
	{
		Matcher results = question.matcher(message);
		if (results.matches())
		{
			RunsafePlayer who = RunsafeServer.Instance.getPlayer(results.group(2));
			if (who != null && !(who instanceof RunsafeAmbiguousPlayer))
			{
				ICommand command = getSeenCommand();
				if (command == null)
					return null;
				return command.OnExecute(RunsafeServer.Instance.getPlayerExact(player), new String[]{who.getName()});
			}
		}
		return null;
	}

	private ICommand getSeenCommand()
	{
		return RunsafePlugin.getPluginCommand("seen");
	}

	private static final Pattern question = Pattern.compile(".*(has|have|when).*seen ([a-z0-9_-]+)");
}
