package no.runsafe.dog.cortex.command;

import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.framework.command.RunsafeAsyncCommand;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;
import org.apache.commons.lang.StringUtils;

public class SpeakCommand extends RunsafeAsyncCommand
{
	public SpeakCommand(Speech speechCenter, IScheduler scheduler)
	{
		super("speak", scheduler, "message");
		speech = speechCenter;
	}

	@Override
	public String requiredPermission()
	{
		return "runsafe.dog.speak";
	}

	@Override
	public String OnExecute(RunsafePlayer executor, String[] args)
	{
		speech.Speak(StringUtils.join(args, " "));
		return "DOG has been commanded. Please use AI puppeting sparingly.";
	}

	private Speech speech;
}
