package no.runsafe.dog.cortex.language;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerFakeChatEvent;
import no.runsafe.framework.minecraft.player.RunsafeFakePlayer;

public class Speech implements Subsystem
{
	@Override
	public void reload(IConfiguration configuration)
	{
		if (configuration == null)
			return;
		personality = new RunsafeFakePlayer(
			configuration.getConfigValueAsString("name"),
			configuration.getConfigValueAsString("group")
		);
		personality.setWorld(RunsafeServer.Instance.getWorld(configuration.getConfigValueAsString("world")));
		whisperFormat = configuration.getConfigValueAsString("whisper");
	}

	public void Speak(String message)
	{
		RunsafePlayerFakeChatEvent.Broadcast(personality, message);
	}

	public void Whisper(IPlayer player, String message)
	{
		player.sendColouredMessage(whisperFormat, personality.getPrettyName(), message);
	}

	private RunsafeFakePlayer personality;
	private String whisperFormat;
}
