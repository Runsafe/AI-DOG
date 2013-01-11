package no.runsafe.dog.cortex.language;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.event.player.RunsafePlayerFakeChatEvent;
import no.runsafe.framework.server.player.RunsafeFakePlayer;

public class Speech implements Subsystem
{
	public Speech(RunsafeServer server)
	{
		this.server = server;
	}

	@Override
	public void reload(IConfiguration configuration)
	{
		personality = new RunsafeFakePlayer(configuration.getConfigValueAsString("name"));
		personality.getGroups().add(configuration.getConfigValueAsString("group"));
		personality.setWorld(RunsafeServer.Instance.getWorld(configuration.getConfigValueAsString("world")));
	}

	public void Speak(String message)
	{
		RunsafePlayerFakeChatEvent event = new RunsafePlayerFakeChatEvent(personality, message);
		event.Fire();
		if (!event.getCancelled())
			this.server.broadcastMessage(String.format(event.getFormat(), event.getPlayer().getName(), event.getMessage()));
	}


	private final RunsafeServer server;
	private RunsafeFakePlayer personality;

}
