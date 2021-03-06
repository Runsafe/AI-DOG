package no.runsafe.dog.cortex.language;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.player.RunsafeFakePlayer;
import no.runsafe.nchat.channel.IChatChannel;

public class Speech implements Subsystem
{
	public Speech(IServer server)
	{
		this.server = server;
	}

	@Override
	public void reload(IConfiguration configuration)
	{
		if (configuration == null)
			return;
		personality = new RunsafeFakePlayer(
			configuration.getConfigValueAsString("name"),
			configuration.getConfigValueAsString("group")
		);
		personality.setWorld(server.getWorld(configuration.getConfigValueAsString("world")));
		whisperFormat = configuration.getConfigValueAsString("whisper");
	}

	public void Speak(String message, IChatChannel channel)
	{
		channel.Send(new DogChatResponseEvent(personality, message));
	}

	public void Speak(String message)
	{
		new DogChatResponseEvent(personality, message).Fire();
	}

	public void Whisper(IPlayer player, String message)
	{
		player.sendColouredMessage(whisperFormat, personality.getPrettyName(), message);
	}

	private final IServer server;
	private RunsafeFakePlayer personality;
	private String whisperFormat;
}
