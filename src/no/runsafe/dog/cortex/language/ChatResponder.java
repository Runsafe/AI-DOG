package no.runsafe.dog.cortex.language;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.dog.cortex.memory.ChatTriggerRepository;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.ai.IChatResponseTrigger;
import no.runsafe.framework.api.event.player.IPlayerChatEvent;
import no.runsafe.framework.api.event.plugin.IPluginEnabled;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerChatEvent;
import no.runsafe.framework.timer.Worker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ChatResponder extends Worker<String, String> implements Runnable, Subsystem, IPlayerChatEvent, IPluginEnabled
{
	public ChatResponder(
		IScheduler scheduler,
		ChatTriggerRepository repository,
		Speech speechCenter,
		IOutput output
	)
	{
		super(scheduler);
		this.scheduler = scheduler;
		this.console = output;
		this.chatTriggerRepository = repository;
		this.speech = speechCenter;
	}

	@Override
	public void OnPluginEnabled()
	{
		staticResponders.addAll(RunsafePlugin.getPluginAPI(IChatResponseTrigger.class));
	}

	@Override
	public void reload(IConfiguration config)
	{
		List<ChatResponderRule> rules = chatTriggerRepository.getRules();
		if (rules != null)
		{
			activeTriggers.clear();
			activeTriggers.addAll(rules);
			activeTriggers.addAll(staticResponders);
		}
		console.writeColoured(
			"Successfully loaded &a%d chat responders&r.",
			activeTriggers.size()
		);
		if (config == null)
			return;
		ruleCooldown = config.getConfigValueAsInt("autoresponder.cooldown.rule");
		playerCooldown = config.getConfigValueAsInt("autoresponder.cooldown.player");
		setInterval(config.getConfigValueAsInt("autoresponder.delay"));
		dogName = config.getConfigValueAsString("name");
	}

	@Override
	public void OnPlayerChatEvent(RunsafePlayerChatEvent event)
	{
		if (event.getPlayer().getName().equals(dogName))
			return;
		console.finer(String.format("Receiving message '%s' from %s", event.getMessage(), event.getPlayer().getName()));
		Push(event.getPlayer().getName(), event.getMessage());
	}

	@Override
	public void process(String player, String message)
	{
		console.finer(String.format("Checking message '%s' from '%s'", message, player));

		if (isPlayerOffCooldown(player))
			for (IChatResponseTrigger rule : activeTriggers)
			{
				String response = rule.getResponse(player, message);
				if (response != null)
				{
					applyRuleCooldown(rule);
					applyPlayerCooldown(player);
					console.fine(String.format("Sending response '%s'", response));
					speech.Speak(response);
					break;
				}
			}
	}

	private boolean isPlayerOffCooldown(String player)
	{
		if (playerCooldown > 0 && playerCooldowns.containsKey(player))
		{
			console.fine("Player is on cooldown.");
			if (playerCooldowns.get(player) < new Date().getTime())
				playerCooldowns.remove(player);
			else
				return false;
		}
		return true;
	}

	private void applyRuleCooldown(final IChatResponseTrigger rule)
	{
		if (ruleCooldown > 0)
		{
			console.fine("Putting rule on cooldown");
			Runnable callback = new Runnable()
			{
				@Override
				public void run()
				{
					activeTriggers.add(rule);
				}
			};
			activeTriggers.remove(rule);
			scheduler.createAsyncTimer(callback, ruleCooldown);
		}
	}

	private void applyPlayerCooldown(String player)
	{
		if (playerCooldown > 0)
			playerCooldowns.put(player, new Date().getTime() + (playerCooldown * 1000));
	}

	private final IScheduler scheduler;
	private final ChatTriggerRepository chatTriggerRepository;
	private final ConcurrentHashMap<String, Long> playerCooldowns = new ConcurrentHashMap<String, Long>();
	private final ArrayList<IChatResponseTrigger> activeTriggers = new ArrayList<IChatResponseTrigger>();
	private final Speech speech;
	private final IOutput console;
	private final List<IChatResponseTrigger> staticResponders = new ArrayList<IChatResponseTrigger>();
	private int ruleCooldown;
	private int playerCooldown;
	private String dogName;
}
