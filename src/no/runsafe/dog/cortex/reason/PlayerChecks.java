package no.runsafe.dog.cortex.reason;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.player.IPlayer;

import java.util.List;

public class PlayerChecks implements Subsystem
{
	public boolean isGuest(IPlayer player)
	{
		List<String> groups = player.getGroups();
		return groups.isEmpty() || (groups.size() == 1 && groups.contains("default"));
	}

	@Override
	public void reload(IConfiguration configuration)
	{
	}
}
