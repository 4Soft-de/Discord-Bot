package de.foursoft.discordbot.listener;


import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import net.dv8tion.jda.api.entities.TextChannel;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DadListenerTest {

	@Mock
	private TextChannel channel;

	@Mock
	private GuildMessageReceivedEvent guildMessageReceivedEvent;

	@Test
	void should_return_correct_message_in_channel() {
		// ARRANGE
		Message message = mock(Message.class);
		when(guildMessageReceivedEvent.getMessage()).thenReturn(message);
		when(message.getContentRaw()).thenReturn("ich bin dein Vater");
		when(guildMessageReceivedEvent.getChannel()).thenReturn(channel);

		final DadListener dadListener = new DadListener();

		// ACT
		dadListener.accept(guildMessageReceivedEvent);

		// ASSERT
		verify(channel).sendMessage("Hi dein Vater, I'm dad");
	}
}