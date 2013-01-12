package no.runsafe.dog.cortex.language;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.event.player.RunsafePlayerFakeChatEvent;
import no.runsafe.framework.server.player.RunsafeFakePlayer;
import no.runsafe.framework.server.player.RunsafePlayer;

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
		whisperFormat = configuration.getConfigValueAsString("whisper");
	}

	public void Speak(String message)
	{
		RunsafePlayerFakeChatEvent event = new RunsafePlayerFakeChatEvent(personality, message);
		event.Fire();
		if (!event.getCancelled())
			this.server.broadcastMessage(String.format(event.getFormat(), event.getPlayer().getName(), event.getMessage()));
	}

	public void Whisper(RunsafePlayer player, String message)
	{
		player.sendColouredMessage(whisperFormat, personality.getPrettyName(), message);
	}

	private final RunsafeServer server;
	private RunsafeFakePlayer personality;
	private String whisperFormat;
}
