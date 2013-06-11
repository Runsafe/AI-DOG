package no.runsafe.dog;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;

public class DogBrain implements IConfigurationChanged
{
	public DogBrain(Subsystem[] subsystems)
	{
		this.subsystems = subsystems;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		for (Subsystem system : subsystems)
			system.reload(configuration);
	}

	private final Subsystem[] subsystems;
}
