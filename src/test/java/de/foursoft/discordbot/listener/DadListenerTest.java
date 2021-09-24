package de.foursoft.discordbot.listener;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

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
		when(message.getContentRaw()).thenReturn("ich bin Luke");
		when(guildMessageReceivedEvent.getChannel()).thenReturn(channel);

		MessageAction action = mock(MessageAction.class);
		when(channel.sendMessage((CharSequence) any())).thenReturn(action);

		final DadListener dadListener = new DadListener();

		// ACT
		dadListener.accept(guildMessageReceivedEvent);

		// ASSERT
		verify(channel).sendMessage("Hi luke, I'm dad");
		verify(action).queue();
	}
}