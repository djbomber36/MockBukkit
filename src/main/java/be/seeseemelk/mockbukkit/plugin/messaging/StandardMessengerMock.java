package be.seeseemelk.mockbukkit.plugin.messaging;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class StandardMessengerMock extends StandardMessenger {

	// A list of messages sent, in a working environment, to bungeecord
	private final Queue<PluginMessage> pluginMessages = new LinkedTransferQueue<>();

	public StandardMessengerMock() {

	}

	/**
	 * Add plugin message.
	 *
	 * @param pluginMessage Plugin message to add
	 */
	public void addPluginMessage(PluginMessage pluginMessage) {
		pluginMessages.add(pluginMessage);
	}

	/**
	 * Returns next available plugin message
	 *
	 * @return Next available plugin message
	 */
	public PluginMessage nextPluginMessage() {
		return pluginMessages.poll();
	}

	/**
	 * Asserts that a specific plugin message was received by the specified player with the specified message and channel
	 * @param expectedPlayer The player UUID that should have received the plugin message.
	 * @param expectedChannel The channel that should have been sent over the plugin message.
	 * @param expectedMessage The message that should have been received by the player.
	 */
	public void assertSent(@NotNull UUID expectedPlayer, @NotNull String expectedChannel, byte[] expectedMessage) {
		PluginMessage pluginMessage = nextPluginMessage();
		if (pluginMessage == null)
		{
			fail("No more plugin messages were sent");
		}
		else
		{
			String verifiedChannel = StandardMessenger.validateAndCorrectChannel(expectedChannel);
			assertEquals(expectedPlayer, pluginMessage.getPlayerMock().getUniqueId());
			assertEquals(verifiedChannel, pluginMessage.getChannel());
			assertEquals(Arrays.toString(expectedMessage), Arrays.toString(pluginMessage.getMessage()));
		}
	}

	/**
	 * Easy to access information of a plugin message
	 */
	public static class PluginMessage {

		private final PlayerMock playerMock;
		private final String channel;
		private final byte[] message;

		public PluginMessage(PlayerMock playerMock, String channel, byte[] message) {
			this.playerMock = playerMock;
			this.channel = channel;
			this.message = message;
		}

		public PlayerMock getPlayerMock() {
			return playerMock;
		}

		public String getChannel() {
			return channel;
		}

		public byte[] getMessage() {
			return message;
		}
	}

}
