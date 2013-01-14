package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.command.ICommandHandler;
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
			RunsafePlayer who = RunsafeServer.Instance.getPlayer(results.group(2).toLowerCase());
			if (who != null && !(who instanceof RunsafeAmbiguousPlayer))
			{
				ICommandHandler command = getSeenCommand();
				if (command == null)
					return null;
				return command.prepare(RunsafeServer.Instance.getPlayerExact(player), new String[]{who.getName()}).execute();
			}
		}
		return null;
	}

	private ICommandHandler getSeenCommand()
	{
		return RunsafePlugin.getPluginCommand("seen");
	}

	private static final Pattern question = Pattern.compile(".*(has.*seen|have.*seen|when.*was) ([a-z0-9_-]+).*");
}
