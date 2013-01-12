package no.runsafe.dog.cortex.language;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.dog.cortex.memory.ChatTriggerRepository;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.player.IPlayerChatEvent;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.event.player.RunsafePlayerChatEvent;
import no.runsafe.framework.timer.IScheduler;
import no.runsafe.framework.timer.Worker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ChatResponder extends Worker<String, String> implements Runnable, Subsystem, IPlayerChatEvent
{
	public ChatResponder(
		IScheduler scheduler,
		ChatTriggerRepository repository,
		Speech speechCenter,
		IOutput output,
		ChatResponderRule[] responders
	)
	{
		super(scheduler);
		this.scheduler = scheduler;
		this.console = output;
		this.chatTriggerRepository = repository;
		this.speech = speechCenter;
		this.staticResponders = responders;
	}

	@Override
	public void reload(IConfiguration config)
	{
		List<ChatResponderRule> rules = chatTriggerRepository.getRules();
		if (rules != null)
		{
			activeTriggers.clear();
			activeTriggers.addAll(rules);
			Collections.addAll(activeTriggers, staticResponders);
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
			for (ChatResponderRule rule : activeTriggers.toArray(new ChatResponderRule[activeTriggers.size()]))
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

	private void applyRuleCooldown(final ChatResponderRule rule)
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
	private final ArrayList<ChatResponderRule> activeTriggers = new ArrayList<ChatResponderRule>();
	private int ruleCooldown;
	private int playerCooldown;
	private final Speech speech;
	private final IOutput console;
	private final ChatResponderRule[] staticResponders;
	private String dogName;
}
