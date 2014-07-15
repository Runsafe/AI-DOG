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
import no.runsafe.framework.features.Database;
import no.runsafe.framework.features.Events;

public class DOGPlugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void pluginSetup()
	{
		// Framework features
		addComponent(Commands.class);
		addComponent(Events.class);
		addComponent(Database.class);

		// Plugin components
		addComponent(DogBrain.class);
		addComponent(ChatTriggerRepository.class);
		addComponent(Speech.class);
		addComponent(ChatResponder.class);
		addComponent(PlayerChecks.class);
		addComponent(Observer.class);
		addComponent(PlayerLogin.class);

		// Chat responder rules
		addComponent(Seen.class);
		addComponent(Time.class);
		addComponent(Where.class);
		addComponent(Insult.class);
		addComponent(Compliment.class);
		addComponent(Doge.class);
		addComponent(Clan.class);
		addComponent(Calculate.class);

		// Commands
		Command dogCommand = new Command("dog", "Commands to control DOG", null);
		dogCommand.addSubCommand(getInstance(ReloadCommand.class));
		dogCommand.addSubCommand(getInstance(SpeakCommand.class));
		addComponent(dogCommand);
	}
}
