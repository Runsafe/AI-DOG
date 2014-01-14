package no.runsafe.dog.cortex.command;

import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.TrailingArgument;

public class SpeakCommand extends ExecutableCommand
{
	public SpeakCommand(Speech speechCenter)
	{
		super("speak", "Command DOG to say something clever", "runsafe.dog.speak", new TrailingArgument("message"));
		speech = speechCenter;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		speech.Speak(parameters.get("message"));
		return "DOG has been commanded. Please use AI puppeteering sparingly.";
	}

	private final Speech speech;

}
