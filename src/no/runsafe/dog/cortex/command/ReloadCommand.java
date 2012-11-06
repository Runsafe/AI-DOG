package no.runsafe.dog.cortex.command;

import no.runsafe.dog.DogBrain;
import no.runsafe.dog.Plugin;
import no.runsafe.framework.command.RunsafeAsyncCommand;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

public class ReloadCommand extends RunsafeAsyncCommand
{
	public ReloadCommand(DogBrain dog, IScheduler scheduler)
	{
		super("reload", scheduler);
		this.dog = dog;
	}

	@Override
	public String requiredPermission()
	{
		return "runsafe.dog.reload";
	}

	@Override
	public String OnExecute(RunsafePlayer executor, String[] args)
	{
		dog.OnConfigurationChanged(null);
		return "DOG has been reloaded.";
	}

	private final DogBrain dog;
}
