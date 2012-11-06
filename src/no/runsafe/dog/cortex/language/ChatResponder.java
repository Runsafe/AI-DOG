package no.runsafe.dog.cortex.language;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.dog.cortex.memory.ChatTriggerRepository;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.player.IPlayerChatEvent;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.event.player.RunsafePlayerChatEvent;
import no.runsafe.framework.timer.IScheduler;
import no.runsafe.framework.timer.Worker;
import org.bukkit.ChatColor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatResponder extends Worker<String, String> implements Runnable, Subsystem, IPlayerChatEvent
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
	public void reload(IConfiguration config)
	{
		HashMap<Pattern, String> rules = chatTriggerRepository.getRules();
		if (rules != null)
		{
			activeTriggers.clear();
			activeTriggers.putAll(rules);
		}
		console.outputColoredToConsole(
			String.format(
				"Successfully loaded %s%d chat responders%s.",
				ChatColor.GREEN, activeTriggers.size(), ChatColor.RESET
			), Level.INFO
		);
		if(config == null)
			return;
		ruleCooldown = config.getConfigValueAsInt("autoresponder.cooldown.rule");
		playerCooldown = config.getConfigValueAsInt("autoresponder.cooldown.player");
		setInterval(config.getConfigValueAsInt("autoresponder.delay"));
	}

	@Override
	public void OnPlayerChatEvent(RunsafePlayerChatEvent event)
	{
		console.finer(String.format("Receiving message '%s' from %s", event.getMessage(), event.getPlayer().getName()));
		Push(event.getPlayer().getName(), event.getMessage());
	}

	@Override
	public void process(String player, String message)
	{
		console.finer(String.format("Checking message '%s' from '%s'", message, player));

		if (!isPlayerCooldown(player))
			for (Map.Entry<Pattern, String> rule : activeTriggers.entrySet())
			{
				Matcher matcher = rule.getKey().matcher(message);
				if (matcher.matches())
				{
					applyRuleCooldown(rule);
					applyPlayerCooldown(player);
					String response = matcher.replaceAll(rule.getValue().replace("%player%", player));
					console.fine(String.format("Sending response '%s'", response));
					speech.Speak(response);
					break;
				}
			}
	}

	private boolean isPlayerCooldown(String player)
	{
		if (playerCooldown > 0 && playerCooldowns.containsKey(player))
		{
			console.fine("Player is on cooldown.");
			if (playerCooldowns.get(player) < new Date().getTime())
				playerCooldowns.remove(player);
			else
				return true;
		}
		return false;
	}

	private void applyRuleCooldown(Map.Entry<Pattern, String> rule)
	{
		if (ruleCooldown > 0)
		{
			console.fine("Putting rule on cooldown");
			final Pattern pattern = rule.getKey();
			final String response = rule.getValue();
			Runnable callback = new Runnable()
			{
				@Override
				public void run()
				{
					activeTriggers.put(pattern, response);
				}
			};
			activeTriggers.remove(rule.getKey());
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
	private final HashMap<String, Long> playerCooldowns = new HashMap<String, Long>();
	private final HashMap<Pattern, String> activeTriggers = new HashMap<Pattern, String>();
	private int ruleCooldown;
	private int playerCooldown;
	private final Speech speech;
	private final IOutput console;
}
