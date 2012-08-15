package no.runsafe.dog.cortex.command;

import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.framework.command.RunsafeAsyncCommand;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

public class SpeakCommand extends RunsafeAsyncCommand
{
	public SpeakCommand(Speech speechCenter, IScheduler scheduler)
	{
		super("speak", scheduler);
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
		if (args.length > 0)
		{
			speech.Speak(args[0]);
			return "DOG has been commanded. Please use AI puppeting sparingly.";
		}
		return "Requires 1 argument: message";
	}

	private Speech speech;
}
