package no.runsafe.dog.cortex.command;

import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.framework.command.ExecutableCommand;
import no.runsafe.framework.server.ICommandExecutor;

import java.util.HashMap;

public class SpeakCommand extends ExecutableCommand
{
	public SpeakCommand(Speech speechCenter)
	{
		super("speak", "Command DOG to say something clever", "runsafe.dog.speak", "message");
		speech = speechCenter;
		captureTail();
	}

	@Override
	public String OnExecute(ICommandExecutor executor, HashMap<String, String> parameters)
	{
		speech.Speak(parameters.get("message"));
		return "DOG has been commanded. Please use AI puppeteering sparingly.";
	}

	private final Speech speech;

}
