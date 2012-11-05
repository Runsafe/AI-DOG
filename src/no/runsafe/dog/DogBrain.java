package no.runsafe.dog;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.IPluginEnabled;

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

	private Subsystem[] subsystems;
}
