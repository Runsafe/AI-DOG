package no.runsafe.dog.cortex.language.queries;

import no.runsafe.dog.cortex.language.ChatResponderRule;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.api.command.ICommandHandler;
import no.runsafe.framework.api.command.IPreparedCommand;
import no.runsafe.framework.internal.command.prepared.PreparedAsynchronousCommand;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafeAmbiguousPlayer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

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

				IPreparedCommand seen = command.prepare(RunsafeServer.Instance.getPlayerExact(player), new String[]{who.getName()});
				if (seen instanceof PreparedAsynchronousCommand)
					return ((PreparedAsynchronousCommand) seen).executeDirect();
				else
					return seen.execute();
			}
		}
		return null;
	}

	private ICommandHandler getSeenCommand()
	{
		return RunsafePlugin.getPluginCommand("seen");
	}

	private static final Pattern question = Pattern.compile("(?i).*(has.*seen|have.*seen|when.*was) ([a-zA-Z0-9_-]+).*");
}
