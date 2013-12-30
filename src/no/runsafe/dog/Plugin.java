package no.runsafe.dog;

import no.runsafe.dog.cortex.command.ReloadCommand;
import no.runsafe.dog.cortex.command.SpeakCommand;
import no.runsafe.dog.cortex.language.ChatResponder;
import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.dog.cortex.language.queries.*;
import no.runsafe.dog.cortex.memory.ChatTriggerRepository;
import no.runsafe.dog.cortex.reason.PlayerChecks;
import no.runsafe.dog.cortex.visual.Observer;
import no.runsafe.dog.events.PlayerLogin;
import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.api.command.Command;
import no.runsafe.framework.features.Commands;
import no.runsafe.framework.features.Events;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void pluginSetup()
	{
		// Framework features
		addComponent(Commands.class);
		addComponent(Events.class);

		// Plugin components
		this.addComponent(DogBrain.class);
		this.addComponent(ChatTriggerRepository.class);
		this.addComponent(Speech.class);
		this.addComponent(ChatResponder.class);
		this.addComponent(PlayerChecks.class);
		this.addComponent(Observer.class);
		this.addComponent(PlayerLogin.class);

		// Chat responder rules
		this.addComponent(Seen.class);
		this.addComponent(Calculate.class);
		this.addComponent(Where.class);
		this.addComponent(Insult.class);
		this.addComponent(Compliment.class);
		this.addComponent(Doge.class);

		// Commands
		Command dogCommand = new Command("dog", "Commands to control DOG", null);
		dogCommand.addSubCommand(getInstance(ReloadCommand.class));
		dogCommand.addSubCommand(getInstance(SpeakCommand.class));
		this.addComponent(dogCommand);
	}
}
