package no.runsafe.dog.cortex.command;

import no.runsafe.dog.DogBrain;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.internal.command.AsyncCommand;

import java.util.HashMap;

public class ReloadCommand extends AsyncCommand
{
	public ReloadCommand(DogBrain dog, IScheduler scheduler)
	{
		super("reload", "Triggers a reload of DOG responders.", "runsafe.dog.reload", scheduler);
		this.dog = dog;
	}

	@Override
	public String OnAsyncExecute(ICommandExecutor executor, HashMap<String, String> parameters)
	{
		dog.OnConfigurationChanged(null);
		return "DOG has been reloaded.";
	}

	private final DogBrain dog;
}
