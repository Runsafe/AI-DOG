package no.runsafe.dog.cortex.language;

import no.runsafe.dog.cortex.Subsystem;
import no.runsafe.dog.cortex.memory.ChatTriggerRepository;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.ai.IChatResponseTrigger;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.event.IServerReady;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.log.IDebug;
import no.runsafe.framework.timer.Worker;
import no.runsafe.nchat.channel.IChannelManager;
import no.runsafe.nchat.channel.IChatChannel;
import no.runsafe.nchat.channel.IChatResponder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

public class ChatResponder extends Worker<String, ChatResponder.ChannelMessage> implements Runnable, Subsystem, IChatResponder, IServerReady
{
	public static class ChannelMessage
	{
		IChatChannel channel;
		String message;

		public ChannelMessage(IChatChannel channel, String message)
		{
			this.channel = channel;
			this.message = message;
		}
	}

	public ChatResponder(
		IScheduler scheduler,
		ChatTriggerRepository repository,
		Speech speechCenter,
		IDebug output,
		IConsole console, IChannelManager manager)
	{
		super(scheduler);
		this.scheduler = scheduler;
		this.debugger = output;
		this.chatTriggerRepository = repository;
		this.speech = speechCenter;
		this.console = console;
		this.manager = manager;
		this.setInterval(10);
	}

	@Override
	public void OnServerReady()
	{
		staticResponders.addAll(RunsafePlugin.getPluginAPI(IChatResponseTrigger.class));
		console.logInformation("Added %d static chat responders.", staticResponders.size());
		manager.registerResponderHook(this);
		activeTriggers.addAll(staticResponders);
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

		console.logInformation("Successfully loaded %d chat responders.", activeTriggers.size());

		if (config == null)
			return;
		ruleCooldown = config.getConfigValueAsInt("autoresponder.cooldown.rule");
		playerCooldown = config.getConfigValueAsInt("autoresponder.cooldown.player");
		setInterval(config.getConfigValueAsInt("autoresponder.delay"));
		dogName = config.getConfigValueAsString("name");
	}

	@Override
	public void processChatMessage(final IChatChannel channel, final ICommandExecutor player, final String message)
	{
		if (player.getName().equals(dogName))
			return;
		debugger.debugFiner(String.format("Receiving message '%s' from %s", message, player));
		Push(player.getName(), new ChannelMessage(channel, message));
	}

	@Override
	public void process(String player, ChannelMessage message)
	{
		debugger.debugFiner(String.format("Checking message '%s' from '%s'", message, player));

		if (!isPlayerOffCooldown(player))
			return;

		for (IChatResponseTrigger rule : activeTriggers)
		{
			Matcher matcher = rule.getRule().matcher(message.message);
			if (!matcher.matches())
				continue;
			String response = rule.getResponse(player, matcher);
			if (response == null)
				continue;
			applyRuleCooldown(rule);
			applyPlayerCooldown(player);
			debugger.debugFine(String.format("Sending response '%s'", response));
			speech.Speak(response, message.channel);
			break;
		}
	}

	private boolean isPlayerOffCooldown(String player)
	{
		if (playerCooldown <= 0 || !playerCooldowns.containsKey(player))
			return true;

		if (playerCooldowns.get(player) < new Date().getTime())
		{
			playerCooldowns.remove(player);
			return true;
		}

		debugger.debugFine("Player is on cooldown.");
		return false;
	}

	private void applyRuleCooldown(final IChatResponseTrigger rule)
	{
		if (ruleCooldown > 0)
		{
			debugger.debugFine("Putting rule on cooldown");
			Runnable callback = () ->
			{
				if (!activeTriggers.contains(rule))
					activeTriggers.add(rule);
			};
			activeTriggers.remove(rule);
			scheduler.createAsyncTimer(callback, ruleCooldown);
		}
	}

	private void applyPlayerCooldown(String player)
	{
		if (playerCooldown > 0)
			playerCooldowns.put(player, new Date().getTime() + (playerCooldown * 1000L));
	}

	private final IScheduler scheduler;
	private final ChatTriggerRepository chatTriggerRepository;
	private final ConcurrentHashMap<String, Long> playerCooldowns = new ConcurrentHashMap<>();
	private final ArrayList<IChatResponseTrigger> activeTriggers = new ArrayList<>();
	private final Speech speech;
	private final IDebug debugger;
	private final IConsole console;
	private final List<IChatResponseTrigger> staticResponders = new ArrayList<>();
	private final IChannelManager manager;
	private int ruleCooldown;
	private int playerCooldown;
	private String dogName;
}
