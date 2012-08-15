package no.runsafe.dog.cortex.language;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.dog.cortex.memory.ChatTriggerRepository;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.player.IPlayerChatEvent;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.event.player.RunsafePlayerChatEvent;
import no.runsafe.framework.timer.IScheduler;
import no.runsafe.framework.timer.ITimer;
import org.bukkit.ChatColor;

import java.util.Date;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatResponder implements Runnable, Subsystem, IPlayerChatEvent
{
	public ChatResponder(
		IScheduler scheduler,
		RunsafeServer server,
		IConfiguration configuration,
		ChatTriggerRepository repository,
		Speech speechCenter,
		IOutput output
	)
	{
		this.scheduler = scheduler;
		this.console = output;
		this.worker = scheduler.createAsyncTimer(this, 10L, 10L);
		this.config = configuration;
		this.chatTriggerRepository = repository;
		this.speech = speechCenter;
	}

	@Override
	public void reload()
	{
		activeTriggers.clear();
		activeTriggers.putAll(chatTriggerRepository.getRules());
		console.outputColoredToConsole(
			String.format(
				"Successfully loaded %s%d chat responders%s.",
				ChatColor.GREEN, activeTriggers.size(), ChatColor.RESET
			), Level.INFO
		);
		ruleCooldown = config.getConfigValueAsInt("autoresponder.cooldown.rule");
		playerCooldown = config.getConfigValueAsInt("autoresponder.cooldown.player");
		long delay = config.getConfigValueAsInt("autoresponder.delay");
		if (delay > 0)
		{
			if(this.worker != null)
				worker.stop();
			worker = scheduler.createAsyncTimer(this, 10L, delay);
		}
	}

	@Override
	public void OnPlayerChatEvent(RunsafePlayerChatEvent event)
	{
		console.finer(String.format("Receiving message '%s' from %s", event.getMessage(), event.getPlayer().getName()));
		playerLastMessage.put(event.getPlayer().getName(), event.getMessage());
		if (messageQueue.contains(event.getPlayer().getName()))
			messageQueue.remove(event.getPlayer().getName());
		messageQueue.push(event.getPlayer().getName());
		console.finer(String.format("%d messages in queue", messageQueue.size()));
		pokeWorker();
	}

	@Override
	public void run()
	{
		if (messageQueue.empty())
		{
			console.finer("Stopping worker.");
			worker.stop();
			return;
		}

		String player = messageQueue.pop();
		String message = playerLastMessage.get(player);

		console.finer(String.format("Checking message '%s' from '%s'", message, player));

		playerLastMessage.remove(player);
		if (playerCooldown > 0 && playerCooldowns.contains(player))
		{
			console.fine("Player is on cooldown.");
			if (playerCooldowns.get(player) < new Date().getTime())
				playerCooldowns.remove(player);
			else
				return;
		}
		for (Map.Entry<Pattern, String> rule : activeTriggers.entrySet())
		{
			Matcher matcher = rule.getKey().matcher(message);
			if (matcher.matches())
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
				if (playerCooldown > 0)
				{
					playerCooldowns.put(player, new Date().getTime() + (playerCooldown * 1000));
				}
				String response = matcher.replaceAll(rule.getValue().replace("%player%", player));
				console.fine(String.format("Sending response '%s'", response));
				speech.Speak(response);
			}
		}
	}

	private void pokeWorker()
	{
		if (worker == null)
			console.fine("Worker is null");
		else
			console.fine("Worker is " + (worker.isRunning() ? "" : "not ") + "running");
		if (worker != null && !worker.isRunning())
			worker.start();
	}

	private IScheduler scheduler;
	private ChatTriggerRepository chatTriggerRepository;
	private ConcurrentHashMap<String, Long> playerCooldowns = new ConcurrentHashMap<String, Long>();
	private ConcurrentHashMap<Pattern, String> activeTriggers = new ConcurrentHashMap<Pattern, String>();
	private ConcurrentHashMap<String, String> playerLastMessage = new ConcurrentHashMap<String, String>();
	private Stack<String> messageQueue = new Stack<String>();
	private ITimer worker;
	private IConfiguration config;
	private int ruleCooldown;
	private int playerCooldown;
	private Speech speech;
	private IOutput console;
}
