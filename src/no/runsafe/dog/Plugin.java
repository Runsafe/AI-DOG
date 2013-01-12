package no.runsafe.dog;

import no.runsafe.dog.cortex.command.ReloadCommand;
import no.runsafe.dog.cortex.command.SpeakCommand;
import no.runsafe.dog.cortex.language.ChatResponder;
import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.dog.cortex.memory.ChatTriggerRepository;
import no.runsafe.dog.cortex.reason.PlayerChecks;
import no.runsafe.dog.cortex.visual.Observer;
import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.command.ICommand;
import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.configuration.IConfigurationFile;

import java.io.InputStream;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		this.addComponent(DogBrain.class);
		this.addComponent(ChatTriggerRepository.class);
		this.addComponent(Speech.class);
		this.addComponent(ChatResponder.class);
		this.addComponent(PlayerChecks.class);
		this.addComponent(Observer.class);

		ICommand dogCommand = new RunsafeCommand("dog");
		dogCommand.addSubCommand(getInstance(ReloadCommand.class));
		dogCommand.addSubCommand(getInstance(SpeakCommand.class));
		this.addComponent(dogCommand);
	}
}
