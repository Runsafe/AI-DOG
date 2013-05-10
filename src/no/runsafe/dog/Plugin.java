package no.runsafe.dog;

import no.runsafe.dog.cortex.command.ReloadCommand;
import no.runsafe.dog.cortex.command.SpeakCommand;
import no.runsafe.dog.cortex.language.ChatResponder;
import no.runsafe.dog.cortex.language.Speech;
import no.runsafe.dog.cortex.language.queries.Calculate;
import no.runsafe.dog.cortex.language.queries.Seen;
import no.runsafe.dog.cortex.language.queries.Where;
import no.runsafe.dog.cortex.memory.ChatTriggerRepository;
import no.runsafe.dog.cortex.reason.PlayerChecks;
import no.runsafe.dog.cortex.visual.Observer;
import no.runsafe.dog.events.PlayerLogin;
import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.command.Command;

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
		this.addComponent(PlayerLogin.class);

		// Chat responder rules
		this.addComponent(Seen.class);
		this.addComponent(Calculate.class);
		this.addComponent(Where.class);

		Command dogCommand = new Command("dog", "Commands to control DOG", null);
		dogCommand.addSubCommand(getInstance(ReloadCommand.class));
		dogCommand.addSubCommand(getInstance(SpeakCommand.class));
		this.addComponent(dogCommand);
	}
}
